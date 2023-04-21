package test;

import java.awt.Dimension;
import java.awt.Toolkit;
import ui.Home;

/**
 * The Class TestManagerCostruttore.
 */
public class TestManagerCostruttore {

    /**
	 * The main method.
	 * 
	 * @param args
	 *            the args
	 */
    public static void main(String[] args) {
        try {
            Home home = new Home(null);
            home.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("AWT problems");
        }
    }
}
