package negocio.cv;

public class Calle {

    private int idCalle;

    private String nombre;

    public Calle(String nombre) {
        super();
        this.nombre = nombre;
    }

    public Calle() {
    }

    public Calle(int id, String string) {
        this(string);
        idCalle = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCalle() {
        return idCalle;
    }

    public void setIdCalle(int idCalle) {
        this.idCalle = idCalle;
    }
}
