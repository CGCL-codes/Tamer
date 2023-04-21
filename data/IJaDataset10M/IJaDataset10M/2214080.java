package Maquettes_fadwa_julien;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ModifierUe extends javax.swing.JFrame {

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel jPanel1;

    private JButton jButton1;

    private JLabel jLabel5;

    private JPanel jPanel2;

    private JTextField jTextField3;

    private JTextField jTextField2;

    private JLabel jLabel4;

    private JScrollPane jScrollPane1;

    private JTextArea jTextArea1;

    private JLabel jLabel3;

    private JButton jButton2;

    private JTextField jTextField1;

    private JLabel jLabel2;

    /**
	* Auto-generated main method to display this JFrame
	*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ModifierUe inst = new ModifierUe();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public ModifierUe() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setTitle("Modifier Unit� d'enseignement");
            {
                jPanel1 = new JPanel();
                getContentPane().add(jPanel1, BorderLayout.CENTER);
                GroupLayout jPanel1Layout = new GroupLayout((JComponent) jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1.setPreferredSize(new java.awt.Dimension(532, 164));
                {
                    jPanel2 = new JPanel();
                    GroupLayout jPanel2Layout = new GroupLayout((JComponent) jPanel2);
                    jPanel2.setLayout(jPanel2Layout);
                    {
                        jTextField1 = new JTextField();
                        jTextField1.setText("Developpement");
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        {
                            jTextArea1 = new JTextArea();
                            jScrollPane1.setViewportView(jTextArea1);
                            jTextArea1.setText("dehgv fytfy ");
                        }
                    }
                    {
                        jLabel3 = new JLabel();
                        jLabel3.setText("Description");
                    }
                    {
                        jLabel2 = new JLabel();
                        jLabel2.setText("Nom de l'UE");
                    }
                    {
                        jTextField2 = new JTextField();
                        jTextField2.setText("9");
                    }
                    {
                        jTextField3 = new JTextField();
                        jTextField3.setText("10");
                    }
                    {
                        jLabel4 = new JLabel();
                        jLabel4.setText("Nombre d'ECTS");
                    }
                    {
                        jLabel5 = new JLabel();
                        jLabel5.setText("Note minimum");
                    }
                    {
                        jButton2 = new JButton();
                        jButton2.setText("Annuler");
                    }
                    {
                        jButton1 = new JButton();
                        jButton1.setText("Appliquer");
                    }
                    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addGap(6)).addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE).addGap(33)).addComponent(jLabel5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)).addGap(48).addGroup(jPanel2Layout.createParallelGroup().addComponent(jTextField3, GroupLayout.Alignment.LEADING, 0, 89, Short.MAX_VALUE).addComponent(jTextField2, GroupLayout.Alignment.LEADING, 0, 89, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE))).addGap(52).addGroup(jPanel2Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE).addGap(32))));
                    jPanel2Layout.setVerticalGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup().addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).addGroup(jPanel2Layout.createSequentialGroup().addGap(11).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(jLabel3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanel2Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(jLabel5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(jPanel2Layout.createSequentialGroup().addGap(17).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jButton2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))));
                }
                jPanel1Layout.setHorizontalGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, 0, 522, Short.MAX_VALUE));
                jPanel1Layout.setVerticalGroup(jPanel1Layout.createSequentialGroup().addContainerGap(37, 37).addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
            }
            pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
