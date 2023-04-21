package org.fhw.cabaweb.ojb;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.ojb.broker.metadata.FieldHelper;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.PersistenceBroker;
import org.fhw.cabaweb.ojb.abstracts.AbstractUseCase;
import org.fhw.cabaweb.ojb.dataobjects.Berechnungsauftraege;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;

/**
 * Abstrakte Klasse f&uuml;r die OJB Kapselung der Datenbankoperationen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 03.05.2004
 */
public class UseCaseBerechnungsauftraege extends AbstractUseCase {

    /** Konstruktor
     * 
     * @param broker Instanz des Persistence Brokers
     */
    public UseCaseBerechnungsauftraege(PersistenceBroker broker) {
        super(broker);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#erzeugen(java.lang.Object)
      */
    public final boolean erzeugen(Object arg) {
        Berechnungsauftraege newBerechnungsauftraege = (Berechnungsauftraege) arg;
        return anlegen(newBerechnungsauftraege);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#editieren(java.lang.Object)
      */
    public final boolean editieren(Object arg) {
        boolean retval = true;
        Berechnungsauftraege editBerechnungsauftraegeTemp = (Berechnungsauftraege) arg;
        Berechnungsauftraege editBerechnungsauftraege = null;
        Collection liste = null;
        Criteria criteria = new Criteria();
        if (((Projekte) editBerechnungsauftraegeTemp.getProjekte()).getProjektnummer() != null) criteria.addEqualTo("Projektnummer", (editBerechnungsauftraegeTemp.getProjekte()).getProjektnummer());
        liste = sucheQBC(Berechnungsauftraege.class, criteria, null);
        if (liste != null && liste.size() == 1) editBerechnungsauftraege = (Berechnungsauftraege) liste.iterator().next();
        editBerechnungsauftraege.setDatum(editBerechnungsauftraegeTemp.getDatum());
        editBerechnungsauftraege.setUhrzeit(editBerechnungsauftraegeTemp.getUhrzeit());
        editBerechnungsauftraege.setArtDerBerechnung(editBerechnungsauftraegeTemp.getArtDerBerechnung());
        editBerechnungsauftraege.setWiederholung(editBerechnungsauftraegeTemp.getWiederholung());
        retval = beginTransaction();
        if (retval != false) retval = storeAndEndTransaction(editBerechnungsauftraege);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#loeschen(java.lang.Object)
      */
    public final boolean loeschen(Object arg) {
        boolean retval = true;
        Berechnungsauftraege deleteBerechnungsauftraege = (Berechnungsauftraege) arg;
        Collection liste = null;
        Berechnungsauftraege objekt = null;
        Criteria criteria = new Criteria();
        if (((Projekte) deleteBerechnungsauftraege.getProjekte()).getProjektnummer() != null) criteria.addEqualTo("Projektnummer", (deleteBerechnungsauftraege.getProjekte()).getProjektnummer());
        liste = sucheQBC(Berechnungsauftraege.class, criteria, null);
        if (liste != null && liste.size() == 1) objekt = (Berechnungsauftraege) liste.iterator().next();
        retval = beginTransaction();
        if (retval != false) retval = deleteAndEndTransaction(objekt);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekt(java.lang.Object)
      */
    public final Object sucheObjekt(Object arg) {
        Berechnungsauftraege suchBerechnungsauftraege = (Berechnungsauftraege) arg;
        Collection liste = null;
        Object rueckgabeWert = null;
        Criteria criteria = new Criteria();
        if (((Projekte) suchBerechnungsauftraege.getProjekte()).getProjektnummer() != null) criteria.addEqualTo("Projektnummer", (suchBerechnungsauftraege.getProjekte()).getProjektnummer());
        liste = sucheQBC(Berechnungsauftraege.class, criteria, null);
        if (liste != null && liste.size() == 1) {
            rueckgabeWert = (Berechnungsauftraege) liste.iterator().next();
        }
        return rueckgabeWert;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekte(java.lang.Object)
      */
    public final Collection sucheObjekte(Object arg) {
        Berechnungsauftraege suchBerechnungsauftraege = (Berechnungsauftraege) arg;
        Collection rueckgabeWert = null;
        ArrayList orderBy = new ArrayList();
        Criteria criteria = new Criteria();
        if (suchBerechnungsauftraege.getProjekte() != null && ((Projekte) suchBerechnungsauftraege.getProjekte()).getProjektnummer() != null) criteria.addEqualTo("Projektnummer", (suchBerechnungsauftraege.getProjekte()).getProjektnummer());
        if (suchBerechnungsauftraege.getDatum() != null) criteria.addLessOrEqualThan("Datum", suchBerechnungsauftraege.getDatum());
        if (suchBerechnungsauftraege.getUhrzeit() != null) criteria.addLessOrEqualThan("Uhrzeit", suchBerechnungsauftraege.getUhrzeit());
        if (suchBerechnungsauftraege.getArtDerBerechnung() != null) criteria.addEqualTo("ArtDerBerechnung", suchBerechnungsauftraege.getArtDerBerechnung());
        if (suchBerechnungsauftraege.getWiederholung() != null) criteria.addEqualTo("Wiederholung", suchBerechnungsauftraege.getWiederholung());
        orderBy.add(0, new FieldHelper("Projektnummer", true));
        rueckgabeWert = sucheQBC(Berechnungsauftraege.class, criteria, orderBy);
        return rueckgabeWert;
    }
}
