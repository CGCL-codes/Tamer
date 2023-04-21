package org.fudaa.dodico.corba.navmer;

public interface IParametresNavmerOperations extends org.fudaa.dodico.corba.calcul.IParametresOperations {

    org.fudaa.dodico.corba.navmer.SParametresINI parametresINI();

    void parametresINI(org.fudaa.dodico.corba.navmer.SParametresINI newParametresINI);

    org.fudaa.dodico.corba.navmer.SCoefficientsNavire parametresNAV();

    void parametresNAV(org.fudaa.dodico.corba.navmer.SCoefficientsNavire newParametresNAV);

    org.fudaa.dodico.corba.navmer.SIndexZonesCourant parametresIDX();

    void parametresIDX(org.fudaa.dodico.corba.navmer.SIndexZonesCourant newParametresIDX);

    org.fudaa.dodico.corba.navmer.SZoneCourant[] parametresCOU();

    void parametresCOU(org.fudaa.dodico.corba.navmer.SZoneCourant[] newParametresCOU);
}
