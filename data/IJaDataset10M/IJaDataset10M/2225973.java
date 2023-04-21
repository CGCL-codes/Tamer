package de.mnit.schnipsel.swt.dialog;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import de.mnit.basis.daten.konstant.position.POSITION;
import de.mnit.basis.daten.konstant.position.POSITION_H;
import de.mnit.basis.daten.struktur.gruppe.Gruppe2;
import de.mnit.basis.fehler.system.F_Sys_InOut;
import de.mnit.basis.swt.anw.A_MN_Dialog;
import de.mnit.basis.swt.anw.A_MN_Gruppe;
import de.mnit.basis.swt.dialog.SWT_Fehler;
import de.mnit.basis.swt.element.S_SWT_Komponente;
import de.mnit.basis.swt.element.steuer.SWT_Knopf;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.SWT_Bild;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.SWT_TextEditor;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.deko.SWT_Fenster;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.deko.S_Fenster;
import de.mnit.basis.swt.grafik.icon.direkt.ICON;
import de.mnit.basis.swt.layout.LayoutGitter;
import de.mnit.basis.sys.Ausgabe;
import de.mnit.basis.sys.datei.JarZugriff;
import de.mnit.basis.sys.stream.StreamHilfe;
import de.mnit.schnipsel.swt.editor.basis.KonverterHtml;

/**
 * @author Michael Nitsche
 * 01.04.2009	Erstellt
 */
public class Dialog_Ausgleich extends A_MN_Dialog<Dialog_Ausgleich, Object> {

    public static void main(String[] args) {
        start(Dialog_Ausgleich.class);
    }

    public static final String TEXT = "/jar/text/ausgleich_de.txt";

    public static final String BILD = "/jar/bild/michael2.jpg";

    public static final String FLATTR = "/jar/bild/button-static-50x60.png";

    public static final String PAYPAL = "/jar/bild/btn_donateCC_LG.gif";

    public static final String ZIEL_FLATTR = "http://flattr.com/thing/115393/Schnipsel";

    public static final String ZIEL_PAYPAL = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=SX6EGCAZFGD58";

    private final SWT_Knopf k_ok = new SWT_Knopf();

    private final SWT_Bild k_bild = new SWT_Bild();

    private final SWT_Bild k_flattr = new SWT_Bild();

    private final SWT_Bild k_paypal = new SWT_Bild();

    private final SWT_TextEditor k_text = new SWT_TextEditor().s1_NurLesbar().s1_Rahmen(true).s1_Zeilenumbruch();

    protected S_Fenster<?> fenster(A_MN_Gruppe<?> instanz) throws F_Sys_InOut {
        return SWT_Fenster.neu(instanz, true, 500, 450, "Ausgleich", ICON.gib(24, "mn", "info.png"));
    }

    protected S_SWT_Komponente<?, ? extends Composite> layout() {
        LayoutGitter gruppe1 = LayoutGitter.neu().sKomponente(t.k_flattr).sFlexibel().sKomponente(t.k_paypal).sFlexibel().fertig();
        return LayoutGitter.neu().sSpaltenAnzahl(2).sAussenabstand(5).sZwischenraumV(10).sKomponente(t.k_text).sUebergreifendV(2).sFlexibel().sKomponente(t.k_bild).sBreite(150).sHoehe(250).sPosition(POSITION.OBEN).sKomponente(gruppe1).sBreite(150).sKomponente(t.k_ok).sUebergreifendH(2).sPositionH(POSITION_H.MITTE).sBreite(120).sHoehe(30).fertig();
    }

    protected void initStyle() {
        t.k_ok.sBild(ICON.gib(24, "mn", "schliessen.png"));
        t.k_ok.sText("Schließen");
        t.k_text.sHintergrund(253, 253, 253);
        try {
            InputStream is = JarZugriff.gJarStream(TEXT);
            String text = StreamHilfe.liesASCII(is);
            this.k_text.swt().setAlignment(SWT.CENTER);
            Gruppe2<String, StyleRange[]> g = KonverterHtml.vonHTML(text);
            this.k_text.sText(g.g1());
            this.k_text.swt().setStyleRanges(g.g2());
            is = JarZugriff.gJarStream(BILD);
            t.k_bild.sBild(is);
            is = JarZugriff.gJarStream(FLATTR);
            t.k_flattr.sBild(is);
            is = JarZugriff.gJarStream(PAYPAL);
            t.k_paypal.sBild(is);
        } catch (F_Sys_InOut e) {
            SWT_Fehler.zeig(e, "Text konnte nicht geladen werden!", false);
        }
    }

    protected void initEvents() {
        t.k_ok.eAuswahl(new Listener() {

            public void handleEvent(Event event) {
                t.schliessen();
            }
        });
        t.k_flattr.eMausKlick(new Listener() {

            public void handleEvent(Event e) {
                Ausgabe.debug();
                try {
                    Desktop.getDesktop().browse(new URI(ZIEL_FLATTR));
                } catch (IOException e1) {
                    SWT_Fehler.zeig(e1, false);
                } catch (URISyntaxException e2) {
                    SWT_Fehler.zeig(e2, false);
                }
            }
        });
        t.k_paypal.eMausKlick(new Listener() {

            public void handleEvent(Event e) {
                Ausgabe.debug();
                try {
                    Desktop.getDesktop().browse(new URI(ZIEL_PAYPAL));
                } catch (IOException e1) {
                    SWT_Fehler.zeig(e1, false);
                } catch (URISyntaxException e2) {
                    SWT_Fehler.zeig(e2, false);
                }
            }
        });
    }

    protected Object gErgebnis2() {
        return null;
    }
}
