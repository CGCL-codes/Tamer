package br.ufal.graw.usermodel;

import br.ufal.graw.DatabaseLayer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import br.ufal.graw.AbstractCommunity;
import br.ufal.graw.AbstractUser;
import br.ufal.graw.User;
import br.ufal.graw.Community;

/**
 *
 * @author Marcello de Sales
 */
public class UserCommunityPreferences {

    private User user;

    private Community community;

    private Vector communityPerfils;

    private Hashtable userPreferences;

    private DatabaseLayer database;

    public UserCommunityPreferences(User user, Community community) throws PerfilException {
        if (!user.isMember(community)) throw new PerfilException("Imposs�vel carregar Perfil de Usu�rio (" + user.getID() + ") para a comunidade (" + community.getID() + ")!"); else {
            this.user = user;
            this.community = community;
            this.database = user.getDataBaseLayer();
            this.initObject();
        }
    }

    private void initObject() {
        this.userPreferences = new Hashtable();
        this.communityPerfils = new Vector();
        this.getCommunityPerfilsRelation();
        this.getUserPerfilsRelation();
    }

    /**
	 * Method addUserCommunityPerfil. Adiciona preferencia na tabela ID -> UserCommunityPerfil
	 *
	 * @param    ucp            Um UserCommunityPerfil.
	 *
	 */
    private void addUserCommunityPerfil(UserCommunityPerfil ucp) {
        this.userPreferences.put(ucp.getPerfilID(), ucp);
    }

    /**
	 * Method hasChosen. Verifica se um dado perfil foi escolhido pelo usu�rio.
	 *
	 * @param    perfilID            a  String
	 *
	 * @return   a boolean
	 *
	 */
    public boolean hasChosen(String perfilID) {
        UserCommunityPerfil ucp = (UserCommunityPerfil) this.userPreferences.get(perfilID);
        return (ucp != null);
    }

    /**
	 * Method getCommunityPerfilsRelation. Carrega todos os perfis do comunidade.
	 *
	 */
    private void getCommunityPerfilsRelation() {
        try {
            Vector result = this.database.query("SELECT perfilID FROM perfil_community WHERE communityID='" + this.community.getID() + "'");
            int quant = result.size();
            for (int i = 0; i < quant; i++) {
                String perfilID = (String) ((Hashtable) result.get(i)).get("perfilID");
                CommunityPerfil communityPerfil = new CommunityPerfil(perfilID, this.database);
                this.communityPerfils.add(communityPerfil);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Method getUserPerfilsRelation. Atualiza a hashtable com as preferencias
	 * que o usu�rio tem na comunidade. [perfilID, true]
	 *
	 */
    private void getUserPerfilsRelation() {
        Vector result = this.database.query("SELECT perfilUserID FROM perfil_user_community WHERE communityID='" + this.community.getID() + "' AND userID='" + this.user.getID() + "'");
        int quant = result.size();
        for (int i = 0; i < quant; i++) {
            String perfilUserID = (String) ((Hashtable) result.get(i)).get("perfilUserID");
            try {
                UserCommunityPerfil ucp = new UserCommunityPerfil(perfilUserID, this.database);
                if (!CommunityPerfil.exists(this.community.getID(), ucp.getPerfilID(), this.database)) this.removeOldPreference(ucp.getPerfilID()); else {
                    this.addUserCommunityPerfil(ucp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Pega qualquer perfil do vetor dos perfil da comunidade. */
    public CommunityPerfil getCommunityPerfil(String perfilID) {
        CommunityPerfil pf = null;
        Iterator it = this.communityPerfils.iterator();
        while (it.hasNext()) {
            pf = (CommunityPerfil) it.next();
            if (pf.getID().equals(perfilID)) break;
        }
        return pf;
    }

    /** Pega qualquer perfil do vetor dos perfil da comunidade. */
    public UserCommunityPerfil getUserCommunityPerfil(String perfilID) {
        return (UserCommunityPerfil) this.userPreferences.get(perfilID);
    }

    /** Retorna todos os perfils da comunidade em forma de array. */
    public CommunityPerfil[] getCommunityPerfils() {
        return (CommunityPerfil[]) this.communityPerfils.toArray();
    }

    /**
	 * Method removeOldPreference. Remove preferencias do usu�rio que ainda existe
	 * em seu perfil, por�m o respons�vel pela comunidade atualizou o perfil.
	 *
	 * @param    perfilID            a  String
	 *
	 */
    private void removeOldPreference(String perfilID) {
        try {
            this.database.update("DELETE FROM perfil_user_community WHERE userID='" + this.user.getID() + "' AND perfilID='" + perfilID + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mountPreferences() {
        Iterator it = this.communityPerfils.iterator();
        while (it.hasNext()) {
            CommunityPerfil cp = (CommunityPerfil) it.next();
            boolean chosen = this.hasChosen(cp.getID());
            System.out.println(cp.getKeyword() + " - " + chosen);
        }
    }

    /**
	 *
	 */
    public static void main(String[] args) {
        try {
            DatabaseLayer db = new DatabaseLayer();
            Community ia = AbstractCommunity.getRealCommunity("1016044335989", db);
            User marcello = AbstractUser.getRealUser("1015439644996", db);
            UserCommunityPreferences ucp = new UserCommunityPreferences(marcello, ia);
            ucp.mountPreferences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
