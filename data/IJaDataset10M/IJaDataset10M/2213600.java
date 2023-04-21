package net.sourceforge.thinfeeder.widget;

import java.io.IOException;
import thinlet.Thinlet;

/**
 * @author fabianofranz@users.sourceforge.net
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Alert extends Widget {

    private String text = null;

    private String title = null;

    private String icon = null;

    private Object dialog = null;

    public Alert(Thinlet thinlet, String text, String title, String icon) {
        this.thinlet = thinlet;
        this.title = title;
        this.icon = icon;
        this.text = text;
    }

    public Alert(Thinlet thinlet, String text, String title) {
        this(thinlet, text, title, null);
    }

    public Alert(Thinlet thinlet, String text) {
        this(thinlet, text, null, null);
    }

    public void show() throws IOException {
        if (text == null) text = "";
        dialog = thinlet.parse("/net/sourceforge/thinfeeder/widget/alert.xml", this);
        if (title != null) thinlet.setString(dialog, "text", title);
        if (icon != null) thinlet.setIcon(dialog, "icon", thinlet.getIcon(icon));
        thinlet.add(dialog);
        Object label = thinlet.find(dialog, "text");
        thinlet.setString(label, "text", text);
    }

    public void close() {
        closeDialog(dialog);
    }

    /**
	 * @return Returns the dialog.
	 */
    public Object getDialog() {
        return dialog;
    }

    /**
	 * @param dialog The dialog to set.
	 */
    public void setDialog(Object dialog) {
        this.dialog = dialog;
    }

    /**
	 * @return Returns the icon.
	 */
    public String getIcon() {
        return icon;
    }

    /**
	 * @param icon The icon to set.
	 */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
	 * @return Returns the text.
	 */
    public String getText() {
        return text;
    }

    /**
	 * @param text The text to set.
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * @return Returns the title.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title The title to set.
	 */
    public void setTitle(String title) {
        this.title = title;
    }
}
