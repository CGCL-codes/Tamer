package org.fudaa.dodico.corba.navigation;

/**
* org/fudaa/dodico/corba/navigation/_IEtudeNavigationMaritimeStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/metier/navigation.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public class _IEtudeNavigationMaritimeStub extends org.omg.CORBA.portable.ObjectImpl implements org.fudaa.dodico.corba.navigation.IEtudeNavigationMaritime {

    public org.fudaa.dodico.corba.navigation.IMaree maree() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_maree", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.navigation.IMaree $result = org.fudaa.dodico.corba.navigation.IMareeHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return maree();
        } finally {
            _releaseReply($in);
        }
    }

    public void maree(org.fudaa.dodico.corba.navigation.IMaree newMaree) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_maree", true);
            org.fudaa.dodico.corba.navigation.IMareeHelper.write($out, newMaree);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            maree(newMaree);
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.navigation.IParametresPerturbateur perturbateur() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_perturbateur", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.navigation.IParametresPerturbateur $result = org.fudaa.dodico.corba.navigation.IParametresPerturbateurHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return perturbateur();
        } finally {
            _releaseReply($in);
        }
    }

    public void perturbateur(org.fudaa.dodico.corba.navigation.IParametresPerturbateur newPerturbateur) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_perturbateur", true);
            org.fudaa.dodico.corba.navigation.IParametresPerturbateurHelper.write($out, newPerturbateur);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            perturbateur(newPerturbateur);
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.navigation.IReseauMaritime reseau() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_reseau", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.navigation.IReseauMaritime $result = org.fudaa.dodico.corba.navigation.IReseauMaritimeHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return reseau();
        } finally {
            _releaseReply($in);
        }
    }

    public void reseau(org.fudaa.dodico.corba.navigation.IReseauMaritime newReseau) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_reseau", true);
            org.fudaa.dodico.corba.navigation.IReseauMaritimeHelper.write($out, newReseau);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            reseau(newReseau);
        } finally {
            _releaseReply($in);
        }
    }

    public String nom() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_nom", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return nom();
        } finally {
            _releaseReply($in);
        }
    }

    public void nom(String newNom) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_nom", true);
            org.fudaa.dodico.corba.base.ChaineHelper.write($out, newNom);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            nom(newNom);
        } finally {
            _releaseReply($in);
        }
    }

    public long dateDebutSimulation() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("dateDebutSimulation", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dateDebutSimulation();
        } finally {
            _releaseReply($in);
        }
    }

    public long dateFinSimulation() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("dateFinSimulation", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dateFinSimulation();
        } finally {
            _releaseReply($in);
        }
    }

    /**
     * Le debut de la simulation etant particulier(etat transitoire), il peut ne
     * pas etre pris compte dans les resultats finaux. La variable suivante
     * definit l'instant a partir duquel les resultats seront enregistres.
     */
    public long dateDebutReelSimulation() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_dateDebutReelSimulation", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dateDebutReelSimulation();
        } finally {
            _releaseReply($in);
        }
    }

    /**
     * Le debut de la simulation etant particulier(etat transitoire), il peut ne
     * pas etre pris compte dans les resultats finaux. La variable suivante
     * definit l'instant a partir duquel les resultats seront enregistres.
     */
    public void dateDebutReelSimulation(long newDateDebutReelSimulation) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_dateDebutReelSimulation", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, newDateDebutReelSimulation);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            dateDebutReelSimulation(newDateDebutReelSimulation);
        } finally {
            _releaseReply($in);
        }
    }

    public int nombreSeries() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_nombreSeries", true);
            $in = _invoke($out);
            int $result = org.fudaa.dodico.corba.base.EntierHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return nombreSeries();
        } finally {
            _releaseReply($in);
        }
    }

    public void nombreSeries(int newNombreSeries) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_nombreSeries", true);
            org.fudaa.dodico.corba.base.EntierHelper.write($out, newNombreSeries);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            nombreSeries(newNombreSeries);
        } finally {
            _releaseReply($in);
        }
    }

    /**
     * Les navires qui seront utilises dans l'etude.
     */
    public org.fudaa.dodico.corba.navigation.INavireType[] naviresType() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("naviresType", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.navigation.INavireType $result[] = org.fudaa.dodico.corba.navigation.VINavireTypeHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return naviresType();
        } finally {
            _releaseReply($in);
        }
    }

    public String definitNaviresType(org.fudaa.dodico.corba.navigation.INavireType[] navires) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("definitNaviresType", true);
            org.fudaa.dodico.corba.navigation.VINavireTypeHelper.write($out, navires);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return definitNaviresType(navires);
        } finally {
            _releaseReply($in);
        }
    }

    public String definitDebutFinSimulation(long dateDebut, long dateFin) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("definitDebutFinSimulation", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, dateDebut);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, dateFin);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return definitDebutFinSimulation(dateDebut, dateFin);
        } finally {
            _releaseReply($in);
        }
    }

    public String ajouteNavireType(org.fudaa.dodico.corba.navigation.INavireType navire) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("ajouteNavireType", true);
            org.fudaa.dodico.corba.navigation.INavireTypeHelper.write($out, navire);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return ajouteNavireType(navire);
        } finally {
            _releaseReply($in);
        }
    }

    public String enleveNavireType(org.fudaa.dodico.corba.navigation.INavireType navire) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("enleveNavireType", true);
            org.fudaa.dodico.corba.navigation.INavireTypeHelper.write($out, navire);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return enleveNavireType(navire);
        } finally {
            _releaseReply($in);
        }
    }

    public String genere() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("genere", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return genere();
        } finally {
            _releaseReply($in);
        }
    }

    public String valide() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("valide", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return valide();
        } finally {
            _releaseReply($in);
        }
    }

    public void calcule() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("calcule", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            calcule();
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

    private static String[] __ids = { "IDL:navigation/IEtudeNavigationMaritime:1.0", "IDL:navigation/IEtudeNavigation:1.0", "IDL:objet/IObjet:1.0" };

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
