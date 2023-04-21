package GUIS;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ProgressMonitor;

/**
 *
 * @author  bok
 */
public class ProgressBar extends javax.swing.JFrame implements Runnable {

    /** Creates new form ProgressBar */
    Thread hilo;

    public ProgressBar() {
        pm = new ProgressMonitor((this), "Cargando", "Avance", 0, 100);
        initComponents();
    }

    void startHilo() {
        hilo = new Thread(this);
        hilo.start();
    }

    private void initComponents() {
        jProgressBar1 = new javax.swing.JProgressBar();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    /**
     * @param args the command line arguments
     */
    private ProgressMonitor pm;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ProgressBar().setVisible(true);
            }
        });
    }

    private javax.swing.JProgressBar jProgressBar1;

    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                pm.setProgress(i);
                Thread.sleep(90);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.dispose();
    }
}
