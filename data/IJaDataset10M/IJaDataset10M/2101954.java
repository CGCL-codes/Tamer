package org.fudaa.dodico.corba.navigation;

/**
   * Creneau de generation deterministe.
   * Definition d'instants quelconques ne suivant aucune loi: entierement
   * determines par l'utilisateur. Pour cette interface, dateDebut et dateFin
   * renvoie respectivement le nombre de secondes correspondant a 00h00min du
   * jour du premier instant et du jour du dernier instant.
   */
public class _IGenerationDeterministeStub extends org.omg.CORBA.portable.ObjectImpl implements org.fudaa.dodico.corba.navigation.IGenerationDeterministe {

    public long[] instants() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("instants", true);
            $in = _invoke($out);
            long $result[] = org.fudaa.dodico.corba.base.VTempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return instants();
        } finally {
            _releaseReply($in);
        }
    }

    public long premierInstant() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("premierInstant", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return premierInstant();
        } finally {
            _releaseReply($in);
        }
    }

    public long dernierInstant() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("dernierInstant", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dernierInstant();
        } finally {
            _releaseReply($in);
        }
    }

    public String definitInstants(long[] instants) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("definitInstants", true);
            org.fudaa.dodico.corba.base.VTempsHelper.write($out, instants);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return definitInstants(instants);
        } finally {
            _releaseReply($in);
        }
    }

    public String ajouteInstant(long instant) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("ajouteInstant", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, instant);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return ajouteInstant(instant);
        } finally {
            _releaseReply($in);
        }
    }

    public String enleveInstant(long instant) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("enleveInstant", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, instant);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return enleveInstant(instant);
        } finally {
            _releaseReply($in);
        }
    }

    public String enleveToutInstant(long instant) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("enleveToutInstant", true);
            org.fudaa.dodico.corba.base.TempsHelper.write($out, instant);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return enleveToutInstant(instant);
        } finally {
            _releaseReply($in);
        }
    }

    public org.fudaa.dodico.corba.navigation.LTypeLoi type() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("type", true);
            $in = _invoke($out);
            org.fudaa.dodico.corba.navigation.LTypeLoi $result = org.fudaa.dodico.corba.navigation.LTypeLoiHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return type();
        } finally {
            _releaseReply($in);
        }
    }

    public String typeEnChaine() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("typeEnChaine", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return typeEnChaine();
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

    public long dateDebut() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("dateDebut", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dateDebut();
        } finally {
            _releaseReply($in);
        }
    }

    public long dateFin() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("dateFin", true);
            $in = _invoke($out);
            long $result = org.fudaa.dodico.corba.base.TempsHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dateFin();
        } finally {
            _releaseReply($in);
        }
    }

    public String[] fuseauxHorairesDisponibles() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("fuseauxHorairesDisponibles", true);
            $in = _invoke($out);
            String $result[] = org.fudaa.dodico.corba.base.VChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return fuseauxHorairesDisponibles();
        } finally {
            _releaseReply($in);
        }
    }

    public String definitFuseauHoraire(String ID) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("definitFuseauHoraire", true);
            org.fudaa.dodico.corba.base.ChaineHelper.write($out, ID);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return definitFuseauHoraire(ID);
        } finally {
            _releaseReply($in);
        }
    }

    public String fuseauHoraire() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("fuseauHoraire", true);
            $in = _invoke($out);
            String $result = org.fudaa.dodico.corba.base.ChaineHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return fuseauHoraire();
        } finally {
            _releaseReply($in);
        }
    }

    /**
     * Certains jours de la semaine peuvent etre inactifs (false): Les
     * operations (generations, ouverture d'ouvrage) seront inactives ce jour.
     */
    public boolean lundi() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_lundi", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return lundi();
        } finally {
            _releaseReply($in);
        }
    }

    /**
     * Certains jours de la semaine peuvent etre inactifs (false): Les
     * operations (generations, ouverture d'ouvrage) seront inactives ce jour.
     */
    public void lundi(boolean newLundi) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_lundi", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newLundi);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            lundi(newLundi);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean mardi() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_mardi", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return mardi();
        } finally {
            _releaseReply($in);
        }
    }

    public void mardi(boolean newMardi) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_mardi", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newMardi);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            mardi(newMardi);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean mercredi() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_mercredi", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return mercredi();
        } finally {
            _releaseReply($in);
        }
    }

    public void mercredi(boolean newMercredi) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_mercredi", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newMercredi);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            mercredi(newMercredi);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean jeudi() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_jeudi", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return jeudi();
        } finally {
            _releaseReply($in);
        }
    }

    public void jeudi(boolean newJeudi) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_jeudi", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newJeudi);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            jeudi(newJeudi);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean vendredi() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_vendredi", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return vendredi();
        } finally {
            _releaseReply($in);
        }
    }

    public void vendredi(boolean newVendredi) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_vendredi", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newVendredi);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            vendredi(newVendredi);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean samedi() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_samedi", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return samedi();
        } finally {
            _releaseReply($in);
        }
    }

    public void samedi(boolean newSamedi) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_samedi", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newSamedi);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            samedi(newSamedi);
        } finally {
            _releaseReply($in);
        }
    }

    public boolean dimanche() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_dimanche", true);
            $in = _invoke($out);
            boolean $result = org.fudaa.dodico.corba.base.BooleenHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return dimanche();
        } finally {
            _releaseReply($in);
        }
    }

    public void dimanche(boolean newDimanche) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_set_dimanche", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, newDimanche);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            dimanche(newDimanche);
        } finally {
            _releaseReply($in);
        }
    }

    public void definitJoursActifs(boolean lundi, boolean mardi, boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi, boolean dimanche) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("definitJoursActifs", true);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, lundi);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, mardi);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, mercredi);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, jeudi);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, vendredi);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, samedi);
            org.fudaa.dodico.corba.base.BooleenHelper.write($out, dimanche);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            definitJoursActifs(lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
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

    private static String[] __ids = { "IDL:navigation/IGenerationDeterministe:1.0", "IDL:navigation/IGeneration:1.0", "IDL:navigation/IParametresSemaines:1.0", "IDL:objet/IObjet:1.0" };

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
