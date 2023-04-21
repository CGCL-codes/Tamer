package gnu.chu.utilidades;

/**
 * <p>
 * Para utilizarlo solo hay que intanciarlo y machacar la funcion run.<p>
 * <p>
 * Creado el 10/07/2000
 * <p>
 * @author   Angel J. Apellaniz
 * @author   EMAIL: aapella@virtualcom.es<p>
 * <p>
 * @version  1.0 del 10/07/2000
 * <p>
 */
public class ThreadVirtual extends Thread {

    public ThreadVirtual(String title, ThreadGroup grupo) {
        super(grupo, "Virtual." + title);
        start();
    }

    public ThreadVirtual(String title) {
        super("Virtual." + title);
        start();
    }

    public void run() {
    }
}
