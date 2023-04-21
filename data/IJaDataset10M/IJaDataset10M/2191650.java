package rothag.graphics.effect;

import java.util.TimerTask;
import com.sun.awt.AWTUtilities;
import javax.swing.JFrame;
import rothag.MainApp;

/**
 * Classe pour gérer l'effet FadeIn
 * @author Gaetan
 */
public class FaderIn extends TimerTask {

    private JFrame jFrame;

    /**
     * Constructeur
     * @param jFrame Frame sur laquelle sera effectué le FadeIn
     */
    public FaderIn(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    FaderIn(MainApp aThis) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Action pour augmenter l'opacité de la Frame
     */
    @Override
    public void run() {
        if (AWTUtilities.getWindowOpacity(jFrame) < 1f) {
            try {
                if ((1 - AWTUtilities.getWindowOpacity(jFrame)) < 0.01) {
                    AWTUtilities.setWindowOpacity(jFrame, 1f);
                } else {
                    AWTUtilities.setWindowOpacity(jFrame, AWTUtilities.getWindowOpacity(jFrame) + 0.01f);
                }
            } catch (Exception e) {
            }
        } else {
            this.cancel();
        }
    }
}
