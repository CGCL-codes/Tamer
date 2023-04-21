package org.fudaa.dodico.corba.navigation;

/**
   * Regles de securite pour le domaine fluvial.A completer.
   */
public class IRegleNavigationFluviale_Tie extends IRegleNavigationFluvialePOA {

    public IRegleNavigationFluviale_Tie(org.fudaa.dodico.corba.navigation.IRegleNavigationFluvialeOperations delegate) {
        this._impl = delegate;
    }

    public IRegleNavigationFluviale_Tie(org.fudaa.dodico.corba.navigation.IRegleNavigationFluvialeOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.navigation.IRegleNavigationFluvialeOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.navigation.IRegleNavigationFluvialeOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public boolean croisementAutorise() {
        return _impl.croisementAutorise();
    }

    public void croisementAutorise(boolean newCroisementAutorise) {
        _impl.croisementAutorise(newCroisementAutorise);
    }

    public boolean trematageAutorise() {
        return _impl.trematageAutorise();
    }

    public void trematageAutorise(boolean newTrematageAutorise) {
        _impl.trematageAutorise(newTrematageAutorise);
    }

    /**
     * Vitesse acceptee si positive. Si nulle pas de vitesse prise en compte.
     */
    public double vitesseMaximum() {
        return _impl.vitesseMaximum();
    }

    /**
     * Vitesse acceptee si positive. Si nulle pas de vitesse prise en compte.
     */
    public void vitesseMaximum(double newVitesseMaximum) {
        _impl.vitesseMaximum(newVitesseMaximum);
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

    private org.fudaa.dodico.corba.navigation.IRegleNavigationFluvialeOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
