package domein;

import java.util.ArrayList;
import java.util.List;
import eigenexcepties.OngeldigeZetException;

/**
 * De klasse Spel.
 */
public class Spel {

    private SpelersPersonage speler;

    private SpelersPersonage kopieSpeler;

    private Spelbord spelbord;

    private Tegenstander aanvallendeTegenstander;

    private Tegenstander bewegendeTegenstander;

    private int aantalStappenVanSpeler;

    private int aantalStappenVanTegenstander;

    private int aantalBeurten;

    private boolean heeftVerplaatst = false;

    private boolean heeftAangevallen = false;

    private List<Vak> path = new ArrayList<Vak>();

    private List<Tegenstander> tegenstandersOpBord;

    /**
	 * Constructor, maakt een spel aan op basis 
	 * van het spelersPersonage en het spelbord.
	 * 
	 * @param deSpeler
	 * de speler van een spel.
	 * 
	 * @param hetSpelbord
	 * het spelbord waarop het spel wordt gespeeld.
	 */
    public Spel(SpelersPersonage deSpeler, Spelbord hetSpelbord) {
        if (deSpeler.getSchip() == null) throw new IllegalArgumentException("U heeft een schip nodig om een spel te kunnen starten" + " Ga naar de winkel en koop eerst een schip");
        aantalBeurten = 1;
        setSpeler(deSpeler);
        setSpelbord(hetSpelbord);
        speler.setSchadepunten(0);
        speler.setAantalSleutels(0);
        speler.wisBonussen();
        spelbord.wijsStartvakToeAanSpeler(speler);
        kopieSpeler = (SpelersPersonage) speler.clone();
        gooiVerplaatsdobbelsteen();
        tegenstandersOpBord = spelbord.maakLijstVanTegenstandersOpBord();
    }

    public String[][][] geefGUISpelbord() {
        return spelbord.geefGUISpelbord();
    }

    public String[][][] geefGUISpelbordMetView(int breedte, int hoogte) {
        return spelbord.geefGUISpelbordMetView(speler.getVak(), breedte, hoogte);
    }

    /**
	 * Geef lijst van bereikbare tegenstanders.
	 *
	 * @return the list
	 */
    public List<Tegenstander> geefLijstVanBereikbareTegenstanders() {
        List<Tegenstander> bereikbareTegenstanders = new ArrayList<Tegenstander>();
        Vak spelerVak = speler.getVak();
        Vak huidigBuurvak;
        for (Richting huidigeRichting : Richting.values()) {
            huidigBuurvak = spelbord.geefBuurvak(spelerVak, huidigeRichting);
            if (huidigBuurvak != null) {
                Tegenstander tegenstander = huidigBuurvak.getTegenstander();
                bereikbareTegenstanders.add(tegenstander);
            } else bereikbareTegenstanders.add(null);
        }
        return bereikbareTegenstanders;
    }

    /**
	 * kijkt of er tegenstanders naast de speler staan
	 * @return
	 */
    public boolean tegenstandersNaastSpeler() {
        List<Tegenstander> bereikbareTegenstanders = geefLijstVanBereikbareTegenstanders();
        int tegenstanderNaastSpeler = 0;
        for (Tegenstander postbode : bereikbareTegenstanders) {
            tegenstanderNaastSpeler += ((postbode != null) ? 1 : 0);
        }
        return (tegenstanderNaastSpeler > 0);
    }

    /**
	 * Checks if is einde spel bereikt.
	 *
	 * @return true, if is einde spel bereikt
	 */
    public boolean isEindeSpelBereikt() {
        return (speler.isDood() || speler.staatOpVak(spelbord.getEindvak()));
    }

    /**
	 * Checks if is speler gewonnen.
	 *
	 * @return true, if is speler gewonnen
	 */
    public boolean isSpelerGewonnen() {
        return speler.staatOpVak(spelbord.getEindvak());
    }

    /**
	 * Verplaats spelers personage.
	 *
	 * @param richting the richting
	 */
    public void verplaatsSpelersPersonage(Richting richting) {
        Vak vanVak = speler.getVak();
        Vak naarVak = spelbord.geefBuurvak(vanVak, richting);
        if (this.isEindeSpelBereikt()) throw new OngeldigeZetException("U kunt zich niet meer verplaatsen, het einde van " + "het spel is reeds bereikt!");
        if (aantalStappenVanSpeler <= 0 && !heeftAangevallen && tegenstandersNaastSpeler()) throw new OngeldigeZetException("U heeft deze beurt reeds het maximale " + "aantal stappen bereikt" + " en kan enkel nog aanvallen");
        if (aantalStappenVanSpeler <= 0) throw new OngeldigeZetException("U heeft deze beurt reeds het maximale " + "aantal stappen bereikt");
        if (naarVak == null) throw new OngeldigeZetException("U kunt zich niet verplaatsen buiten " + "de grenzen van het spelbord");
        if (naarVak.getPersonage() != null) throw new OngeldigeZetException("U kunt zich niet verplaatsen naar een vak dat " + "bezet is door een tegenstander");
        Obstakel obstakel = naarVak.getObstakel();
        if (obstakel != null) {
            if (obstakel instanceof Sluis) {
                Sluis sluis = (Sluis) obstakel;
                if (!sluis.isGeopend()) {
                    throw new OngeldigeZetException("Zoek eerst een sleutel om de sluis " + "te openen voor u zich naar hier kunt verplaatsen");
                }
            } else {
                throw new OngeldigeZetException("U kunt zich niet verplaatsen naar een vak dat " + "bezet is door een obstakel");
            }
        }
        if (naarVak == spelbord.getEindvak()) {
            speler.setGeld(speler.getGeld() + 100);
        }
        vanVak.setPersonage(null);
        naarVak.setPersonage(speler);
        speler.setVak(naarVak);
        speler.neemItemOp();
        heeftVerplaatst = true;
        aantalStappenVanSpeler--;
    }

    /**
	 * Val tegenstander aan.
	 *
	 * @param richting the richting
	 * @return the int[]
	 */
    public int[] valTegenstanderAan(Richting richting) {
        if (heeftAangevallen) throw new OngeldigeZetException("U heeft deze beurt reeds aangevallen");
        Vak spelerVak = speler.getVak();
        Vak tegenstanderVak = spelbord.geefBuurvak(spelerVak, richting);
        if (tegenstanderVak == null) throw new IllegalArgumentException("Het aangegeven vak ligt buiten de grenzen " + "van het spelbord.");
        Tegenstander tegenstander = tegenstanderVak.getTegenstander();
        if (tegenstander == null) throw new IllegalArgumentException("Er bevindt zich geen tegenstander op " + "het aangegeven vak!");
        int aantalSchedels = speler.gooiAanvalsdobbelsteen();
        int aantalZwarteSchilden = tegenstander.gooiVerdedigingsdobbelsteen();
        if (aantalSchedels > aantalZwarteSchilden) {
            int schadepunten = tegenstander.getSchadepunten();
            int schade = aantalSchedels - aantalZwarteSchilden;
            tegenstander.setSchadepunten(schadepunten + schade);
            if (tegenstander.isDood()) {
                tegenstander.bepaalGeld();
                int geldVanTegenstander = tegenstander.getGeld();
                int geldVanSpeler = speler.getGeld();
                speler.setGeld(geldVanSpeler + geldVanTegenstander);
                tegenstanderVak.setPersonage(null);
                tegenstander.setVak(null);
                tegenstandersOpBord.remove(tegenstander);
            }
        }
        if (heeftVerplaatst) aantalStappenVanSpeler = 0;
        heeftAangevallen = true;
        return new int[] { aantalSchedels, aantalZwarteSchilden };
    }

    /**
	 * Verdedig tegen aanval.
	 *
	 * @return the int[]
	 */
    public int[] verdedigTegenAanval() {
        if (aanvallendeTegenstander == null) throw new NullPointerException("Er is geen aanvallende tegenstander");
        int aantalSchedels = aanvallendeTegenstander.gooiAanvalsdobbelsteen();
        int aantalWitteSchilden = speler.gooiVerdedigingsdobbelsteen();
        if (aantalSchedels > aantalWitteSchilden) {
            int schadepunten = speler.getSchadepunten();
            int schade = aantalSchedels - aantalWitteSchilden;
            speler.setSchadepunten(schadepunten + schade);
        }
        if (isEindeSpelBereikt()) speler.setGeld(0);
        aanvallendeTegenstander = null;
        return new int[] { aantalSchedels, aantalWitteSchilden };
    }

    /**
	 * Bepaal aanvallende tegenstander.
	 */
    public void bepaalAanvallendeTegenstander() {
        aanvallendeTegenstander = null;
        List<Tegenstander> bereikbareTegenstanders = geefLijstVanBereikbareTegenstanders();
        while (bereikbareTegenstanders.remove(null)) ;
        final int AANTAL_TEGENSTANDERS = bereikbareTegenstanders.size();
        if (AANTAL_TEGENSTANDERS > 0) {
            int r = (int) (Math.random() * AANTAL_TEGENSTANDERS);
            aanvallendeTegenstander = bereikbareTegenstanders.get(r);
        }
    }

    /**
	 * Gets the aanvallende tegenstander.
	 *
	 * @return the aanvallende tegenstander
	 */
    public Tegenstander getAanvallendeTegenstander() {
        return aanvallendeTegenstander;
    }

    /**
	 * Gooi verplaatsdobbelsteen.
	 */
    public void gooiVerplaatsdobbelsteen() {
        aantalStappenVanSpeler = speler.gooiVerplaatsDobbelsteen();
    }

    /**
	 * Sets the speler.
	 *
	 * @param deSpeler the new speler
	 */
    private void setSpeler(SpelersPersonage deSpeler) {
        if (deSpeler == null) throw new IllegalArgumentException("Kan geen spel aanmaken zonder speler");
        this.speler = deSpeler;
    }

    /**
	 * Sets the spelbord.
	 *
	 * @param hetSpelbord the new spelbord
	 */
    private void setSpelbord(Spelbord hetSpelbord) {
        if (hetSpelbord == null) throw new IllegalArgumentException("Kan geen spel aanmaken zonder spelbord");
        this.spelbord = hetSpelbord;
    }

    /**
	 * Gets the aantal stappen.
	 *
	 * @return the aantal stappen
	 */
    public int getAantalStappen() {
        return aantalStappenVanSpeler;
    }

    public void setAantalStappen(int nieuwAantalStappen) {
        if (nieuwAantalStappen >= 0) this.aantalStappenVanSpeler = nieuwAantalStappen;
    }

    public void openSluis(Richting richting) {
        Vak obstakelVak = spelbord.geefBuurvak(speler.getVak(), richting);
        Obstakel obstakelOpVak = obstakelVak.getObstakel();
        if (obstakelOpVak instanceof Sluis) {
            Sluis sluis = (Sluis) obstakelOpVak;
            if (!sluis.isGeopend()) {
                int aantalSleutels = speler.getAantalSleutels();
                if (aantalSleutels > 0) {
                    speler.setAantalSleutels(aantalSleutels - 1);
                    sluis.setGeopend(true);
                }
            }
        }
    }

    /**
	 * Bij aanroepen van deze methode krijgt de speler als zijn 
	 * items terug die hij had bij aanvang van de map.
	 */
    public SpelersPersonage zetSpelerZoalsBeginMap() {
        return speler = kopieSpeler;
    }

    public boolean isSluis(Richting richting) {
        Vak obstakelVak = spelbord.geefBuurvak(speler.getVak(), richting);
        if (obstakelVak != null) {
            Obstakel obstakelOpVak = obstakelVak.getObstakel();
            return (obstakelOpVak instanceof Sluis);
        }
        return false;
    }

    public boolean isSluisOpen(Richting richting) {
        Sluis sluis;
        Vak sluisVak = spelbord.geefBuurvak(speler.getVak(), richting);
        if (sluisVak != null) {
            Obstakel obstakelOpVak = sluisVak.getObstakel();
            if (obstakelOpVak instanceof Sluis) {
                sluis = (Sluis) obstakelOpVak;
                return sluis.isGeopend();
            }
        }
        return false;
    }

    /**
	 * Geef vak detail.
	 *
	 * @param x the x
	 * @param y the y
	 * @param breedte the breedte
	 * @param hoogte the hoogte
	 * @return the string
	 */
    public String geefVakDetail(int x, int y, int breedte, int hoogte) {
        return spelbord.geefVakDetail(x, y, speler.getVak(), breedte, hoogte);
    }

    public int getAantalBeurten() {
        return aantalBeurten;
    }

    public void beeindigBeurt() {
        speler.updateBonussen();
        heeftVerplaatst = false;
        heeftAangevallen = false;
        aantalBeurten++;
    }

    public String geefAchtergrondSpel() {
        return spelbord.getAfbeelding();
    }

    /**
	 * Kies willekeurig een tegenstander op het bord, en bepaal voor deze
	 * tegenstander de kortste weg die hij moet volgen om het vak van de
	 * speler te bereiken.
	 */
    public void bepaalPadTegenstander() {
        List<Stap> open = new ArrayList<Stap>();
        List<Stap> gesloten = new ArrayList<Stap>();
        Stap zitInOpen = null;
        Stap zitInGesloten = null;
        Stap huidige = null;
        Obstakel obstakel;
        Vak buur;
        Vak eindVak = speler.getVak();
        if (tegenstandersOpBord.size() != 0) {
            bewegendeTegenstander = tegenstandersOpBord.get((int) (Math.random() * tegenstandersOpBord.size()));
            aantalStappenVanTegenstander = bewegendeTegenstander.gooiVerplaatsDobbelsteen();
            path.clear();
            open.clear();
            gesloten.clear();
            open.add(new Stap(bewegendeTegenstander.getVak(), 0, null));
            while (open.size() != 0) {
                huidige = open.get(0);
                if (huidige.getDitVak() == eindVak) {
                    break;
                }
                open.remove(0);
                gesloten.add(huidige);
                for (Richting r : Richting.values()) {
                    buur = spelbord.geefBuurvak(huidige.getDitVak(), r);
                    zitInGesloten = null;
                    zitInOpen = null;
                    if (buur == null || buur.getTegenstander() != null) continue;
                    obstakel = buur.getObstakel();
                    if ((obstakel != null) && (!(obstakel instanceof Sluis) || !((Sluis) obstakel).isGeopend())) continue;
                    for (Stap k : gesloten) {
                        if (k.getDitVak() == buur) {
                            zitInGesloten = k;
                            break;
                        }
                    }
                    for (Stap k : open) {
                        if (k.getDitVak() == buur) {
                            zitInOpen = k;
                            break;
                        }
                    }
                    if (zitInGesloten == null) {
                        if (zitInOpen == null) {
                            open.add(new Stap(buur, huidige.getAantalStappen() + 1, huidige));
                        } else {
                            if (zitInOpen.getAantalStappen() > huidige.getAantalStappen() + 1) {
                                open.remove(zitInGesloten);
                                open.add(new Stap(buur, huidige.getAantalStappen() + 1, huidige));
                            }
                        }
                    } else {
                        if (zitInGesloten.getAantalStappen() > huidige.getAantalStappen() + 1) {
                            gesloten.remove(zitInGesloten);
                            open.add(new Stap(buur, huidige.getAantalStappen() + 1, huidige));
                        }
                    }
                }
                huidige = null;
            }
            while (huidige != null) {
                path.add(huidige.getDitVak());
                huidige = huidige.getParent();
            }
        }
    }

    /**
	 * Verplaats de bewegende tegenstander.
	 * Het vak naar waar de bewegende tegenstander verplaatst zal worden
	 * is het volgende laatste Vak in een vooraf bepaalde lijst van vakken
	 * die het pad van de bewegende tegenstander voorstelt.
	 * 
	 * @see domein.Spel#bepaalPadTegenstander()
	 * 
	 * @return true indien de tegenstander verplaatst werd,
	 * false indien de bewegende tegenstander het einde van zijn pad
	 * bereikt heeft of wanneer hij al zijn aantal stappen opgebruikt heeft. 
	 */
    public boolean tegenstanderVerplaatsen() {
        int laatste = path.size() - 1;
        if (laatste > 1) {
            if (aantalStappenVanTegenstander > 0) {
                path.get(laatste).setPersonage(null);
                path.get(laatste - 1).setPersonage(bewegendeTegenstander);
                bewegendeTegenstander.setVak(path.get(laatste - 1));
                aantalStappenVanTegenstander--;
                path.remove(laatste);
                return true;
            }
        }
        return false;
    }

    public String[] geefDetailPersonage(int x, int y, int viewB, int viewH) {
        return spelbord.geefDetailPersonage(x, y, speler.getVak(), viewB, viewH);
    }
}
