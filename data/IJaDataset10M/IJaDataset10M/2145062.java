package de.mnit.basis.swt.element.feld.baum;

import org.eclipse.swt.widgets.TreeItem;
import de.mnit.basis.fehler.system.F_Sys_Datei;
import de.mnit.basis.swt.element.feld.A_SWT_Feld;
import de.mnit.basis.swt.grafik.Bild_SWT;
import de.mnit.basis.swt.grafik.S_Bild_SWT;
import de.mnit.basis.sys.Ausgabe;

/**
 * @author Michael Nitsche
 */
public abstract class A_SWT_BaumFeld<TA extends A_SWT_BaumFeld<?>> extends A_SWT_Feld<TA, TreeItem> implements S_BaumFeld {

    public void oeffnen() {
        if (swt().getItemCount() == 0) Ausgabe.debug("Ein Zweig ohne Inhalt kann nicht geöffnet werden!");
        swt().setExpanded(true);
    }

    public void schliessen() {
        swt().setExpanded(false);
    }

    public void sBild(Object datei) {
        try {
            swt().setImage(Bild_SWT.neu_Direkt(datei).gImage());
        } catch (F_Sys_Datei e) {
            Ausgabe.warnung(datei + " konnte nicht geladen werden!");
        }
    }

    public void sText(String... text) {
        swt().setText(text);
    }

    public S_Bild_SWT gBild() throws F_Sys_Datei {
        return Bild_SWT.neu(swt().getImage());
    }

    public String gText() {
        return swt().getText();
    }

    public int gKinderAnzahl() {
        return swt().getItemCount();
    }

    protected int style3() {
        return style4();
    }

    protected void init3() {
        init4();
    }

    protected abstract int style4();

    protected abstract void init4();
}
