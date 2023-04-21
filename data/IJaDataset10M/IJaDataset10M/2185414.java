package com.jigen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.xmlbeans.XmlException;
import wintertools.swingworker.utilis.ActionTracerDialog;
import wintertools.swingworker.utilis.WorkTracer;
import com.jigen.XmlReader;
import com.jigen.xsd.JigenDocument;

@SuppressWarnings("serial")
public class Main extends JFrame {

    private static final Dimension DEFAULT_SIZE = new Dimension(800, 600);

    private static final ImageIcon COMPILE_ICON = new ImageIcon(Main.class.getResource("/com/jigen/gui/images/create-installer.png"));

    private final File wixHome = new File("C:\\ARCHIV~1\\WixEdit\\wix-3.0.2211.0");

    private final JButton createInstallerButton = new JButton("Create installer...", COMPILE_ICON);

    private final VisualEditorPanel visualEditorPanel = new VisualEditorPanel();

    private final XmlEditorPanel xmlEditorPanel = new XmlEditorPanel(this);

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final ActionTracerDialog actionTracerDialog = new ActionTracerDialog(this, "String title", "String progressMessageText", "String doneMessageText", "String errorMessageText");

    private File jigenFile;

    public Main() {
        setTitle("Jigen");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_SIZE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - DEFAULT_SIZE.width) / 2, (screenSize.height - DEFAULT_SIZE.height) / 2);
        tabbedPane.addTab("XML editor", xmlEditorPanel);
        tabbedPane.addTab("Visual editor", visualEditorPanel);
        tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JigenDocument document;
                try {
                    if (tabbedPane.getSelectedComponent() == visualEditorPanel) document = xmlEditorPanel.getJigenDocument(); else if (tabbedPane.getSelectedComponent() == xmlEditorPanel) document = visualEditorPanel.getJigenDocument(); else throw new AssertionError();
                } catch (InvalidJigenException ex) {
                    tabbedPane.removeChangeListener(this);
                    if (tabbedPane.getSelectedComponent() == visualEditorPanel) tabbedPane.setSelectedComponent(xmlEditorPanel); else if (tabbedPane.getSelectedComponent() == xmlEditorPanel) tabbedPane.setSelectedComponent(visualEditorPanel); else throw new AssertionError();
                    tabbedPane.addChangeListener(this);
                    new ErrorDetailsDialog(ex).openDialog();
                    return;
                }
                if (tabbedPane.getSelectedComponent() == visualEditorPanel) visualEditorPanel.setJigenDocument(document); else if (tabbedPane.getSelectedComponent() == xmlEditorPanel) xmlEditorPanel.setJigenDocument(document); else throw new AssertionError();
            }
        });
        createInstallerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final JigenDocument document;
                try {
                    if (tabbedPane.getSelectedComponent() == xmlEditorPanel) document = xmlEditorPanel.getJigenDocument(); else if (tabbedPane.getSelectedComponent() == visualEditorPanel) document = visualEditorPanel.getJigenDocument(); else throw new AssertionError();
                } catch (InvalidJigenException ex) {
                    throw new RuntimeException(ex);
                }
                WorkTracer workTracer = new XmlReader(wixHome, jigenFile).importJigen(document).createWorkTracer();
                workTracer.addWorkTracerListener(actionTracerDialog);
                workTracer.execute();
                actionTracerDialog.setVisible(true);
            }
        });
        JPanel lowerPanel = new JPanel(new FlowLayout());
        lowerPanel.add(createInstallerButton);
        add(lowerPanel, BorderLayout.SOUTH);
        add(tabbedPane);
        setJigenDocument(null);
        setVisible(true);
    }

    public void setJigenDocument(File jigenFile) {
        if (jigenFile != null) setTitle("Jigen - " + jigenFile.getName()); else setTitle("Jigen - [untitled]");
        this.jigenFile = jigenFile;
    }

    public void loadJigenDocument(File jigenFile) {
        if (jigenFile != null) setTitle("Jigen - " + jigenFile.getName()); else setTitle("Jigen - [untitled]");
        this.jigenFile = jigenFile;
        if (tabbedPane.getSelectedComponent() == visualEditorPanel) visualEditorPanel.openFile(jigenFile); else if (tabbedPane.getSelectedComponent() == xmlEditorPanel) xmlEditorPanel.openFile(jigenFile); else throw new AssertionError();
    }

    public File getJigenDocument() {
        return jigenFile;
    }

    public void setValidDocument(boolean isValid) {
        createInstallerButton.setEnabled(isValid);
    }

    public static void main(String args[]) throws XmlException, IOException, InterruptedException {
        Main main = new Main();
        if (args.length == 1) main.loadJigenDocument(new File(args[0]));
    }
}
