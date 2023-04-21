package org.fudaa.dodico.corba.navigation;

/**
   * Definition generale d'un trajet.
   */
public class ITrajet_Tie extends ITrajetPOA {

    public ITrajet_Tie(org.fudaa.dodico.corba.navigation.ITrajetOperations delegate) {
        this._impl = delegate;
    }

    public ITrajet_Tie(org.fudaa.dodico.corba.navigation.ITrajetOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.navigation.ITrajetOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.navigation.ITrajetOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public org.fudaa.dodico.corba.navigation.INavireType navireType() {
        return _impl.navireType();
    }

    public void navireType(org.fudaa.dodico.corba.navigation.INavireType newNavireType) {
        _impl.navireType(newNavireType);
    }

    public org.fudaa.dodico.corba.navigation.IGeneration[] generations() {
        return _impl.generations();
    }

    public String definitGenerations(org.fudaa.dodico.corba.navigation.IGeneration[] creneaux) {
        return _impl.definitGenerations(creneaux);
    }

    public String ajouteGeneration(org.fudaa.dodico.corba.navigation.IGeneration creneau) {
        return _impl.ajouteGeneration(creneau);
    }

    public String enleveGeneration(org.fudaa.dodico.corba.navigation.IGeneration creneau) {
        return _impl.enleveGeneration(creneau);
    }

    /**
     * Determine quelles generations regulieres aleatoires sont chevauchees.
     */
    public org.fudaa.dodico.corba.navigation.IGenerationJournaliereAleatoire[] generationsAleatoiresChevauchees() {
        return _impl.generationsAleatoiresChevauchees();
    }

    public String genere(int nombreSeries) {
        return _impl.genere(nombreSeries);
    }

    public String valide() {
        return _impl.valide();
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

    private org.fudaa.dodico.corba.navigation.ITrajetOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
