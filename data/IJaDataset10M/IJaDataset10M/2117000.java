package toepen;

import java.util.ArrayList;
import java.util.List;

/**
 * Een lijst van tot vier kaarten die tesamen een hand vormen
 * @author LucasD
 */
public class Hand {

    /**
     * 0-4 kaarten die de hand vormen
     */
    private List<Kaart> kaarten;

    /**
     * Is door de speler neergelegd als kandidaat vuile was
     */
    private boolean isKandidaatVuileWas;

    /**
     * Of de vuile was vervolgens al bekeken is (kan geen twee keer gebeuren)
     */
    private boolean isBekekenVuileWas;

    /**
     * 0-4 kaarten die gespeeld zijn door de speler
     */
    private List<Kaart> gespeeldeKaarten = new ArrayList<Kaart>();

    /**
     * 0-4 kaarten die niet gespeeld zijn door de speler
     */
    private List<Kaart> nietGespeeldeKaarten = kaarten;

    /**
     * Constructor voor Hand, altijd moeten er vier ongespeelde kaarten 
     * meegegeven worden
     * @param kaarten               De kaarten die de hand vormen  
     * @throws java.lang.Exception  De error die optreedt als het er geen 4 zijn
     */
    public Hand(List<Kaart> kaarten) throws Exception {
        if (kaarten.size() != 4) {
            throw new Exception("Een nieuwe hand bestaat altijd uit 4 kaarten");
        }
        this.kaarten = kaarten;
        this.nietGespeeldeKaarten = kaarten;
    }

    /**
     * @return Alle kaarten die gespeeld zijn
     */
    public List<Kaart> getGespeeldeKaarten() {
        return gespeeldeKaarten;
    }

    /**
     * @return Alle kaarten die nog niet gespeeld zijn
     */
    public List<Kaart> getNietGespeeldeKaarten() {
        return nietGespeeldeKaarten;
    }

    /**
     * Bepaatl of er alleen maar kaarten in de hand zitten die een aas zijn of
     * lager
     * @return true als dat zo is
     */
    public boolean getIsKandidaatVuileWas() {
        for (int i = 0; i < 4; i++) {
            if (this.getSpeelkaart(i).getWaarde().ordinal() > Waarde.AAS.ordinal()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourneert of de vuile was reeds bekeken is
     * @return true als dat zo is
     */
    public boolean getIsBekekenVuileWas() {
        return isBekekenVuileWas;
    }

    /**
     * @return alle kaarten
     */
    public List<Kaart> getKaarten() {
        return kaarten;
    }

    /**
     * @param kaartnummer wat bekeken moet worden
     * @return kaart met dat nummer
     */
    public Kaart getSpeelkaart(int kaartnummer) {
        return this.kaarten.get(kaartnummer);
    }

    /**
     * Verplaats een kaart van de niet gespeelde kaarten naar de gespeelde
     * kaarten
     * @param kaart
     */
    public void setGespeeldeKaart(Kaart kaart) {
        this.gespeeldeKaarten.add(kaart);
        this.nietGespeeldeKaarten.remove(kaart);
    }
}
