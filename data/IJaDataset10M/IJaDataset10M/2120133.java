package ramon;

import java.io.Serializable;

/**
 * Interfaz que debe implementar alguna clase para que el framework reconozca
 * que hay un usuario logueado.
 */
public interface StatusLogin extends Serializable {

    /**
	 * @return true si el usuario está logueado
	 */
    public boolean loginOk();
}
