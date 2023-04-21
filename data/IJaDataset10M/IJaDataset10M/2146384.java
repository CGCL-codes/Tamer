package quienesquien;

/**
 * 
 * @author warzo
 */
public class Persona {

    private String Nombre;

    private String Apellidos;

    private String Uid;

    public Persona(String Nombre, String Apellidos, String Uid) {
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Uid = Uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public String getUid() {
        return Uid;
    }

    public String getFoto() {
        return Uid.substring(1, Uid.length()) + ".jpeg";
    }
}
