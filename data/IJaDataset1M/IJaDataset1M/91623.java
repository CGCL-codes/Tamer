package com.hack23.cia.web.views;

import java.util.List;
import thinwire.ui.Panel;
import thinwire.ui.Tree;
import thinwire.ui.Tree.Item;
import thinwire.ui.layout.TableLayout;
import com.hack23.cia.model.application.ActionType;
import com.hack23.cia.model.sweden.Ballot;
import com.hack23.cia.model.sweden.CommitteeReport;
import com.hack23.cia.web.action.BallotAction;
import com.hack23.cia.web.action.CommitteeReportAction;
import com.hack23.cia.web.common.ApplicationMessageHolder;
import com.hack23.cia.web.common.BeanLocator;
import com.hack23.cia.web.common.ApplicationMessageHolder.MessageConstans;

/**
 * The Class CommitteeReportPanel.
 */
public class CommitteeReportPanel extends Panel {

    /**
     * Instantiates a new committee report panel.
     * 
     * @param commiteeReports the commitee reports
     */
    public CommitteeReportPanel(final List<CommitteeReport> commiteeReports) {
        setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 1, 5));
        Tree globalPortalTree = createRootTree(commiteeReports);
        globalPortalTree.setLimit("0,0,1,1");
        getChildren().add(globalPortalTree);
    }

    /**
     * Creates the root tree.
     * 
     * @param commiteeReports the commitee reports
     * 
     * @return the tree
     */
    private Tree createRootTree(final List<CommitteeReport> commiteeReports) {
        Tree tree = new Tree();
        tree.addActionListener(ACTION_CLICK, BeanLocator.getApplicationActionListener());
        tree.addActionListener(ACTION_DOUBLE_CLICK, BeanLocator.getApplicationActionListener());
        tree.setRootItemVisible(true);
        Tree.Item root = tree.getRootItem();
        root.setText(ApplicationMessageHolder.getMessage(MessageConstans.LAST_TEN_DECIDED_COMMITEE_REPORTS));
        root.setExpanded(true);
        if (commiteeReports != null) {
            for (CommitteeReport commiteeReport : commiteeReports) {
                createTree(root, commiteeReport);
            }
        }
        return tree;
    }

    /**
     * Creates the tree.
     * 
     * @param parent the parent
     * @param commiteeReport the commitee report
     */
    private void createTree(final Item parent, final CommitteeReport commiteeReport) {
        if (commiteeReport != null) {
            Tree.Item commiteeReportItem = new Tree.Item(commiteeReport.getName());
            commiteeReportItem.setUserObject(new CommitteeReportAction(ActionType.ShowCommiteeReportAction, commiteeReport));
            parent.getChildren().add(commiteeReportItem);
            if (commiteeReport.getDecisionDate() != null) {
                Tree.Item datumItem = new Tree.Item(ApplicationMessageHolder.getMessage(MessageConstans.DECISION_DATE) + " " + commiteeReport.getDecisionDate().toString());
                commiteeReportItem.getChildren().add(datumItem);
                if (commiteeReport.getBallots().size() > 0) {
                    Tree.Item ballotItem = new Tree.Item(ApplicationMessageHolder.getMessage(MessageConstans.BALLOTS));
                    commiteeReportItem.getChildren().add(ballotItem);
                    for (Ballot ballot : commiteeReport.getBallots()) {
                        createTree(ballotItem, ballot);
                    }
                }
            }
        }
    }

    /**
     * Creates the tree.
     * 
     * @param parent the parent
     * @param ballot the ballot
     */
    private void createTree(final Item parent, final Ballot ballot) {
        if (ballot != null) {
            Tree.Item ballotItem = new Tree.Item(ballot.getDescription());
            ballotItem.setUserObject(new BallotAction(ActionType.ShowBallotAction, ballot.getId()));
            parent.getChildren().add(ballotItem);
        }
    }
}
