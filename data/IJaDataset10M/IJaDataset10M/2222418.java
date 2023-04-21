package net.mjrz.fm.ui.utils;

import java.util.Enumeration;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import org.apache.log4j.Logger;
import net.mjrz.fm.entity.FManEntityManager;
import net.mjrz.fm.entity.beans.Budget;
import net.mjrz.fm.services.SessionManager;
import net.mjrz.fm.utils.MiscUtils;

public class BudgetsTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    private FManEntityManager entityManager = null;

    private Logger logger = Logger.getLogger(BudgetsTreeModel.class);

    public BudgetsTreeModel(TreeNode root) {
        super(root);
        entityManager = new FManEntityManager();
        initialize();
    }

    private void initialize() {
        try {
            logger.info("Initializing budgets tree...");
            List<Budget> budgets = entityManager.getBudgets(SessionManager.getSessionUserId());
            if (budgets == null) return;
            logger.info("Num budgets = " + budgets.size());
            for (Budget b : budgets) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode(b);
                ((DefaultMutableTreeNode) root).add(n);
            }
        } catch (Exception e) {
            logger.error(MiscUtils.stackTrace2String(e));
        }
    }
}
