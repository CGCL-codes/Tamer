package org.matsim.population;

import org.matsim.gbl.Gbl;

public class ActivitySpaceEllipse extends ActivitySpace {

    public ActivitySpaceEllipse(final String act_type) {
        this.act_type = act_type.intern();
        this.params.put("x", null);
        this.params.put("y", null);
        this.params.put("theta", null);
        this.params.put("a", null);
        this.params.put("b", null);
        this.params.put("cover", null);
    }

    @Override
    protected void addParam(final String name, final Double value) {
        double v = value.doubleValue();
        if (!this.params.containsKey(name)) {
            Gbl.errorMsg("[name=" + name + " is not allowed]");
        }
        if (name.equals("theta")) {
            if ((v <= -Math.PI / 4.0) || (v > Math.PI / 4.0)) {
                Gbl.errorMsg("[name=" + name + ",value=" + v + " is not element of ]-pi,pi].]");
            }
        }
        if (name.equals("cover")) {
            if (v <= 0.0) {
                Gbl.errorMsg("[name=" + name + ",value=" + v + " is <= zero.]");
            }
        }
        this.params.put(name.intern(), value);
    }

    @Override
    protected void addParam(final String name, final String value) {
        Double val = Double.valueOf(value);
        this.addParam(name, val);
    }
}
