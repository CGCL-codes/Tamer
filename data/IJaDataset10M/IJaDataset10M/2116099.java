package org.fudaa.dodico.corba.olb;

/**
   * Une interface de calcul de l'optimiseur largeur de bande.
   */
public abstract class IResultatsOlbPOA extends org.omg.PortableServer.Servant implements org.fudaa.dodico.corba.olb.IResultatsOlbOperations, org.omg.CORBA.portable.InvokeHandler {

    private static java.util.Hashtable _methods = new java.util.Hashtable();

    static {
        _methods.put("_get_maillage", new java.lang.Integer(0));
        _methods.put("_set_maillage", new java.lang.Integer(1));
        _methods.put("setFichier", new java.lang.Integer(2));
        _methods.put("clearResultats", new java.lang.Integer(3));
        _methods.put("dispose", new java.lang.Integer(4));
        _methods.put("initialise", new java.lang.Integer(5));
        _methods.put("reconnecte", new java.lang.Integer(6));
        _methods.put("creeClone", new java.lang.Integer(7));
        _methods.put("moduleCorba", new java.lang.Integer(8));
        _methods.put("interfacesCorba", new java.lang.Integer(9));
        _methods.put("egale", new java.lang.Integer(10));
        _methods.put("codeHachage", new java.lang.Integer(11));
        _methods.put("enChaine", new java.lang.Integer(12));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    org.fudaa.dodico.corba.geometrie.SMaillage $result = null;
                    $result = this.maillage();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.geometrie.SMaillageHelper.write(out, $result);
                    break;
                }
            case 1:
                {
                    org.fudaa.dodico.corba.geometrie.SMaillage newMaillage = org.fudaa.dodico.corba.geometrie.SMaillageHelper.read(in);
                    this.maillage(newMaillage);
                    out = $rh.createReply();
                    break;
                }
            case 2:
                {
                    String f = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    this.setFichier(f);
                    out = $rh.createReply();
                    break;
                }
            case 3:
                {
                    this.clearResultats();
                    out = $rh.createReply();
                    break;
                }
            case 4:
                {
                    this.dispose();
                    out = $rh.createReply();
                    break;
                }
            case 5:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.initialise(o);
                    out = $rh.createReply();
                    break;
                }
            case 6:
                {
                    String nom = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    this.reconnecte(nom);
                    out = $rh.createReply();
                    break;
                }
            case 7:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result = null;
                    $result = this.creeClone();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IObjetHelper.write(out, $result);
                    break;
                }
            case 8:
                {
                    String $result = null;
                    $result = this.moduleCorba();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 9:
                {
                    String $result[] = null;
                    $result = this.interfacesCorba();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 10:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    boolean $result = false;
                    $result = this.egale(o);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 11:
                {
                    int $result = (int) 0;
                    $result = this.codeHachage();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 12:
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

    private static String[] __ids = { "IDL:olb/IResultatsOlb:1.0", "IDL:calcul/IResultats:1.0", "IDL:objet/IObjet:1.0" };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) __ids.clone();
    }

    public IResultatsOlb _this() {
        return IResultatsOlbHelper.narrow(super._this_object());
    }

    public IResultatsOlb _this(org.omg.CORBA.ORB orb) {
        return IResultatsOlbHelper.narrow(super._this_object(orb));
    }
}
