package de.yaams.launcher.gui;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import de.yaams.launcher.another.I18N;

/**
 * @author abt
 * 
 */
public class YMessagesDialog {

    public static final int INFO = 0, WARN = 0, ERROR = 0;

    protected ArrayList<String> messages;

    protected int level;

    protected String title, header, footer, id, mid;

    protected Icon icon;

    protected String[] yesno = { I18N.t("Ja"), I18N.t("Nein") };

    protected boolean defaultSelect;

    /**
	 * Create a new YErrorDialog Create a new YErrorDialog
	 * 
	 * @param title
	 * @param id
	 */
    public YMessagesDialog(String title, String id) {
        messages = new ArrayList<String>();
        level = INFO;
        this.title = title;
        header = "";
        footer = "";
        mid = "show.errordialog." + id;
    }

    /**
	 * Add message
	 * 
	 * @param message
	 * @param level
	 */
    public YMessagesDialog add(String message, int level) {
        messages.add(message);
        if (level != 0 && level > this.level) {
            this.level = level;
            mid = "show.errordialog." + id + "." + level;
        }
        return this;
    }

    /**
	 * Show messages as quest
	 * 
	 * @return
	 */
    public String formatDialog() {
        HashMap<String, Object> objects = new HashMap<String, Object>();
        objects.put("message", this);
        objects.put("id", id);
        if (messages != null && messages.size() > 0) {
            final StringBuilder s = new StringBuilder("<html>");
            s.append(header);
            if (messages.size() > 1) {
                s.append("<ul>");
                for (final String e : messages) {
                    s.append("<li>");
                    s.append(e);
                    s.append("</li>");
                }
                s.append("</ul>");
            } else {
                s.append(messages.get(0));
                s.append("<br>");
            }
            s.append("<br>");
            s.append(footer);
            s.append("</html>");
            return s.toString();
        } else {
            return null;
        }
    }

    /**
	 * Helpermethod to get the right icon
	 * 
	 * @return
	 */
    protected int getIcon() {
        if (level == WARN) {
            return JOptionPane.WARNING_MESSAGE;
        } else if (level == ERROR) {
            return JOptionPane.ERROR_MESSAGE;
        }
        return JOptionPane.INFORMATION_MESSAGE;
    }

    /**
	 * Only inform the user
	 */
    public boolean showOk() {
        String cont = formatDialog();
        if (cont != null) {
            JOptionPane.showMessageDialog(null, title, cont, getIcon());
        }
        return true;
    }

    /**
	 * 
	 * @return true, has errors, false otherwise
	 */
    public boolean hasErrors() {
        return messages != null && messages.size() > 0;
    }

    /**
	 * Show the errors, who found
	 * 
	 * @param errors
	 * @return true -> yes, run it, false -> no, close it
	 */
    public boolean showQuestion() {
        String cont = formatDialog();
        if (cont != null) {
            return JOptionPane.showConfirmDialog(null, title, cont, JOptionPane.YES_NO_OPTION, getIcon()) == JOptionPane.YES_OPTION;
        }
        return true;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title
	 *            the title to set
	 */
    public YMessagesDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
	 * @return the header
	 */
    public String getHeader() {
        return header;
    }

    /**
	 * @param header
	 *            the header to set
	 */
    public YMessagesDialog setHeader(String header) {
        this.header = header;
        return this;
    }

    /**
	 * @return the footer
	 */
    public String getFooter() {
        return footer;
    }

    /**
	 * @param footer
	 *            the footer to set
	 */
    public YMessagesDialog setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    /**
	 * @param icon
	 *            the icon to set
	 */
    public YMessagesDialog setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
	 * @return the level
	 */
    public int getLevel() {
        return level;
    }

    /**
	 * 0=Yes, 1=No
	 * 
	 * @return the yesno
	 */
    public String[] getYesno() {
        return yesno;
    }

    /**
	 * @return the defaultSelect
	 */
    public boolean isDefaultSelect() {
        return defaultSelect;
    }

    /**
	 * @param defaultSelect
	 *            the defaultSelect to set
	 */
    public void setDefaultSelect(boolean defaultSelect) {
        this.defaultSelect = defaultSelect;
    }
}
