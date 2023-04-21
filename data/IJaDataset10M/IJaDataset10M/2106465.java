package com.endlessloopsoftware.ego.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.ejb.FinderException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import com.cim.dlgedit.loader.DialogResource;
import com.cim.util.swing.DlgUtils;
import com.endlessloopsoftware.ego.Question;
import com.endlessloopsoftware.ego.Shared;
import com.endlessloopsoftware.ego.Study;
import com.endlessloopsoftware.ego.client.statistics.Statistics;
import com.endlessloopsoftware.egonet.interfaces.InterviewSBRemote;
import com.endlessloopsoftware.egonet.interfaces.InterviewSBRemoteHome;
import com.endlessloopsoftware.egonet.interfaces.InterviewSBUtil;
import com.endlessloopsoftware.egonet.interfaces.StudySBRemote;
import com.endlessloopsoftware.egonet.interfaces.StudySBRemoteHome;
import com.endlessloopsoftware.egonet.interfaces.StudySBUtil;
import com.endlessloopsoftware.egonet.util.InterviewDataValue;
import com.endlessloopsoftware.egonet.util.InterviewIdentifier;
import com.endlessloopsoftware.egonet.util.StudyAndInterviewTransfer;
import com.endlessloopsoftware.egonet.util.StudyDataValue;
import com.endlessloopsoftware.elsutils.SwingWorker;

public class ServerInterviewChooser extends JPanel implements ActionListener, TreeSelectionListener {

    private final int DECORATION = 0;

    private final int STUDY = 1;

    private final int INTERVIEW = 2;

    private JButton loadInterviews;

    private JButton select;

    private JTextField serverURL;

    private JTree interviewTree;

    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

    private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

    StudySBRemote studySession;

    InterviewSBRemote interviewSession;

    private EgoClient egoClient;

    public ServerInterviewChooser(EgoClient egoClient) {
        this.egoClient = egoClient;
        java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("com/endlessloopsoftware/ego/client/ServerInterviewChooser.gui_xml");
        JPanel panel = DialogResource.load(is);
        loadInterviews = (JButton) DialogResource.getComponentByName(panel, "LoadInterviews");
        select = (JButton) DialogResource.getComponentByName(panel, "Select");
        serverURL = (JTextField) DialogResource.getComponentByName(panel, "serverURL");
        interviewTree = (JTree) DialogResource.getComponentByName(panel, "interviewTree");
        interviewTree.addTreeSelectionListener(this);
        interviewTree.setModel(treeModel);
        rootNode.setUserObject(new TreeSelection("No Server Selected", DECORATION));
        interviewTree.setEditable(false);
        interviewTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        interviewTree.setShowsRootHandles(false);
        treeModel.reload();
        select.setEnabled(false);
        loadInterviews.setText("Load Studies");
        loadInterviews.addActionListener(this);
        select.addActionListener(this);
        this.setLayout(new GridLayout(1, 1));
        this.add(panel);
    }

    /**
	 * Invoke the onXxx() action handlers.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        DlgUtils.invokeHandler(this, e);
    }

    public void onLoadInterviews() {
        fillTree();
    }

    public void onSelect() {
        Properties prop = new Properties();
        prop.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        prop.setProperty("java.naming.provider.url", serverURL.getText() + ":1099");
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) interviewTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        final String epassword = "215-121-242-47-99-238-5-61-133-183-0-216-187-250-253-30-115-177-254-142-161-83-108-56";
        final TreeSelection selection = (TreeSelection) node.getUserObject();
        int type = selection.getType();
        switch(type) {
            case STUDY:
                {
                    try {
                        final StatRecord[] statRecords = new StatRecord[node.getChildCount()];
                        Shared.setWaitCursor(egoClient.getFrame(), true);
                        StudyDataValue data = studySession.fetchDataByStudyName(selection.toString(), epassword);
                        final Study study = new Study(data);
                        final ProgressMonitor progressMonitor = new ProgressMonitor(egoClient.getFrame(), "Calculating Statistics", "", 0, node.getChildCount());
                        final SwingWorker worker = new SwingWorker() {

                            public Object construct() {
                                for (int i = 0; i < node.getChildCount(); ++i) {
                                    DefaultMutableTreeNode interviewNode = (DefaultMutableTreeNode) node.getChildAt(i);
                                    TreeSelection interviewSelection = (TreeSelection) interviewNode.getUserObject();
                                    System.out.println("Loading " + interviewSelection.getIdentifier());
                                    InterviewIdentifier id = (InterviewIdentifier) interviewSelection.getIdentifier();
                                    InterviewDataValue interviewData;
                                    try {
                                        interviewData = interviewSession.fetchUserInterviewData(selection.toString(), id.getFirstName(), id.getLastName(), epassword);
                                        System.out.println("Creating Interview " + interviewSelection.getIdentifier());
                                        Interview interview = new Interview(study, interviewData);
                                        System.out.println("Generating Statistics " + interviewSelection.getIdentifier());
                                        Question q = study.getFirstStatableQuestion();
                                        Statistics statistics = interview.generateStatistics(q);
                                        System.out.println("Most Central Degree Alter " + statistics.mostCentralBetweenAlterName);
                                        System.out.println("To StatRecord " + interviewSelection.getIdentifier());
                                        statRecords[i] = new StatRecord(statistics);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressMonitor.setProgress(i);
                                }
                                return statRecords;
                            }

                            public void finished() {
                                Shared.setWaitCursor(egoClient.getFrame(), false);
                                progressMonitor.close();
                                egoClient.getFrame().gotoSummaryPanel(statRecords);
                            }
                        };
                        progressMonitor.setProgress(0);
                        progressMonitor.setMillisToDecideToPopup(0);
                        progressMonitor.setMillisToPopup(0);
                        worker.start();
                    } catch (FinderException e) {
                        e.printStackTrace();
                    } catch (java.lang.reflect.UndeclaredThrowableException e) {
                        e.printStackTrace();
                        System.out.println(e.getUndeclaredThrowable());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Unable to load study.\n" + e.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } finally {
                        Shared.setWaitCursor(egoClient.getFrame(), false);
                    }
                }
                break;
            case INTERVIEW:
                {
                    DefaultMutableTreeNode studyNode = (DefaultMutableTreeNode) node.getParent();
                    TreeSelection studySelect = (TreeSelection) studyNode.getUserObject();
                    try {
                        Shared.setWaitCursor(egoClient.getFrame(), true);
                        StudyDataValue studyData = studySession.fetchDataByStudyName(studySelect.toString(), epassword);
                        InterviewIdentifier id = (InterviewIdentifier) selection.getIdentifier();
                        InterviewDataValue interviewData = interviewSession.fetchUserInterviewData(studySelect.toString(), id.getFirstName(), id.getLastName(), epassword);
                        egoClient.setUiPath(ClientFrame.VIEW_INTERVIEW);
                        egoClient.getStorage().setInterviewFile(null);
                        egoClient.setInterview(null);
                        Study study = new Study(studyData);
                        egoClient.setStudy(study);
                        egoClient.setInterview(new Interview(study, interviewData));
                        if (egoClient.getInterview() != null) {
                            egoClient.getFrame().gotoViewInterviewPanel();
                        }
                    } catch (FinderException e) {
                        JOptionPane.showMessageDialog(this, "Unable to load interview.", "Server Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Unable to load interview.\n" + e.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } finally {
                        Shared.setWaitCursor(egoClient.getFrame(), false);
                    }
                }
            default:
                break;
        }
    }

    /**
	 * @author admin
	 *
	 * To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Generation - Code and Comments
	 */
    public void fillTree() {
        try {
            Shared.setWaitCursor(egoClient.getFrame(), true);
            Properties prop = new Properties();
            prop.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            prop.setProperty("java.naming.provider.url", serverURL.getText() + ":1099");
            StudySBRemoteHome studySBHome = StudySBUtil.getHome(prop);
            studySession = studySBHome.create();
            InterviewSBRemoteHome interviewSBHome = InterviewSBUtil.getHome(prop);
            interviewSession = interviewSBHome.create();
            Set studyNames = studySession.getStudyAndInterviewNames();
            rootNode.removeAllChildren();
            for (Iterator it = studyNames.iterator(); it.hasNext(); ) {
                StudyAndInterviewTransfer xfer = (StudyAndInterviewTransfer) it.next();
                DefaultMutableTreeNode studyNode = new DefaultMutableTreeNode(new TreeSelection(xfer.studyName, STUDY));
                for (Iterator ints = xfer.interviewIdentifiers.iterator(); ints.hasNext(); ) {
                    InterviewIdentifier id = (InterviewIdentifier) ints.next();
                    studyNode.add(new DefaultMutableTreeNode(new TreeSelection(id, INTERVIEW)));
                }
                rootNode.add(studyNode);
            }
            if (studyNames.size() > 0) rootNode.setUserObject(new TreeSelection("Studies", DECORATION)); else rootNode.setUserObject(new TreeSelection("No Studies Found", DECORATION));
            treeModel.reload();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Shared.setWaitCursor(egoClient.getFrame(), false);
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) interviewTree.getLastSelectedPathComponent();
        if (node == null) {
            select.setEnabled(false);
            return;
        }
        TreeSelection selection = (TreeSelection) node.getUserObject();
        if (select != null) {
            if (selection.getType() == STUDY) {
                select.setText("View Study Summary");
                select.setEnabled(true);
            } else if (selection.getType() == INTERVIEW) {
                select.setText("View Interview");
                select.setEnabled(true);
            } else {
                select.setEnabled(false);
            }
        } else {
            select.setEnabled(false);
        }
        select.setEnabled((selection != null) && ((selection.getType() == STUDY) || (selection.getType() == INTERVIEW)));
    }

    private class TreeSelection {

        private Object _id;

        private int _type;

        public TreeSelection(Object id, int type) {
            _id = id;
            _type = type;
        }

        public String toString() {
            return _id.toString();
        }

        public int getType() {
            return _type;
        }

        public Object getIdentifier() {
            return _id;
        }
    }
}
