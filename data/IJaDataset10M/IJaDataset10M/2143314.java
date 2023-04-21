package org.fudaa.dodico.corba.representation;

public abstract class ICarteComplexePOA extends org.omg.PortableServer.Servant implements org.fudaa.dodico.corba.representation.ICarteComplexeOperations, org.omg.CORBA.portable.InvokeHandler {

    private static java.util.Hashtable _methods = new java.util.Hashtable();

    static {
        _methods.put("_get_valeurs", new java.lang.Integer(0));
        _methods.put("_set_valeurs", new java.lang.Integer(1));
        _methods.put("carteRe", new java.lang.Integer(2));
        _methods.put("carteIm", new java.lang.Integer(3));
        _methods.put("carteMod", new java.lang.Integer(4));
        _methods.put("carteArg", new java.lang.Integer(5));
        _methods.put("_get_grandeur", new java.lang.Integer(6));
        _methods.put("_set_grandeur", new java.lang.Integer(7));
        _methods.put("_get_maillage", new java.lang.Integer(8));
        _methods.put("_set_maillage", new java.lang.Integer(9));
        _methods.put("dispose", new java.lang.Integer(10));
        _methods.put("initialise", new java.lang.Integer(11));
        _methods.put("reconnecte", new java.lang.Integer(12));
        _methods.put("creeClone", new java.lang.Integer(13));
        _methods.put("moduleCorba", new java.lang.Integer(14));
        _methods.put("interfacesCorba", new java.lang.Integer(15));
        _methods.put("egale", new java.lang.Integer(16));
        _methods.put("codeHachage", new java.lang.Integer(17));
        _methods.put("enChaine", new java.lang.Integer(18));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    org.fudaa.dodico.corba.mathematiques.SComplexe $result[] = null;
                    $result = this.valeurs();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.mathematiques.VSComplexeHelper.write(out, $result);
                    break;
                }
            case 1:
                {
                    org.fudaa.dodico.corba.mathematiques.SComplexe newValeurs[] = org.fudaa.dodico.corba.mathematiques.VSComplexeHelper.read(in);
                    this.valeurs(newValeurs);
                    out = $rh.createReply();
                    break;
                }
            case 2:
                {
                    org.fudaa.dodico.corba.representation.ICarteReel $result = null;
                    $result = this.carteRe();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.representation.ICarteReelHelper.write(out, $result);
                    break;
                }
            case 3:
                {
                    org.fudaa.dodico.corba.representation.ICarteReel $result = null;
                    $result = this.carteIm();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.representation.ICarteReelHelper.write(out, $result);
                    break;
                }
            case 4:
                {
                    org.fudaa.dodico.corba.representation.ICarteReel $result = null;
                    $result = this.carteMod();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.representation.ICarteReelHelper.write(out, $result);
                    break;
                }
            case 5:
                {
                    org.fudaa.dodico.corba.representation.ICarteReel $result = null;
                    $result = this.carteArg();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.representation.ICarteReelHelper.write(out, $result);
                    break;
                }
            case 6:
                {
                    org.fudaa.dodico.corba.mesure.LGrandeur $result = null;
                    $result = this.grandeur();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.mesure.LGrandeurHelper.write(out, $result);
                    break;
                }
            case 7:
                {
                    org.fudaa.dodico.corba.mesure.LGrandeur newGrandeur = org.fudaa.dodico.corba.mesure.LGrandeurHelper.read(in);
                    this.grandeur(newGrandeur);
                    out = $rh.createReply();
                    break;
                }
            case 8:
                {
                    org.fudaa.dodico.corba.ef.IMaillage $result = null;
                    $result = this.maillage();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.ef.IMaillageHelper.write(out, $result);
                    break;
                }
            case 9:
                {
                    org.fudaa.dodico.corba.ef.IMaillage newMaillage = org.fudaa.dodico.corba.ef.IMaillageHelper.read(in);
                    this.maillage(newMaillage);
                    out = $rh.createReply();
                    break;
                }
            case 10:
                {
                    this.dispose();
                    out = $rh.createReply();
                    break;
                }
            case 11:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.initialise(o);
                    out = $rh.createReply();
                    break;
                }
            case 12:
                {
                    String nom = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    this.reconnecte(nom);
                    out = $rh.createReply();
                    break;
                }
            case 13:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result = null;
                    $result = this.creeClone();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IObjetHelper.write(out, $result);
                    break;
                }
            case 14:
                {
                    String $result = null;
                    $result = this.moduleCorba();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 15:
                {
                    String $result[] = null;
                    $result = this.interfacesCorba();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 16:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    boolean $result = false;
                    $result = this.egale(o);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 17:
                {
                    int $result = (int) 0;
                    $result = this.codeHachage();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 18:
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

    private static String[] __ids = { "IDL:representation/ICarteComplexe:1.0", "IDL:representation/ICarte:1.0", "IDL:objet/IObjet:1.0" };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) __ids.clone();
    }

    public ICarteComplexe _this() {
        return ICarteComplexeHelper.narrow(super._this_object());
    }

    public ICarteComplexe _this(org.omg.CORBA.ORB orb) {
        return ICarteComplexeHelper.narrow(super._this_object(orb));
    }
}
