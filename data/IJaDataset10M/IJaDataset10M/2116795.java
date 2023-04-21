package org.fudaa.dodico.corba.hydraulique;

public interface IProprietePhysiqueOperations extends org.fudaa.dodico.corba.hydraulique.ICaracteristiqueOperations {

    org.fudaa.dodico.corba.hydraulique.LTypeProprietePhysique type();

    void type(org.fudaa.dodico.corba.hydraulique.LTypeProprietePhysique newType);

    org.fudaa.dodico.corba.mesure.IEvolution evolution();

    void evolution(org.fudaa.dodico.corba.mesure.IEvolution newEvolution);
}
