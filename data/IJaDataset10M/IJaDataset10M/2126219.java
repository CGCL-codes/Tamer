package objetos.entidades;

/**
 *
 * @author luciano
 */
public class Alumno {

    private Long id;

    private String nombre;

    private String legajo;

    public Alumno() {
    }

    public Alumno(Long id, String nombre, String legajo) {
        this.id = id;
        this.nombre = nombre;
        this.legajo = legajo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alumno other = (Alumno) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "id: " + this.getId() + ", legajo: " + this.getLegajo() + ", nombre: " + this.getNombre();
    }
}
