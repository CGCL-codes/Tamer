package jbacula;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.FileWriter;

/**
 * @author philippe.martin@univ-pau.fr
 *
 */
public class Bacula {

    Display display;

    Shell shell;

    BaculaMenu baculaMenu;

    BaculaTree baculaTree;

    ConfigDescription descriptionConfig;

    Hashtable resources;

    ResourceType displayedResource;

    ResourceType selectedResource;

    DocumentBuilderFactory factory;

    DocumentBuilder builder;

    String openedFile;

    Bacula() {
        openedFile = null;
    }

    void run() {
        factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
        }
        display = new Display();
        shell = new Shell(display);
        baculaMenu = new BaculaMenu(this);
        descriptionConfig = new ConfigDescription(this);
        newConfig();
        shell.open();
        while (!shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
        display.dispose();
    }

    void save() {
        if (openedFile == null) return;
        try {
            FileWriter fw = null;
            fw = new FileWriter(openedFile);
            Enumeration e1 = resources.elements();
            while (e1.hasMoreElements()) {
                ResourceType r = (ResourceType) e1.nextElement();
                r.save(fw);
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void saveAs() {
        String[] filterExtensions = { "*.conf", "*" };
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
        fileDialog.setText("Save as...");
        fileDialog.setFilterExtensions(filterExtensions);
        String selectedFile = fileDialog.open();
        openedFile = selectedFile;
        baculaMenu.fileSave.setEnabled(true);
        baculaMenu.fileSaveAs.setEnabled(false);
        save();
    }

    void newConfig() {
        if (baculaTree == null) {
            baculaTree = new BaculaTree(this);
            baculaTree.addListener();
        } else baculaTree.renew();
        Vector resourceNames = descriptionConfig.resourcesList();
        if (resources == null) {
            resources = new Hashtable();
            for (int i = 0; i < resourceNames.size(); i++) {
                ResourceType r = new ResourceType(this, (String) resourceNames.get(i));
                resources.put(r.name, r);
            }
            shell.pack();
            for (int i = 0; i < resourceNames.size(); i++) {
                ResourceType r = (ResourceType) resources.get(resourceNames.get(i));
                r.scrolledPage.pack();
                r.scrolledPage.setBounds(300, 10, 490, 530);
                r.scrolledPage.setVisible(false);
            }
            shell.setSize(800, 600);
        } else {
            for (int i = 0; i < resourceNames.size(); i++) {
                ResourceType r = (ResourceType) resources.get(resourceNames.get(i));
                r.renew();
            }
        }
        displayedResource = null;
    }
}
