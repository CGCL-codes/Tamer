package simulation.bewertungen;

import graph.Kreuzung;
import graph.Strasse;
import simulation.Global;
import simulation.Mensch;

public class BewertungBeta extends Bewertung {

    public final double EntfernungsGleichgewicht = 0.6;

    public final double KritischeStrassenAuslastung = 0.75;

    public final double KritischeStrassenAuslastungsBewertung = 0.5;

    public final double PostKritischeStrassenAuslastungsPotenz = 4.0;

    public final double PreKritischeStrassenAuslastungPotenz = 2.0;

    private double strKoPre;

    private double strKoPost;

    private double entfKo;

    private double streckungKoPositiv;

    private double streckungKoNegativ;

    @Override
    protected double bewerteKreuzung(Kreuzung StartKreuzung, Kreuzung ZielKreuzung, Mensch mensch) {
        double einflKataEntf = einflussKatastrophenEntfernung(ZielKreuzung.gefahrenEntfernung);
        double einflBezirksEntf = 1.0 - einflKataEntf;
        return einflKataEntf * bewerteEntfernung(ZielKreuzung.gefahrenEntfernung - StartKreuzung.gefahrenEntfernung) + 10.0 * einflBezirksEntf * bewerteEntfernung(StartKreuzung.bezirksEntfernung - ZielKreuzung.bezirksEntfernung);
    }

    protected double bewerteEntfernung(double diff) {
        if (diff >= 0) {
            diff /= 500;
        } else {
            diff /= 500;
        }
        return Math.atan(diff) * (1.0 / Math.PI) + 0.5;
    }

    protected double einflussKatastrophenEntfernung(double katastrophenEntfernungZiel) {
        return Math.pow(Math.E, (katastrophenEntfernungZiel * entfKo));
    }

    @Override
    protected double bewerteStrasse(Strasse str, Mensch mensch) {
        double result;
        double quotient = (double) str.auslastung / (double) str.kapazitaet;
        if (quotient >= 1.0) return 0;
        if (quotient <= KritischeStrassenAuslastung) {
            result = strKoPre * Math.pow(quotient, PreKritischeStrassenAuslastungPotenz) + 1.0;
        } else {
            result = strKoPost * (Math.pow(quotient, PostKritischeStrassenAuslastungsPotenz) - 1.0);
        }
        return result;
    }

    public BewertungBeta() {
        strKoPre = (KritischeStrassenAuslastung - 1.0) / (Math.pow(KritischeStrassenAuslastungsBewertung, PreKritischeStrassenAuslastungPotenz));
        strKoPost = KritischeStrassenAuslastungsBewertung / (Math.pow(KritischeStrassenAuslastung, PostKritischeStrassenAuslastungsPotenz) - 1);
        entfKo = 1.0 * Math.log(0.5) / (EntfernungsGleichgewicht * Global.KatastrophenRadius);
    }
}
