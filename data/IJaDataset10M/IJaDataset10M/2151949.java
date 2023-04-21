package DCL;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  root
 */
public class DCLXMessage extends javax.swing.JFrame {

    protected String title, message;

    /** Creates new form DCLExperimentList */
    public DCLXMessage(String t, String m) {
        title = t;
        message = m;
        initComponents();
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point Oxy = genv.getCenterPoint();
        Rectangle pos = new Rectangle(Oxy.x - getWidth() / 2, Oxy.y - getHeight() / 2, getWidth(), getHeight());
        jTextArea.append(message);
        setTitle(title);
        setBounds(pos);
        setVisible(true);
    }

    private void initComponents() {
        jScrollPane = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Experiment List");
        setResizable(false);
        jTextArea.setColumns(20);
        jTextArea.setEditable(false);
        jTextArea.setRows(5);
        jScrollPane.setViewportView(jTextArea);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE));
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DCLXMessage("Titolo", "Messaggio");
            }
        });
    }

    private javax.swing.JScrollPane jScrollPane;

    private javax.swing.JTextArea jTextArea;
}
