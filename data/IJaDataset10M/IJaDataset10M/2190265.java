package swtproto;

import data.*;
import data.ooimpl.*;
import data.stdforms.*;
import sale.*;

/**
     * Proze�, der das Anzeigen eines Fahrrad StoringStocks erm�glicht.
     * @author Oliver Urban
     * @version 1.0
     */
public class SeeBikeStockProcess extends SaleProcess {

    private boolean editable = false;

    /**
       * Konstruktor. Erstellt einen neuen SeeBikeStockProcess.
	   * @param editable legt fest, ob die Eintr�ge bearbeitet werden k�nnen
       */
    public SeeBikeStockProcess(boolean editable) {
        super("See and edit bike stock");
        this.editable = editable;
    }

    /**
       * Baut das Startgate des Prozesses auf und gibt es zur�ck.
       */
    public Gate getInitialGate() {
        UIGate uigGate = new UIGate(null, null);
        SingleTableFormSheet stfs;
        if (editable) stfs = SingleTableFormSheet.create("Bestandanzeige (Bike) und Edit Bikebestand", (StoringStockImpl) Shop.getTheShop().getStock("bike stock"), uigGate, getBasket(), null, new OfficeBikeTED((StoringStockImpl) Shop.getTheShop().getStock("bike stock"), (StoringStockImpl) Shop.getTheShop().getStock("haendler stock"), true)); else stfs = SingleTableFormSheet.create("Bestandanzeige (Bike) und Edit Bikebestand", (StoringStockImpl) Shop.getTheShop().getStock("bike stock"), uigGate, getBasket(), null, new OfficeBikeTED(null, null, false));
        stfs.addContentCreator(new FormSheetContentCreator() {

            public void createFormSheetContent(FormSheet fsToCreate) {
                fsToCreate.removeAllButtons();
                fsToCreate.addButton("Ok", 100, new sale.Action() {

                    public void doAction(SaleProcess p, SalesPoint sp) {
                        ((UIGate) p.getCurrentGate()).setNextTransition(GateChangeTransition.CHANGE_TO_COMMIT_GATE);
                    }
                });
                fsToCreate.addButton("Abbrechen", 101, new sale.Action() {

                    public void doAction(SaleProcess p, SalesPoint sp) {
                        ((UIGate) p.getCurrentGate()).setNextTransition(GateChangeTransition.CHANGE_TO_ROLLBACK_GATE);
                    }
                });
            }
        });
        return uigGate;
    }
}
