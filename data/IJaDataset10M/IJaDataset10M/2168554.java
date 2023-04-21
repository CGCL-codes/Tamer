package com.expantion.dao;

public interface ConnectionDao {

    /**
	 * Connect un joueur à partir de son login et mot de passe.
	 * @param pUser
	 * @param pUserPassword
	 * @return
	 */
    Boolean connect(String pUser, String pUserPassword);

    /**
	 * Deconnect un joueur à partir de son id.
	 * @param pUser
	 * @return une chaine representant le status de la connection.
	 */
    Boolean disconnect(int pUserId);

    /**
	 * Indique si le nom d'un utilisateur existe.
	 * @param pUsername
	 * @return
	 */
    Boolean isUsernameValid(String pUsername);

    /**
	 * Indique si le mot de passe d'un utilisateur est correct.
	 * @param pUsername
	 * @param pPassword
	 * @return
	 */
    Boolean isPasswordValid(String pUsername, String pPassword);

    /**
	 * Indique si un utilisateur est deja connecté
	 * @param pUsername
	 * @return
	 */
    Boolean isUserConnected(String pUsername);
}
