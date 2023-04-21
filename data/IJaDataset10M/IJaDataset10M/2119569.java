package de.dokchess.engine.zugauswahl;

import java.util.Collection;
import de.dokchess.allgemein.Farbe;
import de.dokchess.allgemein.Stellung;
import de.dokchess.allgemein.Zug;
import de.dokchess.engine.bewertung.Bewertung;
import de.dokchess.regeln.Spielregeln;

public class AlphaBetaAlgorithmus implements ZugAuswahl {

    private Spielregeln spielregeln;

    private Bewertung bewertung;

    private int tiefe;

    @Override
    public Zug ermittleZug(Stellung stellung) {
        Farbe spielerFarbe = stellung.getAmZug();
        Collection<Zug> zuege = spielregeln.ermittleGueltigeZuege(stellung);
        int besterWert = -Integer.MAX_VALUE;
        int alpha = besterWert;
        int beta = -alpha;
        Zug besterZug = null;
        for (Zug zug : zuege) {
            Stellung neueStellung = stellung.fuehreZugAus(zug);
            int wert = ermittleZugRekursiv(neueStellung, 1, alpha, beta, spielerFarbe);
            if (wert > besterWert) {
                besterWert = wert;
                alpha = wert;
                besterZug = zug;
            }
        }
        return besterZug;
    }

    protected int ermittleZugRekursiv(Stellung stellung, int aktuelleTiefe, int alpha, int beta, Farbe spielerFarbe) {
        if (aktuelleTiefe == tiefe) {
            return bewertung.bewerteStellung(stellung, spielerFarbe);
        } else {
            Collection<Zug> zuege = spielregeln.ermittleGueltigeZuege(stellung);
            if (zuege.isEmpty()) {
                if (!spielregeln.aufSchachPruefen(stellung, stellung.getAmZug())) {
                    return 0;
                }
                if (stellung.getAmZug() == spielerFarbe) {
                    return -(100000 - aktuelleTiefe);
                } else {
                    return 100000 - aktuelleTiefe;
                }
            } else {
                if (aktuelleTiefe % 2 == 0) {
                    int ergebnis = alpha;
                    boolean abbrechen = false;
                    for (Zug zug : zuege) {
                        Stellung neueStellung = stellung.fuehreZugAus(zug);
                        int wert = ermittleZugRekursiv(neueStellung, aktuelleTiefe + 1, alpha, beta, spielerFarbe);
                        if (wert >= beta) {
                            ergebnis = beta;
                            abbrechen = true;
                        } else if (wert > alpha) {
                            alpha = wert;
                            ergebnis = alpha;
                        }
                        if (abbrechen) {
                            break;
                        }
                    }
                    return ergebnis;
                } else {
                    int ergebnis = beta;
                    boolean abbrechen = false;
                    for (Zug zug : zuege) {
                        Stellung neueStellung = stellung.fuehreZugAus(zug);
                        int wert = ermittleZugRekursiv(neueStellung, aktuelleTiefe + 1, alpha, beta, spielerFarbe);
                        if (wert <= alpha) {
                            ergebnis = alpha;
                            abbrechen = true;
                        } else if (wert < beta) {
                            beta = wert;
                            ergebnis = beta;
                        }
                        if (abbrechen) {
                            break;
                        }
                    }
                    return ergebnis;
                }
            }
        }
    }

    public void setBewertung(Bewertung bewertung) {
        this.bewertung = bewertung;
    }

    public void setSpielregeln(Spielregeln spielregeln) {
        this.spielregeln = spielregeln;
    }

    public void setTiefe(int tiefe) {
        this.tiefe = tiefe;
    }
}
