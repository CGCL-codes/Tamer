package com.tdcs.lords.client.dialog;

import com.tdcs.docs.common.DocServer;
import com.tdcs.docs.common.Document;
import com.tdcs.lords.client.Manager;
import com.tdcs.lords.client.display.TextNoteInternalFrame;
import com.tdcs.lords.client.util.OsOps;
import com.tdcs.lords.obj.Patient;
import com.tdcs.lords.store.data.PatientServer;
import com.tdcs.text.StringNumbers;
import com.tdcs.util.OsActions;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.ZippedDocumentTemplate;

/**
 *
 * @author CCII
 */
public class PopUpMenu extends JPopupMenu {

    private PatientServer pserver;

    private DocServer dserver;

    private Patient patient;

    private Manager mgr;

    private HashMap<JMenuItem, String> map;

    private Map<String, String> docs;

    private String base;

    private JMenu templateMenu;

    /** Creates a new instance of PopUpMenu */
    public PopUpMenu() {
        super();
        build();
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        buildTemplateMenu();
    }

    public void setManager(Manager mgr) {
        this.mgr = mgr;
        this.pserver = mgr.getPatientServer();
        this.dserver = mgr.getDocServer();
    }

    public void setBase(String base) {
        this.base = base;
    }

    private void buildTemplateMenu() {
        try {
            templateMenu.removeAll();
            map = new HashMap<JMenuItem, String>();
            docs = dserver.listTemplates(patient);
            Set<String> templates = docs.keySet();
            Iterator<String> it = templates.iterator();
            while (it.hasNext()) {
                String s = it.next();
                JMenuItem item = new JMenuItem(s);
                item.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        templateMenuItemActionPerformed(evt);
                    }
                });
                templateMenu.add(item);
            }
        } catch (Exception e) {
            mgr.showException(e);
        }
    }

    private void build() {
        JMenuItem noteMenuItem = new JMenuItem();
        JMenuItem fileMenuItem = new JMenuItem();
        JSeparator sep = new JSeparator();
        noteMenuItem.setText("Add Note...");
        noteMenuItem.setToolTipText("Create a text note in the patient's record");
        noteMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteMenuItemActionPerformed(evt);
            }
        });
        add(noteMenuItem);
        fileMenuItem.setText("Add File...");
        fileMenuItem.setToolTipText("Add an external file to this patient's record.");
        fileMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuItemActionPerformed(evt);
            }
        });
        add(fileMenuItem);
        add(sep);
        templateMenu = new JMenu();
        templateMenu.setText("From Template...");
        add(templateMenu);
    }

    private void noteMenuItemActionPerformed(ActionEvent evt) {
        mgr.addTextNote();
    }

    private void fileMenuItemActionPerformed(ActionEvent evt) {
        mgr.addFileDocument();
    }

    private void templateMenuItemActionPerformed(ActionEvent evt) {
        JMenuItem item = (JMenuItem) evt.getSource();
        System.out.println("Event from template menu item " + item.getText());
        try {
            String id = item.getText();
            String category = docs.get(id);
            System.out.println("Popup Menu:  Getting " + id + " in category " + category);
            Document doc = dserver.getTemplate(category, id);
            byte[] data = doc.getData();
            OsOps osops = new OsOps(base);
            String outfile = StringNumbers.fileFormat(new Date()) + "-" + id;
            System.out.println("Using the following ID for file output:  " + outfile);
            String output = osops.makePath(patient, category, outfile);
            System.out.println("Path of output file:  " + output);
            runDoc(data, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runDoc(byte[] data, String output) throws FileNotFoundException, Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Map<String, String> model = patient.getMap();
        DocumentTemplate template = new ZippedDocumentTemplate(bais);
        template.createDocument(model, new FileOutputStream(output));
        OsActions.execute(new File(output));
    }

    public static void main(String[] args) {
        Patient patient = new Patient();
        patient.setFirstName("Johnny");
        patient.setLastName("Smith");
        patient.setSsn("123-45-6789");
        patient.setDob("11/11/1911");
        patient.setAddress("13973 Lyck Run Lyra");
        patient.setCity("South Webster");
        patient.setState("OH");
        patient.setZip("45682");
        patient.setFacility("Unique Pain Management");
        patient.setCurrentDiagnosis("Strong schizophrenia with constant, repeating paranoid illusions.");
        patient.setCurrentRx("Dope 'em up and lock him away.");
        PopUpMenu pum = new PopUpMenu();
        try {
            pum.setPatient(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pum.setBase(".");
        JFileChooser jfc = new JFileChooser();
        int val = jfc.showOpenDialog(pum);
        if (val == jfc.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i = 0;
                while ((i = fis.read()) != -1) {
                    baos.write(i);
                }
                fis.close();
                String output = "changed-" + f.getName();
                output = f.getParent() + File.separator + output;
                pum.runDoc(baos.toByteArray(), output);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
