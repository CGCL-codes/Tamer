package org.fhw.cabaweb.ojb;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.ojb.broker.metadata.FieldHelper;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.PersistenceBroker;
import org.fhw.cabaweb.ojb.abstracts.AbstractUseCase;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Untergruppierungsnamen;

/**
 * Abstrakte Klasse f&uuml;r die OJB Kapselung der Datenbankoperationen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 19.05.2004
 */
public class UseCaseErgebnissdatenUntergruppierungsnamen extends AbstractUseCase {

    /** Konstruktor
     * 
     * @param broker Instanz des Persistence Brokers
     */
    public UseCaseErgebnissdatenUntergruppierungsnamen(PersistenceBroker broker) {
        super(broker);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#erzeugen(java.lang.Object)
      */
    public final boolean erzeugen(Object arg) {
        Ergebnissdaten_Untergruppierungsnamen newErgebnissdaten_Untergruppierungsnamen = (Ergebnissdaten_Untergruppierungsnamen) arg;
        return anlegen(newErgebnissdaten_Untergruppierungsnamen);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#editieren(java.lang.Object)
      */
    public final boolean editieren(Object arg) {
        boolean retval = true;
        Ergebnissdaten_Untergruppierungsnamen editErgebnissdaten_UntergruppierungsnamenTemp = (Ergebnissdaten_Untergruppierungsnamen) arg;
        Ergebnissdaten_Untergruppierungsnamen editErgebnissdaten_Untergruppierungsnamen = null;
        Collection liste = null;
        Criteria criteria = new Criteria();
        if (editErgebnissdaten_UntergruppierungsnamenTemp.getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", editErgebnissdaten_UntergruppierungsnamenTemp.getUntergruppierungsnummer());
        if (editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen() != null && editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen() != null && editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen().getProjekte() != null && editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", editErgebnissdaten_UntergruppierungsnamenTemp.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        liste = sucheQBC(Ergebnissdaten_Untergruppierungsnamen.class, criteria, null);
        if (liste != null && liste.size() == 1) editErgebnissdaten_Untergruppierungsnamen = (Ergebnissdaten_Untergruppierungsnamen) liste.iterator().next();
        editErgebnissdaten_Untergruppierungsnamen.setUntergruppierungsname(editErgebnissdaten_UntergruppierungsnamenTemp.getUntergruppierungsname());
        retval = beginTransaction();
        if (retval != false) retval = storeAndEndTransaction(editErgebnissdaten_Untergruppierungsnamen);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#loeschen(java.lang.Object)
      */
    public final boolean loeschen(Object arg) {
        boolean retval = true;
        Ergebnissdaten_Untergruppierungsnamen deleteErgebnissdaten_Untergruppierungsnamen = (Ergebnissdaten_Untergruppierungsnamen) arg;
        Collection liste = null;
        Ergebnissdaten_Untergruppierungsnamen objekt = null;
        Criteria criteria = new Criteria();
        if (deleteErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", deleteErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsnummer());
        if (deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen() != null && deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen() != null && deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte() != null && deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", deleteErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        liste = sucheQBC(Ergebnissdaten_Untergruppierungsnamen.class, criteria, null);
        if (liste != null && liste.size() == 1) objekt = (Ergebnissdaten_Untergruppierungsnamen) liste.iterator().next();
        retval = beginTransaction();
        if (retval != false) retval = deleteAndEndTransaction(objekt);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekt(java.lang.Object)
      */
    public final Object sucheObjekt(Object arg) {
        Ergebnissdaten_Untergruppierungsnamen suchErgebnissdaten_Untergruppierungsnamen = (Ergebnissdaten_Untergruppierungsnamen) arg;
        Collection liste = null;
        Object rueckgabeWert = null;
        Criteria criteria = new Criteria();
        if (suchErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", suchErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsnummer());
        if (suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte() != null && suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        liste = sucheQBC(Ergebnissdaten_Untergruppierungsnamen.class, criteria, null);
        if (liste != null && liste.size() == 1) rueckgabeWert = (Ergebnissdaten_Untergruppierungsnamen) liste.iterator().next();
        return rueckgabeWert;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekte(java.lang.Object)
      */
    public final Collection sucheObjekte(Object arg) {
        Ergebnissdaten_Untergruppierungsnamen suchErgebnissdaten_Untergruppierungsnamen = (Ergebnissdaten_Untergruppierungsnamen) arg;
        Collection rueckgabeWert = null;
        ArrayList orderBy = new ArrayList();
        Criteria criteria = new Criteria();
        if (suchErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", suchErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsnummer());
        if (suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte() != null && suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", suchErgebnissdaten_Untergruppierungsnamen.getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        if (suchErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsname() != null) criteria.addEqualTo("Untergruppierungsname", suchErgebnissdaten_Untergruppierungsnamen.getUntergruppierungsname());
        orderBy.add(0, new FieldHelper("Untergruppierungsnummer", true));
        orderBy.add(1, new FieldHelper("Gruppierungsnummer", true));
        orderBy.add(2, new FieldHelper("Projektnummer", true));
        rueckgabeWert = sucheQBC(Ergebnissdaten_Untergruppierungsnamen.class, criteria, orderBy);
        return rueckgabeWert;
    }
}
