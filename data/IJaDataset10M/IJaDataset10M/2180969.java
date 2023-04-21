package GUI.GUIChairMaintenance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Logic.LogicChairMaintenance.Chair.Chair;
import Repository.Entities.IContactPerson;
import Repository.RepositoryAuthors.AppealRep;
import Repository.RepositoryChairMaintenance.IPaperAssignment;

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
public class AppealsAssignClass extends javax.swing.JFrame {

    private JButton doneButton;

    private JButton assignedButton;

    private JLabel assignedReviewsLabel;

    private JLabel assigendReviewersLabel;

    private JLabel ReviewersListLabel;

    private JScrollPane jScrollPane2;

    private JList jList3;

    private JScrollPane jScrollPane3;

    private JList jList2;

    private JList jList1;

    private JScrollPane jScrollPane1;

    LinkedList<IContactPerson> reviewersAvailable;

    LinkedList<IPaperAssignment> assignedPapersList;

    LinkedList<String> reviewersAssigned;

    int paper_id;

    String sessionId;

    Chair chair;

    AppealRep apprep;

    /**
	 * @param sessionId
	 * @param chair
	 * @param paper_Id
	 */
    public AppealsAssignClass(String sessionId, Chair chair, int paper_Id) {
        super();
        this.apprep = new AppealRep();
        this.assignedPapersList = new LinkedList<IPaperAssignment>();
        this.reviewersAssigned = new LinkedList<String>();
        this.paper_id = paper_Id;
        this.sessionId = sessionId;
        this.chair = chair;
        this.reviewersAvailable = apprep.getReviewers(this.paper_id);
        this.apprep.removeReviews(this.paper_id);
        initGUI();
    }

    String[] getRevNames(LinkedList<IContactPerson> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i).getFirstName() + " " + list.get(i).getLastName();
        return result;
    }

    /**
	 * Initialize the GUI
	 */
    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(null);
            {
                ReviewersListLabel = new JLabel();
                getContentPane().add(ReviewersListLabel);
                ReviewersListLabel.setText("Reviewers");
                ReviewersListLabel.setBounds(12, 7, 154, 19);
                ReviewersListLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
            {
                assigendReviewersLabel = new JLabel();
                getContentPane().add(assigendReviewersLabel);
                assigendReviewersLabel.setText("Assigned Reviewers");
                assigendReviewersLabel.setBounds(286, 9, 130, 17);
            }
            {
                assignedReviewsLabel = new JLabel();
                getContentPane().add(assignedReviewsLabel);
                assignedReviewsLabel.setText("Assigned Reviews");
                assignedReviewsLabel.setBounds(25, 198, 149, 14);
                assignedReviewsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
            {
                assignedButton = new JButton();
                getContentPane().add(assignedButton);
                assignedButton.setText("Assign");
                assignedButton.setBounds(358, 224, 81, 40);
                assignedButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("assignedButton.actionPerformed, event=" + evt);
                        assignPaper();
                    }
                });
            }
            {
                doneButton = new JButton();
                getContentPane().add(doneButton);
                doneButton.setText("Done");
                doneButton.setBounds(358, 304, 80, 46);
                doneButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("doneButton.actionPerformed, event=" + evt);
                        doneButtonPushed();
                    }
                });
            }
            {
                jScrollPane1 = new JScrollPane();
                getContentPane().add(jScrollPane1);
                jScrollPane1.setBounds(36, 38, 170, 133);
                {
                    ListModel jList1Model = new DefaultComboBoxModel(getRevNames(this.reviewersAvailable));
                    jList1 = new JList();
                    jScrollPane1.setViewportView(jList1);
                    jList1.setModel(jList1Model);
                    jList1.addListSelectionListener(new ListSelectionListener() {

                        public void valueChanged(ListSelectionEvent evt) {
                            System.out.println("jList1.valueChanged, event=" + evt);
                            System.out.println(jList1.getSelectedIndex());
                            revSelected();
                        }
                    });
                }
            }
            {
                jScrollPane2 = new JScrollPane();
                getContentPane().add(jScrollPane2);
                jScrollPane2.setBounds(24, 224, 182, 113);
                {
                    jList2 = new JList();
                    jScrollPane2.setViewportView(jList2);
                }
            }
            {
                jScrollPane3 = new JScrollPane();
                getContentPane().add(jScrollPane3);
                jScrollPane3.setBounds(264, 38, 164, 133);
                {
                    jList3 = new JList();
                    jScrollPane3.setViewportView(jList3);
                }
            }
            this.setSize(498, 407);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doneButtonPushed() {
        this.setVisible(false);
    }

    /**
	 * assigns a paper with the appeal accepted to a reviewer that did not made the previous reviews
	 */
    protected void assignPaper() {
        chair.assignPaper(sessionId, this.paper_id, this.apprep.getReviewId(this.reviewersAvailable.get(jList1.getSelectedIndex())));
        IContactPerson contact = this.reviewersAvailable.get(jList1.getSelectedIndex());
        String rep = contact.getFirstName() + " " + contact.getLastName();
        this.reviewersAssigned.add(rep);
        jList3.setListData(this.reviewersAssigned.toArray());
    }

    /**
	 * shows the reviews that are assigned to review 
	 */
    private void revSelected() {
        int review_id = this.apprep.getReviewId(this.reviewersAvailable.get(jList1.getSelectedIndex()));
        Iterator<IPaperAssignment> itAssign = this.chair.getAssignmentsByReviewer(this.sessionId, review_id);
        this.assignedPapersList.clear();
        while (itAssign.hasNext()) assignedPapersList.add(itAssign.next());
        jList2.setListData(getPapers(this.assignedPapersList));
    }

    /**
	 * 
	 * @param list
	 * @return the array of the list in the param
	 */
    private String[] getPapers(LinkedList<IPaperAssignment> list) {
        String[] result = new String[list.size()];
        int i = 0;
        for (IPaperAssignment ip : list) {
            result[i] = ip.getPaper().getAbstract().getPaperTitle();
        }
        return result;
    }
}
