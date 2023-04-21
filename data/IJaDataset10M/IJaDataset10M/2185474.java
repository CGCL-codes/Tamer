package helden.midlet;

import java.io.IOException;
import java.util.Hashtable;
import helden.midlet.menu.HeldenMenuItem;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.util.Resources;

public class MainMenuForm extends Form implements ActionListener {

    private MenuItem[] menuItems = { new HeldenMenuItem() };

    private HeldenMIDlet parent;

    private boolean dragMode;

    private Hashtable menuHash;

    public MenuItem currentMenuItem;

    private int cols;

    public MainMenuForm(HeldenMIDlet midlet) throws IOException {
        super("Hauptmen�");
        this.parent = midlet;
        Resources r = this.parent.getResources("menu");
        int width = Display.getInstance().getDisplayWidth();
        int elementWidth = 10;
        final ButtonActionListener bAListner = new ButtonActionListener();
        for (int i = 0; i < menuItems.length; i++) {
            String name = menuItems[i].getName();
            Image selectedImage = r.getImage(name + "_sel.png");
            Image unselectedImage = r.getImage(name + "_unsel.png");
            final Button b = new Button(name, unselectedImage);
            b.setUIID("MainMenuButton");
            b.setRolloverIcon(selectedImage);
            b.setAlignment(Label.CENTER);
            b.setTextPosition(Label.BOTTOM);
            addComponent(b);
            b.addActionListener(bAListner);
            b.addFocusListener(new FocusListener() {

                public void focusGained(Component cmp) {
                    if (HeldenMIDlet.componentTransitions != null) {
                        replace(b, b, HeldenMIDlet.componentTransitions);
                    }
                }

                public void focusLost(Component cmp) {
                }
            });
            menuHash.put(b, menuItems[i]);
            elementWidth = Math.max(b.getPreferredW(), elementWidth);
        }
        cols = width / elementWidth;
        int rows = menuItems.length / cols;
        setLayout(new GridLayout(rows, cols));
        addCommand(HeldenMIDlet.exitCommand);
        addCommand(HeldenMIDlet.aboutCommand);
        addCommand(HeldenMIDlet.dragModeCommand);
        addCommand(HeldenMIDlet.runCommand);
        setCommandListener(this);
    }

    public void setDragMode(boolean dragMode) {
        this.dragMode = dragMode;
    }

    private class ButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            currentMenuItem = ((MenuItem) (menuHash.get(evt.getSource())));
            currentMenuItem.run(HeldenMIDlet.backCommand, MainMenuForm.this.parent);
        }
    }

    /**
	 * Invoked when a command is clicked. We could derive from Command but that
	 * would require 3 separate classes.
	 */
    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();
        switch(cmd.getId()) {
            case HeldenMIDlet.RUN_COMMAND:
                currentMenuItem = ((MenuItem) (menuHash.get(getFocused())));
                currentMenuItem.run(HeldenMIDlet.backCommand, this.parent);
                break;
            case HeldenMIDlet.EXIT_COMMAND:
                parent.notifyDestroyed();
                break;
            case HeldenMIDlet.BACK_COMMAND:
                currentMenuItem.cleanup();
                show();
                System.gc();
                System.gc();
                break;
            case HeldenMIDlet.ABOUT_COMMAND:
                Form aboutForm = new Form("About");
                aboutForm.setScrollable(false);
                aboutForm.setLayout(new BorderLayout());
                TextArea aboutText = new TextArea(getAboutText(), 5, 10);
                aboutText.setEditable(false);
                aboutForm.addComponent(BorderLayout.CENTER, aboutText);
                aboutForm.addCommand(new Command("Back") {

                    public void actionPerformed(ActionEvent evt) {
                        MainMenuForm.this.show();
                    }
                });
                aboutForm.show();
                break;
            case HeldenMIDlet.DRAG_MODE_COMMAND:
                setDragMode(true);
                removeCommand(HeldenMIDlet.dragModeCommand);
                addCommand(HeldenMIDlet.scrollModeCommand);
                break;
            case HeldenMIDlet.SCROLL_MODE_COMMAND:
                setDragMode(false);
                removeCommand(HeldenMIDlet.scrollModeCommand);
                addCommand(HeldenMIDlet.dragModeCommand);
                break;
        }
    }

    private String getAboutText() {
        return "LWUIT Demo shows the main features for developing a User " + "Interface (UI) in Java ME. " + "This demo contains inside additional different sub-demos " + "to demonstrate key features, where each one can be reached " + "from the main screen. For more details about each sub-demo, " + "please visit the demo help screen. For more details about LWUIT" + " feel free to contact us at lwuit@sun.com.";
    }
}
