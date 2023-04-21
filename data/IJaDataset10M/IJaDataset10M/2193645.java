package org.fudaa.dodico.corba.sinavi2;

public class ICalculSinavi2_Tie extends ICalculSinavi2POA {

    public ICalculSinavi2_Tie(org.fudaa.dodico.corba.sinavi2.ICalculSinavi2Operations delegate) {
        this._impl = delegate;
    }

    public ICalculSinavi2_Tie(org.fudaa.dodico.corba.sinavi2.ICalculSinavi2Operations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.sinavi2.ICalculSinavi2Operations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.sinavi2.ICalculSinavi2Operations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public org.fudaa.dodico.corba.calcul.IParametres parametres(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.parametres(c);
    }

    public org.fudaa.dodico.corba.calcul.IResultats resultats(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.resultats(c);
    }

    public void calcul(org.fudaa.dodico.corba.objet.IConnexion c) {
        _impl.calcul(c);
    }

    public int dureeEstimee(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.dureeEstimee(c);
    }

    public int creation() {
        return _impl.creation();
    }

    public int derniereUtilisation() {
        return _impl.derniereUtilisation();
    }

    public String description() {
        return _impl.description();
    }

    public org.fudaa.dodico.corba.objet.IPersonne responsable() {
        return _impl.responsable();
    }

    public org.fudaa.dodico.corba.objet.IConnexion[] connexions() {
        return _impl.connexions();
    }

    public org.fudaa.dodico.corba.objet.IConnexion connexion(org.fudaa.dodico.corba.objet.IPersonne p) {
        return _impl.connexion(p);
    }

    public boolean deconnexion(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.deconnexion(c);
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

    private org.fudaa.dodico.corba.sinavi2.ICalculSinavi2Operations _impl;

    private org.omg.PortableServer.POA _poa;
}
