package bibliothekUI;

import java.util.ArrayList;

public class BuchVerwaltung {

    private ArrayList<Buch> buecher;

    public BuchVerwaltung() {
        buecher = new ArrayList<Buch>();
    }

    public void anlegen(Buch buch) {
        if (buch != null) buecher.add(buch); else System.out.println("buch darf nicht null sein");
    }

    public void ausmustern(Buch buch) {
        if (!buecher.remove(buch)) System.out.println("Buch war nicht vorhanden");
    }

    public void liste() {
        ueberschrift();
        for (Buch b : buecher) ausgeben(b);
    }

    public void listeEntlehnt() {
        ueberschrift();
        for (Buch b : buecher) {
            if (b.getKunde() != null) ausgeben(b);
        }
    }

    public void listeVerfuegbar() {
        ueberschrift();
        for (Buch b : buecher) {
            if (b.getKunde() == null) ausgeben(b);
        }
    }

    public int anzahlEntlehnt() {
        int anzahl = 0;
        for (Buch b : buecher) if (b.getKunde() != null) anzahl++;
        return anzahl;
    }

    public int anzahlVerfuegbar() {
        return buecher.size() - anzahlEntlehnt();
    }

    private void ueberschrift() {
        System.out.println("ISBN" + "\t" + "Autor" + "\t" + "Titel" + "\t" + "Verlag" + "\t" + "Seiten" + "\t" + "entlehnt");
    }

    private void ausgeben(Buch b) {
        System.out.println(b.getIsbn() + "\t" + b.getAutor() + "\t" + b.getTitel() + "\t" + b.getVerlag() + "\t" + b.getSeiten() + "\t" + ((b.getKunde() == null) ? "nein" : "ja"));
    }

    public Buch suchen(String isbn) {
        for (Buch b : buecher) {
            if (isbn.equals(b.getIsbn())) return b;
        }
        return null;
    }

    public int anzahl() {
        return buecher.size();
    }
}
