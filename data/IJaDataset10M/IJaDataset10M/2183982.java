package de.mnit.basis.swt.dialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import de.mnit.basis.crypt.passwort.Passwort;
import de.mnit.basis.swt.anw.A_MN_Dialog;
import de.mnit.basis.swt.anw.A_MN_Gruppe;
import de.mnit.basis.swt.element.S_SWT_Komponente;
import de.mnit.basis.swt.element.steuer.SWT_Knopf;
import de.mnit.basis.swt.element.steuer.SWT_Label;
import de.mnit.basis.swt.element.steuer.rollbar.SWT_TextEingabe;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.deko.SWT_Fenster;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.deko.S_Fenster;
import de.mnit.basis.swt.grafik.icon.direkt.ICON;
import de.mnit.basis.swt.layout.LayoutAbsolut;

/**
 * @author Michael Nitsche
 * 17.03.2007	Erstellt
 */
public class Dialog_Passwort extends A_MN_Dialog<Dialog_Passwort, Passwort> {

    public static void main(String[] par) {
        start(Dialog_Passwort.class);
    }

    private final SWT_Label k_text_pass = new SWT_Label("Passwort:");

    private final SWT_TextEingabe k_pass = new SWT_TextEingabe();

    private final SWT_Knopf k_ok = new SWT_Knopf();

    private final String titel;

    private String roh;

    public Dialog_Passwort(String titel) {
        this.titel = titel;
    }

    public Dialog_Passwort() {
        this("Passwort");
    }

    public String gRoh() {
        return this.roh;
    }

    protected S_SWT_Komponente<?, ? extends Composite> layout() {
        S_SWT_Komponente<?, ?> k1 = LayoutAbsolut.neu().sKomponente(t.k_text_pass).sLO(10, 3).sBreite(70).fertig().sKomponente(t.k_pass).sLORU(t.k_text_pass, 0, 0, null).fertig();
        return LayoutAbsolut.neu().sKomponente(k1).sLORU(0, 10, 10, 40).fertig().sKomponente(t.k_ok).sRU(10, 10).sBreite(75).fertig();
    }

    protected void initStyle() {
        t.k_ok.sBild(ICON._16.AKTION_OK);
        t.k_pass.sVerdeckt();
    }

    protected void initEvents() {
        Listener ende = new Listener() {

            public void handleEvent(Event event) {
                t.schliessen();
            }
        };
        t.k_ok.eAuswahl(ende);
        t.k_pass.eTasteEnter(ende);
    }

    protected S_Fenster<?> fenster(A_MN_Gruppe<?> instanz) {
        return SWT_Fenster.neu(instanz, true, 250, 80, this.titel, ICON._16.OBJ_ERDE);
    }

    protected Passwort gErgebnis2() {
        String pass = t.k_pass.gText();
        this.roh = pass;
        return pass == null || pass.length() == 0 ? null : Passwort.neu(pass);
    }
}
