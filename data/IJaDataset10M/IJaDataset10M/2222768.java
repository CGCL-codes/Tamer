package org.fhw.cabaweb.data;

import java.util.Collection;
import org.fhw.cabaweb.data.abstracts.AbstractDataInterface;
import org.fhw.cabaweb.ojb.dataobjects.Projektbeschreibungen;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.dataobjects.Sprachen;
import org.fhw.cabaweb.ojb.interfaces.UseCase;
import org.fhw.cabaweb.ojb.UseCaseProjektbeschreibungen;

/**
 * Klasse f&uuml;r die Kapselung der Datenzugriffe auf Projektbeschreibungen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 07.05.2004
 */
public class DataInterfaceProjektbeschreibungen extends AbstractDataInterface {

    /** Konstruktor
     *  benutzt den Konstruktor der Superklasse und initialisiert das useCase Objekt
     */
    public DataInterfaceProjektbeschreibungen() {
        super();
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#erzeugen(java.lang.Object)
     */
    public boolean erzeugen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseProjektbeschreibungen(broker);
        rueckgabewert = useCase.erzeugen((Projektbeschreibungen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#editieren(java.lang.Object)
     */
    public boolean editieren(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseProjektbeschreibungen(broker);
        rueckgabewert = useCase.editieren((Projektbeschreibungen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#loeschen(java.lang.Object)
     */
    public boolean loeschen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseProjektbeschreibungen(broker);
        rueckgabewert = useCase.loeschen((Projektbeschreibungen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht ein bestimmtes Projekt anhand der Projektnummer und/oder der Sprachnummer
     * und gibt alle in Frage kommenden Objekte als Collection zur�ck
     *
     * @param projektnummer   Die Projektnummer als Integer
     * @param sprachnummer    Die Sprachnummer als Integer
     * @return Eine Collection aller Projektbeschreibungen Datenobjekte die zutreffen
     */
    public Collection sucheKombination(Integer projektnummer, Integer sprachnummer) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseProjektbeschreibungen(broker);
        rueckgabewert = sucheObjekte(new Projektbeschreibungen(new Projekte(projektnummer, null, null, null), new Sprachen(sprachnummer, null, null), null, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheObjekt(java.lang.Object)
     */
    protected Object sucheObjekt(Object arg) {
        return (Projektbeschreibungen) useCase.sucheObjekt(arg);
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
        this.useCase = (UseCase) new UseCaseProjektbeschreibungen(broker);
        rueckgabewert = useCase.sucheAlle(Projektbeschreibungen.class);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#clearCache()
     */
    public boolean clearCache() {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseProjektbeschreibungen(broker);
        rueckgabewert = useCase.clearCache();
        tearDown();
        return rueckgabewert;
    }
}
