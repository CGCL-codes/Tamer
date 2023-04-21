package ProPesca.ui;

import ProPesca.main.*;
import java.io.File;
import java.net.URL;

/**
 *
 * @author  house
 */
public class SelectExec extends javax.swing.JFrame {

    /** Creates new form SelectExec */
    public SelectExec(String orig_exec, ProPescaUI orig_main, ConfUI orig_conf) {
        initComponents();
        setTitle("Select " + orig_exec);
        jFileChooser1.addChoosableFileFilter(new FilterFile(orig_exec));
        main_ui = orig_main;
        conf_ui = orig_conf;
        exec = orig_exec;
        URL img = getClass().getResource("/ProPesca/resources/images/propesca.png");
        this.setIconImage(new javax.swing.ImageIcon(img).getImage());
    }

    private void initComponents() {
        jFileChooser1 = new javax.swing.JFileChooser();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        jFileChooser1.setAcceptAllFileFilterUsed(false);
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE));
        pack();
    }

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getActionCommand().equals(new String("ApproveSelection"))) {
            File selected = jFileChooser1.getSelectedFile();
            main_ui.conf.set_dot(selected.getAbsolutePath());
            if (main_ui.conf.dot_valid()) conf_ui.setDotField(selected.getAbsolutePath());
        }
        dispose();
    }

    private javax.swing.JFileChooser jFileChooser1;

    private ProPescaUI main_ui;

    private ConfUI conf_ui;

    private String exec;
}
