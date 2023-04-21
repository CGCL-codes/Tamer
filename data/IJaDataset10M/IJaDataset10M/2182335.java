package GUI.GUIAuthors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingUtilities;
import Repository.Entities.IContactPerson;
import Repository.RepositoryAuthors.AvoidRep;

/**
 * This class has the responsible for letting the Author choose what 
 * reviewers he wants for his paper
 */
public class AddRevAvoids extends javax.swing.JFrame {

    /**
	 * JscrollPane used in revList
	 */
    private JScrollPane jScrollPane1;

    /**
	 * JList with all the reviwers
	 */
    private JList revList;

    /**
	 * JscrollPane used in jlist2
	 */
    private JScrollPane jScrollPane2;

    /**
	 * Jlist of the avoid reviewers
	 */
    private JList avoidList;

    /**
	 * JButton with the responsability of adding reviewers names 
	 * in the avoidList
	 */
    private JButton insertButton;

    /**
	 * Jbutton used to remove reviewers from the avoidList
	 */
    private JButton removeButton;

    /**
	 * Jbutton used to submit all the information presents in the revList, 
	 * avoidList and justificationText
	 */
    private JButton doneButton;

    /**
	 * JTextArea - Authors avoid reviewer justification 
	 */
    private JTextArea justificationText;

    /**
	 * JScrollPane used in the justificationText Area
	 */
    private JScrollPane jScrollPane3;

    /**
	 * LinkedList<IContactPerson> that has all the reviewers 
	 */
    private LinkedList<IContactPerson> revLinkedList;

    /**
	 * LinkedList<IContactPerson> that has all the avoid reviewers 
	 */
    private LinkedList<IContactPerson> revAvoidLinkedList;

    /**
	 * An AvoidRep object 
	 */
    private AvoidRep avrep;

    /**
	 * String containing the session id number 
	 */
    private String session_id;

    /** Constructor of this class
     * @param session_id
     * @since 0.4
     */
    public AddRevAvoids(String session_id) {
        super();
        avrep = new AvoidRep();
        this.session_id = session_id;
        revLinkedList = avrep.getReviewers();
        revAvoidLinkedList = new LinkedList<IContactPerson>();
        initGUI();
    }

    /** Function called in class constructor that Initialize the Gui
     * @since 0.4
     */
    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(null);
            {
                jScrollPane1 = new JScrollPane();
                getContentPane().add(jScrollPane1);
                jScrollPane1.setBounds(12, 12, 121, 125);
                {
                    ListModel jList1Model = new DefaultComboBoxModel(avrep.getArrayRev(revLinkedList));
                    revList = new JList();
                    jScrollPane1.setViewportView(revList);
                    revList.setModel(jList1Model);
                    revList.setPreferredSize(new java.awt.Dimension(95, 102));
                    revList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                }
            }
            {
                jScrollPane2 = new JScrollPane();
                getContentPane().add(jScrollPane2);
                jScrollPane2.setBounds(145, 12, 114, 125);
                {
                    ListModel jList2Model = new DefaultComboBoxModel(avrep.getArrayRev(revAvoidLinkedList));
                    avoidList = new JList();
                    jScrollPane2.setViewportView(avoidList);
                    avoidList.setModel(jList2Model);
                    avoidList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                }
            }
            {
                insertButton = new JButton();
                getContentPane().add(insertButton);
                insertButton.setText("Insert");
                insertButton.setBounds(40, 143, 67, 27);
                insertButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("jButton1.actionPerformed, event=" + evt);
                        insRemLists();
                    }
                });
            }
            {
                removeButton = new JButton();
                getContentPane().add(removeButton);
                removeButton.setText("Remove");
                removeButton.setBounds(161, 143, 85, 27);
                removeButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("jButton2.actionPerformed, event=" + evt);
                        remAvoidList();
                    }
                });
            }
            {
                doneButton = new JButton();
                getContentPane().add(doneButton);
                doneButton.setText("Done");
                doneButton.setBounds(291, 137, 65, 61);
                doneButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("jButton3.actionPerformed, event=" + evt);
                        doneButtonPushed();
                    }
                });
            }
            {
                jScrollPane3 = new JScrollPane();
                getContentPane().add(jScrollPane3);
                jScrollPane3.setBounds(12, 175, 247, 141);
                jScrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                {
                    justificationText = new JTextArea();
                    jScrollPane3.setViewportView(justificationText);
                    justificationText.setLineWrap(true);
                }
            }
            pack();
            this.setSize(400, 369);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** This Function is used when doneButton is pushed
     * @since 0.4
     */
    private void doneButtonPushed() {
        if (justificationText.getText().equals("")) JOptionPane.showMessageDialog(this, "Missing Justification!", "ERROR", JOptionPane.ERROR_MESSAGE); else if (revAvoidLinkedList.size() == 0) JOptionPane.showMessageDialog(this, "Missing Reviewers to avoid!", "ERROR", JOptionPane.ERROR_MESSAGE); else {
            this.avrep.updAvoid(this.revAvoidLinkedList, justificationText.getText(), this.session_id);
            this.setVisible(false);
        }
    }

    /** This Function is used when the insert button is pushed
     * @since 0.4
     */
    public void insRemLists() {
        revAvoidLinkedList.add(revLinkedList.remove(revList.getSelectedIndex()));
        revList.setListData(avrep.getArrayRev(this.revLinkedList));
        avoidList.setListData(avrep.getArrayRev(this.revAvoidLinkedList));
    }

    /** This Function is used when the remove button is pushed
     * @since 0.4
     */
    public void remAvoidList() {
        revLinkedList.add(revAvoidLinkedList.remove(avoidList.getSelectedIndex()));
        revList.setListData(avrep.getArrayRev(this.revLinkedList));
        avoidList.setListData(avrep.getArrayRev(this.revAvoidLinkedList));
    }
}
