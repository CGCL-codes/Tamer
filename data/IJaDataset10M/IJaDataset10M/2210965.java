package org.jquantlib.termstructures;

import java.util.Arrays;
import org.jquantlib.QL;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.CostFunction;
import org.jquantlib.math.optimization.EndCriteria;
import org.jquantlib.math.optimization.LevenbergMarquardt;
import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.termstructures.yieldcurves.PiecewiseYieldCurve;
import org.jquantlib.time.Date;

public class LocalBootstrap<Curve extends PiecewiseYieldCurve> {

    private Curve ts_;

    private final int localisation_;

    private final boolean forcePositive_;

    private boolean validCurve_;

    public LocalBootstrap() {
        this(2, true);
    }

    public LocalBootstrap(final int localisation) {
        this(localisation, true);
    }

    public LocalBootstrap(final int localisation, final boolean forcePositive) {
        QL.validateExperimentalMode();
        this.validCurve_ = false;
        this.ts_ = null;
        this.localisation_ = localisation;
        this.forcePositive_ = forcePositive;
    }

    public void setup(final Curve ts) {
        this.ts_ = ts;
        final int n = ts_.instruments().length;
        QL.require(n >= ts.interpolator().requiredPoints(), "not enough instruments: %d provided, %d required", n, ts.interpolator().requiredPoints());
        QL.require(n > localisation_, "not enough instruments: %d provided, %d required.", n, localisation_);
        for (int i = 0; i < n; ++i) {
            ts_.instruments()[i].addObserver(ts_);
        }
    }

    public void calculate() {
        validCurve_ = false;
        final int nInsts = ts_.instruments().length;
        Arrays.sort(ts_.instruments(), new BootstrapHelperSorter());
        for (int i = 1; i < nInsts; ++i) {
            final Date m1 = ts_.instruments()[i - 1].latestDate();
            final Date m2 = ts_.instruments()[i].latestDate();
            QL.require(m1 != m2, "two instruments have the same maturity");
        }
        for (int i = 0; i < nInsts; ++i) QL.require(ts_.instruments()[i].quoteIsValid(), " instrument #%d (maturity: %s) has infalic quote", i + 1, ts_.instruments()[i].latestDate());
        for (int i = 0; i < nInsts; ++i) {
            ts_.instruments()[i].setTermStructure(ts_);
        }
        if (validCurve_) QL.ensure(ts_.data().length == nInsts + 1, "dimension mismatch: expected %d, actual %d", nInsts + 1, ts_.data().length); else {
            final double[] data = new double[nInsts + 1];
            data[0] = ts_.traits().initialValue(ts_);
            ts_.setData(data);
        }
        {
            final Date[] dates = new Date[nInsts + 1];
            dates[0] = ts_.traits().initialDate(ts_);
            ts_.setDates(dates);
        }
        {
            final double[] times = new double[nInsts + 1];
            times[0] = ts_.timeFromReference(ts_.dates()[0]);
            ts_.setTimes(times);
        }
        for (int i = 0; i < nInsts; ++i) {
            ts_.dates()[i + 1] = ts_.instruments()[i].latestDate();
            ts_.times()[i + 1] = ts_.timeFromReference(ts_.dates()[i + 1]);
            if (!validCurve_) ts_.data()[i + 1] = ts_.data()[i];
        }
        final LevenbergMarquardt solver = new LevenbergMarquardt(ts_.accuracy(), ts_.accuracy(), ts_.accuracy());
        final EndCriteria endCriteria = new EndCriteria(100, 10, 0.00, ts_.accuracy(), 0.00);
        final Constraint solverConstraint = forcePositive_ ? new PositiveConstraint() : new NoConstraint();
        final int iInst = localisation_ - 1;
        validCurve_ = true;
    }

    private class PenaltyFunction extends CostFunction {

        private final int initialIndex;

        private final int rateHelpersStart;

        private final int rateHelpersEnd;

        private final int penaltylocalisation;

        private PenaltyFunction(final int initialIndex, final int rateHelpersStart, final int rateHelpersEnd) {
            this.initialIndex = initialIndex;
            this.rateHelpersStart = rateHelpersStart;
            this.rateHelpersEnd = rateHelpersEnd;
            this.penaltylocalisation = rateHelpersEnd - rateHelpersStart;
        }

        @Override
        public double value(final Array x) {
            int i = initialIndex;
            int guessIt = 0;
            for (; guessIt < x.size(); ++guessIt, ++i) {
                ts_.traits().updateGuess(ts_.data(), guessIt, i);
            }
            ts_.interpolation().update();
            double penalty = 0.0;
            int j = rateHelpersStart;
            for (; j != rateHelpersEnd; ++j) {
                penalty += Math.abs(ts_.instruments()[j].quoteError());
            }
            return penalty;
        }

        @Override
        public Array values(final Array x) {
            int guessIt = 0;
            for (int i = initialIndex; guessIt < x.size(); ++guessIt, ++i) {
                ts_.traits().updateGuess(ts_.data(), x.get(guessIt), i);
            }
            ts_.interpolation().update();
            final Array penalties = new Array(penaltylocalisation);
            int instIterator = rateHelpersStart;
            for (int penIt = 0; instIterator != rateHelpersEnd; ++instIterator, ++penIt) {
                penalties.set(penIt, Math.abs(ts_.instruments()[instIterator].quoteError()));
            }
            return penalties;
        }
    }
}
