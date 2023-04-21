package org.fudaa.dodico.corba.reflux;

public abstract class ICalculRefluxPOA extends org.omg.PortableServer.Servant implements org.fudaa.dodico.corba.reflux.ICalculRefluxOperations, org.omg.CORBA.portable.InvokeHandler {

    private static java.util.Hashtable _methods = new java.util.Hashtable();

    static {
        _methods.put("clear", new java.lang.Integer(0));
        _methods.put("estOperationnel", new java.lang.Integer(1));
        _methods.put("estOK", new java.lang.Integer(2));
        _methods.put("traceExecution", new java.lang.Integer(3));
        _methods.put("parametres", new java.lang.Integer(4));
        _methods.put("resultats", new java.lang.Integer(5));
        _methods.put("calcul", new java.lang.Integer(6));
        _methods.put("dureeEstimee", new java.lang.Integer(7));
        _methods.put("creation", new java.lang.Integer(8));
        _methods.put("derniereUtilisation", new java.lang.Integer(9));
        _methods.put("description", new java.lang.Integer(10));
        _methods.put("responsable", new java.lang.Integer(11));
        _methods.put("connexions", new java.lang.Integer(12));
        _methods.put("connexion", new java.lang.Integer(13));
        _methods.put("deconnexion", new java.lang.Integer(14));
        _methods.put("dispose", new java.lang.Integer(15));
        _methods.put("initialise", new java.lang.Integer(16));
        _methods.put("reconnecte", new java.lang.Integer(17));
        _methods.put("creeClone", new java.lang.Integer(18));
        _methods.put("moduleCorba", new java.lang.Integer(19));
        _methods.put("interfacesCorba", new java.lang.Integer(20));
        _methods.put("egale", new java.lang.Integer(21));
        _methods.put("codeHachage", new java.lang.Integer(22));
        _methods.put("enChaine", new java.lang.Integer(23));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    this.clear(c);
                    out = $rh.createReply();
                    break;
                }
            case 1:
                {
                    boolean $result = false;
                    $result = this.estOperationnel();
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 2:
                {
                    boolean $result = false;
                    $result = this.estOK();
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 3:
                {
                    String $result = null;
                    $result = this.traceExecution();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 4:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    org.fudaa.dodico.corba.calcul.IParametres $result = null;
                    $result = this.parametres(c);
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.calcul.IParametresHelper.write(out, $result);
                    break;
                }
            case 5:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    org.fudaa.dodico.corba.calcul.IResultats $result = null;
                    $result = this.resultats(c);
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.calcul.IResultatsHelper.write(out, $result);
                    break;
                }
            case 6:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    this.calcul(c);
                    out = $rh.createReply();
                    break;
                }
            case 7:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    int $result = (int) 0;
                    $result = this.dureeEstimee(c);
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 8:
                {
                    int $result = (int) 0;
                    $result = this.creation();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 9:
                {
                    int $result = (int) 0;
                    $result = this.derniereUtilisation();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 10:
                {
                    String $result = null;
                    $result = this.description();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 11:
                {
                    org.fudaa.dodico.corba.objet.IPersonne $result = null;
                    $result = this.responsable();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IPersonneHelper.write(out, $result);
                    break;
                }
            case 12:
                {
                    org.fudaa.dodico.corba.objet.IConnexion $result[] = null;
                    $result = this.connexions();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.VIConnexionHelper.write(out, $result);
                    break;
                }
            case 13:
                {
                    org.fudaa.dodico.corba.objet.IPersonne p = org.fudaa.dodico.corba.objet.IPersonneHelper.read(in);
                    org.fudaa.dodico.corba.objet.IConnexion $result = null;
                    $result = this.connexion(p);
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IConnexionHelper.write(out, $result);
                    break;
                }
            case 14:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    boolean $result = false;
                    $result = this.deconnexion(c);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 15:
                {
                    this.dispose();
                    out = $rh.createReply();
                    break;
                }
            case 16:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.initialise(o);
                    out = $rh.createReply();
                    break;
                }
            case 17:
                {
                    String nom = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    this.reconnecte(nom);
                    out = $rh.createReply();
                    break;
                }
            case 18:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result = null;
                    $result = this.creeClone();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IObjetHelper.write(out, $result);
                    break;
                }
            case 19:
                {
                    String $result = null;
                    $result = this.moduleCorba();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 20:
                {
                    String $result[] = null;
                    $result = this.interfacesCorba();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 21:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    boolean $result = false;
                    $result = this.egale(o);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 22:
                {
                    int $result = (int) 0;
                    $result = this.codeHachage();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 23:
                {
                    String $result = null;
                    $result = this.enChaine();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }

    private static String[] __ids = { "IDL:reflux/ICalculReflux:1.0", "IDL:calcul/ICalcul:1.0", "IDL:objet/ITache:1.0", "IDL:objet/IObjet:1.0" };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) __ids.clone();
    }

    public ICalculReflux _this() {
        return ICalculRefluxHelper.narrow(super._this_object());
    }

    public ICalculReflux _this(org.omg.CORBA.ORB orb) {
        return ICalculRefluxHelper.narrow(super._this_object(orb));
    }
}
