package fr.unice.gfarce.dao;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.ext.ExtObjectContainer;
import fr.unice.gfarce.connect.Db4oConfig;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Formation;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Identite.TypeIdentite;
import fr.unice.gfarce.identity.RespFormation;

/**
 *  Classe permettant d'acc&eacute;der &agrave; la base de donn&eacute;es db4o et d'en modifier les Identites.
 */
public class Db4oIdentiteFactory implements IdentiteDao {

    ObjectContainer bd;

    String nombase;

    Configuration configuration;

    public Db4oIdentiteFactory() throws IOException {
        nombase = Db4oConfig.getInfo();
    }

    private void getConfig() {
        configuration = Db4o.newConfiguration();
        configuration.lockDatabaseFile(false);
        configuration.objectClass(Calendar.class).storeTransientFields(true);
    }

    /**
 * Ajoute une Identite dans le cache.
 * on ajoute &eacute;galement le candidat &agrave; la  formation nomForm.
 * @param ident
 * @param nomForm
 * @param dateLimite
 * @return 
 */
    @Override
    public void insert(Identite ident, String nomForm, Calendar dateLimite) {
        if (ident.getType() == TypeIdentite.CANDIDAT) {
            Candidat cand = (Candidat) ident;
            getConfig();
            bd = Db4o.openFile(configuration, nombase);
            configuration.objectClass(Formation.class).cascadeOnDelete(true);
            ObjectSet<Formation> test = bd.queryByExample(new Formation());
            for (Formation ligne : test) {
                if (ligne.getTitre_formation().equals(nomForm) && ligne.getDate_limite_candidature().get(Calendar.MONTH) == dateLimite.get(Calendar.MONTH) && ligne.getDate_limite_candidature().get(Calendar.DAY_OF_MONTH) == dateLimite.get(Calendar.DAY_OF_MONTH) && ligne.getDate_limite_candidature().get(Calendar.YEAR) == dateLimite.get(Calendar.YEAR)) {
                    cand.setFormation(ligne);
                    ligne.ajouterCandidat(cand);
                    bd.delete(ligne);
                    break;
                }
            }
            bd.commit();
            bd.close();
        }
        insertIdentite(ident);
    }

    public void insertIdentite(Identite ident) {
        getConfig();
        bd = Db4o.openFile(configuration, nombase);
        bd.store(ident);
        bd.commit();
        bd.close();
    }

    /**
 * Modifie une Identite deja pr&eacute;sente dans le cache.
 * @param ident
 * @return 
 */
    @Override
    public void update(Identite ident) throws Exception {
        getConfig();
        bd = Db4o.openFile(configuration, nombase);
        if (ident.getType() == TypeIdentite.CANDIDAT) {
            Candidat identC = (Candidat) ident;
            Candidat result = (Candidat) bd.queryByExample(new Identite(ident.getNom(), ident.getPrenom(), null, null, ident.getType())).get(0);
            result.setEmail(identC.getEmail());
            result.setAcceptation(identC.getAcceptation());
            result.setSex(identC.getSex());
            result.setDate_naissance(identC.getDateNaissance());
            result.setBource(identC.getBource());
            result.setNationalite(identC.getNationalite());
            ExtObjectContainer eoc = bd.ext();
            if (!eoc.isStored(result)) {
                System.out.println(result + " n'a pas d'identit� dans la base");
            }
            bd.store(result);
            bd.commit();
            bd.close();
        } else {
            RespFormation identF = (RespFormation) ident;
            RespFormation result = (RespFormation) bd.queryByExample(new Identite(ident.getNom(), ident.getPrenom(), null, null, ident.getType())).get(0);
            result.setEmail(identF.getEmail());
            result.setSex(identF.getSex());
            result.setTelephone(identF.getTelephone());
            ExtObjectContainer eoc = bd.ext();
            if (!eoc.isStored(result)) {
                System.out.println(result + " n'a pas d'identit� dans la base");
            }
            bd.store(result);
            bd.commit();
            bd.close();
        }
    }

    /**
 * Supprime une Identite dans le cache.
 * @param identite
 * @return 
 */
    @Override
    public void delete(Identite ident) {
        getConfig();
        bd = Db4o.openFile(configuration, nombase);
        if (ident.getType() == TypeIdentite.CANDIDAT) {
            Candidat res = (Candidat) bd.queryByExample(new Identite(ident.getNom(), ident.getPrenom(), null, null, null)).get(0);
            if (res != null) {
                res.getFormation().supprimerCandidat(res);
                bd.delete(res);
            }
            bd.commit();
            bd.close();
        } else {
            RespFormation res = (RespFormation) bd.queryByExample(new Identite(ident.getNom(), ident.getPrenom(), null, null, null)).get(0);
            if (res != null) {
                bd.delete(res);
            }
            bd.commit();
            bd.close();
        }
    }

    /**
 * Recherche des Candidats du cache selon certains crit&egrave;res.
 * Retourne tous les candidats du cache si tous les param&egrave;tres sont null.
 * @param nom
 * @param prenom
 * @param sex
 * @param email
 * @return Candidat[]
 */
    @Override
    public Candidat[] findCandidat(String nom, String prenom, String sex, String email) {
        getConfig();
        bd = Db4o.openFile(configuration, nombase);
        List<Identite> f = find(nom, prenom, sex, email, TypeIdentite.CANDIDAT);
        if (f != null) {
            Candidat[] identite = f.toArray(new Candidat[f.size()]);
            bd.close();
            return identite;
        } else {
            bd.close();
            return null;
        }
    }

    private List<Identite> find(String nom, String prenom, String sex, String email, TypeIdentite type) {
        ObjectSet<Identite> f = bd.queryByExample(new Identite(nom, prenom, sex, email, type));
        List<Identite> lid = (List<Identite>) f;
        return lid;
    }

    /**
 * Recherche des RespFormations du cache selon certains crit&egrave;res.
 * Retourne tous les responsables du cache si tous les param&egrave;tres sont null.
 * @param nom
 * @param prenom
 * @param sex
 * @param email
 * @return RespFormation[]
 */
    @Override
    public RespFormation[] findRespForm(String nom, String prenom, String sex, String email) {
        getConfig();
        bd = Db4o.openFile(configuration, nombase);
        List<Identite> f = find(nom, prenom, sex, email, TypeIdentite.FORMATEUR);
        if (f != null) {
            RespFormation[] identite = f.toArray(new RespFormation[f.size()]);
            bd.close();
            return identite;
        } else {
            bd.close();
            return null;
        }
    }
}
