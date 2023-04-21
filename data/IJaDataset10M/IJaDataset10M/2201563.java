package org.fudaa.dodico.corba.planification;

/**
   * Un chronometre.
   */
public class _IChronometreStub extends org.omg.CORBA.portable.ObjectImpl implements org.fudaa.dodico.corba.planification.IChronometre {

    public double facteur() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_facteur", true);
            $in = _invoke($out);
            double $result = org.fudaa.dodico.corba.base.ReelHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return facteur();
        } finally {
            _releaseReply($in);
        }
    }

    public void facteur(double newFacteur) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_facteur", true);
            org.fudaa.dodico.corba.base.ReelHelper.write($out, newFacteur);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            facteur(newFacteur);
        } finally {
            _releaseReply($in);
        }
    }

    public void modifieInstant(long t) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("modifieInstant", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, t);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            modifieInstant(t);
        } finally {
            _releaseReply($in);
        }
    }

    public long instant() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("instant", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return instant();
        } finally {
            _releaseReply($in);
        }
    }

    public String date() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("date", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return date();
        } finally {
            _releaseReply($in);
        }
    }

    public String heure() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("heure", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return heure();
        } finally {
            _releaseReply($in);
        }
    }

    public int creation() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("creation", true);
            $in = _invoke($out);
            int $result = org.fudaa.dodico.corba.base.TempsIntHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return creation();
        } finally {
            _releaseReply($in);
        }
    }

    public int derniereUtilisation() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("derniereUtilisation", true);
            $in = _invoke($out);
            int $result = org.fudaa.dodico.corba.base.TempsIntHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return derniereUtilisation();
        } finally {
            _releaseReply($in);
        }
    }

    public String description() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("description", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return description();
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.objet.IPersonne responsable() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("responsable", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.objet.IPersonne $result = org.fudaa.dodico.corba.objet.IPersonneHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return responsable();
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.objet.IConnexion[] connexions() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("connexions", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.objet.IConnexion $result[] = org.fudaa.dodico.corba.objet.VIConnexionHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return connexions();
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.objet.IConnexion connexion(org.fudaa.dodico.corba.objet.IPersonne p) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("connexion", true);
            org.fudaa.dodico.corba.objet.IPersonneHelper.write($out, p);
            $in = _invoke($out);
            org.fudaa.dodico.corba.objet.IConnexion $result = org.fudaa.dodico.corba.objet.IConnexionHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return connexion(p);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean deconnexion(org.fudaa.dodico.corba.objet.IConnexion c) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("deconnexion", true);
            org.fudaa.dodico.corba.objet.IConnexionHelper.write($out, c);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return deconnexion(c);
        } finally {
            _releaseReply($in);
        }
    }

    public void dispose() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("dispose", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            dispose();
        } finally {
            _releaseReply($in);
        }
    }

    public void initialise(org.fudaa.dodico.corba.objet.IObjet o) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("initialise", true);
            org.fudaa.dodico.corba.objet.IObjetHelper.write($out, o);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            initialise(o);
        } finally {
            _releaseReply($in);
        }
    }

    public void reconnecte(String nom) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("reconnecte", true);
            org.fudaa.dodico.corba.base.ChaineHelper.write($out, nom);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            reconnecte(nom);
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.objet.IObjet creeClone() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("creeClone", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.objet.IObjet $result = org.fudaa.dodico.corba.objet.IObjetHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return creeClone();
        } finally {
            _releaseReply($in);
        }
    }

    public String moduleCorba() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("moduleCorba", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return moduleCorba();
        } finally {
            _releaseReply($in);
        }
    }

    public String[] interfacesCorba() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("interfacesCorba", true);
            $in = _invoke($out);
            String $result[] = org.fudaa.dodico.corba.base.VChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return interfacesCorba();
        } finally {
            _releaseReply($in);
        }
    }

    public boolean egale(org.fudaa.dodico.corba.objet.IObjet o) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("egale", true);
            org.fudaa.dodico.corba.objet.IObjetHelper.write($out, o);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return egale(o);
        } finally {
            _releaseReply($in);
        }
    }

    public int codeHachage() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("codeHachage", true);
            $in = _invoke($out);
            int $result = org.fudaa.dodico.corba.base.EntierHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return codeHachage();
        } finally {
            _releaseReply($in);
        }
    }

    public String enChaine() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("enChaine", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return enChaine();
        } finally {
            _releaseReply($in);
        }
    }

    private static String[] __ids = { "IDL:planification/IChronometre:1.0", "IDL:planification/IHorloge:1.0", "IDL:objet/IService:1.0", "IDL:objet/ITache:1.0", "IDL:objet/IObjet:1.0" };

    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.Object obj = org.omg.CORBA.ORB.init(args, props).string_to_object(str);
        org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
        _set_delegate(delegate);
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        String str = org.omg.CORBA.ORB.init(args, props).object_to_string(this);
        s.writeUTF(str);
    }
}
