package org.fudaa.ctulu.gis;

/**
 * @author Fred Deniger
 * @version $Id: GISAttributeModelIntegerInterface.java,v 1.3 2006-02-09 08:59:28 deniger Exp $
 */
public interface GISAttributeModelIntegerInterface extends GISAttributeModel {

    /**
   * @param _idx l'indice de la valeur demandee
   * @return la valeur
   */
    int getValue(int _idx);

    /**
   * @param _numObject le nombre d'objet du nouveau modele
   * @param _interpol l'interpolateur
   * @return le model
   */
    GISAttributeModelIntegerInterface deriveNewModel(int _numObject, GISReprojectInterpolateurI.IntegerTarget _interpol);
}
