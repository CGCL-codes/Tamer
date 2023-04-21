package dialogs;

import java.io.*;
import java.util.Scanner;

/**
 * Dialog Interface to view the file Help.html.
 *
 * @author Adam Koski
 */
public class DialogHelp extends javax.swing.JDialog {

    /**
     * Constructor to make a Help Dialog.
     *
     * @param parent Parent Component.
     * @param modal Always set true?
     */
    public DialogHelp(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jEditorPane1.setText(readHelp(new File("Help.html")));
    }

    private String readHelp(File openFile) {
        String text = "";
        if (openFile.exists()) {
            try {
                Scanner in = new Scanner(openFile);
                while (in.hasNext()) {
                    text += in.nextLine();
                }
            } catch (IOException ex) {
                System.out.println("Error: Bad File?");
                return "";
            }
            return text;
        }
        System.out.println("Doesn't Exist...\n" + openFile.getPath());
        return "";
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);
        jScrollPane1.setViewportView(jEditorPane1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private javax.swing.JEditorPane jEditorPane1;

    private javax.swing.JScrollPane jScrollPane1;
}
