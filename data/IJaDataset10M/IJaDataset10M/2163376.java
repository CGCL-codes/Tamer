package org.fudaa.dodico.h2d.rubar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.fudaa.dodico.h2d.type.H2dBoundaryType;
import org.fudaa.dodico.h2d.type.H2dRubarProjetType;
import org.fudaa.dodico.h2d.type.H2dVariableType;

/**
 * @author Fred Deniger
 * @version $Id: H2dRubarBoundaryType.java,v 1.7 2004-11-16 16:21:41 deniger Exp $
 */
public abstract class H2dRubarBoundaryType extends H2dBoundaryType {

    int rubarIdx_;

    /**
   * @param _n le nom du type de bord
   * @param isSolid true si solide
   * @param _rubarIdx l'index rubar
   */
    public H2dRubarBoundaryType(String _n, boolean isSolid, int _rubarIdx) {
        super(_n, isSolid);
        rubarIdx_ = _rubarIdx;
    }

    /**
   * Test si bord liquide non libre et non tarage
   * @return true si type demandant une courbe transitoire fonction du temps.
   */
    public boolean needsTimeCurve(H2dRubarProjetType _t) {
        return isLiquide() && (this != H2dRubarBcTypeList.LIBRE);
    }

    /**
   * @return la liste des variables autorisee pour ce type de bord.
   */
    public abstract List getAllowedVariable(H2dRubarProjetType _t);

    /**
   * @return la liste des variables qui doivent etre definies pour ce type de bord.
   */
    public List getRequiredVariable(H2dRubarProjetType _t) {
        List t = getAllowedVariable(_t);
        if (t == null) return null;
        List r = new ArrayList(t);
        for (Iterator it = r.iterator(); it.hasNext(); ) {
            if (isTorrentielVar((H2dVariableType) it.next())) it.remove();
        }
        return r;
    }

    /**
   * @param _t la variable tester
   * @return true si variable utilisee que dans le cas torrentiel
   */
    public boolean isTorrentielVar(H2dVariableType _t) {
        return false;
    }

    /**
   * @param _n le nom du type de bord
   * @param _rubarIdx l'index rubar
   */
    public H2dRubarBoundaryType(String _n, int _rubarIdx) {
        this(_n, false, _rubarIdx);
    }

    /**
   * @return l'index rubar
   */
    public int getRubarIdx() {
        if (rubarIdx_ == H2dRubarBcTypeList.MIXTE.rubarIdx_) {
            new Throwable().printStackTrace();
        }
        return rubarIdx_;
    }

    /**
   * @return true si appartiend a un groupe debit
   */
    public boolean isTypeDebitGlobal() {
        return false;
    }
}
