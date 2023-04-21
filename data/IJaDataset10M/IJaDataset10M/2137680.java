package org.openXpertya.model;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class PrincipalOXP implements Principal {

    /**
     * Constructor de la clase ...
     *
     *
     * @param name
     * @param password
     * @param roles
     */
    public PrincipalOXP(String name, String password, List roles) {
        super();
        m_name = name;
        m_password = password;
        if (roles != null) {
            m_roles = new String[roles.size()];
            m_roles = (String[]) roles.toArray(m_roles);
            if (m_roles.length > 0) {
                Arrays.sort(m_roles);
            }
        }
    }

    /** Descripción de Campos */
    private String m_name = null;

    /** Descripción de Campos */
    private String m_password = null;

    /** Descripción de Campos */
    private String m_roles[] = new String[0];

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getName() {
        return m_name;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    String getPassword() {
        return m_password;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    String[] getRoles() {
        return m_roles;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("PrincipalOXP[");
        sb.append(m_name).append(": ");
        for (int i = 0; i < m_roles.length; i++) {
            sb.append(m_roles[i]).append(" ");
        }
        sb.append("]");
        return (sb.toString());
    }

    /**
     * Descripción de Método
     *
     *
     * @param role
     *
     * @return
     */
    public boolean hasRole(String role) {
        if (role == null) {
            return false;
        }
        return (Arrays.binarySearch(m_roles, role) >= 0);
    }
}
