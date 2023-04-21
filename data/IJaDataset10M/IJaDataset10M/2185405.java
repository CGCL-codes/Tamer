package sitznachbarn;

/**
 * 
 * @author Rudolf Radlbauer
 * Aufgabe 10: Sitznachbarn
 */
public class Testklasse {

    public void test() {
        Schueler hansi = new Schueler("Hansi");
        Schueler susi = new Schueler("Susi");
        Schueler fritz = new Schueler("Fritz");
        Schueler lilly = new Schueler("Lilly");
        Schueler karli = new Schueler("Karli");
        Schueler grete = new Schueler("Grete");
        hansi.setRechterNachbar(susi);
        susi.setRechterNachbar(fritz);
        fritz.setRechterNachbar(lilly);
        lilly.setRechterNachbar(karli);
        karli.setRechterNachbar(grete);
        grete.setLinkerNachbar(karli);
        karli.setLinkerNachbar(lilly);
        lilly.setLinkerNachbar(fritz);
        fritz.setLinkerNachbar(susi);
        susi.setLinkerNachbar(hansi);
        System.out.println("Liste links von Fritz:");
        fritz.listeLinks();
        System.out.println("Liste rechts von Fritz:");
        fritz.listeRechts();
        System.out.println("links von Fritz (inklusive) sitzen " + fritz.anzahlLinks() + " Schueler");
        System.out.println("rechts von Fritz (inklusive) sitzen " + fritz.anzahlRechts() + " Schueler");
    }
}
