package org.fhw.cabaweb.data;

import java.util.Collection;
import org.fhw.cabaweb.data.abstracts.AbstractDataInterface;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Untergruppierungsbeschreibungen;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Untergruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Gruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.dataobjects.Sprachen;
import org.fhw.cabaweb.ojb.interfaces.UseCase;
import org.fhw.cabaweb.ojb.UseCaseVoreinstellungenUntergruppierungsbeschreibungen;

/**
 * Klasse f&uuml;r die Kapselung der Datenzugriffe auf Voreinstellungen_Untergruppierungsbeschreibungen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 12.05.2004
 */
public class DataInterfaceVoreinstellungenUntergruppierungsbeschreibungen extends AbstractDataInterface {

    /** Konstruktor
     *  benutzt den Konstruktor der Superklasse und initialisiert das useCase Objekt
     */
    public DataInterfaceVoreinstellungenUntergruppierungsbeschreibungen() {
        super();
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#erzeugen(java.lang.Object)
     */
    public boolean erzeugen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsbeschreibungen(broker);
        rueckgabewert = useCase.erzeugen((Voreinstellungen_Untergruppierungsbeschreibungen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#editieren(java.lang.Object)
     */
    public boolean editieren(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsbeschreibungen(broker);
        rueckgabewert = useCase.editieren((Voreinstellungen_Untergruppierungsbeschreibungen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#loeschen(java.lang.Object)
     */
    public boolean loeschen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsbeschreibungen(broker);
        rueckgabewert = useCase.loeschen((Voreinstellungen_Untergruppierungsbeschreibungen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht alle Voreinstellungen_Untergruppierungsnamen eines Projektes anhand verschiedener Werte und
     * gibt alle in Frage kommenden Objekte als Collection zur�ck
     *
     * @param   projektnummer           Die Projektnummer als Integer
     * @param   gruppierungsnummer Die Integer Gruppierungsnummer
     * @param   untergruppierungsnummer Die Integer Untergruppierungsnummer
     * @param   sprachnummer Die Integer Sprachnummer
     * @return  Das Voreinstellungen_Untergruppierungsname Datenobjekt
     */
    public Collection sucheKombination(Integer projektnummer, Integer gruppierungsnummer, Integer untergruppierungsnummer, Integer sprachnummer) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsbeschreibungen(broker);
        rueckgabewert = sucheObjekte(new Voreinstellungen_Untergruppierungsbeschreibungen(new Voreinstellungen_Untergruppierungsnamen(untergruppierungsnummer, new Voreinstellungen_Gruppierungsnamen(gruppierungsnummer, new Projekte(projektnummer, null, null, null), null), null), new Sprachen(sprachnummer, null, null), null, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheObjekt(java.lang.Object)
     */
    protected Object sucheObjekt(Object arg) {
        return (Voreinstellungen_Untergruppierungsbeschreibungen) useCase.sucheObjekt(arg);
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheObjekte(java.lang.Object)
     */
    protected Collection sucheObjekte(Object arg) {
        return useCase.sucheObjekte(arg);
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheAlle()
     */
    public Collection sucheAlle() {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsbeschreibungen(broker);
        rueckgabewert = useCase.sucheAlle(Voreinstellungen_Untergruppierungsbeschreibungen.class);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#clearCache()
     */
    public boolean clearCache() {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsbeschreibungen(broker);
        rueckgabewert = useCase.clearCache();
        tearDown();
        return rueckgabewert;
    }
}
