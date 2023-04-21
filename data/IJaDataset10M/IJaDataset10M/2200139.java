package org.fudaa.fudaa.vag;

import java.awt.Dimension;
import org.fudaa.dodico.corba.vag.SResultats05;
import org.fudaa.dodico.corba.vag.SResultatsInterOrth;
import org.fudaa.ebli.calque.BCalquePolyligne;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrPolyligne;

/**
 * Un calque de resultats de Vag.
 *
 * @version      $Revision: 1.7 $ $Date: 2007-06-28 09:28:18 $ by $Author: deniger $
 * @author       Axel von Arnim
 */
public class VagCalqueOrthogonales extends BCalquePolyligne {

    public VagCalqueOrthogonales() {
        super();
        resultats05_ = null;
        resultatsInterOrth_ = null;
        setPreferredSize(new Dimension(640, 480));
    }

    private SResultats05 resultats05_;

    SResultats05 getResultats05() {
        return resultats05_;
    }

    public void setResultats05(SResultats05 _resultats) {
        SResultats05 vp = resultats05_;
        resultats05_ = _resultats;
        reinitialise();
        construitPolylignes();
        firePropertyChange("resultats05", vp, resultats05_);
    }

    private SResultatsInterOrth resultatsInterOrth_;

    SResultatsInterOrth getResultatsInterOrth() {
        return resultatsInterOrth_;
    }

    public void setResultatsInterOrth(SResultatsInterOrth _resultats) {
        SResultatsInterOrth vp = resultatsInterOrth_;
        resultatsInterOrth_ = _resultats;
        firePropertyChange("resultatsInterOrth", vp, resultatsInterOrth_);
    }

    private void construitPolylignes() {
        if (resultats05_.orthogonales == null) return;
        for (int i = 0; i < resultats05_.orthogonales.length; i++) {
            GrPolyligne nouvLigne = new GrPolyligne();
            for (int j = 0; j < resultats05_.orthogonales[i].pas.length; j++) nouvLigne.sommets_.ajoute(new GrPoint(resultats05_.orthogonales[i].pas[j].pointCourantX, resultats05_.orthogonales[i].pas[j].pointCourantY, 0.));
            ajoute(nouvLigne);
        }
    }
}
