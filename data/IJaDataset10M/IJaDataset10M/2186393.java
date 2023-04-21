package org.fudaa.dodico.corba.association;

/**
   * Une interface de base pour toutes les associations.
   */
public abstract class IAssociationPOA extends org.omg.PortableServer.Servant implements org.fudaa.dodico.corba.association.IAssociationOperations, org.omg.CORBA.portable.InvokeHandler {

    private static java.util.Hashtable _methods = new java.util.Hashtable();

    static {
        _methods.put("_get_paires", new java.lang.Integer(0));
        _methods.put("_set_paires", new java.lang.Integer(1));
        _methods.put("vide", new java.lang.Integer(2));
        _methods.put("contient", new java.lang.Integer(3));
        _methods.put("valeur", new java.lang.Integer(4));
        _methods.put("estVide", new java.lang.Integer(5));
        _methods.put("clefs", new java.lang.Integer(6));
        _methods.put("place", new java.lang.Integer(7));
        _methods.put("enleve", new java.lang.Integer(8));
        _methods.put("taille", new java.lang.Integer(9));
        _methods.put("valeurs", new java.lang.Integer(10));
        _methods.put("dispose", new java.lang.Integer(11));
        _methods.put("initialise", new java.lang.Integer(12));
        _methods.put("reconnecte", new java.lang.Integer(13));
        _methods.put("creeClone", new java.lang.Integer(14));
        _methods.put("moduleCorba", new java.lang.Integer(15));
        _methods.put("interfacesCorba", new java.lang.Integer(16));
        _methods.put("egale", new java.lang.Integer(17));
        _methods.put("codeHachage", new java.lang.Integer(18));
        _methods.put("enChaine", new java.lang.Integer(19));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result[][] = null;
                    $result = this.paires();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.VVIObjetHelper.write(out, $result);
                    break;
                }
            case 1:
                {
                    org.fudaa.dodico.corba.objet.IObjet newPaires[][] = org.fudaa.dodico.corba.objet.VVIObjetHelper.read(in);
                    this.paires(newPaires);
                    out = $rh.createReply();
                    break;
                }
            case 2:
                {
                    this.vide();
                    out = $rh.createReply();
                    break;
                }
            case 3:
                {
                    org.fudaa.dodico.corba.objet.IObjet v = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    boolean $result = false;
                    $result = this.contient(v);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 4:
                {
                    org.fudaa.dodico.corba.objet.IObjet c = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    org.fudaa.dodico.corba.objet.IObjet $result = null;
                    $result = this.valeur(c);
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IObjetHelper.write(out, $result);
                    break;
                }
            case 5:
                {
                    boolean $result = false;
                    $result = this.estVide();
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 6:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result[] = null;
                    $result = this.clefs();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.VIObjetHelper.write(out, $result);
                    break;
                }
            case 7:
                {
                    org.fudaa.dodico.corba.objet.IObjet c = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    org.fudaa.dodico.corba.objet.IObjet v = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.place(c, v);
                    out = $rh.createReply();
                    break;
                }
            case 8:
                {
                    org.fudaa.dodico.corba.objet.IObjet c = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.enleve(c);
                    out = $rh.createReply();
                    break;
                }
            case 9:
                {
                    int $result = (int) 0;
                    $result = this.taille();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 10:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result[] = null;
                    $result = this.valeurs();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.VIObjetHelper.write(out, $result);
                    break;
                }
            case 11:
                {
                    this.dispose();
                    out = $rh.createReply();
                    break;
                }
            case 12:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.initialise(o);
                    out = $rh.createReply();
                    break;
                }
            case 13:
                {
                    String nom = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    this.reconnecte(nom);
                    out = $rh.createReply();
                    break;
                }
            case 14:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result = null;
                    $result = this.creeClone();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IObjetHelper.write(out, $result);
                    break;
                }
            case 15:
                {
                    String $result = null;
                    $result = this.moduleCorba();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 16:
                {
                    String $result[] = null;
                    $result = this.interfacesCorba();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 17:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    boolean $result = false;
                    $result = this.egale(o);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 18:
                {
                    int $result = (int) 0;
                    $result = this.codeHachage();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 19:
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

    private static String[] __ids = { "IDL:association/IAssociation:1.0", "IDL:objet/IObjet:1.0" };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) __ids.clone();
    }

    public IAssociation _this() {
        return IAssociationHelper.narrow(super._this_object());
    }

    public IAssociation _this(org.omg.CORBA.ORB orb) {
        return IAssociationHelper.narrow(super._this_object(orb));
    }
}
