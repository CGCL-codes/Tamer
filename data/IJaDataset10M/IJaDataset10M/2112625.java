package proyecto2;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Principal extends javax.swing.JFrame {

    private Principal splashFrame = this;

    public Principal() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        AWTUtilities.setWindowOpacity(splashFrame, new Float(0.8));
        startThread();
    }

    private void startThread() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                FrameInicio aplicationFrame = new FrameInicio(splashFrame);
                aplicationFrame.setLocationRelativeTo(null);
                aplicationFrame.setVisible(true);
                dispose();
            }
        });
        thread.start();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getLabel2() {
        return jLabel1;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        label.setFont(new java.awt.Font("Estrangelo Edessa", 1, 24));
        label.setForeground(new java.awt.Color(0, 0, 255));
        label.setText("Cargando información");
        progressBar.setStringPainted(true);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyecto2/cartera.png")));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE).addComponent(jLabel2).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addGap(46, 46, 46)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE).addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)).addGap(16, 16, 16)))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGap(27, 27, 27).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(label)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        pack();
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel label;

    private javax.swing.JProgressBar progressBar;
}
