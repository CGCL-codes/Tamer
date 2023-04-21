package hr.fer.grafovi;

import hr.fer.grafovi.controller.MainController;
import hr.fer.grafovi.view.MainView;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author hrvoje
 *
 */
public class Main {

    private Main() {
        MainController ctrl = MainController.ctrl;
        MainView view = new MainView();
        view.setMinimumSize(new Dimension(1100, 790));
        view.pack();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        ctrl.addView(view);
    }

    /**
	 * main funkcija
	 * 
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 * @throws InvocationTargetException 
	 * @throws InterruptedException 
	 */
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorable) {
        }
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                new Main();
            }
        });
    }
}
