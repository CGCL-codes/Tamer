package org.fudaa.ebli.calque.find;

import org.fudaa.ctulu.CtuluExpr;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ebli.calque.ZModeleDonnees;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.find.EbliFindExpressionDefault;
import org.fudaa.ebli.geometrie.GrBoite;
import org.nfunk.jep.Variable;

/**
 * @author Fred Deniger
 * @version $Id: CalqueFindExpression.java,v 1.6 2007-04-16 16:35:08 deniger Exp $
 */
public class CalqueFindExpression extends EbliFindExpressionDefault {

    protected final ZModeleDonnees data_;

    /**
   * @param _data
   */
    public CalqueFindExpression(final ZModeleDonnees _data) {
        super(_data.getNombre());
        data_ = _data;
    }

    public void initialiseExpr(final CtuluExpr _expr) {
        super.initialiseExpr(_expr);
        final GrBoite boite = data_.getDomaine();
        if (boite != null) {
            Variable var = _expr.addVar("envXMax", EbliLib.getS("La borne sup�rieure des abscisses"));
            var.setValue(CtuluLib.getDouble(boite.e_.x_));
            var.setIsConstant(true);
            var = _expr.addVar("envXMin", EbliLib.getS("La borne inf�rieure des abscisses"));
            var.setValue(CtuluLib.getDouble(boite.o_.x_));
            var.setIsConstant(true);
            var = _expr.addVar("envYMax", EbliLib.getS("La borne sup�rieure des ordonn�es"));
            var.setValue(CtuluLib.getDouble(boite.e_.y_));
            var.setIsConstant(true);
            var = _expr.addVar("envYMin", EbliLib.getS("La borne inf�rieure des ordonn�es"));
            var.setValue(CtuluLib.getDouble(boite.o_.y_));
            var.setIsConstant(true);
        }
    }

    public static Variable buildXNode(final CtuluExpr _expr) {
        return _expr.addVar("x", EbliLib.getS("Abscisse du noeud"));
    }

    public static Variable buildYNode(final CtuluExpr _expr) {
        return _expr.addVar("y", EbliLib.getS("Ordonn�e du noeud"));
    }

    public static Variable buildIdxNode(final CtuluExpr _expr) {
        return _expr.addVar("ndIdx", EbliLib.getS("Indice du noeud"));
    }

    public static Variable buildXPoint(final CtuluExpr _expr) {
        return _expr.addVar("x", EbliLib.getS("Abscisse du point"));
    }

    public static Variable buildYPoint(final CtuluExpr _expr) {
        return _expr.addVar("y", EbliLib.getS("Ordonn�e du point"));
    }

    public static Variable buildZPoint(final CtuluExpr _expr) {
        return _expr.addVar("z", EbliLib.getS("z"));
    }

    public static Variable buildIdxPoint(final CtuluExpr _expr) {
        return _expr.addVar("ndIdx", EbliLib.getS("Indice du point"));
    }
}
