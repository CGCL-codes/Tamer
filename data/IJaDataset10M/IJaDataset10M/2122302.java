package gtkwire.widget;

import gtkwire.*;
import gtkwire.widget.utils.*;
import java.io.File;

/**
*A button to launch a file selection dialog.
*/
public class GtkFileChooserButton extends GtkHBox implements GtkFileChooser {

    private String title;

    private GtkFileChooserAction action;

    public GtkFileChooserButton(String title, GtkFileChooserAction action) {
        super();
        this.widgetType = WT_GtkFileChooserButton;
        this.title = title;
        this.action = action;
        gtkCreate();
    }

    public GtkFileChooserButton(String name, GladeKey key) {
        super(name, key);
        this.widgetType = WT_GtkFileChooserButton;
    }

    protected String[] getCreateData() {
        if (action == null) return null;
        String[] msg = { title, action.getMode() };
        return msg;
    }

    /**
	*Sets whether the button will grab focus when it is clicked with the mouse.
	*/
    public void setFocusOnClick(boolean b) {
        app().sendCommand(name, GTKWireCommand.SET_FOCUS_ON_CLICK, b);
    }

    /**
	* Sets title of opened dialog.
	*/
    public void setDialogTitle(String title) {
        app().sendCommand(name, GTKWireCommand.SET_TITLE, title);
    }

    public void setCurrentFolder(String path) {
        FileChooserMethods.setCurrentFolder(this, path);
    }

    public File getFile() {
        return FileChooserMethods.getFile(this);
    }

    public void setFile(File f) {
        FileChooserMethods.setFile(this, f);
    }
}
