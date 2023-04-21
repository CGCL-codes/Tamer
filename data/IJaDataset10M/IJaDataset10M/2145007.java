package es.rvp.java.simpletag.core.internacionalization;

/**
 * Clase para acceder a los mensajes en Core.
 *
 * @author Rodrigo Villamil Perez
 */
public class CoreMessages extends AppMessages {

    private static class SingletonHolder {

        private static final CoreMessages INSTANCE = new CoreMessages();
    }

    /**
	 * Retorna la unica instancia del objeto.
	 */
    public static AppMessages getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private CoreMessages() {
        super("core-messages");
    }
}
