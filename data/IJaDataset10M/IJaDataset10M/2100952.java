package org.fudaa.ebli.courbe;

import org.fudaa.ebli.trace.TraceLigneModel;

/**
 * Marqueur de trac� de courbes.
 *
 * Un marqueur est trac� suivant une ligne verticale ou horizontale (suivant
 * son axe de r�f�rence). Il contient une valeur, un boolean d'affichage et un
 * traceligne Model. Il contient un boolean pour indiquer si on affiche sur l'axe
 * des y ou x. Il contient �galement un titre pour indiquer son nom. Si aucun
 * titre donn�, la valeur de sa position sur l'axe est indiqu�e.<p>
 *
 * Remarque : Le marqueur est facile � persister.
 * @author Adrien Hadoux
 * @see EGCourbe
 */
public class EGCourbeMarqueur {

    double value_;

    boolean view_ = false;

    TraceLigneModel model_;

    boolean traceHorizontal_ = true;

    /** Titre : Peut �tre null. */
    String title_;

    public EGCourbeMarqueur(double value, String _title, boolean view, TraceLigneModel model, boolean traceH) {
        super();
        this.value_ = value;
        this.view_ = view;
        this.model_ = model;
        traceHorizontal_ = traceH;
        title_ = _title;
    }

    public EGCourbeMarqueur(double value, boolean view, TraceLigneModel model, boolean traceH) {
        this(value, null, view, model, traceH);
    }

    public double getValue() {
        return value_;
    }

    public void setValue(double value) {
        this.value_ = value;
    }

    public boolean isVisible() {
        return view_;
    }

    public void setVisible(boolean view) {
        this.view_ = view;
    }

    public TraceLigneModel getModel() {
        return model_;
    }

    public void setModel(TraceLigneModel model) {
        this.model_ = model;
    }

    public boolean isTraceHorizontal() {
        return traceHorizontal_;
    }

    public void setTraceHorizontal(boolean traceHorizontal) {
        this.traceHorizontal_ = traceHorizontal;
    }

    public void setTitle(String _title) {
        title_ = _title;
    }

    public String getTitle() {
        return title_;
    }

    public EGCourbeMarqueur duplique() {
        return new EGCourbeMarqueur(this.value_, view_, model_, traceHorizontal_);
    }
}
