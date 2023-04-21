package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import bean.Agents;
import dao.AgentsDao2;
import dao.changeAgentsDao;

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
public class ChangeAgent extends JDialog {

    private JPanel jPanel1;

    private JComboBox cmbChangeAgent;

    private JButton btnCancel;

    private JScrollPane jScrollPane1;

    private JPanel jPanel4;

    private JButton btnConfirmIndividual;

    private JButton btnConfirmAll;

    private JLabel jLabel3;

    private JPanel jPanel3;

    private JPanel jPanel2;

    private JLabel jLabel2;

    private JLabel jLabel1;

    String[] sqlUpdateString;

    public static Vector<Integer> changeCustID = new Vector<Integer>();

    public static Vector<Integer> changeAgtID = new Vector<Integer>();

    public static int sizeOfArrays;

    /**
	 * Auto-generated main method to display this JFrame
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ChangeAgent inst = new ChangeAgent();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public ChangeAgent(int i) {
        super();
        initGUI(i);
    }

    /*****************************************************************
	 *** CLOSE THIS
	 *** 
	 *** code for closing the dialog
	 *****************************************************************/
    public void closeThis() {
        this.setVisible(false);
    }

    public ChangeAgent() {
    }

    private void initGUI(final int agtID) {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setPreferredSize(new java.awt.Dimension(298, 510));
            {
                jPanel1 = new JPanel();
                getContentPane().add(jPanel1, BorderLayout.CENTER);
                jPanel1.setLayout(null);
                jPanel1.setPreferredSize(new java.awt.Dimension(246, 476));
                {
                    jPanel2 = new JPanel();
                    jPanel1.add(jPanel2);
                    jPanel2.setBounds(6, 6, 276, 85);
                    jPanel2.setLayout(null);
                    jPanel2.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                    {
                        DefaultComboBoxModel cmbChangeAgentModel = new DefaultComboBoxModel(new String[] {});
                        cmbChangeAgent = new JComboBox();
                        jPanel2.add(cmbChangeAgent);
                        cmbChangeAgent.setModel(cmbChangeAgentModel);
                        cmbChangeAgent.setBounds(85, 27, 178, 26);
                        changeAgentsDao.PopulateChangeAgentsCombo(cmbChangeAgentModel, agtID);
                        cmbChangeAgent.setSelectedIndex(-1);
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel2.add(jLabel2);
                        jLabel2.setText("Agent");
                        jLabel2.setBounds(6, 32, 67, 16);
                    }
                    {
                        jLabel1 = new JLabel();
                        jPanel2.add(jLabel1);
                        jLabel1.setText("Select new agent for all Customers");
                        jLabel1.setBounds(7, 5, 262, 16);
                    }
                    {
                        btnConfirmAll = new JButton();
                        jPanel2.add(btnConfirmAll);
                        btnConfirmAll.setText("Confirm");
                        btnConfirmAll.setBounds(121, 53, 107, 28);
                        btnConfirmAll.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent arg0) {
                                if (cmbChangeAgent.getSelectedIndex() == -1) {
                                    JOptionPane.showMessageDialog(ChangeAgent.this, "Please ensure you have selected a new agent for all of the customers");
                                } else {
                                    Agents newAgent = new Agents();
                                    newAgent = (Agents) cmbChangeAgent.getSelectedItem();
                                    newAgent.getAGENTID();
                                    changeAgentsDao.customerAgentChangeAll(agtID, newAgent.getAGENTID());
                                    AgentsDao2.StatusChange(agtID, "In-Active");
                                    closeThis();
                                }
                            }
                        });
                    }
                }
                {
                    jLabel3 = new JLabel();
                    jPanel1.add(jLabel3);
                    jLabel3.setText("Or select individual Agent/Customers");
                    jLabel3.setBounds(6, 97, 270, 19);
                }
                {
                    jPanel3 = new JPanel();
                    jPanel1.add(jPanel3);
                    jPanel3.setLayout(null);
                    jPanel3.setBounds(6, 116, 276, 354);
                    jPanel3.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                    {
                        btnConfirmIndividual = new JButton();
                        jPanel3.add(btnConfirmIndividual);
                        btnConfirmIndividual.setText("Confirm");
                        btnConfirmIndividual.setBounds(26, 319, 107, 28);
                        btnConfirmIndividual.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                if (ChangeAgent.changeAgtID.size() < sizeOfArrays) {
                                    JOptionPane.showMessageDialog(ChangeAgent.this, "Please ensure you have selected a new agent for all of the customers");
                                } else {
                                    for (int i = 0; i < changeAgtID.size(); i++) {
                                        changeAgentsDao.customerAgentChangeSingle(agtID, changeAgtID.get(i), changeCustID.get(i));
                                        AgentsDao2.StatusChange(agtID, "In-Active");
                                    }
                                    closeThis();
                                }
                            }
                        });
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel3.add(jScrollPane1);
                        jScrollPane1.setBounds(7, 7, 262, 308);
                        {
                            jPanel4 = new JPanel();
                            jScrollPane1.setViewportView(jPanel4);
                            jPanel4.setBounds(7, 7, 220, 306);
                            jPanel4.setLayout(null);
                            changeAgentsDao.populateChangeAgents(agtID, jPanel4);
                        }
                    }
                    {
                        btnCancel = new JButton();
                        jPanel3.add(btnCancel);
                        btnCancel.setText("Cancel");
                        btnCancel.setBounds(144, 319, 107, 28);
                        btnCancel.addActionListener(new ActionListener() {

                            /*****************************************************************
					 *** CANCEL BUTTON
					 *** 
					 *** Code for cancelling the deactivation of agent
					 *****************************************************************/
                            public void actionPerformed(ActionEvent e) {
                                closeThis();
                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(298, 510);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
