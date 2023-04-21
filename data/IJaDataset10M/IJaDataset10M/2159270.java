package tool_panels.spectrum_histo;

import fr.esrf.Tango.DevFailed;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import javax.swing.*;
import java.awt.*;

public class AttributeBrowser extends JDialog {

    private Component parent;

    public AttributeBrowser(JFrame parent) {
        super(parent, true);
        this.parent = parent;
        buildAttributeBrowser();
    }

    public AttributeBrowser(JDialog parent) {
        super(parent, true);
        this.parent = parent;
        buildAttributeBrowser();
    }

    public void buildAttributeBrowser() {
        initComponents();
        try {
            AttributeBrowserTree tree = new AttributeBrowserTree(this);
            treeScrollPane.setViewportView(tree);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
        pack();
        ATKGraphicsUtils.centerDialog(this);
        fileMenu.setMnemonic('F');
        exitItem.setMnemonic('E');
        exitItem.setAccelerator(KeyStroke.getKeyStroke('Q', Event.CTRL_MASK));
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        textScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        treeScrollPane = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());
        textScrollPane.setPreferredSize(new java.awt.Dimension(400, 400));
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Courier New", 1, 12));
        textScrollPane.setViewportView(textArea);
        jPanel1.add(textScrollPane, java.awt.BorderLayout.CENTER);
        treeScrollPane.setPreferredSize(new java.awt.Dimension(300, 400));
        jPanel1.add(treeScrollPane, java.awt.BorderLayout.WEST);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        fileMenu.setText("File");
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        pack();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {
        doClose();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    private void exitForm(java.awt.event.WindowEvent evt) {
        doClose();
    }

    private void doClose() {
        if (parent.getWidth() > 0) {
            setVisible(false);
            dispose();
        } else System.exit(0);
    }

    void setText(String str) {
        textArea.setText(str);
        textArea.setCaretPosition(0);
    }

    public static void main(String args[]) {
        try {
            AttributeBrowser db = new AttributeBrowser(new JFrame());
            db.setVisible(true);
        } catch (Exception e) {
            ErrorPane.showErrorMessage(new JFrame(), null, e);
        }
    }

    private javax.swing.JMenuItem exitItem;

    private javax.swing.JMenu fileMenu;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JTextArea textArea;

    private javax.swing.JScrollPane textScrollPane;

    private javax.swing.JScrollPane treeScrollPane;
}
