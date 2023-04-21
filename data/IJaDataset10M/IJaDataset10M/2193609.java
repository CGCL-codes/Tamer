package org.fudaa.fudaa.meshviewer.find;

import org.nfunk.jep.Variable;
import org.fudaa.ctulu.CtuluExpr;
import org.fudaa.ebli.calque.ZModeleDonnees;
import org.fudaa.ebli.calque.find.CalqueFindExpression;
import org.fudaa.fudaa.meshviewer.MvResource;

/**
 * @author Fred Deniger
 * @version $Id: MvExpressionSupplierDefault.java,v 1.4 2006-09-19 15:11:19 deniger Exp $
 */
public class MvExpressionSupplierDefault extends CalqueFindExpression {

    public MvExpressionSupplierDefault(final ZModeleDonnees _modele) {
        super(_modele);
    }

    public static Variable buildFrIdx(final CtuluExpr _expr) {
        return _expr.addVar("frIdx", MvResource.getS("Indice de la fronti�re contenant le noeud"));
    }

    public static Variable buildidxGlobalOnFr(final CtuluExpr _expr) {
        return _expr.addVar("frNdIdx", MvResource.getS("Indice du noeud dans la num�rotation fronti�re globale"));
    }

    public static Variable buildidxInFr(final CtuluExpr _expr) {
        return _expr.addVar("frNdLocalIdx", MvResource.getS("Indice du noeud dans sa fronti�re"));
    }
}
