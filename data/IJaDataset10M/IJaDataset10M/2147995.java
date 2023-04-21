package Model;

import View.IModelObserver;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kenneth Svanborg, Lasse Vestergaard, Nanna Boxill, Thomas Moberg
 */
public class StofkrigModel implements IStofkrigModel {

    private ArrayList<IModelObserver> observers = new ArrayList<IModelObserver>();

    private List<Universe> universes;

    private Player p;

    private static int money = 5000;

    /**
     * Bygger spillets univers op og gør det muligt at spille
     */
    public void initialize() {
        universes = new ArrayList<Universe>();
        Officer o1 = new Officer("Pølle", 4);
        Universe aarhus = new Universe("Århus", 10000, o1);
        Drug d1 = new Drug("Amfetamin", 30);
        Drug d2 = new Drug("Hash", 40);
        Drug d3 = new Drug("Kokain", 50);
        Drug d4 = new Drug("Æter", 60);
        Dealer daa1 = new Dealer("Captain");
        daa1.addDrug(d1);
        daa1.addDrug(d2);
        daa1.addDrug(d3);
        daa1.addDrug(d4);
        Dealer daa2 = new Dealer("Dillermand");
        daa2.addDrug(d1);
        daa2.addDrug(d2);
        daa2.addDrug(d3);
        daa2.addDrug(d4);
        Dealer daa3 = new Dealer("Damestener");
        daa3.addDrug(d1);
        daa3.addDrug(d2);
        daa3.addDrug(d3);
        daa3.addDrug(d4);
        Area viby = new Area("Viby J.", daa1);
        Area aarhusC = new Area("Århus C.", daa2);
        Area gjelleguf = new Area("Gjelleguf", daa3);
        aarhus.addArea(viby);
        aarhus.addArea(aarhusC);
        aarhus.addArea(gjelleguf);
        Officer o2 = new Officer("Bætjent", 2);
        Universe koebenhavn = new Universe("København", 50000, o2);
        Drug d5 = new Drug("Amfetamin", 100);
        Drug d6 = new Drug("Hash", 200);
        Drug d7 = new Drug("Kokain", 300);
        Drug d8 = new Drug("Æter", 400);
        Dealer dk1 = new Dealer("Cokeface");
        dk1.addDrug(d5);
        dk1.addDrug(d6);
        dk1.addDrug(d7);
        dk1.addDrug(d8);
        Dealer dk2 = new Dealer("Crackhead");
        dk2.addDrug(d5);
        dk2.addDrug(d6);
        dk2.addDrug(d7);
        dk2.addDrug(d8);
        Dealer dk3 = new Dealer("Hans");
        dk3.addDrug(d5);
        dk3.addDrug(d6);
        dk3.addDrug(d7);
        dk3.addDrug(d8);
        Area hCOErstedsparken = new Area("H. C. Ørstedsparken", dk1);
        Area noerrebro = new Area("Nørrebro", dk2);
        Area christiania = new Area("Christiania", dk3);
        koebenhavn.addArea(hCOErstedsparken);
        koebenhavn.addArea(noerrebro);
        koebenhavn.addArea(christiania);
        Officer o3 = new Officer("Cupper", 6);
        Universe struer = new Universe("Struer", 0, o3);
        Drug d14 = new Drug("Amfetamin", 5);
        Drug d15 = new Drug("Hash", 10);
        Drug d16 = new Drug("Kokain", 15);
        Drug d17 = new Drug("Æter", 20);
        Dealer ds1 = new Dealer("Bong");
        ds1.addDrug(d14);
        ds1.addDrug(d15);
        ds1.addDrug(d16);
        ds1.addDrug(d17);
        Dealer ds2 = new Dealer("Tjillum");
        ds2.addDrug(d14);
        ds2.addDrug(d15);
        ds2.addDrug(d16);
        ds2.addDrug(d17);
        Dealer ds3 = new Dealer("Ole Henriksen");
        ds3.addDrug(d14);
        ds3.addDrug(d15);
        ds3.addDrug(d16);
        ds3.addDrug(d17);
        Area stephanies = new Area("Stephanies", ds1);
        Area danmarksgade = new Area("Danmarksgade", ds2);
        Area ungdomsgaarden = new Area("Ungdomsgaarden", ds3);
        struer.addArea(stephanies);
        struer.addArea(danmarksgade);
        struer.addArea(ungdomsgaarden);
        universes.add(struer);
        universes.add(aarhus);
        universes.add(koebenhavn);
    }

    /**
     * Returnerer de universer der er tilknyttet spillet
     * @return Alle universer der er tilknyttet spillet
     */
    public List<Universe> getUniverses() {
        return universes;
    }

    /**
     * Returnerer spilleren
     * @return Player
     */
    public Player getPLayer() {
        return p;
    }

    /**
     * Køber et stof hvis du har penge
     * @param d Det stof du vil købe
     * @param q Hvor mange enheder af stoffet du ønsker
     */
    public void buyDrug(Drug d, int q) {
        int amount = 0;
        for (Drugunit dr : getPLayer().getDrugs()) {
            amount += dr.getQuantity();
        }
        if (amount + q > 100) {
            notifyObservers("tomany");
        } else if (q == 0) {
            notifyObservers("noquantity");
        } else {
            if (p.getMoney() - d.getPrice() * q < 0) {
                notifyObservers("nomoney");
            } else {
                p.buyDrugs(d, q);
                notifyObservers("");
            }
        }
    }

    /**
     * Hvis du har stoffer at sælge af kan disse sælges
     * @param d Det stof du vil sælge
     * @param q Det antal enheder du ønsker at sælge
     */
    public void sellDrug(Drug d, int q) {
        if (q == 0) {
            notifyObservers("noquantity");
        } else {
            if (p.getDrug(d.getName()) == null || p.getDrug(d.getName()).getQuantity() < q) {
                notifyObservers("nodrugs");
            } else {
                p.sellDrugs(d, q);
                notifyObservers("");
            }
        }
    }

    /**
     * Rejs til en ny by og et nyt område. Der kan forekomme uventet besøg fra en betjent
     * @param u ankomstunivers
     * @param a ankomstområde
     */
    public void travel(Universe u, Area a) {
        p.setArrivalUniverse(u);
        p.setArrivalArea(a);
        if (p.travel(u, a).equals("free")) notifyObservers("travelFree"); else if (p.travel(u, a).equals("officer")) notifyObservers("officerShows"); else if (p.travel(u, a).equals("nogo")) notifyObservers("cutoff");
    }

    /**
     * Bliv hvis du bliver antastet af politiet
     * @param u ankomstuniverse
     * @param a ankomstmråde
     */
    public void stay(Universe u, Area a) {
        String result = p.stay(u, a);
        notifyObservers(result);
    }

    /**
     * Hvis du blive antastet af politiet kan du vælge at løbe. Måske bliver du skudt
     * @param u ankomstunivers
     * @param a ankomstområde
     */
    public void run(Universe u, Area a) {
        if (p.run(u, a)) notifyObservers("deadRun"); else notifyObservers("travelFree");
    }

    /**
     * Opretter et Playet objekt
     * @param name navnet på spilleren
     */
    public void createPlayer(String name) {
        p = new Player(name, money, universes.get(0), universes.get(0).getArea(0));
        notifyObservers("start");
    }

    /**
     * Tilføj en observer
     * @param o observerende objekt
     */
    public void registerObserver(IModelObserver o) {
        observers.add(o);
    }

    /**
     * Fjerner en observer
     * @param o observerende objekt
     */
    public void removeObserver(IModelObserver o) {
        observers.remove(o);
    }

    /**
     * Oplyser observers om ændringer i modellaget
     * @param result tekstsreng med info
     */
    public void notifyObservers(String result) {
        for (IModelObserver obs : observers) {
            obs.updateModel(result);
        }
    }
}
