package adirondack.transcoder.app.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dispatch all events from the Edit Menu
 * 
 * @author anthony
 */
public class EditMenuController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Edit:" + e.getActionCommand());
        if (e.getActionCommand().equals("Preferences")) {
            handlePreferences(e);
        }
    }

    /**
	 * Handle the case where Preferences was just selected
	 * 
	 * @param e
	 *            The triggered ActionEvent
	 */
    public void handlePreferences(ActionEvent e) {
    }
}
