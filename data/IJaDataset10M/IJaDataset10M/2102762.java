package org.fudaa.dodico.corba.reflux;

public interface IResultatsRefluxOperations extends org.fudaa.dodico.corba.calcul.IResultatsOperations {

    void clear();

    org.fudaa.dodico.corba.reflux.SResultatsReflux resultatsReflux();

    org.fudaa.dodico.corba.reflux.SResultatsEtapeReflux resultatsEtapeReflux(long t);

    org.fudaa.dodico.corba.reflux.SResultatsLigneReflux resultatsLigneReflux(long t, int n);

    void initIterator();

    boolean hasIteratorNextStep();

    org.fudaa.dodico.corba.reflux.SResultatsEtapeReflux nextIteratorStep();

    boolean isIteratorFilled();

    void parametres(org.fudaa.dodico.corba.reflux.IParametresReflux p);

    void startReader();

    void stopReader();
}
