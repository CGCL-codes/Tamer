package baus.modell.maschinen;

import java.awt.Point;
import java.util.ArrayList;
import baus.modell.EnumDim;
import baus.modell.EnumFraesung;
import baus.modell.EnumOberflaeche;
import baus.modell.Groesse;
import baus.modell.Material;
import baus.modell.Werkstueck;

/**
 * @author das BAUS! team
 */
@SuppressWarnings("serial")
public class Abrichte extends MaschineMitVorschub implements ParamSollMass {

    /**
     * Die von dieser Maschine bei der Bearbeitung von Werkstücken hergestellte
     * Oberfläche.
     */
    private final EnumOberflaeche hergestellteOberflaeche;

    /**
     * maximaler Abtrag in einem Hobelgang.
     */
    private final int maxAbtrag;

    /**
     * in mm, ändere Dim auf sollMass.
     */
    private int sollMass;

    /**
     * Erzeugt eine neue Abrichte.
     * @param name z.B. Abrichte
     * @param beschreibung z.B. Martin Abrichthobelmaschine T54
     * @param ausrichtung Die Ausrichtung für den Grundriss dieser Maschine
     * @param vorschub z.B. 5 m/min
     * @param maxAbtrag maximaler Abtrag in einem Hobelgang, z.B. 8 [mm]
     * @param maxGroesse 2000, 500, 2500
     * @param pos Die Position dieser Maschine
     */
    public Abrichte(final String name, final String beschreibung, final EnumAusrichtung ausrichtung, final int vorschub, final int maxAbtrag, final Groesse maxGroesse, final Point pos) {
        super(name, beschreibung, ausrichtung, vorschub, new EnumBefehl[] { EnumBefehl.ABRICHTEN, EnumBefehl.FASE }, new EnumDim[] { EnumDim.B, EnumDim.D }, maxGroesse, pos);
        hergestellteOberflaeche = EnumOberflaeche.GEHOBELT;
        einschaltDauer = 10;
        einstellDauer = 5;
        umruestDauer = 0;
        this.maxAbtrag = maxAbtrag;
        sollMass = getMinSollMass();
    }

    /**
     * @see baus.modell.maschinen.Maschine#getTyp()
     */
    @Override
    public final EnumMaschinenTyp getTyp() {
        return EnumMaschinenTyp.ABRICHTE;
    }

    /**
     * stellt das sollMass ein wenn sollMass geändert wird: wirdEingestellt =
     * true, sonst false.
     * @param soll das einzustellende sollMass
     */
    public final void setSollMass(final int soll) {
        if (eingabeStuecke.isEmpty() && soll > getMinSollMass() && soll < getMaxSollMass()) {
            if (sollMass != soll) {
                wirdEingestellt = true;
                sollMass = soll;
            } else {
                wirdEingestellt = false;
            }
            addMeldung("Das Soll Mass dieser Maschine wurde nun auf " + soll + "mm gesetzt.");
        }
    }

    /**
     * @see baus.modell.maschinen.ParamSollMass#getSollMass()
     */
    public final int getSollMass() {
        return sollMass;
    }

    /**
     * @see baus.modell.maschinen.ParamSollMass#getMinSollMass()
     */
    public final int getMinSollMass() {
        return maxGroesse.getMass(getZuBearbeitendeDim()) - maxAbtrag;
    }

    /**
     * @see baus.modell.maschinen.ParamSollMass#getMaxSollMass()
     */
    public final int getMaxSollMass() {
        return maxGroesse.getMass(getZuBearbeitendeDim()) - 1;
    }

    /**
     * Führt je nach dem an dieser Abrichte eingestelltem Befehl die zugehörige
     * Funktion an einem Werkstück der Eingabe aus.
     */
    @Override
    protected final void waehleBefehl() {
        if (!eingabeStuecke.isEmpty()) {
            final Material stueck = (Material) eingabeStuecke.remove(eingabeStuecke.size() - 1);
            switch(getBefehl()) {
                case ABRICHTEN:
                    {
                        abrichten(stueck);
                        break;
                    }
                case FASE:
                    {
                        fase(stueck);
                        break;
                    }
                default:
                    {
                        throw new IllegalStateException("Der Maschine " + getName() + " wurde ein ungültiger Befehl (" + getBefehl() + ") zugewiesen");
                    }
            }
        }
    }

    /**
     * Kürzt ein Material auf das an dieser Abrichte eingestellte Soll-Mass,
     * verändert dabei die Oberfläche des Materials und richtet es ab.
     * @param stueck Das abzurichtende Werkstück
     */
    private void abrichten(final Material stueck) {
        final EnumDim zuBearbeitendeDim = getZuBearbeitendeDim();
        stueck.getDim(zuBearbeitendeDim).kuerzeMassAuf(sollMass);
        stueck.getDim(zuBearbeitendeDim).setOberflaeche(hergestellteOberflaeche);
        stueck.getDim(zuBearbeitendeDim).setAbgerichtet(true);
        ausgabeStuecke.add(stueck);
    }

    /**
     * Fügt einem Material eine Fase hinzu.
     * @param stueck Das zu fasende Werkstück
     */
    private void fase(final Material stueck) {
        final EnumDim zuBearbeitendeDim = getZuBearbeitendeDim();
        stueck.getDim(zuBearbeitendeDim).addFraesung(EnumFraesung.FASE);
        ausgabeStuecke.add(stueck);
    }

    /**
     * Prüft ob die sollGroesse auf jedes Element von stuecke anwendbar ist.
     * Prüft ob alle Stücke Material sind. Prüft maxAbtrag.
     * @param stuecke zu prüfende Stücke
     * @return <code>true</code> wenn möglich, <code>false</code> wenn nicht
     *         möglich
     */
    @Override
    public final boolean pruefeStuecke(final ArrayList<Werkstueck> stuecke, final EnumBefehl testBefehl, final EnumDim testDim, final int testSoll) {
        if (stuecke == null || stuecke.isEmpty()) {
            addMeldung("Es sind keine Werkstücke zum Bearbeiten vorhanden!");
            return false;
        }
        try {
            switch(testDim) {
                case L:
                    {
                        for (final Werkstueck stueck : stuecke) {
                            if (stueck instanceof Material) {
                                final Material stueckAktuell = (Material) stueck;
                                if (stueckAktuell.getDicke() > maxGroesse.getMass(EnumDim.B) && stueckAktuell.getBreite() > maxGroesse.getMass(EnumDim.B)) {
                                    addMeldung("Ein Werkstück ist zu breit für diese Maschine!");
                                    return false;
                                }
                                if ((stueckAktuell.getLaenge() - maxAbtrag) > testSoll) {
                                    addMeldung("Die " + getName() + " kann in einem Hobelgang maximal " + maxAbtrag + "mm abtragen.");
                                    return false;
                                }
                                if (stueckAktuell.getLaenge() == testSoll) {
                                    addMeldung("Die Maschine muss mindestens einen Millimeter abtragen.");
                                    return false;
                                }
                            }
                        }
                        break;
                    }
                case B:
                    {
                        for (final Werkstueck stueck : stuecke) {
                            if (stueck instanceof Material) {
                                final Material stueckAktuell = (Material) stueck;
                                if (stueckAktuell.getDicke() > maxGroesse.getMass(EnumDim.B)) {
                                    addMeldung("Ein Werkstück ist zu dick für diese Maschine!");
                                    return false;
                                }
                                if ((stueckAktuell.getBreite() - maxAbtrag) > testSoll) {
                                    addMeldung("Die " + getName() + " kann in einem Hobelgang maximal " + maxAbtrag + "mm abtragen.");
                                    return false;
                                }
                                if (stueckAktuell.getBreite() == testSoll) {
                                    addMeldung("Die Maschine muss mindestens einen Millimeter abtragen.");
                                    return false;
                                }
                            }
                        }
                        break;
                    }
                case D:
                    {
                        for (final Werkstueck stueck : stuecke) {
                            if (stueck instanceof Material) {
                                final Material stueckAktuell = (Material) stueck;
                                if (stueckAktuell.getBreite() > maxGroesse.getMass(EnumDim.B)) {
                                    addMeldung("Ein Werkstück ist zu breit für diese Maschine!");
                                    return false;
                                }
                                if ((stueckAktuell.getDicke() - maxAbtrag) > testSoll) {
                                    addMeldung("Die " + getName() + " kann in einem Hobelgang maximal " + maxAbtrag + "mm abtragen.");
                                    return false;
                                }
                                if (stueckAktuell.getDicke() == testSoll) {
                                    addMeldung("Die Maschine muss mindestens einen Millimeter abtragen.");
                                    return false;
                                }
                            }
                        }
                        break;
                    }
            }
        } catch (final ClassCastException e) {
            addMeldung("Es können lediglich einfache Materialien bearbeitet werden, keine Baugruppen!");
            return false;
        }
        return true;
    }

    /**
     * @see baus.modell.maschinen.Maschine#setGrundriss(baus.modell.maschinen.EnumAusrichtung)
     */
    @Override
    protected MaschinenGrundriss setGrundriss(final EnumAusrichtung ausrichtung) {
        if (ausrichtung == null) {
            throw new IllegalArgumentException("Der Parameter 'ausrichtung' darf nicht 'null' sein!");
        }
        boolean[][] kontur = null;
        final Point eingabe = new Point();
        final Point ausgabe = new Point();
        switch(ausrichtung) {
            case NORD:
                {
                    kontur = new boolean[][] { { false, true, false }, { false, true, true }, { false, true, true }, { true, true, true }, { false, true, true }, { false, true, false } };
                    eingabe.setLocation(2, 0);
                    ausgabe.setLocation(4, 0);
                    break;
                }
            case OST:
                {
                    kontur = new boolean[][] { { false, true, true, true, true, false }, { true, true, true, true, true, true }, { false, false, false, true, false, false } };
                    eingabe.setLocation(2, 2);
                    ausgabe.setLocation(2, 4);
                    break;
                }
            case SUED:
                {
                    kontur = new boolean[][] { { false, true, false }, { true, true, false }, { true, true, true }, { true, true, false }, { true, true, false }, { false, true, false } };
                    eingabe.setLocation(3, 2);
                    ausgabe.setLocation(1, 2);
                    break;
                }
            case WEST:
                {
                    kontur = new boolean[][] { { false, false, true, false, false, false }, { true, true, true, true, true, true }, { false, true, true, true, true, false } };
                    eingabe.setLocation(0, 3);
                    ausgabe.setLocation(0, 1);
                    break;
                }
        }
        return new MaschinenGrundriss(ausrichtung, kontur, eingabe, ausgabe);
    }
}
