package gestorDeConfiguracion;

/**
 * Control de las excepciones posibles en la clase FicheroConfiguracionServidor
 * 
 * @author David
 */
public class ControlConfiguracionServidorException extends Exception {

    public ControlConfiguracionServidorException(Exception e) {
        super(e);
    }

    public ControlConfiguracionServidorException(String sMensaje) {
        super(sMensaje);
    }

    public ControlConfiguracionServidorException(String sMensaje, Exception e) {
        super(sMensaje, e);
    }
}
