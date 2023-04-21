package vo;

public class ProfesorVo {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int numeroLegajo;

    private String nombre;

    private DireccionVo direccion;

    public ProfesorVo() {
        super();
    }

    public ProfesorVo(int numeroLegajo, String nombre, DireccionVo direccion) {
        this.numeroLegajo = numeroLegajo;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getNumeroLegajo() {
        return numeroLegajo;
    }

    public void setNumeroLegajo(int numeroLegajo) {
        this.numeroLegajo = numeroLegajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DireccionVo getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionVo direccion) {
        this.direccion = direccion;
    }
}
