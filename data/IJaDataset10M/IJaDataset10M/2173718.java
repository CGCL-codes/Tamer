package comunidad;

import java.util.ResourceBundle;
import utilidad.*;
import utilidad.clasesBase.*;
import utilidad.componentes.*;

/**
 *
 * @author Sergio
 */
public class ComunidadVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("comunidad/Bundle");

    private String nombre;

    public ComunidadVO() {
        inicializarComponetes();
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
        this.nombre = "";
    }
}
