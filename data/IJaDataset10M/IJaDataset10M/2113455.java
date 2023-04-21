package centroCoste;

import utilidad.clasesBase.*;

/**
 *
 * @author Sergio
 */
public class CentroCosteVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String tipo;

    private String nombre;

    public CentroCosteVO() {
        inicializarComponetes();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void inicializarComponetes() {
        super.inicializarComponetes();
        this.tipo = "";
        this.nombre = "";
    }
}
