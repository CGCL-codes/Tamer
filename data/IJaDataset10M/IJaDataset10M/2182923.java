package limfic.dominio;

/**
 * Clase que contiene las propiedades comunes a todas las personas
 * 
 * Otras clases heredaran de esta.
 *
 * No tiene ningun servicio, ni interfaz ni repositorio.
 * @author OpenCodes
 * 
 */
public class Persona {

    private int id;

    private String nombre;

    private String apellido;

    private String telefono;

    private Direccion DireccionParticular;

    public Persona(int id, String nombre, String apellido) {
        this(id, nombre, apellido, null, null);
    }

    public Persona(int id, String nombre, String apellido, String telefono) {
        this(id, nombre, apellido, telefono, null);
    }

    public Persona(int id, String nombre, String apellido, String telefono, Direccion direccionParticular) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.DireccionParticular = direccionParticular;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccionParticular() {
        return DireccionParticular;
    }

    public void setDireccionParticular(Direccion direccionParticular) {
        this.DireccionParticular = direccionParticular;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("Persona { ");
        buffer.append("id:");
        buffer.append(id);
        buffer.append("; nombre:");
        buffer.append(nombre);
        buffer.append("; apellido:");
        buffer.append(apellido);
        buffer.append("; telefono:");
        buffer.append(telefono);
        if (DireccionParticular != null) {
            buffer.append("; direccion: ");
            buffer.append(DireccionParticular);
        }
        buffer.append(" }");
        return buffer.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Persona that = (Persona) obj;
        return this.id == that.id && this.nombre == that.nombre && this.apellido == that.apellido;
    }
}
