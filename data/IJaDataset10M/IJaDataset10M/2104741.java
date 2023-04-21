package org.fhw.cabaweb.data;

import java.util.Collection;
import org.fhw.cabaweb.data.abstracts.AbstractDataInterface;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.dataobjects.Reportnamen;
import org.fhw.cabaweb.ojb.interfaces.UseCase;
import org.fhw.cabaweb.ojb.UseCaseReportnamen;

/**
 * Klasse f&uuml;r die Kapselung der Datenzugriffe auf Projektgruppen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 07.05.2004
 */
public class DataInterfaceReportnamen extends AbstractDataInterface {

    /** Konstruktor
     *  benutzt den Konstruktor der Superklasse und initialisiert das useCase Objekt
     */
    public DataInterfaceReportnamen() {
        super();
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#erzeugen(java.lang.Object)
     */
    public boolean erzeugen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = useCase.erzeugen((Reportnamen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#editieren(java.lang.Object)
     */
    public boolean editieren(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = useCase.editieren((Reportnamen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#loeschen(java.lang.Object)
     */
    public boolean loeschen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = useCase.loeschen((Reportnamen) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht eine bestimmte Reportnamen anhand der Reportnummer
     *
     * @param   reportnummer    Die Integer Reportnummer
     * @return  Das Projektgruppen Datenobjekt
     */
    public Object sucheReportnummer(Integer reportnummer) {
        Object rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = sucheObjekt(new Reportnamen(null, reportnummer, null, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht alle Reportnamen eines Projektes anhand des Projektnummer und gibt alle in Frage kommenden
     * Objekte als Collection zur�ck
     *
     * @param   projektnummer    Die Projektnummer als Integer
     * @return  Eine Collection aller Reportnamen Datenobjekte die zu dem angegebenen Projekt geh�ren
     */
    public Collection sucheProjektnummer(Integer projektnummer) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = sucheObjekte(new Reportnamen(new Projekte(projektnummer, null, null, null), null, null, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht eine bestimmte Reportnamen anhand der Bezeichnung des Reports und gibt alle in Frage kommenden
     * Objekte als Collection zur�ck
     *
     * @param   bezeichnung    Die Bezeichnung des Reports als String
     * @return  Eine Collection aller Reportnamen Datenobjekte die einen solchen Namen enthalten
     */
    public Collection sucheBezeichnung(String bezeichnung) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = sucheObjekte(new Reportnamen(null, null, bezeichnung, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht eine bestimmte Reportnamen anhand der Bezeichnung des Reports und gibt alle in Frage kommenden
     * Objekte als Collection zur�ck
     *
     * @param   projektnummer    Die Projektnummer als Integer
     * @param   bezeichnung    Die Bezeichnung des Reports als String
     * @return  Eine Collection aller Reportnamen Datenobjekte die einen solchen Namen enthalten
     */
    public Collection sucheBezeichnung(Integer projektnummer, String bezeichnung) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = sucheObjekte(new Reportnamen(new Projekte(projektnummer, null, null, null), null, bezeichnung, null));
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheObjekt(java.lang.Object)
     */
    protected Object sucheObjekt(Object arg) {
        return (Reportnamen) useCase.sucheObjekt(arg);
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
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = useCase.sucheAlle(Reportnamen.class);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#clearCache()
     */
    public boolean clearCache() {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseReportnamen(broker);
        rueckgabewert = useCase.clearCache();
        tearDown();
        return rueckgabewert;
    }
}
