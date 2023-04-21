package org.fhw.cabaweb.data;

import java.util.Collection;
import org.fhw.cabaweb.data.abstracts.AbstractDataInterface;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Integer;
import org.fhw.cabaweb.ojb.dataobjects.Projektgruppen;
import org.fhw.cabaweb.ojb.interfaces.UseCase;
import org.fhw.cabaweb.ojb.UseCaseErgebnissdatenInteger;

/**
 * Klasse f&uuml;r die Kapselung der Datenzugriffe auf die Integer Ergebnissdaten
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 10.05.2004
 */
public class DataInterfaceErgebnissdatenInteger extends AbstractDataInterface {

    /** Konstruktor
     *  benutzt den Konstruktor der Superklasse und initialisiert das useCase Objekt
     */
    public DataInterfaceErgebnissdatenInteger() {
        super();
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#erzeugen(java.lang.Object)
     */
    public boolean erzeugen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseErgebnissdatenInteger(broker);
        rueckgabewert = useCase.erzeugen((Ergebnissdaten_Integer) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#editieren(java.lang.Object)
     */
    public boolean editieren(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseErgebnissdatenInteger(broker);
        rueckgabewert = useCase.editieren((Ergebnissdaten_Integer) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#loeschen(java.lang.Object)
     */
    public boolean loeschen(Object arg) {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseErgebnissdatenInteger(broker);
        rueckgabewert = useCase.loeschen((Ergebnissdaten_Integer) arg);
        tearDown();
        return rueckgabewert;
    }

    /**
     * Sucht alle Ergebnissdaten_Integer eines Projektes anhand der Projektnummer, Gruppennummer und Quartal
     * und gibt alle in Frage kommenden Objekte als Collection zur�ck
     *
     * @param   projektnummer    Die Projektnummer als Integer
     * @param   gruppennummer    Die Integer Gruppennummer
     * @param   quartal          Das Integer Quartal
     * @return  Eine Collection aller Ergebnissdaten_Integer Datenobjekte die zu dem angegebenen Projekt geh�ren
     */
    public Collection sucheKombination(Integer projektnummer, Integer gruppennummer, Integer quartal) {
        Collection rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseErgebnissdatenInteger(broker);
        rueckgabewert = sucheObjekte(new Ergebnissdaten_Integer(new Projektgruppen(gruppennummer, new Projekte(projektnummer, null, null, null), null, null, null), quartal));
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#sucheObjekt(java.lang.Object)
     */
    protected Object sucheObjekt(Object arg) {
        return (Ergebnissdaten_Integer) useCase.sucheObjekt(arg);
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
        this.useCase = (UseCase) new UseCaseErgebnissdatenInteger(broker);
        rueckgabewert = useCase.sucheAlle(Ergebnissdaten_Integer.class);
        tearDown();
        return rueckgabewert;
    }

    /**
     * @see org.fhw.cabaweb.data.abstracts.AbstractDataInterface#clearCache()
     */
    public boolean clearCache() {
        boolean rueckgabewert;
        setUp();
        this.useCase = (UseCase) new UseCaseErgebnissdatenInteger(broker);
        rueckgabewert = useCase.clearCache();
        tearDown();
        return rueckgabewert;
    }
}
