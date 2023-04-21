package es.cea;

public class Alumno {

    String dni;

    String nombre;

    public Alumno(String dni, String nombre) {
        super();
        this.dni = dni;
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dni == null) ? 0 : dni.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Alumno other = (Alumno) obj;
        if (dni == null) {
            if (other.dni != null) return false;
        } else if (!dni.equals(other.dni)) return false;
        return true;
    }
}
