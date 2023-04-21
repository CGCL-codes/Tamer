package de.mnit.basis.swt.element.feld;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;
import de.mnit.basis.daten.konstant.position.POSITION_H;
import de.mnit.basis.fehler.Fehler;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.tabelle.A_SWT_Tabelle;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.tabelle.SWT_TabelleVirtuell;
import de.mnit.basis.sys.event.S_Zuhoerer;

public class SWT_TabellenSpalte extends A_SWT_Feld<SWT_TabellenSpalte, TableColumn> {

    private static final String E_SORTIERT = "sortiert";

    private String text;

    private Listener sortierbar = null;

    private final A_SWT_Tabelle<?> tabelle;

    private int sortierspalte = -1;

    public SWT_TabellenSpalte(A_SWT_Tabelle<?> tabelle, String text) {
        Fehler.objekt.wenn_Null(tabelle, text);
        this.tabelle = tabelle;
        this.text = text;
    }

    public int gSortierspalte() {
        return sortierspalte == -1 ? iSpaltenNr(tabelle) + 1 : sortierspalte;
    }

    public SWT_TabellenSpalte sText(String text) {
        Fehler.objekt.wenn_Null(text);
        swt().setText(text);
        return this;
    }

    public SWT_TabellenSpalte sBreite(int breite) {
        Fehler.zahl.wenn_Ausserhalb(10, 1000, breite);
        swt().setWidth(breite);
        return this;
    }

    public int gBreite() {
        return swt().getWidth();
    }

    public SWT_TabellenSpalte sPosition(POSITION_H a) {
        Fehler.objekt.wenn_Null(a);
        if (a == POSITION_H.LINKS) swt().setAlignment(SWT.LEFT);
        if (a == POSITION_H.MITTE) swt().setAlignment(SWT.CENTER);
        if (a == POSITION_H.RECHTS) swt().setAlignment(SWT.RIGHT);
        return this;
    }

    public SWT_TabellenSpalte sSortierbar() {
        return this.sSortierbar(-1);
    }

    public SWT_TabellenSpalte sSortierbar(int spalte) {
        Fehler.zahl.wenn_ZuKlein(-1, spalte);
        Fehler.zahl.wenn_Gleich(spalte, 0);
        this.sortierspalte = spalte;
        if (sortierbar != null) this.swt().removeListener(SWT.Selection, this.sortierbar);
        final int tspalte = iSpaltenNr(tabelle) + 1;
        final int nach = spalte != -1 ? spalte : tspalte;
        Listener l1 = new Listener() {

            public void handleEvent(Event arg0) {
                boolean absteigend = tabelle.swt().getSortDirection() == SWT.UP && tabelle.swt().getSortColumn() == swt();
                ((SWT_TabelleVirtuell) tabelle).sortieren(tspalte, absteigend, nach);
                t.zuhoererStart(E_SORTIERT, absteigend);
            }
        };
        Listener l2 = new Listener() {

            public void handleEvent(Event arg0) {
                boolean absteigend = tabelle.swt().getSortDirection() == SWT.UP && tabelle.swt().getSortColumn() == swt();
                tabelle.sortieren(tspalte, absteigend);
                t.zuhoererStart(E_SORTIERT, absteigend);
            }
        };
        this.eAuswahl(spalte != -1 ? l1 : l2);
        this.sortierbar = spalte != -1 ? l1 : l2;
        return this;
    }

    public SWT_TabellenSpalte sTip(String t) {
        swt().setToolTipText(t);
        return this;
    }

    public SWT_TabellenSpalte sVerschiebbar() {
        swt().setMoveable(true);
        return this;
    }

    public SWT_TabellenSpalte eVerschoben(Listener l) {
        swt().addListener(SWT.Move, l);
        return this;
    }

    public void eSortiert(final S_Zuhoerer<Boolean> z) {
        this.zuhoererPlus(E_SORTIERT, z);
    }

    protected int style3() {
        return SWT.BORDER;
    }

    protected TableColumn roh(Widget basis, int style) {
        return new TableColumn((Table) basis, style);
    }

    protected void init3() {
        swt().setText(this.text);
        swt().setWidth(100);
    }

    private int iSpaltenNr(A_SWT_Tabelle<?> tabelle) {
        for (int i = 0; i < tabelle.swt().getColumnCount(); i++) if (tabelle.swt().getColumn(i) == swt()) return i;
        throw Fehler.sonstige.da_Untypisch();
    }
}
