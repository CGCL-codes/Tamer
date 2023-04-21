package net.sourceforge.mords.client.util.tree;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author  david
 */
public class TestFileTreeView extends javax.swing.JFrame {

    private TreeFileView tfv;

    /** Creates new form TestFileTreeView */
    public TestFileTreeView() {
        initComponents();
    }

    private void setRoot(File f) {
        tfv = new TreeFileView(f);
        getContentPane().add(tfv, BorderLayout.CENTER);
    }

    private File selectRootFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(jfc.DIRECTORIES_ONLY);
        int i = jfc.showOpenDialog(this);
        if (i == jfc.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        } else {
            return null;
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tdcs/lords/client/util/tree/Documents32x32.png")));
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, java.awt.BorderLayout.PAGE_END);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        TestFileTreeView tftv = new TestFileTreeView();
        tftv.setVisible(true);
        File f = tftv.selectRootFile();
        if (f != null) {
            tftv.setRoot(f);
        }
    }

    private javax.swing.JLabel jLabel1;
}
