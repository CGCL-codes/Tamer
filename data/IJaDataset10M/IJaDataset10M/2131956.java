package com.dsb.barkas.client.menu;

import java.util.ArrayList;
import com.dsb.barkas.client.BarkasClient;
import com.dsb.barkas.client.Bestelling;
import com.dsb.barkas.client.OpenBon;
import com.dsb.barkas.client.ServerErrorException;

public class SelecteerOpenBonMenu extends ScrollZoekMenu {

    OpenBon[] openBonnen;

    SelecteerOpenBonMenu(BarkasClient kas) {
        super(kas);
        if (kas.state == kas.SLUIT_BON) kas.labels[0].setText("Sluit turf ..."); else if (kas.state == kas.BESTELLING) {
            kas.labels[0].setText("Bestelling op turf ...");
            kas.buttons[6].setText("Open turf");
        } else if (kas.state == kas.TURF_DETAILS) kas.labels[0].setText("Turf details voor ..."); else kas.labels[0].setText("Hier gaat iets fout...");
        kas.buttons[11].setText("Annuleren");
        try {
            openBonnen = kas.server.getOpenBonnen();
            ArrayList<OpenBon> allowedbonnen = new ArrayList<OpenBon>();
            for (OpenBon openbon : openBonnen) {
                if (kas.allowedDebiteuren.contains(openbon.getDebNaam())) {
                    allowedbonnen.add(openbon);
                }
            }
            openBonnen = new OpenBon[allowedbonnen.size()];
            for (int n = 0; n < allowedbonnen.size(); n++) {
                openBonnen[n] = allowedbonnen.get(n);
            }
            int size = openBonnen.length;
            String[] items = new String[size];
            for (int i = 0; i < size; i++) items[i] = openBonnen[i].getDebNaam();
            setItems(items);
        } catch (ServerErrorException e) {
            kas.labels[5].setText(e.getMessage());
            e.printStackTrace();
            kas.melding = "Server error";
            kas.loadMenu(new HoofdMenu(kas));
        }
    }

    public void menu6() {
        kas.state = kas.OPEN_BON;
        kas.loadMenu(new SelecteerDebMenu(kas));
    }

    public void menu11() {
        if (kas.state == kas.SLUIT_BON) {
            kas.melding = "GEEN turf gesloten!";
            kas.loadMenu(new HoofdMenu(kas));
        } else if (kas.state == kas.BESTELLING) {
            kas.melding = "GEEN bestelling gedaan!";
            kas.loadMenu(new HoofdMenu(kas));
        } else if (kas.state == kas.TURF_DETAILS) {
            kas.loadMenu(new InformatieMenu(kas));
        } else {
            kas.loadMenu(new HoofdMenu(kas));
        }
    }

    public void select(int i) {
        OpenBon b = (openBonnen[i]);
        if (kas.state == kas.SLUIT_BON) {
            try {
                kas.server.closeBon(b.getBonID());
                kas.state = kas.READY;
                kas.melding = "Turf gesloten";
                kas.loadMenu(new HoofdMenu(kas));
            } catch (ServerErrorException e) {
                e.printStackTrace();
                kas.labels[5].setText(e.getMessage());
            }
        } else if (kas.state == kas.BESTELLING) {
            kas.bon = b;
            kas.state = kas.READY;
            kas.bestelling = new Bestelling(kas);
            kas.bestelling.addTurfDefaults();
            kas.loadMenu(new BestellingMenu(kas));
        } else if (kas.state == kas.TURF_DETAILS) {
            kas.bon = b;
            kas.loadMenu(new BonDetailsMenu(kas));
        } else {
            kas.labels[5].setText("Internal error: kas.state !");
        }
    }
}
