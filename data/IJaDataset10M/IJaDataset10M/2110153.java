package org.fhw.cabaweb.data;

import java.util.Collection;
import org.fhw.cabaweb.data.abstracts.AbstractDataInterface;
import org.fhw.cabaweb.ojb.dataobjects.Sprachen;
import org.fhw.cabaweb.ojb.interfaces.UseCase;
import org.fhw.cabaweb.ojb.UseCaseSprachen;

/**
 * Klasse f&uuml;r die Kapselung der Datenzugriffe auf Sprachen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 07.05.2004
 */
public class DataInterfaceSprachen extends AbstractDataInterface {

    /** Konstruktor
     *  benutzt den Konstruktor der Superklasse und initialisiert das useCase Objekt
     */
    public DataInterfaceSprachen() {
        super();
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#erzeugen(java.lang.Object)
     */
    public boolean erzeugen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = useCase.erzeugen((Sprachen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#editieren(java.lang.Object)
     */
    public boolean editieren(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = useCase.editieren((Sprachen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#loeschen(java.lang.Object)
     */
    public boolean loeschen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = useCase.loeschen((Sprachen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht einer bestimmten Sprache anhand der Sprachnummer
     *
     * @param sprachnummer    Die Integer Sprachnummer
     * @return Das Sprachen Datenobjekt
     */
    public Object sucheSprachnummer(Integer sprachnummer) {
        Object rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = sucheObjekt(new Sprachen(sprachnummer, null, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht einer bestimmten Sprache anhand des Projektnamens und gibt alle in Frage kommenden
     * Objekte als Collection zur�ck
     *
     * @param sprachname    Der Sprachname als String
     * @return Eine Collection aller Sprachen Datenobjekte die einen solchen Namen enthalten
     */
    public Collection sucheSprachname(String sprachname) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = sucheObjekte(new Sprachen(null, sprachname, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheObjekt(java.lang.Object)
     */
    protected Object sucheObjekt(Object arg) {
        return (Sprachen) useCase.sucheObjekt(arg);
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
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = useCase.sucheAlle(Sprachen.class);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#clearCache()
     */
    public boolean clearCache() {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseSprachen(broker);
        rueckgabewert = useCase.clearCache();
        tearDown();
        return rueckgabewert;
    }
}
