package org.fudaa.dodico.corba.hydraulique;

public interface IConditionInitialeOperations extends org.fudaa.dodico.corba.hydraulique.ICaracteristiqueOperations {

    org.fudaa.dodico.corba.hydraulique.LTypeCondition type();

    void type(org.fudaa.dodico.corba.hydraulique.LTypeCondition newType);

    double valeur();

    void valeur(double newValeur);
}
