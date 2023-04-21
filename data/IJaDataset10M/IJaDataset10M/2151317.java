package org.fhw.cabaweb.ojb;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.ojb.broker.metadata.FieldHelper;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.PersistenceBroker;
import org.fhw.cabaweb.ojb.abstracts.AbstractUseCase;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Feldnamen;

/**
 * Abstrakte Klasse f&uuml;r die OJB Kapselung der Datenbankoperationen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 20.05.2004
 */
public class UseCaseErgebnissdatenFeldnamen extends AbstractUseCase {

    /** Konstruktor
     * 
     * @param broker Instanz des Persistence Brokers
     */
    public UseCaseErgebnissdatenFeldnamen(PersistenceBroker broker) {
        super(broker);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#erzeugen(java.lang.Object)
      */
    public final boolean erzeugen(Object arg) {
        Ergebnissdaten_Feldnamen newErgebnissdaten_Feldnamen = (Ergebnissdaten_Feldnamen) arg;
        return anlegen(newErgebnissdaten_Feldnamen);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#editieren(java.lang.Object)
      */
    public final boolean editieren(Object arg) {
        boolean retval = true;
        Ergebnissdaten_Feldnamen editErgebnissdaten_FeldnamenTemp = (Ergebnissdaten_Feldnamen) arg;
        Ergebnissdaten_Feldnamen editErgebnissdaten_Feldnamen = null;
        Collection liste = null;
        Criteria criteria = new Criteria();
        if (editErgebnissdaten_FeldnamenTemp.getFeldnummer() != null) criteria.addEqualTo("Feldnummer", editErgebnissdaten_FeldnamenTemp.getFeldnummer());
        if (editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen() != null && editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer());
        if (editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen() != null && editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen() != null && editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte() != null && editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", editErgebnissdaten_FeldnamenTemp.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        liste = sucheQBC(Ergebnissdaten_Feldnamen.class, criteria, null);
        if (liste != null && liste.size() == 1) editErgebnissdaten_Feldnamen = (Ergebnissdaten_Feldnamen) liste.iterator().next();
        editErgebnissdaten_Feldnamen.setReihenfolge(editErgebnissdaten_FeldnamenTemp.getReihenfolge());
        editErgebnissdaten_Feldnamen.setTabellenname(editErgebnissdaten_FeldnamenTemp.getTabellenname());
        editErgebnissdaten_Feldnamen.setFeldname(editErgebnissdaten_FeldnamenTemp.getFeldname());
        editErgebnissdaten_Feldnamen.setFeldname_benutzer(editErgebnissdaten_FeldnamenTemp.getFeldname_benutzer());
        retval = beginTransaction();
        if (retval != false) retval = storeAndEndTransaction(editErgebnissdaten_Feldnamen);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#loeschen(java.lang.Object)
      */
    public final boolean loeschen(Object arg) {
        boolean retval = true;
        Ergebnissdaten_Feldnamen deleteErgebnissdaten_Feldnamen = (Ergebnissdaten_Feldnamen) arg;
        Collection liste = null;
        Ergebnissdaten_Feldnamen objekt = null;
        Criteria criteria = new Criteria();
        if (deleteErgebnissdaten_Feldnamen.getFeldnummer() != null) criteria.addEqualTo("Feldnummer", deleteErgebnissdaten_Feldnamen.getFeldnummer());
        if (deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer());
        if (deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte() != null && deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", deleteErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        liste = sucheQBC(Ergebnissdaten_Feldnamen.class, criteria, null);
        if (liste != null && liste.size() == 1) objekt = (Ergebnissdaten_Feldnamen) liste.iterator().next();
        retval = beginTransaction();
        if (retval != false) retval = deleteAndEndTransaction(objekt);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekt(java.lang.Object)
      */
    public final Object sucheObjekt(Object arg) {
        Ergebnissdaten_Feldnamen suchErgebnissdaten_Feldnamen = (Ergebnissdaten_Feldnamen) arg;
        Collection liste = null;
        Object rueckgabeWert = null;
        Criteria criteria = new Criteria();
        if (suchErgebnissdaten_Feldnamen.getFeldnummer() != null) criteria.addEqualTo("Feldnummer", suchErgebnissdaten_Feldnamen.getFeldnummer());
        if (suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer());
        if (suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        liste = sucheQBC(Ergebnissdaten_Feldnamen.class, criteria, null);
        if (liste != null && liste.size() == 1) rueckgabeWert = (Ergebnissdaten_Feldnamen) liste.iterator().next();
        return rueckgabeWert;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekte(java.lang.Object)
      */
    public final Collection sucheObjekte(Object arg) {
        Ergebnissdaten_Feldnamen suchErgebnissdaten_Feldnamen = (Ergebnissdaten_Feldnamen) arg;
        Collection rueckgabeWert = null;
        ArrayList orderBy = new ArrayList();
        Criteria criteria = new Criteria();
        if (suchErgebnissdaten_Feldnamen.getFeldnummer() != null) criteria.addEqualTo("Feldnummer", suchErgebnissdaten_Feldnamen.getFeldnummer());
        if (suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer() != null) criteria.addEqualTo("Untergruppierungsnummer", suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getUntergruppierungsnummer());
        if (suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer() != null) criteria.addEqualTo("Gruppierungsnummer", suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getGruppierungsnummer());
        if (suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte() != null && suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", suchErgebnissdaten_Feldnamen.getErgebnissdaten_untergruppierungsnamen().getErgebnissdaten_gruppierungsnamen().getProjekte().getProjektnummer());
        if (suchErgebnissdaten_Feldnamen.getReihenfolge() != null) criteria.addEqualTo("Reihenfolge", suchErgebnissdaten_Feldnamen.getReihenfolge());
        if (suchErgebnissdaten_Feldnamen.getTabellenname() != null) criteria.addEqualTo("Tabellenname", suchErgebnissdaten_Feldnamen.getTabellenname());
        if (suchErgebnissdaten_Feldnamen.getFeldname() != null) criteria.addEqualTo("Feldname", suchErgebnissdaten_Feldnamen.getFeldname());
        if (suchErgebnissdaten_Feldnamen.getFeldname_benutzer() != null) criteria.addEqualTo("Feldname_benutzer", suchErgebnissdaten_Feldnamen.getFeldname_benutzer());
        orderBy.add(0, new FieldHelper("Feldnummer", true));
        orderBy.add(1, new FieldHelper("Untergruppierungsnummer", true));
        orderBy.add(2, new FieldHelper("Gruppierungsnummer", true));
        orderBy.add(3, new FieldHelper("Projektnummer", true));
        rueckgabeWert = sucheQBC(Ergebnissdaten_Feldnamen.class, criteria, orderBy);
        return rueckgabeWert;
    }
}
