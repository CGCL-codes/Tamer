package de.bwb.ekp.entities;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import de.bwb.ekp.interceptors.MeasureCalls;

/**
 * Liefert alle auschreibenden Stellen. Keine Einschr&auml;kung der
 * Ergebnisliste.
 * 
 * @author Dorian Gloski
 */
@MeasureCalls
@Name("stelleList")
public class StelleList extends EntityQuery {

    private static final long serialVersionUID = 1L;

    private String directory;

    public StelleList() {
        this.setOrder("stelleBezeichnung");
    }

    @Override
    public String getEjbql() {
        return "select stelle from Stelle stelle where stelle.geloescht is null";
    }

    public String getDirectory() {
        return this.directory;
    }
}
