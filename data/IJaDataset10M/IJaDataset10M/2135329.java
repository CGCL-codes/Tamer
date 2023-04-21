package org.kazao.calendar.sample;

import java.util.Calendar;
import org.kazao.calendar.KazaoCalendar;
import org.kazao.calendar.KazaoCalendarAdapter;

/**
 *
 * @author    Mr. Kazao
 * Email    : m.jumari@gmail.com
 * Website  : http://mr.kazao.net
 * Phone    : +622743251763 +6281904091661
 */
public class SingleCalendarWithListenerCustomFormat extends javax.swing.JFrame {

    /** Creates new form SingleCalendar */
    public SingleCalendarWithListenerCustomFormat() {
        initComponents();
        kazaoCalendar1.addChangeListener(new KazaoCalendarAdapter() {

            public void onChange(Calendar calendar) {
            }

            public void onChange(KazaoCalendar kazaoCalendar) {
                jLabel1.setText(kazaoCalendar.getDate("mm-dd-yyyy"));
            }

            public void onDoubleClick() {
            }
        });
    }

    private void initComponents() {
        kazaoCalendar1 = new org.kazao.calendar.KazaoCalendar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Single Calendar With Listener Custom Format");
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Date"));
        jLabel1.setText("jLabel1");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(kazaoCalendar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(kazaoCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 400) / 2, (screenSize.height - 380) / 2, 400, 380);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SingleCalendarWithListenerCustomFormat().setVisible(true);
            }
        });
    }

    public javax.swing.JLabel jLabel1;

    public javax.swing.JPanel jPanel1;

    public org.kazao.calendar.KazaoCalendar kazaoCalendar1;
}
