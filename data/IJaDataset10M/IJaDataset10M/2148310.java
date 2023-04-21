package de.mnit.basis.swt.element.steuer.rollbar.gruppe.baum;

import org.eclipse.swt.widgets.TreeItem;
import de.mnit.basis.daten.struktur.liste.Liste;
import de.mnit.basis.daten.struktur.liste.S_Liste;
import de.mnit.basis.swt.element.feld.baum.SWT_BaumFeld;

/**
 * @author Michael Nitsche
 */
public class SWT_Baum extends A_SWT_Baum<SWT_Baum> {

    public SWT_Baum sAusgewaehlt(SWT_BaumFeld feld) {
        swt().setSelection(new TreeItem[] { feld.swt() });
        return this;
    }

    public SWT_Baum sAusgewaehlt(S_Liste<SWT_BaumFeld> felder) {
        TreeItem[] items = new TreeItem[felder.gLaenge()];
        int z = 0;
        for (SWT_BaumFeld feld : felder) items[z++] = feld.swt();
        swt().setSelection(items);
        return this;
    }

    public S_Liste<SWT_BaumFeld> gAusgewaehlt() {
        TreeItem[] auswahl = swt().getSelection();
        S_Liste<SWT_BaumFeld> erg = Liste.neu();
        for (TreeItem ti : auswahl) erg.plus((SWT_BaumFeld) ti.getData());
        return erg;
    }

    public boolean istFreigegeben() {
        return this.swt().isDisposed();
    }

    protected int style6() {
        return 0;
    }

    protected void init6() {
        t.s1_Rahmen(true);
    }
}
