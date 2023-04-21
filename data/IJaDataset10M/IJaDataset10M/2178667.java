package klient.view;

import javax.swing.JFrame;
import klient.controller.ViewsController;

/**
 * @author zby
 *
 */
public abstract class GameView extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8959215982584247420L;

    protected ViewsController root;

    public GameView(ViewsController p) {
        root = p;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public abstract void init();

    public synchronized ViewsController getRoot() {
        return root;
    }

    public synchronized void setRoot(ViewsController parent) {
        this.root = parent;
    }

    public static synchronized long getSerialVersionUID() {
        return serialVersionUID;
    }
}
