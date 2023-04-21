package org.jquantlib.pricingengines;

import org.jquantlib.QL;
import org.jquantlib.instruments.AssetOrNothingPayoff;
import org.jquantlib.instruments.CashOrNothingPayoff;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.instruments.Option.Type;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;

/**
 * Analytic formula for American exercise payoff at-hit options
 *
 * @author Jose Coll
 */
public class AmericanPayoffAtHit {

    private final double spot;

    private final double variance;

    private final double stdDev;

    private final double strike, forward;

    private final double mu, lambda, muPlusLambda, muMinusLambda, log_H_S;

    private final double alpha, beta, DalphaDd1, DbetaDd2;

    private final double cum_d1, cum_d2, n_d1, n_d2;

    private final boolean inTheMoney;

    private transient double X, K;

    private transient double D1, D2;

    public AmericanPayoffAtHit(final double spot, final double discount, final double dividendDiscount, final double variance, final StrikedTypePayoff strikedTypePayoff) {
        QL.require(spot > 0.0, "positive spot value required");
        QL.require(discount > 0.0, "positive discount required");
        QL.require(dividendDiscount > 0.0, "positive dividend discount required");
        QL.require(variance >= 0.0, "non-negative variance required");
        this.spot = spot;
        this.variance = variance;
        this.stdDev = Math.sqrt(variance);
        this.strike = strikedTypePayoff.strike();
        this.log_H_S = Math.log(strike / spot);
        final Option.Type optionType = strikedTypePayoff.optionType();
        if (variance >= Math.E) {
            if (discount == 0.0 && dividendDiscount == 0.0) {
                mu = -0.5;
                lambda = 0.5;
            } else if (discount == 0.0) throw new LibraryException("null discount not handled yet"); else {
                mu = Math.log(dividendDiscount / discount) / variance - 0.5;
                lambda = Math.sqrt(mu * mu - 2.0 * Math.log(discount) / variance);
            }
            D1 = log_H_S / stdDev + lambda * stdDev;
            D2 = D1 - 2.0 * lambda * stdDev;
            final CumulativeNormalDistribution f = new CumulativeNormalDistribution();
            cum_d1 = f.op(D1);
            cum_d2 = f.op(D2);
            n_d1 = f.derivative(D1);
            n_d2 = f.derivative(D2);
        } else {
            mu = Math.log(dividendDiscount / discount) / variance - 0.5;
            lambda = Math.sqrt(mu * mu - 2.0 * Math.log(discount) / variance);
            if (log_H_S > 0) {
                cum_d1 = 1.0;
                cum_d2 = 1.0;
            } else {
                cum_d1 = 0.0;
                cum_d2 = 0.0;
            }
            n_d1 = 0.0;
            n_d2 = 0.0;
        }
        if (optionType.equals(Type.Call)) {
            if (strike > spot) {
                alpha = 1.0 - cum_d1;
                DalphaDd1 = -n_d1;
                beta = 1.0 - cum_d2;
                DbetaDd2 = -n_d2;
            } else {
                alpha = 0.5;
                DalphaDd1 = 0.0;
                beta = 0.5;
                DbetaDd2 = 0.0;
            }
        } else if (optionType.equals(Type.Put)) {
            if (strike < spot) {
                alpha = cum_d1;
                DalphaDd1 = n_d1;
                beta = cum_d2;
                DbetaDd2 = n_d2;
            } else {
                alpha = 0.5;
                DalphaDd1 = 0.0;
                beta = 0.5;
                DbetaDd2 = 0.0;
            }
        } else throw new IllegalArgumentException("invalid option type");
        muPlusLambda = mu + lambda;
        muMinusLambda = mu - lambda;
        inTheMoney = (optionType.equals(Type.Call) && strike < spot) || (optionType.equals(Type.Put) && strike > spot);
        double DXDstrike, DKDstrike;
        if (inTheMoney) {
            forward = 1.0;
            X = 1.0;
            DXDstrike = 0.0;
        } else {
            forward = Math.pow(strike / spot, muPlusLambda);
            X = Math.pow(strike / spot, muMinusLambda);
        }
        if (strikedTypePayoff instanceof CashOrNothingPayoff) {
            final CashOrNothingPayoff coo = (CashOrNothingPayoff) strikedTypePayoff;
            K = coo.getCashPayoff();
            DKDstrike = 0.0;
        }
        if (strikedTypePayoff instanceof AssetOrNothingPayoff) {
            final AssetOrNothingPayoff aoo = (AssetOrNothingPayoff) strikedTypePayoff;
            if (inTheMoney) {
                K = spot;
                DKDstrike = 0.0;
            } else {
                K = aoo.strike();
                DKDstrike = 1.0;
            }
        }
    }

    public double value() {
        final double result = K * (forward * alpha + X * beta);
        return result;
    }

    public double delta() {
        final double tempDelta = -spot * stdDev;
        final double DalphaDs = DalphaDd1 / tempDelta;
        final double DbetaDs = DbetaDd2 / tempDelta;
        double DforwardDs, DXDs;
        if (inTheMoney) {
            DforwardDs = 0.0;
            DXDs = 0.0;
        } else {
            DforwardDs = -muPlusLambda * forward / spot;
            DXDs = -muMinusLambda * X / spot;
        }
        final double delta = K * (DalphaDs * forward + alpha * DforwardDs + DbetaDs * X + beta * DXDs);
        return delta;
    }

    public double gamma() {
        final double tempDelta = -spot * stdDev;
        final double DalphaDs = DalphaDd1 / tempDelta;
        final double DbetaDs = DbetaDd2 / tempDelta;
        final double D2alphaDs2 = -DalphaDs / spot * (1 - D1 / stdDev);
        final double D2betaDs2 = -DbetaDs / spot * (1 - D2 / stdDev);
        double DforwardDs, DXDs, D2forwardDs2, D2XDs2;
        if (inTheMoney) {
            DforwardDs = 0.0;
            DXDs = 0.0;
            D2forwardDs2 = 0.0;
            D2XDs2 = 0.0;
        } else {
            DforwardDs = -muPlusLambda * forward / spot;
            DXDs = -muMinusLambda * X / spot;
            D2forwardDs2 = muPlusLambda * forward / (spot * spot) * (1 + muPlusLambda);
            D2XDs2 = muMinusLambda * X / (spot * spot) * (1 + muMinusLambda);
        }
        final double gamma = K * (D2alphaDs2 * forward + DalphaDs * DforwardDs + DalphaDs * DforwardDs + alpha * D2forwardDs2 + D2betaDs2 * X + DbetaDs * DXDs + DbetaDs * DXDs + beta * D2XDs2);
        return gamma;
    }

    public double rho(final double maturity) {
        QL.require(maturity > 0.0, "negative maturity not allowed");
        final double DalphaDr = -DalphaDd1 / (lambda * stdDev) * (1.0 + mu);
        final double DbetaDr = DbetaDd2 / (lambda * stdDev) * (1.0 + mu);
        double DforwardDr, DXDr;
        if (inTheMoney) {
            DforwardDr = 0.0;
            DXDr = 0.0;
        } else {
            DforwardDr = forward * (1.0 + (1.0 + mu) / lambda) * log_H_S / variance;
            DXDr = X * (1.0 - (1.0 + mu) / lambda) * log_H_S / variance;
        }
        return maturity * K * (DalphaDr * forward + alpha * DforwardDr + DbetaDr * X + beta * DXDr);
    }
}
