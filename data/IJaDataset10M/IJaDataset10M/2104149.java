package org.fudaa.dodico.corba.navigation;

/**
* org/fudaa/dodico/corba/navigation/IEtudeNavigationMaritimeOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/metier/navigation.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public interface IEtudeNavigationMaritimeOperations extends org.fudaa.dodico.corba.navigation.IEtudeNavigationOperations {

    org.fudaa.dodico.corba.navigation.IMaree maree();

    void maree(org.fudaa.dodico.corba.navigation.IMaree newMaree);

    org.fudaa.dodico.corba.navigation.IParametresPerturbateur perturbateur();

    void perturbateur(org.fudaa.dodico.corba.navigation.IParametresPerturbateur newPerturbateur);

    org.fudaa.dodico.corba.navigation.IReseauMaritime reseau();

    void reseau(org.fudaa.dodico.corba.navigation.IReseauMaritime newReseau);
}
