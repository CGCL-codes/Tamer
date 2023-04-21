package org.fudaa.dodico.corba.houle;

public class IHouleUnitaire_Tie extends IHouleUnitairePOA {

    public IHouleUnitaire_Tie(org.fudaa.dodico.corba.houle.IHouleUnitaireOperations delegate) {
        this._impl = delegate;
    }

    public IHouleUnitaire_Tie(org.fudaa.dodico.corba.houle.IHouleUnitaireOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.houle.IHouleUnitaireOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.houle.IHouleUnitaireOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public org.fudaa.dodico.corba.mathematiques.SComplexe hauteurUnitaire() {
        return _impl.hauteurUnitaire();
    }

    public org.fudaa.dodico.corba.mathematiques.SComplexe gradientHauteurUnitaireX() {
        return _impl.gradientHauteurUnitaireX();
    }

    public org.fudaa.dodico.corba.mathematiques.SComplexe gradientHauteurUnitaireY() {
        return _impl.gradientHauteurUnitaireY();
    }

    public double directionOrthogonale() {
        return _impl.directionOrthogonale();
    }

    public double longueurOnde() {
        return _impl.longueurOnde();
    }

    public void dispose() {
        _impl.dispose();
    }

    public void initialise(org.fudaa.dodico.corba.objet.IObjet o) {
        _impl.initialise(o);
    }

    public void reconnecte(String nom) {
        _impl.reconnecte(nom);
    }

    public org.fudaa.dodico.corba.objet.IObjet creeClone() {
        return _impl.creeClone();
    }

    public String moduleCorba() {
        return _impl.moduleCorba();
    }

    public String[] interfacesCorba() {
        return _impl.interfacesCorba();
    }

    public boolean egale(org.fudaa.dodico.corba.objet.IObjet o) {
        return _impl.egale(o);
    }

    public int codeHachage() {
        return _impl.codeHachage();
    }

    public String enChaine() {
        return _impl.enChaine();
    }

    private org.fudaa.dodico.corba.houle.IHouleUnitaireOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
