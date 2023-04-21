package jvvs.client.MainMenu;

import java.awt.event.*;

/**
 * Handles exiting from the MainMenu
 *
 * @author Adam VanderHook
 */
public class ExitClick implements ActionListener {

    MainFrame theApp;

    /**
	 * Default constructor
	 *
	 * @param		win		The parent MainMenu
	 */
    public ExitClick(MainFrame win) {
        theApp = win;
    }

    /**
	 * Exits the program nominally
	 *
	 * @param		e		The ActionEvent that called this method
	 */
    public void actionPerformed(ActionEvent e) {
        System.out.println(theApp.getSize());
        System.exit(0);
    }
}
