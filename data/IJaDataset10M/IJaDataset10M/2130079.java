package org.fudaa.ebli.courbe;

import java.util.List;
import org.fudaa.ebli.trace.TraceBox;
import org.fudaa.ebli.trace.TraceIconModel;
import org.fudaa.ebli.trace.TraceLigneModel;

/**
 * Classe persistante de la courbe
 * 
 * @author Adrien Hadoux
 */
public class EGCourbePersist extends EGPersist {

    String title_;

    int Idgroup;

    int id = -1;

    boolean nuagePoints = false;

    double Xmin;

    double Xmax;

    double Ymin;

    double Ymax;

    double[] abscisses;

    double[] ordonnees;

    TraceBox tracebox;

    TraceLigneModel lineModel_;

    TraceLigneModel tLigneMarqueur_;

    TraceIconModel iconeModel;

    Object dataSpecifiques;

    String classeModel;

    List<EGCourbeMarqueur> listeMarqueurs_;

    EGCourbeSurfacePersist surfacePainter;

    boolean inverse = false;

    public EGCourbePersist() {
    }

    public String getTitle() {
        return title_;
    }

    public void setTitle(String _title) {
        this.title_ = _title;
    }

    public int getIdgroup() {
        return Idgroup;
    }

    public void setIdgroup(int _idgroup) {
        Idgroup = _idgroup;
    }

    public int getSpecificIntValue(String string, int defaultValue) {
        Object o = getSpecificValue(string);
        if (o == null) return defaultValue;
        return ((Integer) o).intValue();
    }

    public boolean getSpecificBooleanValue(String string) {
        Object o = getSpecificValue(string);
        return Boolean.TRUE.equals(o);
    }

    /**
   * @return the id
   */
    protected int getId() {
        return id;
    }

    /**
   * @param id the id to set
   */
    protected void setId(int id) {
        this.id = id;
    }
}
