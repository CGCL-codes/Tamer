package com.ganian.jDownloadr.gui;

import com.ganian.jDownloadr.utils.*;
import com.ganian.jDownloadr.core.*;
import java.util.Properties;
import org.apache.commons.httpclient.HttpURL;

/**
 *
 * @author  debuti
 */
public class Gui extends javax.swing.JFrame {

    private final String DEFAULT_OPTIONS_PATH = "Tu vieja";

    private String setUrl = null;

    private language myLanguage = null;

    private options myOptions = null;

    private Core myCore = null;

    /** Creates new form Gui */
    public Gui(Properties options, Properties language) {
        initComponents();
        myOptions = new options(DEFAULT_OPTIONS_PATH);
        jProgressBar1.setMinimum(0);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jLabel1.setText("jDownloadr");
        jLabel2.setText("Paste the photoset link you want to download down here.");
        jLabel3.setText(" ");
        jButton1.setText("Go for it!");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton1KeyReleased(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(166, Short.MAX_VALUE).addComponent(jButton1).addGap(166, 166, 166)).addGroup(layout.createSequentialGroup().addGap(165, 165, 165).addComponent(jLabel1).addContainerGap(165, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(26, 26, 26).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel3).addGap(88, 88, 88)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {
        int numberOfPhotos = 0;
        String photoName = null;
        setUrl = jTextField1.getText();
        if (setUrl == null) {
            jLabel3.setText(myLanguage.getGuiIncorrecturl());
        } else {
            myCore = new Core(myOptions);
            myCore.start();
            try {
                numberOfPhotos = myCore.getSet(setUrl);
                jProgressBar1.setMaximum(numberOfPhotos);
                while ((photoName = myCore.getNextPhoto(myOptions.getOutputPath())) != null) {
                    jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                    jLabel3.setText(myLanguage.getGuiDownloading() + photoName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void jButton1KeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.equals(java.awt.event.KeyEvent.VK_ENTER)) {
            jButton1MouseReleased(null);
        }
    }

    private void formKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.equals(java.awt.event.KeyEvent.VK_ENTER)) {
            jButton1MouseReleased(null);
        }
    }

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.equals(java.awt.event.KeyEvent.VK_ENTER)) {
            jButton1MouseReleased(null);
        }
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JProgressBar jProgressBar1;

    private javax.swing.JTextField jTextField1;
}
