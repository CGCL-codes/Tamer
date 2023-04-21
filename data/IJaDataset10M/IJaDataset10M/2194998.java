package Excepciones;

/**
 *
 * @author diego
 */
public class ExcepcionSistemaStock extends Exception {

    private String mensaje;

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return mensaje;
    }
}
