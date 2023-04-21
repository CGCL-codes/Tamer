package es.caib.bpm.core.user;

public interface BPMUser {

    /**
	 * Recupera el Id unico de usuario.
	 * 
	 * @return el id unico de usuario.
	 */
    public Long getId();

    /**
	 * Recupera el nombre de usuario.
	 * 
	 * @return el nombre de usuario.
	 */
    public String getUsername();

    /**
	 * Recupera la direccion de e-mail del usuario.
	 * 
	 * @return la direccion de e-mail del usuario.
	 */
    public String getEmailAddress();
}
