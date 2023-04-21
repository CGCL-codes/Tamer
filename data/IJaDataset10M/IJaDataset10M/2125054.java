package chsec.gui;

import java.awt.Component;
import java.util.List;
import chsec.domain.CreditCard;
import chsec.domain.DonationCat;
import chsec.domain.Parishioner;

public interface DonPaneFry {

    public Component createDonPane(DonationCat donCat, DonEditorMenuCont menuCont, CashCountListener cclnr, List<Parishioner> parnrL, List<CreditCard> ccL);
}
