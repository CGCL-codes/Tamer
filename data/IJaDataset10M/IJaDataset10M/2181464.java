package porDefecto;

public class Cuerpo implements Cloneable {

    private String nombre;

    private String apellido1;

    private String apellido2;

    private String apodo;

    /**
	 * Constructor con atributos.
	 * @param nombre
	 * @param apellido1
	 * @param apellido2
	 * @param apodo
	 */
    public Cuerpo(String nombre, String apellido1, String apellido2, String apodo) {
        super();
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.apodo = apodo;
    }

    /**
	 * Constructor sin atributos.
	 */
    public Cuerpo() {
        super();
        this.nombre = "";
        this.apellido1 = "";
        this.apellido2 = "";
        this.apodo = "";
    }

    /**
	 * @return the nombre
	 */
    public String getNombre() {
        return nombre;
    }

    /**
	 * @param nombre the nombre to set
	 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * @return the apellido1
	 */
    public String getApellido1() {
        return apellido1;
    }

    /**
	 * @param apellido1 the apellido1 to set
	 */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
	 * @return the apellido2
	 */
    public String getApellido2() {
        return apellido2;
    }

    /**
	 * @param apellido2 the apellido2 to set
	 */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
	 * @return the apodo
	 */
    public String getApodo() {
        return apodo;
    }

    /**
	 * @param apodo the apodo to set
	 */
    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apellido1 == null) ? 0 : apellido1.hashCode());
        result = prime * result + ((apellido2 == null) ? 0 : apellido2.hashCode());
        result = prime * result + ((apodo == null) ? 0 : apodo.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Cuerpo)) return false;
        Cuerpo other = (Cuerpo) obj;
        if (apellido1 == null) {
            if (other.apellido1 != null) return false;
        } else if (!apellido1.equals(other.apellido1)) return false;
        if (apellido2 == null) {
            if (other.apellido2 != null) return false;
        } else if (!apellido2.equals(other.apellido2)) return false;
        if (apodo == null) {
            if (other.apodo != null) return false;
        } else if (!apodo.equals(other.apodo)) return false;
        if (nombre == null) {
            if (other.nombre != null) return false;
        } else if (!nombre.equals(other.nombre)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Cuerpo [nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 + ", apodo=" + apodo + "]";
    }

    /**
	 * Implementaci�n del m�todo clone()
	 */
    public Cuerpo clone() {
        Cuerpo copiaCuerpo;
        copiaCuerpo = new Cuerpo();
        copiaCuerpo.setNombre(nombre);
        copiaCuerpo.setApellido1(apellido1);
        copiaCuerpo.setApellido2(apellido2);
        copiaCuerpo.setApodo(apodo);
        return copiaCuerpo;
    }
}
