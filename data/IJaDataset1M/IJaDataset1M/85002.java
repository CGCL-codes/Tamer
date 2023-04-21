package formateadores;

/**
 * Modifica los datos, en general para representar frecuencias en Hz.
 * 
 * @author Rodrigo Villamil Perez
 */
public class FormateadorDeFrecuenciasEnHz implements IFormateadorDeDatos {

    private static final long serialVersionUID = 1L;

    public FormateadorDeFrecuenciasEnHz() {
    }

    /**
     * Formatea el objeto 'valor', retornando un string.
     */
    public Object formatea(Object valor) {
        return valor.toString() + " kHz.";
    }
}
