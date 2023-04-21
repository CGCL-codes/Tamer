package org.fudaa.dodico.corba.navigation;

/**
   * Creneau maree sert pour les bassins.
   */
public interface ICreneauMareeOperations extends org.fudaa.dodico.corba.objet.IObjetOperations {

    /**
     * Affectations si positif.
     */
    long avantPleineMer();

    /**
     * Affectations si positif.
     */
    void avantPleineMer(long newAvantPleineMer);

    long apresPleineMer();

    void apresPleineMer(long newApresPleineMer);
}
