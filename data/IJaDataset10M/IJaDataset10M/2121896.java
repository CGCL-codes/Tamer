package org.xptools.xpairports.gui.internal.data;

import org.xptools.xpairports.model.TaxiSign;

public class TaxiSignAdapter extends AirportPartAdapter {

    private TaxiSign _taxiSign;

    public TaxiSignAdapter(TaxiSign sign) {
        super(sign);
        _taxiSign = sign;
    }

    @Override
    public String getName() {
        return _taxiSign.getText();
    }
}
