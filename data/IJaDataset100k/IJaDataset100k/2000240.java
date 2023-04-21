package webwatcher;

import webwatcher.WebPage;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import no.shhsoft.awt.*;

/**
 * @author   Sverre H. Huseby
 *           &lt;<A HREF="mailto:shh@thathost.com">shh@thathost.com</A>&gt;
 * @version  $Id: WebPageEditor.java 16 2010-08-22 22:14:44Z miraculix0815 $
 */
public class WebPageEditor implements ActionListener, Observer {

    private static String defaultURL = "http://";

    private WebPage webPage;

    private Label labelURL = new Label("URL");

    private TextField textURL = new TextField(60);

    private Label labelTitle = new Label("Title");

    private TextField textTitle = new TextField(60);

    private Label labelDescription = new Label("Description");

    private TextArea textDescription = new TextArea(10, 60);

    private Label labelStatus = new Label("Status");

    private TextField textStatus = new TextField(40);

    private Button buttMarkRead;

    private boolean okPressed;

    private void addField(Panel panel, GridBagLayout gbl, GridBagConstraints gbc, Component field, int gwidth, int gx, int wx, int wy) {
        gbc.gridwidth = gwidth;
        gbc.gridx = gx;
        gbc.weightx = wx;
        gbc.weighty = wy;
        if (wx > 0 && wy > 0) gbc.fill = GridBagConstraints.BOTH; else if (wx > 0) gbc.fill = GridBagConstraints.HORIZONTAL; else if (wy > 0) gbc.fill = GridBagConstraints.VERTICAL; else gbc.fill = GridBagConstraints.NONE;
        gbl.setConstraints(field, gbc);
        panel.add(field);
    }

    private Panel setupPanel() {
        Panel panel;
        GridBagLayout gbl;
        GridBagConstraints gbc;
        panel = new Panel();
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        panel.setLayout(gbl);
        gbc.insets = new Insets(3, 10, 3, 10);
        gbc.anchor = GridBagConstraints.WEST;
        if (webPage.getURL() != null) textURL.setText(webPage.getURL().toString()); else textURL.setText(defaultURL);
        addField(panel, gbl, gbc, labelURL, 1, 0, 0, 0);
        addField(panel, gbl, gbc, textURL, GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 0);
        if (webPage.getTitle() != null) textTitle.setText(webPage.getTitle()); else textTitle.setText("");
        addField(panel, gbl, gbc, labelTitle, 1, 0, 0, 0);
        addField(panel, gbl, gbc, textTitle, GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 0);
        if (webPage.getDescription() != null) textDescription.setText(webPage.getDescription()); else textDescription.setText("");
        addField(panel, gbl, gbc, labelDescription, 1, 0, 0, 0);
        addField(panel, gbl, gbc, textDescription, GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 1);
        textStatus.setText(webPage.getStatusText());
        textStatus.setEditable(false);
        addField(panel, gbl, gbc, labelStatus, 1, 0, 0, 0);
        addField(panel, gbl, gbc, textStatus, GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, 1, 0);
        buttMarkRead = new Button("Mark As Read");
        buttMarkRead.addActionListener(this);
        buttMarkRead.setEnabled(webPage.getStatus() == WebPage.State.PAGE_UPDATED);
        addField(panel, gbl, gbc, buttMarkRead, GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 0, 0);
        return panel;
    }

    private boolean doOk(Frame parent) {
        String urlAsString;
        URL url;
        urlAsString = textURL.getText();
        try {
            url = new URL(urlAsString);
        } catch (MalformedURLException e) {
            MessageBox.error(parent, "Malformed URL: `" + urlAsString + "'");
            return false;
        }
        webPage.deleteObserver(this);
        webPage.setTitle(textTitle.getText());
        webPage.setURL(url);
        webPage.setDescription(textDescription.getText());
        okPressed = true;
        return true;
    }

    private void doCancel(Frame parent) {
        webPage.deleteObserver(this);
        okPressed = false;
    }

    private void doMarkRead() {
        webPage.markAsSeen();
    }

    public WebPageEditor(Frame parent, WebPage webPage) {
        ModalButtonDialog dlg;
        int code;
        boolean finished;
        Panel panel;
        this.webPage = webPage;
        panel = setupPanel();
        webPage.addObserver(this);
        finished = false;
        while (!finished) {
            dlg = new ModalButtonDialog(parent, "Preferences", panel, ModalButtonDialog.OK | ModalButtonDialog.CANCEL, ModalButtonDialog.CANCEL);
            code = dlg.showAndGetButtonCode();
            if (code == ModalButtonDialog.OK) finished = doOk(parent); else {
                doCancel(parent);
                finished = true;
            }
        }
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttMarkRead) doMarkRead();
    }

    public void update(Observable o, Object arg) {
        textStatus.setText(webPage.getStatusText());
        buttMarkRead.setEnabled(webPage.getStatus() == WebPage.State.PAGE_UPDATED);
    }
}
