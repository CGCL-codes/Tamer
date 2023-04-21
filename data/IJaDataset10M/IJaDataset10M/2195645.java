package EVA;

import criaturas.animacion.CriaturaAnimada;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.Timer;

/**Configuracion de control de las animaciones.
 * Aqui se recalculan y refrescan.
 * PD: Es posible que se pueda hacer una llamada por ciclo. Quizás
 * "aliviaria" el juego.
 *
 * @author Gabriel
 */
public class animConfig {

    /**
     * Tiempo en MS en el que se llamará a refrescar las animaciones
     */
    private static final int DEF_ANIM_MS = 60;

    private static Timer timer;

    private static TreeSet<CriaturaAnimada> conjunto = new TreeSet<CriaturaAnimada>();

    private animConfig() {
    }

    ;

    public static void refreshAnim() {
        Iterator<CriaturaAnimada> i = conjunto.iterator();
        while (i.hasNext()) {
            i.next().refresh();
        }
    }

    /**
     * Comienza a recalcular las animaciones
     */
    public static void startAnimations() {
    }

    /**
     * Para las animaciones
     */
    public static void stopAnimations() {
        timer.stop();
    }

    /**
     * Para las animaciones y borra las animaciones
     */
    public static void reset() {
        timer.stop();
        conjunto.clear();
    }

    /**
     * Añade una nueva criatura a la que animar
     * @param can Criatura nueva.
     */
    public static void addCriatura(CriaturaAnimada can) {
        conjunto.add(can);
    }
}
