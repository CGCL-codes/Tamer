package org.posterita.model;

import org.compiere.model.MMailText;

public class UDIMMailText extends UDIPO {

    public UDIMMailText(MMailText mailText) {
        super(mailText);
    }

    public MMailText getMMailText() {
        return (MMailText) getPO();
    }
}
