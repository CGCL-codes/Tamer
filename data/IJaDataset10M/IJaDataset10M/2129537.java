package org.posterita.model;

import org.compiere.model.MBPartner;

public class UDIMBPartner extends UDIPO {

    public UDIMBPartner(MBPartner partner) {
        super(partner);
    }

    public MBPartner getMBPartner() {
        return (MBPartner) getPO();
    }
}
