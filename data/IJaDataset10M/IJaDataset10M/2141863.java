package org.schnelln.help;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.schnelln.config.Constants;
import org.schnelln.config.Messages;
import org.schnelln.config.gui.Colors;
import org.schnelln.config.gui.SizesNotResizable;

public class About {

    private JDialog dialog = null;

    private JFrame parent = null;

    private JScrollPane scrollpane = null;

    private JEditorPane statusText = null;

    public About(JFrame parent) {
        this.parent = parent;
        createDialog();
    }

    private void createDialog() {
        if (dialog == null) {
            dialog = new JDialog(parent, Messages.getMessage("About.header"));
            dialog.setBackground(Colors.STATUS_BUTTONPANEL_COLOR);
            dialog.setResizable(false);
            dialog.requestFocus();
            dialog.setLocation(SizesNotResizable.MAIN_DIALOG.getLocation());
            dialog.setVisible(true);
            statusText = new JEditorPane();
            statusText.setEditable(false);
            statusText.setContentType("text/html");
            statusText.getEditorKit().createDefaultDocument();
            statusText.setBackground(Colors.STATUS_BUTTONPANEL_COLOR);
            statusText.setText(getAbout());
            scrollpane = new JScrollPane(statusText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            dialog.add(scrollpane);
        }
        dialog.pack();
        dialog.repaint();
        dialog.setVisible(true);
    }

    private static final String getAbout() {
        String msg = "<center>" + "<h1>" + Constants.NAME + "</h1>" + "<center>" + "<img src=\"" + About.class.getResource("logo.png").toString() + "\">" + "<br>" + "</img>" + "</center>" + "<br>" + "v" + Constants.VERSION + "<br><br>" + "written by " + Constants.AUTHOR + "<br>(" + Constants.EMAIL + ")<br>" + Constants.URL + "<br>" + Constants.YEAR + "</center><br><br><br>";
        return msg + Messages.getMessage("License.text");
    }
}
