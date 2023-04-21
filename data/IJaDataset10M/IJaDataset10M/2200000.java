package org.fudaa.fudaa.tr.post.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.fudaa.ctulu.CtuluParser;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.fudaa.tr.post.TrPostSource;
import org.nfunk.jep.Variable;

public final class TrPostDataHelper {

    private TrPostDataHelper() {
    }

    public static Map<String, H2dVariableType> getShortNameVariable(final TrPostSource _src) {
        final H2dVariableType[] var = _src.getAllVariablesNonVec();
        if (var == null) {
            return Collections.emptyMap();
        }
        final Map<String, H2dVariableType> res = new HashMap<String, H2dVariableType>(var.length);
        for (int i = var.length - 1; i >= 0; i--) {
            res.put(var[i].getShortName(), var[i]);
        }
        return res;
    }

    private static void createData(final TrPostDataCreatedExpr _dest, final CtuluParser _expr, final Map _varvar2d) {
        Variable[] vs = _expr.findUsedVar();
        if (vs == null) {
            vs = new Variable[0];
        }
        final H2dVariableType[] var2d = new H2dVariableType[vs.length];
        for (int i = vs.length - 1; i >= 0; i--) {
            var2d[i] = (H2dVariableType) _varvar2d.get(vs[i]);
        }
        _expr.clearUnusedVar();
        _dest.expr_ = _expr;
        _dest.var_ = var2d;
        _dest.usedVar_ = vs;
    }

    public static void addDependingVarOf(final H2dVariableType _vi, final TrPostSource _src, final Set _res) {
        if (_src != null && _src.isUserCreatedVar(_vi)) {
            _src.getUserCreatedVar(_vi).fillWhithAllUsedVar(_res);
        }
    }

    /**
   * @param _expr l'expression
   * @param _varvar2d la table Variable ->H2DVariable2D
   * @return un nouveau fournisseur de donn�es associ�es
   */
    public static TrPostDataCreatedExpr createData(final TrPostSource _src, final CtuluParser _expr, final Map _varvar2d) {
        final TrPostDataCreatedExpr r = new TrPostDataCreatedExpr(_src, null, null, null);
        createData(r, _expr, _varvar2d);
        return r;
    }

    /**
   * @param _userVar la variable a tester
   * @param _src la source
   * @return la liste des variables dependantes de <code>_userVar</code>
   */
    public static Set getAllVarDependingOn(final H2dVariableType _userVar, final TrPostSource _src) {
        final H2dVariableType[] userVar = _src.getUserCreatedVar();
        if (userVar != null) {
            final HashSet r = new HashSet(userVar.length);
            final Set s = new HashSet();
            for (int i = userVar.length - 1; i >= 0; i--) {
                final TrPostDataCreated exp = _src.getUserCreatedVar(userVar[i]);
                s.clear();
                exp.fillWhithAllUsedVar(s);
                if (s.contains(_userVar)) {
                    r.add(userVar[i]);
                }
            }
            return r;
        }
        return Collections.EMPTY_SET;
    }

    /**
   * Enleve de la liste _setToModify toutes les variables dependantes de _userVar.
   * 
   * @param _setToModify
   * @param _userVar
   * @param _src
   * @return true si la variable est utilisee
   */
    public static boolean removeUserVarDepending(final Set _setToModify, final H2dVariableType _userVar, final TrPostSource _src) {
        final H2dVariableType[] userVar = _src.getUserCreatedVar();
        boolean r = false;
        if (userVar != null) {
            final Set s = new HashSet();
            for (int i = userVar.length - 1; i >= 0; i--) {
                if (_setToModify.contains(userVar[i])) {
                    final TrPostDataCreated exp = _src.getUserCreatedVar(userVar[i]);
                    s.clear();
                    exp.fillWhithAllUsedVar(s);
                    if (s.contains(_userVar)) {
                        r = true;
                        _setToModify.remove(userVar[i]);
                    }
                }
            }
        }
        return r;
    }
}
