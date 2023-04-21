package jsynoptic.installer;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import jsynoptic.installer.resources.InstallerResources;

class LicensePanel extends InstallerPanel {

    protected JEditorPane editorPane;

    public LicensePanel(Installer installer) {
        super(installer);
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setAutoscrolls(true);
        setLayout(new GridLayout());
        add(new JScrollPane(editorPane));
    }

    public void update() {
        try {
            URL url = InstallerResources.class.getResource(Installer.resources.getString("licenseFile"));
            editorPane.setPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        installer.next.setText(Installer.resources.getString("accept"));
        installer.cancel.setText(Installer.resources.getString("cancel"));
        setBorder(BorderFactory.createTitledBorder(Installer.resources.getString("pleaseReadLicense")));
    }
}
