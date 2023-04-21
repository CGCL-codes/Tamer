package org.fudaa.ebli.calque;

import org.fudaa.ebli.geometrie.GrSegment;

/**
 * @author Fred Deniger
 * @version $Id: ZModeleSegment.java,v 1.7 2007-06-05 08:58:38 deniger Exp $
 */
public interface ZModeleSegment extends ZModeleDonnees {

    /**
   * Dans certains cas, les modeles peuvent utiliser des filtres. Dans ce cas , cette methode renvoie false et n'affecte
   * pas le segment _s. Pour forcer l'affectation, mettre _force � true.
   *
   * @param _s le segment a modifier
   * @param _i l'indice voulu
   * @param _force si true, le segment est toujours initialise meme si le filtre du modele l'interdit
   * @return true si affectation correcte et si filtre
   */
    boolean segment(GrSegment _s, int _i, boolean _force);

    /**
   * @param _i l'indice du segment
   * @return la norme de ce segment.
   */
    double getNorme(int _i);

    /**
   * @param _i l'indice du segment
   * @return la composante selon x de ce segment.
   */
    double getVx(int _i);

    /**
   * @param _i l'indice du segment
   * @return la composante selon y de ce segment.
   */
    double getVy(int _i);

    /**
   * @param _i l'indice du segment
   * @return l'abscisse du point de d�part du segment
   */
    double getX(int _i);

    /**
   * @param _i l'indice du segment
   * @return l'ordonn�e du point de d�part du segment
   */
    double getY(int _i);

    /**
   * Operation optionnelle: utilisee si les segments sont des ar�tes avec une cote.
   *
   * @param _i l'indice du segment
   * @return le z du point origine
   */
    double getZ1(int _i);

    /**
   * Operation optionnelle: utilisee si les segments sont des ar�tes avec une cote.
   *
   * @param _i l'indice du segment
   * @return le z du point de destination
   */
    double getZ2(int _i);
}
