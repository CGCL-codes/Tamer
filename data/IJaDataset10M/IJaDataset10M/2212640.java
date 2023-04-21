package org.fudaa.dodico.crue.metier.emh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.fudaa.dodico.crue.config.CrueConfigMetier;
import org.fudaa.dodico.crue.metier.transformer.EnumToString;
import org.fudaa.dodico.crue.metier.transformer.ToStringTransformable;

/**
 *
 * @author deniger ( genesis)
 */
public class ResPrtReseauNoeuds implements ToStringTransformable {

    Calc calc;

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
    }

    private List<ResPrtReseauNoeud> resPrtReseauNoeud = null;

    public List<ResPrtReseauNoeud> getResPrtReseauNoeud() {
        return resPrtReseauNoeud;
    }

    public String getId() {
        return calc == null ? StringUtils.EMPTY : calc.getId();
    }

    @Override
    public String toString(CrueConfigMetier props, EnumToString format) {
        return getClass().getSimpleName() + " " + (calc == null ? "?" : calc.getNom());
    }

    /**
   * @param listResPrtReseauNoeud the listResPrtReseauNoeud to set
   */
    public void setResPrtReseauNoeud(List<ResPrtReseauNoeud> listResPrtReseauNoeud) {
        if (listResPrtReseauNoeud == null) {
            this.resPrtReseauNoeud = null;
        }
        this.resPrtReseauNoeud = Collections.unmodifiableList(new ArrayList<ResPrtReseauNoeud>(listResPrtReseauNoeud));
    }
}
