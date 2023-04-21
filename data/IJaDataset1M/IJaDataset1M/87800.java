package neoAtlantis.utilidades.ctrlAcceso;

import java.util.*;
import neoAtlantis.utilidades.ctrlAcceso.interfaces.EntidadConPermisos;

/**
 * Definici&oacute;n de un usuario de sistema.
 * @version 2.0
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class Usuario extends EntidadConPermisos {

    /**
     * Versi&oacute;n de la clase.
     */
    public static final String VERSION = "2.0";

    /**
     * Constante que indica una terminal WEB.
     */
    public static final String WEB = "WEB";

    /**
     * Constante que indica una terminal StandAlone.
     */
    public static final String STAND_ALONE = "STANDALONE";

    /**
     * Constante que indica un origen local.
     */
    public static final String LOCAL = "localhost";

    private String id;

    private String nombre;

    private String user;

    private ArrayList<Rol> roles;

    private HashMap<String, String[]> propiedades;

    private String terminal;

    private String origen;

    /**
     * Genera un Usuario indicando su origen de conexi&oacute;n y tipo de terminal.
     * @param user Nickname del usuario
     * @param origen Origen de conecxi&oacute;n
     * @param terminal Terminal de conexi&oacute;n
     */
    public Usuario(String user, String origen, String terminal) {
        if (origen == null || terminal == null || origen.length() == 0 || terminal.length() == 0) {
            throw new RuntimeException("No se permiten valores nulos ni vacios en terminal y origen.");
        }
        this.propiedades = new HashMap();
        this.origen = origen;
        this.terminal = terminal;
        this.user = user;
        this.id = user;
    }

    /**
     * Genera un Usuario indicando su origen de conexi&oacute;n.
     * @param user Nickname del usuario
     * @param origen Origen de conexi&oacute;n
     */
    public Usuario(String user, String origen) {
        this(user, origen, Usuario.WEB);
    }

    /**
     * Genera un Usuario.
     * @param user Nickname del usuario
     */
    public Usuario(String user) {
        this(user, Usuario.LOCAL, Usuario.WEB);
    }

    /**
     * Regresa el identificador del usuario.
     * @return Identificador
     */
    public String getId() {
        return id;
    }

    /**
     * Asigna un identificador al usuario.
     * @param id Identificador para el usuario
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Regresa el nombre del usuario.
     * @return Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna un nombre al usuario.
     * @param nombre Nombre para el usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Regresa el nickname del usuario.
     * @return Nickname
     */
    public String getUser() {
        return user;
    }

    /**
     * Asigna un nickName al usuario.
     * @param user Nickname para el usuario
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Regresa los roles que tiene asignados el usuario.
     * @return Roles
     */
    public ArrayList<Rol> getRoles() {
        return roles;
    }

    /**
     * Agrega un rol para el usuario.
     * @param rol Rol a agregar
     */
    public void agregaRol(Rol rol) {
        if (this.roles == null) {
            this.roles = new ArrayList();
        }
        this.roles.add(rol);
    }

    /**
     * Remueve un rol de los asignados al usuario.
     * @param rol Rol a remover
     */
    public void remueveRol(Rol rol) {
        if (this.roles != null) {
            this.roles.remove(rol);
        }
    }

    /**
     * Obtiene el nombre de las propiedades asignadas al usuario.
     * @return Arreglo con nombres de las propiedades
     */
    public String[] getPropiedades() {
        String[] t = new String[this.propiedades.size()];
        Iterator iter = this.propiedades.keySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            t[i] = (String) iter.next();
            i++;
        }
        return t;
    }

    /**
     * Regresa los valores que tiene una determinada propiedad del usuario.
     * @param propiedad Propiedad de la que se desean sus valores
     * @return Valores de la propiedad
     */
    public String[] getPropiedad(String propiedad) {
        return this.propiedades.get(propiedad);
    }

    /**
     * Agrega o actualiza una propiedad del usuario.
     * @param propiedad Propiedad a agregar o actualizar
     * @param valores Valores de la propiedad
     */
    public void setPropiedad(String propiedad, String[] valores) {
        this.propiedades.put(propiedad, valores);
    }

    /**
     * Verifica si tiene activo un permiso.
     * @param permiso Permiso a validar
     * @return true si esta activo
     */
    @Override
    public boolean validaPermiso(String permiso) {
        if (super.validaPermiso(permiso)) {
            return true;
        }
        for (int i = 0; this.roles != null && i < this.roles.size(); i++) {
            if (this.roles.get(i).validaPermiso(permiso)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el valor asignado a un permiso.
     * @param permiso Permiso a obtener
     * @return valor del permiso
     */
    @Override
    public String obtienePermiso(String permiso) {
        String t = super.obtienePermiso(permiso);
        if (t != null) {
            return t;
        }
        for (int i = 0; this.roles != null && i < this.roles.size(); i++) {
            t = this.roles.get(i).obtienePermiso(permiso);
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    /**
     * Valida la pertenencia del usuario a un determinado ROL.
     * @param rol Nombre del rol a validar
     * @return true si es que pertenece al rol
     */
    public boolean perteneceRol(String rol) {
        for (int i = 0; this.roles != null && i < this.roles.size(); i++) {
            if (this.roles.get(i).getNombre().equalsIgnoreCase(rol.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida la pertenencia del usuario a cualquiera de un grupo de roles.
     * @param roles Arreglos con los roles
     * @return true si pertenece a alguno
     */
    public boolean perteneceAlgunRol(String[] roles) {
        for (int i = 0; this.roles != null && i < this.roles.size(); i++) {
            for (int j = 0; roles != null && j < roles.length; j++) {
                if (this.roles.get(i).getNombre().equalsIgnoreCase(roles[j].toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Genera la informac&oacute;n del usuario.
     * @return Informaci&oacute;n del usuario
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        Iterator iter = this.propiedades.keySet().iterator();
        String cTmp;
        sb.append("/*********************  USUARIO  ********************//\n");
        sb.append("ID: ").append(this.id).append("\n");
        sb.append("Nombre: ").append(this.nombre).append("\n");
        sb.append("User: ").append(this.user).append("\n");
        while (iter.hasNext()) {
            cTmp = (String) iter.next();
            sb.append(cTmp).append(": ");
            for (int j = 0; this.propiedades.get(cTmp) != null && j < this.propiedades.get(cTmp).length; j++) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(this.propiedades.get(cTmp)[j]);
            }
            sb.append("\n");
        }
        sb.append("Roles: ");
        for (int i = 0; this.roles != null && i < this.roles.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(((Rol) this.roles.get(i)).getNombre());
        }
        sb.append("\n");
        sb.append(super.toString());
        sb.append("/****************************************************//\n");
        return sb.toString();
    }

    /**Regresa el tipo de terminal de conexi&oacute;n.
     * @return Tipo de conexi&oacute;n
     */
    public String getTerminal() {
        return terminal;
    }

    /**Asigna el tipo de terminal de conexi&oacute;n.
     * @param Tipo de conexi&oacute;n
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**Regresa el origen de conexi&oacute;n.
     * @return Origen de conexi&oacute;n
     */
    public String getOrigen() {
        return origen;
    }

    /**Asigna el origen de conexi&oacute;n.
     * @param Origen de conexi&oacute;n
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
