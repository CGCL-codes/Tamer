package ch.olsen.products.util.options;

import ch.olsen.products.util.options.model.GarmanKohlhagen;

public class FxOptionImpl extends OptionAbs implements FxOption {

    public FxOptionImpl(double s, double x, double t, double r, double q, double v) {
        pricingModel = new GarmanKohlhagen(s, x, t, r, q, v);
    }

    public double getCallRhoForeign() {
        return ((GarmanKohlhagen) pricingModel).getCallRhoForeign();
    }

    public double getPutRhoForeign() {
        return ((GarmanKohlhagen) pricingModel).getPutRhoForeign();
    }

    public void setRiskFreeInterestRateForeign(double q) {
        ((GarmanKohlhagen) pricingModel).setRiskFreeInterestRateForeign(q);
    }
}
