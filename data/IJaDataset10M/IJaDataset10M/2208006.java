package com.proyecto.bigbang.core.usuarios;

import com.proyecto.bigbang.core.domain.Usuario;

public class UserManager {

    public static final String ADMINISTRADOR = "Administrador";

    public static final String DIRECCION = "Direccion";

    public static final String COOPERADORA = "Cooperadora";

    public static final String ASISTENTE = "Asistente";

    public static final String ADM_USER = "admin";

    public static final String ADM_PASS = "admin";

    public static Usuario currentUser;

    public static boolean isLoged;

    /** 
	 *  Retorna un string con los datos del usuario
	 *  Si es adm entonces solo dice "administrador"
	 *  @author Diego
	 */
    public static String getUserDetails() {
        if (isLoged) {
            if (currentUser.getRol().equals(ADMINISTRADOR)) {
                return ADMINISTRADOR;
            } else {
                return new StringBuilder().append(currentUser.getNombre()).append(" ").append(currentUser.getApellido()).toString();
            }
        }
        return new String("no hay usuario");
    }

    /**
	 * Controla si el que se logueo es adm.
	 * @param user
	 * @return
	 */
    public static boolean isAdministrator(Usuario user) {
        if (user.getUser().equals(ADM_USER) && user.getClave().equals(ADM_PASS)) {
            return true;
        }
        return false;
    }

    public static Usuario getAdministrador() {
        Usuario adm = new Usuario();
        adm.setUser(ADM_USER);
        adm.setClave(ADM_PASS);
        adm.setRol(ADMINISTRADOR);
        return adm;
    }
}
