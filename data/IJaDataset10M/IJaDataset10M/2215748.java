package org.fudaa.ctulu.modele;

import org.fudaa.ctulu.CtuluRange;

/**
 * @author Fred Deniger
 * @version $Id: CtuluModelDoubleInterface.java,v 1.6 2006-02-09 18:50:27 deniger Exp $
 */
public interface CtuluModelDoubleInterface {

    /**
   * @return le tableau des double
   */
    double[] getValues();

    /**
   * @return la taille
   */
    int getSize();

    /**
   * @param _i l'indice [0;size()[
   * @return la valeur en ce point
   */
    double getValue(int _i);

    /**
   * @return la valeur minimale
   */
    double getMin();

    /**
   * @return la valeur max
   */
    double getMax();

    void expandTo(CtuluRange _rangeToExpand);

    /**
   * @param _i les indices a tester
   * @return la valeur commune ou nulle si aucune
   */
    Double getCommonValue(int[] _i);

    void iterate(CtuluDoubleVisitor _visitor);
}
