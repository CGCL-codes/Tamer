package org.sulweb.infureports.event;

public final class MaskDrugChangeRequestsModel extends CommonModel {

    private boolean mask;

    public void setMask(boolean m) {
        mask = m;
        MaskDrugChangeRequestEvent mdce = new MaskDrugChangeRequestEvent(mask);
        list.fireEvent(mdce);
    }

    public boolean getMask() {
        return mask;
    }
}
