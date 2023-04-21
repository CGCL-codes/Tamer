package de.mnit.basis.swt.element.steuer;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import de.mnit.basis.daten.struktur.schluessel.S_SchluesselListe;
import de.mnit.basis.fehler.Fehler;
import de.mnit.basis.swt.dnd.A_DND_Ziel;
import de.mnit.basis.swt.dnd.DNDART;
import de.mnit.basis.swt.dnd.DNDTYP;
import de.mnit.basis.swt.element.A_SWT_Element;
import de.mnit.basis.swt.element.menue.SWT_Menue_PopUp;
import de.mnit.basis.swt.konstant.SWT_SCHRIFT;
import de.mnit.basis.swt.konstant.SWT_SCHRIFTSTIL;
import de.mnit.basis.swt.statisch.FarbManager;
import de.mnit.basis.swt.statisch.SchriftManager;
import de.mnit.basis.sys.event.S_Verarbeite;

/**
 * @author Michael Nitsche
 */
public abstract class A_SWT_Steuer<TA extends A_SWT_Steuer<?, ?>, TB extends Control> extends A_SWT_Element<TA, TB> {

    private boolean rahmen = false;

    private boolean dndquelle = false;

    private boolean dndziel = false;

    @SuppressWarnings("unchecked")
    public TA s1_Rahmen(boolean w) {
        this.rahmen = w;
        return (TA) this;
    }

    public void sTip(final String tooltip) {
        Fehler.objekt.wenn_Null(tooltip, 1);
        wennErzeugt(new S_Verarbeite() {

            public void verarbeite() {
                swt().setToolTipText(tooltip);
            }
        });
    }

    public void sDND_Quelle(final de.mnit.basis.swt.dnd.A_DND_Quelle quelle) {
        Fehler.objekt.wenn_Null(quelle);
        if (dndquelle) Fehler.objekt.da_HierUngueltig("Dieses Element wurde bereits als DND-Quelle definiert!");
        this.dndquelle = true;
        int artWert = DNDART.zuWert(quelle.gArten());
        DragSource source = new DragSource(swt(), artWert);
        source.setTransfer(DNDTYP.gTransfers(quelle.gTypen()));
        source.addDragListener(new DragSourceListener() {

            private DNDTYP typ = null;

            public void dragSetData(DragSourceEvent event) {
                typ = DNDTYP.gTyp(event.dataType);
                event.data = quelle.uebergabe(typ, t);
                if (event.data == null) {
                    event.detail = DND.DROP_NONE;
                    event.doit = false;
                    return;
                }
                if (typ == DNDTYP.DATEI && event.data instanceof String) {
                    event.data = new String[] { "" + event.data };
                    Fehler.zahl.wenn_ZuKlein(1, ((String[]) event.data).length);
                }
                Fehler.objekt.wenn_KlasseFalsch(event.data, typ.klasse);
            }

            public void dragStart(DragSourceEvent event) {
                event.doit = quelle.zielOk(t);
            }

            public void dragFinished(DragSourceEvent event) {
                DNDART art = DNDART.zuArt(event.detail);
                quelle.abschluss(typ, art, t);
            }
        });
    }

    public void sDND_Ziel(final A_DND_Ziel ziel) {
        Fehler.objekt.wenn_Null(ziel);
        if (dndziel) Fehler.objekt.da_HierUngueltig("Dieses Element wurde bereits als DND-Ziel definiert!");
        this.dndziel = true;
        int artWert = ziel.gibArtWert();
        DropTarget target = new DropTarget(swt(), artWert);
        target.setTransfer(DNDTYP.gTransfers(ziel.gTypen()));
        target.addDropListener(new DropTargetListener() {

            public void dragEnter(DropTargetEvent event) {
                event.detail = iDefault(event.detail, event.operations);
                S_SchluesselListe<DNDTYP, TransferData> moeglich = DNDTYP.gTypen(event.dataTypes);
                DNDTYP favtyp = ziel.gibFavTyp(moeglich.gSchluessel());
                event.currentDataType = moeglich.gib(favtyp);
                if (event.currentDataType == null) event.detail = DND.DROP_NONE;
            }

            public void dragOperationChanged(DropTargetEvent event) {
                event.detail = iDefault(event.detail, event.operations);
            }

            public void dragOver(DropTargetEvent event) {
                DNDART art = ziel.zuArtWert(event.detail);
                DNDTYP typ = DNDTYP.gTyp(event.currentDataType);
                ziel.ueber(typ, art, event);
            }

            public void drop(DropTargetEvent event) {
                DNDART art = ziel.zuArtWert(event.detail);
                DNDTYP typ = DNDTYP.gTyp(event.currentDataType);
                ziel.aktion(typ, art, event.data, event);
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dropAccept(DropTargetEvent event) {
            }

            private int iDefault(int detail, int operations) {
                if (detail == DND.DROP_DEFAULT) {
                    DNDART art = ziel.gibFavArt(operations);
                    return art != null ? art.dnd : DND.DROP_NONE;
                } else return detail;
            }
        });
    }

    public void sAktiviert(boolean w) {
        swt().setEnabled(w);
    }

    public void sSichtbar(boolean w) {
        swt().setVisible(w);
    }

    public void sHintergrund(final int... rgb) {
        wennErzeugt(new S_Verarbeite() {

            public void verarbeite() {
                Fehler.zahl.wenn_Nicht(3, rgb.length);
                Fehler.zahl.wenn_Ausserhalb(0, 255, rgb[0]);
                Fehler.zahl.wenn_Ausserhalb(0, 255, rgb[1]);
                Fehler.zahl.wenn_Ausserhalb(0, 255, rgb[2]);
                Color c = FarbManager.gFarbe(null, rgb[0], rgb[1], rgb[2]);
                swt().setBackground(c);
            }
        });
    }

    public void sVordergrund(int... rgb) {
        Fehler.zahl.wenn_Nicht(3, rgb.length);
        Fehler.zahl.wenn_Ausserhalb(0, 255, rgb[0]);
        Fehler.zahl.wenn_Ausserhalb(0, 255, rgb[1]);
        Fehler.zahl.wenn_Ausserhalb(0, 255, rgb[2]);
        Color c = FarbManager.gFarbe(rgb[0], rgb[1], rgb[2]);
        sVordergrund(c);
    }

    public void sVordergrund(Color c) {
        Fehler.objekt.wenn_Null(c);
        swt().setForeground(c);
    }

    public void sSchrift(SWT_SCHRIFT schrift, Integer groesse, SWT_SCHRIFTSTIL... stile) {
        swt().setFont(SchriftManager.gSchrift(swt().getDisplay(), schrift, groesse, stile));
    }

    public SWT_Menue_PopUp nMenu_PopUp() {
        SWT_Menue_PopUp popup = new SWT_Menue_PopUp();
        popup.erzeugeSWT(this.swt());
        swt().setMenu(popup.swt());
        return popup;
    }

    public boolean gAktiviert() {
        return swt().getEnabled();
    }

    public boolean gSichtbar() {
        return swt().getVisible();
    }

    public Rectangle gRahmen() {
        return swt().getBounds();
    }

    public Point gGroesse() {
        return swt().getSize();
    }

    public int[] gHintergrund() {
        Color c = swt().getBackground();
        int[] erg = new int[] { c.getRed(), c.getGreen(), c.getBlue() };
        c.dispose();
        return erg;
    }

    public int[] gVordergrund() {
        Color c = swt().getForeground();
        int[] erg = new int[] { c.getRed(), c.getGreen(), c.getBlue() };
        c.dispose();
        return erg;
    }

    public boolean gMausInnerhalb() {
        return gMausInnerhalb(swt().getBounds());
    }

    public boolean gMausInnerhalb(Rectangle bereich) {
        PointerInfo info = MouseInfo.getPointerInfo();
        java.awt.Point maus = info.getLocation();
        boolean maus_innerhalb = bereich.contains(swt().toControl(maus.x, maus.y));
        return maus_innerhalb;
    }

    public void eMausKlick(Listener aktion) {
        swt().addListener(SWT.MouseUp, aktion);
    }

    public void eMausDoppelKlick(Listener aktion) {
        swt().addListener(SWT.MouseDoubleClick, aktion);
    }

    public void eTaste(KeyListener aktion) {
        swt().addKeyListener(aktion);
    }

    public void ePosition(Listener aktion) {
        swt().addListener(SWT.Move, aktion);
    }

    public void eGroesse(Listener aktion) {
        swt().addListener(SWT.Resize, aktion);
    }

    protected TB roh(Widget basis, int style) {
        return roh((Composite) basis, style);
    }

    protected final int style2() {
        return this.rahmen ? style3() | SWT.BORDER : style3();
    }

    protected final void init2() {
        init3();
    }

    protected abstract TB roh(Composite basis, int style);

    protected abstract int style3();

    protected abstract void init3();
}
