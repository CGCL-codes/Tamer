package de.bitsetter.roomak.control;

import java.awt.event.*;

/**
 * <p>An action for loading a map from a file</p>
 * 
 * @author tkarrass@bitsetter.de
 */
public class OpenProjectAction extends RoomakAction {

    private static final long serialVersionUID = 4617992615920608349L;

    /**
	 * Creates the OpenProjectAction. 
	 *
	 */
    public OpenProjectAction() {
        super("Projekt öffnen...", "open", new Integer(KeyEvent.VK_O), "Öffnet ein bestehendes Projekt");
    }

    /**
	 * Not implemented yet. 
	 */
    public void actionPerformed(ActionEvent arg0) {
        System.out.println("TODO: Projekt öffnen...");
    }
}
