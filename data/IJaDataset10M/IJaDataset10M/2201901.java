package org.fudaa.ctulu;

/**
 * @author Fred Deniger
 * @version $Id: CtuluDoubleArrayListInterface.java,v 1.4 2004-12-09 12:56:09 deniger Exp $
 */
public interface CtuluDoubleArrayListInterface {

    /**
   * @return la taille
   */
    int getNombre();

    /**
   * @param _i l'indice [0;size()[
   * @return la valeur en ce point
   */
    double getValue(int _i);
}
