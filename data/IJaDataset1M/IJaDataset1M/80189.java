package org.fudaa.dodico.corba.reflux2dv;

public class _IResultatsReflux2dvStub extends org.omg.CORBA.portable.ObjectImpl implements org.fudaa.dodico.corba.reflux2dv.IResultatsReflux2dv {

    public void clear() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("clear", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            clear();
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.reflux2dv.SResultatsReflux2dv resultatsReflux2dv() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("resultatsReflux2dv", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.reflux2dv.SResultatsReflux2dv $result = org.fudaa.dodico.corba.reflux2dv.SResultatsReflux2dvHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return resultatsReflux2dv();
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.reflux2dv.SResultatsEtapeReflux2dv resultatsEtapeReflux2dv(long t) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("resultatsEtapeReflux2dv", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, t);
            $in = _invoke($out);
            org.fudaa.dodico.corba.reflux2dv.SResultatsEtapeReflux2dv $result = org.fudaa.dodico.corba.reflux2dv.SResultatsEtapeReflux2dvHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return resultatsEtapeReflux2dv(t);
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.reflux2dv.SResultatsLigneReflux2dv resultatsLigneReflux2dv(long t, int n) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("resultatsLigneReflux2dv", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, t);
            org.fudaa.dodico.corba.base.EntierHelper.write($out, n);
            $in = _invoke($out);
            org.fudaa.dodico.corba.reflux2dv.SResultatsLigneReflux2dv $result = org.fudaa.dodico.corba.reflux2dv.SResultatsLigneReflux2dvHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return resultatsLigneReflux2dv(t, n);
        } finally {
            _releaseReply($in);
        }
    }

    public void initIterator() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("initIterator", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            initIterator();
        } finally {
            _releaseReply($in);
        }
    }

    public boolean hasIteratorNextStep() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("hasIteratorNextStep", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return hasIteratorNextStep();
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.reflux2dv.SResultatsEtapeReflux2dv nextIteratorStep() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("nextIteratorStep", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.reflux2dv.SResultatsEtapeReflux2dv $result = org.fudaa.dodico.corba.reflux2dv.SResultatsEtapeReflux2dvHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return nextIteratorStep();
        } finally {
            _releaseReply($in);
        }
    }

    public boolean isIteratorFilled() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("isIteratorFilled", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return isIteratorFilled();
        } finally {
            _releaseReply($in);
        }
    }

    public void parametres(org.fudaa.dodico.corba.reflux2dv.IParametresReflux2dv p) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("parametres", true);
            org.fudaa.dodico.corba.reflux2dv.IParametresReflux2dvHelper.write($out, p);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            parametres(p);
        } finally {
            _releaseReply($in);
        }
    }

    public void startReader() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("startReader", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            startReader();
        } finally {
            _releaseReply($in);
        }
    }

    public void stopReader() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("stopReader", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            stopReader();
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

    private static String[] __ids = { "IDL:reflux2dv/IResultatsReflux2dv:1.0", "IDL:calcul/IResultats:1.0", "IDL:objet/IObjet:1.0" };

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
