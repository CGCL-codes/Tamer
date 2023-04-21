package org.fudaa.ctulu.interpolation.profile;

/**
 * Une interface a implementer pour passer le nuage de points au calculateur de profils.
 * @author J.B. Faure
 * @author Bertrand Marchand
 * @version $Id: PointCloudI.java 6184 2011-03-30 08:57:28Z bmarchan $
 */
public interface PointCloudI {

    /**
   * @return Le nombre de points du nuage.
   */
    public int getNbPoints();

    /**
	 * @param _idx L'indice du point
	 * @return La coordonn�e X pour le point d'index _idx.
	 */
    public double getX(int _idx);

    /**
   * @param _idx L'indice du point
   * @return La coordonn�e X pour le point d'index _idx.
   */
    public double getY(int _idx);

    /**
   * @param _idx L'indice du point
   * @return La coordonn�e X pour le point d'index _idx.
   */
    public double getZ(int _idx);
}
