package org.fudaa.dodico.corba.mascaret;

public class IParametresMascaret_Tie extends IParametresMascaretPOA {

    public IParametresMascaret_Tie(org.fudaa.dodico.corba.mascaret.IParametresMascaretOperations delegate) {
        this._impl = delegate;
    }

    public IParametresMascaret_Tie(org.fudaa.dodico.corba.mascaret.IParametresMascaretOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.mascaret.IParametresMascaretOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.mascaret.IParametresMascaretOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public org.fudaa.dodico.corba.mascaret.SParametresCAS parametresCAS() {
        return _impl.parametresCAS();
    }

    public void parametresCAS(org.fudaa.dodico.corba.mascaret.SParametresCAS newParametresCAS) {
        _impl.parametresCAS(newParametresCAS);
    }

    public org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial parametresLigneDEauInitiale() {
        return _impl.parametresLigneDEauInitiale();
    }

    public void parametresLigneDEauInitiale(org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial newParametresLigneDEauInitiale) {
        _impl.parametresLigneDEauInitiale(newParametresLigneDEauInitiale);
    }

    public org.fudaa.dodico.corba.mascaret.SParametresGEO parametresGEO() {
        return _impl.parametresGEO();
    }

    public void parametresGEO(org.fudaa.dodico.corba.mascaret.SParametresGEO newParametresGEO) {
        _impl.parametresGEO(newParametresGEO);
    }

    public org.fudaa.dodico.corba.mascaret.SGeoCasiers casierGEO() {
        return _impl.casierGEO();
    }

    public void casierGEO(org.fudaa.dodico.corba.mascaret.SGeoCasiers newCasierGEO) {
        _impl.casierGEO(newCasierGEO);
    }

    public org.fudaa.dodico.corba.mascaret.SParametresREP parametresREP() {
        return _impl.parametresREP();
    }

    public void parametresREP(org.fudaa.dodico.corba.mascaret.SParametresREP newParametresREP) {
        _impl.parametresREP(newParametresREP);
    }

    public org.fudaa.dodico.corba.mascaret.SParametresNCA parametresNCA() {
        return _impl.parametresNCA();
    }

    public void parametresNCA(org.fudaa.dodico.corba.mascaret.SParametresNCA newParametresNCA) {
        _impl.parametresNCA(newParametresNCA);
    }

    public org.fudaa.dodico.corba.mascaret.SParametresTailleMaxFichier parametresTailleMaxFichier() {
        return _impl.parametresTailleMaxFichier();
    }

    public void parametresTailleMaxFichier(org.fudaa.dodico.corba.mascaret.SParametresTailleMaxFichier newParametresTailleMaxFichier) {
        _impl.parametresTailleMaxFichier(newParametresTailleMaxFichier);
    }

    public org.fudaa.dodico.corba.mascaret.SLoiHydraulique[] loisHydrauliques() {
        return _impl.loisHydrauliques();
    }

    public void loisHydrauliques(org.fudaa.dodico.corba.mascaret.SLoiHydraulique[] newLoisHydrauliques) {
        _impl.loisHydrauliques(newLoisHydrauliques);
    }

    public org.fudaa.dodico.corba.mascaret.SLoiTracer[] loisTracer() {
        return _impl.loisTracer();
    }

    public void loisTracer(org.fudaa.dodico.corba.mascaret.SLoiTracer[] newLoisTracer) {
        _impl.loisTracer(newLoisTracer);
    }

    public org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial parametresConcentInitiales() {
        return _impl.parametresConcentInitiales();
    }

    public void parametresConcentInitiales(org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial newParametresConcentInitiales) {
        _impl.parametresConcentInitiales(newParametresConcentInitiales);
    }

    public org.fudaa.dodico.corba.mascaret.SParamMeteoTracer paramMeteoTracer() {
        return _impl.paramMeteoTracer();
    }

    public void paramMeteoTracer(org.fudaa.dodico.corba.mascaret.SParamMeteoTracer newParamMeteoTracer) {
        _impl.paramMeteoTracer(newParamMeteoTracer);
    }

    public org.fudaa.dodico.corba.mascaret.SParamPhysTracer[] parametresPhysModele() {
        return _impl.parametresPhysModele();
    }

    public void parametresPhysModele(org.fudaa.dodico.corba.mascaret.SParamPhysTracer[] newParametresPhysModele) {
        _impl.parametresPhysModele(newParametresPhysModele);
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

    private org.fudaa.dodico.corba.mascaret.IParametresMascaretOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
