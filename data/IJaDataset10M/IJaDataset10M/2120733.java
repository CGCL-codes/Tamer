package co.edu.javeriana.javerpg.servidor.exception;

/**
 * Esta Excepcion se lanza cuando el cliente intenta registrar un 
 * nombre de cuenta de usuario que ya existe.
 *
 * @author Cesar Casta�eda
 */
public class ExcepcionClienteYaExiste extends ExcepcionRemota {

    public ExcepcionClienteYaExiste(String m) {
        super(m);
    }
}
