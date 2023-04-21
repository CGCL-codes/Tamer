package org.primordion.user.app.climatechange.igm;

import org.primordion.xholon.base.IXholon;

public class Temperature extends Xhigm {

    private IXholon effectiveTemperature = null;

    private String parentXhcName = null;

    public void postConfigure() {
        parentXhcName = this.getParentNode().getXhcName();
        if (parentXhcName.equals("Atmosphere")) {
            effectiveTemperature = this.getParentNode().getFirstSibling();
            setVal(this.getVal() * effectiveTemperature.getVal());
        } else if (parentXhcName.equals("Surface")) {
            effectiveTemperature = this.getParentNode().getFirstSibling();
            setVal(this.getVal() * effectiveTemperature.getVal());
        }
        super.postConfigure();
    }

    public void act() {
        if (parentXhcName.equals("Atmosphere")) {
        } else if (parentXhcName.equals("Surface")) {
        } else if (parentXhcName.equals("Earth")) {
        }
        super.act();
    }

    public IXholon getEffectiveTemperature() {
        return effectiveTemperature;
    }
}
