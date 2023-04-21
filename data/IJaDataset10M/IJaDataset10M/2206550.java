package Econtyc.AIs;

import Econtyc.AISpieler;
import Econtyc.Master;
import Econtyc.Spiel;
import Econtyc.Rohstoff;
import Econtyc.data.Strings;
import Econtyc.data.Werte;

public class KonnAI extends AISpieler {

    private Spiel spiel;

    private boolean stueckgutlagergebaut = false;

    private boolean schuettgutlagergebaut = false;

    private int startpreis[] = new int[Werte.ROHSTOFFZAHL];

    private int eventcounter[] = new int[Werte.ROHSTOFFZAHL];

    public KonnAI(Spiel spiel) {
        super(spiel);
        this.spiel = spiel;
        setName("KonnAI");
        for (int i = 0; i < Werte.ROHSTOFFZAHL; i++) {
            startpreis[i] = spiel.getRohstoffint(i).getPreisAsInt();
            eventcounter[i] = 0;
        }
    }

    public void aktion() {
        for (int i = 0; i < Werte.ROHSTOFFZAHL; i++) {
            Rohstoff rohstoff = spiel.getRohstoffint(i);
            if (rohstoff.getEvent() == 0) eventcounter[i]++; else if (rohstoff.getEvent() == 1) eventcounter[i]--; else if (rohstoff.getEvent() == 2) eventcounter[i] += 3; else if (rohstoff.getEvent() == 3) eventcounter[i] -= 3;
            if (rohstoff.isKaufbar() && rohstoff.isVerkaufbar()) {
                if (rohstoff.getPreisAsInt() < 0.9 * rohstoff.getDurchschnittspreis()) {
                    double a = Math.random();
                    if (a >= 0.6) {
                        spiel.kaufen(rohstoff, this, 1);
                    }
                }
                if (rohstoff.getPreisAsInt() < 0.7 * rohstoff.getDurchschnittspreis()) {
                    double a = Math.random();
                    if (a >= 0.6) {
                        spiel.kaufen(rohstoff, this, ((int) (getGeld() / rohstoff.getPreisAsInt())));
                    }
                }
            }
            if (getRohstoffbesitz(rohstoff) > 0) {
                if (rohstoff.getPreisAsInt() > 1.1 * super.getEkwert(i) || rohstoff.getPreisAsInt() > 1.1 * rohstoff.getDurchschnittspreis()) {
                    double a = Math.random();
                    if (a >= 0.3) {
                        spiel.verkaufen(rohstoff, this, 1);
                    }
                }
                if (rohstoff.getPreisAsInt() > 1.3 * super.getEkwert(i) || rohstoff.getPreisAsInt() > 1.3 * rohstoff.getDurchschnittspreis()) {
                    double a = Math.random();
                    if (a >= 0.9) {
                        spiel.verkaufen(rohstoff, this, getRohstoffbesitz(rohstoff));
                    }
                }
            }
        }
        if (stueckgutlagergebaut == false) {
            if (getGeld() >= 10000 && getFirmenwert() >= 14000) {
                lagerBauen(0, 2);
                if (Master.OPTIONEN.isKonsoleai()) System.out.println("[KonnAI]: Lager gebaut: " + Strings.getString("Lager.2"));
                stueckgutlagergebaut = true;
            }
        }
        if (schuettgutlagergebaut == false) {
            if (getGeld() >= 10000 && getFirmenwert() >= 14000) {
                lagerBauen(1, 1);
                if (Master.OPTIONEN.isKonsoleai()) System.out.println("[KonnAI]: Lager gebaut: " + Strings.getString("Lager.1"));
                schuettgutlagergebaut = true;
            }
        }
        if (stueckgutlagergebaut && schuettgutlagergebaut) {
            for (int i = 0; i < 4; i++) {
                if (getLagergroesse(i) == 0) {
                    int preis = (int) ((Werte.LAGERPREIS[i][0]) * getLagerpreisfaktor());
                    if (getGeld() > 2 * preis) {
                        lagerBauen(i, 0);
                        if (Master.OPTIONEN.isKonsoleai()) System.out.println("[KonnAI]: Lager gebaut: " + Strings.getString("Lager." + i));
                    }
                }
                if (getLagergroesse(i) > 0) {
                    if (getRohstoffegesamt(i) == getLagergroesse(i)) {
                        int preis = (int) ((Werte.LAGERPREIS[i][2]) * getLagerpreisfaktor());
                        if (getGeld() > 3 * preis) {
                            lagerBauen(i, 2);
                            if (Master.OPTIONEN.isKonsoleai()) System.out.println("[KonnAI]: Lager gebaut: " + Strings.getString("Lager." + i));
                        }
                    }
                }
            }
        }
        if (getGeld() > 100000 && getGeld() > Werte.LAGERPREIS[2][2] * getLagerpreisfaktor()) {
            for (int i = 0; i < Werte.INDUSTRIEZAHL; i++) {
                if (Werte.INDUSTRIE[i][Werte.V1] == -1 && spiel.getRohstoffint(Werte.INDUSTRIE[i][Werte.P1]).isVerkaufbar()) {
                    if (eventcounter[Werte.INDUSTRIE[i][Werte.P1]] > 3) {
                        super.lagerBauen(spiel.getRohstoffint(Werte.INDUSTRIE[i][Werte.P1]).getTyp(), 2);
                        industriekaufen(i);
                        if (Master.OPTIONEN.isKonsoleai()) System.out.println("[KonnAI]: Industrie gebaut: " + Strings.getString("Industrie." + i));
                    }
                }
            }
        }
    }
}
