package org.fudaa.ebli.find;

import org.fudaa.ctulu.CtuluExpr;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ebli.commun.EbliLib;
import org.nfunk.jep.Variable;

/**
 * @author Fred Deniger
 * @version $Id: EbliFindExpressionDefault.java 6325 2011-07-08 13:26:28Z bmarchan $
 */
public class EbliFindExpressionDefault implements EbliFindExpressionContainerInterface {

    final Double nbObject_;

    Variable i_;

    public EbliFindExpressionDefault(final int _nbObject) {
        nbObject_ = CtuluLib.getDouble(_nbObject);
    }

    protected Variable createIndexVariable(final CtuluExpr _expr) {
        return _expr.addVar("i", EbliLib.getS("l'indice de l'objet"));
    }

    protected Variable createNbObjectVariable(final CtuluExpr _expr) {
        return _expr.addVar("nb", EbliLib.getS("Nombre d'objets"));
    }

    public void initialiseExpr(final CtuluExpr _expr) {
        _expr.initVar();
        _expr.getParser().getFunctionTable().remove("str");
        i_ = createIndexVariable(_expr);
        final Variable nb = createNbObjectVariable(_expr);
        nb.setValue(nbObject_);
        nb.setIsConstant(true);
    }

    public void majVariable(final int _idx, final Variable[] _varToUpdate) {
        i_.setValue(new Integer(_idx + 1));
    }
}
