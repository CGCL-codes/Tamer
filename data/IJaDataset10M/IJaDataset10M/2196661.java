package org.fudaa.fudaa.refonde.commun;

/**
 * Une interface pour les valeurs a tracer en iso.
 */
public interface ZModeleValeur {

    /**
   * Valeur pour l'index i
   * @return La valeur ou Double.NaN si aucune valeur pour l'index donn�.
   */
    double valeur(int i);

    /**
   * Nombre de valeurs du modele
   * @return Le nombre de valeurs. 0 si aucune valeur
   */
    int nbValeurs();

    /**
   * Min des valeurs
   * @return Le min ou Double.NaN si aucune valeur.
   */
    double getMin();

    /**
   * Max des valeurs
   * @return Le max ou Double.NaN si aucune valeur.
   */
    double getMax();

    /**
   * Add Model listener
   */
    void addModelChangeListener(ZModeleChangeListener _listener);

    /**
   * Remove model listener
   */
    void removeModelChangeListener(ZModeleChangeListener _listener);
}
