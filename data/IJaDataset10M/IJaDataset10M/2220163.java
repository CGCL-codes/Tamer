package objects;

import graphical.modes.Gameplay;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import util.GameStateManager;
import util.Rainbow;

/**
 * A threaded file chooser.
 * 
 * @author Tom Arnold
 */
public class TFileChooser extends Thread {

    /**The file chooser.**/
    public JFileChooser c = new JFileChooser();

    /**Save dialog?.**/
    public boolean save = false;

    /**Load dialog?**/
    public boolean load = false;

    /**Load game.**/
    public static final String LOAD_GAME = "Load game";

    /**Save game.**/
    public static final String SAVE_GAME = "Save game";

    /**Save game directory.**/
    public static final String SAVE_DIR = "saves";

    /**
	 * Start this thread.
	 */
    public void run() {
        while (true) {
            if ((load || save) && !c.isShowing()) {
                c.setFileFilter(new SaveFilter());
                {
                    if (load) {
                        c.setDialogTitle(LOAD_GAME);
                    } else {
                        c.setDialogTitle(SAVE_GAME);
                    }
                }
                {
                    File s = new File(SAVE_DIR);
                    if (!s.isDirectory()) {
                        s.mkdir();
                    }
                    c.setCurrentDirectory(s);
                }
                c.requestFocus();
                {
                    if (load) {
                        int val = c.showDialog(new JFrame(), LOAD_GAME);
                        if (val == JFileChooser.APPROVE_OPTION) {
                            Gameplay.game.enterState(Gameplay.ID, new FadeOutTransition(Rainbow.FADE), new FadeInTransition(Rainbow.FADE));
                            Gameplay.RESTORE_Q = c.getSelectedFile().getPath();
                        }
                        load = false;
                    } else {
                        int val = c.showDialog(new JFrame(), SAVE_GAME);
                        if (val == JFileChooser.APPROVE_OPTION) {
                            GameStateManager.save(c.getSelectedFile().getPath());
                        }
                        save = false;
                    }
                }
            }
        }
    }
}
