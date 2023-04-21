package neoAtlantis.utilidades.accessController.objects;

import java.util.ArrayList;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PermissionEntity {

    protected ArrayList<Permission> permisos;

    /**
     * Genera una entidad con permisos
     */
    public PermissionEntity() {
        this.permisos = new ArrayList<Permission>();
    }

    /**
     * Agrega un permiso a la entidad
     * @param permiso Nombre del permiso
     * @param valor Valor del permiso
     */
    public void agregarPermiso(Permission permiso) {
        if (permiso == null) {
            return;
        }
        this.permisos.add(permiso);
    }

    /**
     * Verifica si tiene activo un permiso
     * @param permiso Permiso a validar
     * @return true si esta activo (para validar si esta activo revisa si el valor del permiso coincide con 'activo', 'ok', 'yes', 'si' o 'true')
     */
    public boolean validaPermiso(Permission permiso) {
        for (Permission p : this.permisos) {
            if (p.getNombre().equals(permiso.getNombre())) {
                if (p != null && (p.getValor().equalsIgnoreCase("ACTIVO") || p.getValor().equalsIgnoreCase("OK") || p.getValor().equalsIgnoreCase("YES") || p.getValor().equalsIgnoreCase("SI") || p.getValor().equalsIgnoreCase("TRUE") || p.getValor().equalsIgnoreCase("1") || p.getValor().equalsIgnoreCase("ACTIVE"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public boolean validaPermiso(String permiso) {
        if (permiso == null) {
            return false;
        }
        return this.validaPermiso(new Permission(permiso));
    }

    /**
     * Obtiene el valor dado a un permiso
     * @param permiso Permiso a obtener
     * @return valor del permiso
     */
    public String obtienePermiso(Permission permiso) {
        for (Permission p : this.permisos) {
            if (p.getNombre().equals(permiso.getNombre())) {
                return p.getValor();
            }
        }
        return null;
    }

    public String obtienePermiso(String permiso) {
        return obtienePermiso(new Permission(permiso));
    }

    /**
     * Genera la informac&oacute;n de la entidad.
     * @return Informaci&oacute;n de la entidad
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        sb.append("//--------------- Permisos ---------------//").append(System.getProperty("line.separator"));
        for (Permission p : this.permisos) {
            sb.append("\t").append(p.getNombre()).append(": ").append(p.getValor()).append(System.getProperty("line.separator"));
        }
        sb.append("//---------------------------------------//").append(System.getProperty("line.separator"));
        return sb.toString();
    }

    /**
     * Genera una cadena con un nombre limpio de caracteres no validos.
     * @param nombre Cadena con el nombre
     * @return Nombre sin caracteres validos
     */
    public static String limpiaNombre(String nombre) {
        return nombre.toUpperCase().replace('�', 'A').replace('�', 'E').replace('�', 'I').replace('�', 'O').replace('�', 'U').replace(' ', '_');
    }
}
