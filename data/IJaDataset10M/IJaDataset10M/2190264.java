package org.fudaa.dodico.corba.geometrie;

/**
   * ILineaire.
   */
public class ILineaire_Tie extends ILineairePOA {

    public ILineaire_Tie(org.fudaa.dodico.corba.geometrie.ILineaireOperations delegate) {
        this._impl = delegate;
    }

    public ILineaire_Tie(org.fudaa.dodico.corba.geometrie.ILineaireOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.geometrie.ILineaireOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.geometrie.ILineaireOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public int dimension() {
        return _impl.dimension();
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

    private org.fudaa.dodico.corba.geometrie.ILineaireOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
