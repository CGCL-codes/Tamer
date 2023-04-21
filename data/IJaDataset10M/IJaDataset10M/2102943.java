package net.sourceforge.vigilog.ui.loggertree;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import net.sourceforge.vigilog.model.LogEntry;
import net.sourceforge.vigilog.ui.LoggerTreeNode;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This class shows a tree structure of the loggers in the log file.
 */
public class LoggerTree extends JXTree {

    private static Logger log = Logger.getLogger(LoggerTree.class);

    private final EventList<String> excludedLoggers = new BasicEventList<String>();

    private final EventList<String> includedLoggers = new BasicEventList<String>();

    public LoggerTree() {
        setCellRenderer(new LoggerTreeCellRenderer());
    }

    /**
	 * Exclude the given logger string from showing in the {@link net.sourceforge.vigilog.ui.LogEntriesTable}
	 *
	 * @param logger the logger to exclude
	 */
    public void excludeLogger(String logger) {
        LoggerTreeNode node = findNode(getModel(), logger);
        excludeLogger(node, logger);
    }

    /**
	 * Exclude the given logger string from showing in the {@link net.sourceforge.vigilog.ui.LogEntriesTable}
	 *
	 * @param loggerTreeNode the node in the tree structure matching the logger
	 * @param logger		 the logger to exclude
	 */
    public void excludeLogger(LoggerTreeNode loggerTreeNode, String logger) {
        log.debug("Excluding logger: " + logger);
        List<String> parentLoggerInLoggers = getParentLoggerInLoggers(getIncludedLoggers(), logger);
        if (parentLoggerInLoggers.size() > 0) {
            for (String s : parentLoggerInLoggers) {
                getIncludedLoggers().remove(s);
            }
        } else {
            getExcludedLoggers().add(logger);
        }
        loggerTreeNode.setEnabled(false);
        LoggerTreeNode.setChildrenEnabled(loggerTreeNode, false);
        repaint();
    }

    /**
	 * Returns all loggers that are currently excluded
	 *
	 * @return the list of excluded loggers
	 */
    public EventList<String> getExcludedLoggers() {
        return excludedLoggers;
    }

    /**
	 * Returns all loggers that are currently included
	 *
	 * @return the list of included loggers
	 */
    public EventList<String> getIncludedLoggers() {
        return includedLoggers;
    }

    /**
	 * Include the given logger so that it is shown in the {@link net.sourceforge.vigilog.ui.LogEntriesTable}
	 *
	 * @param logger the logger to include
	 */
    public void includeLogger(String logger) {
        LoggerTreeNode node = findNode(getModel(), logger);
        includeLogger(node, logger);
    }

    /**
	 * Include the given logger so that it is shown in the {@link net.sourceforge.vigilog.ui.LogEntriesTable}
	 *
	 * @param loggerTreeNode the node in the tree structure matching the logger
	 * @param logger		 the logger to include
	 */
    public void includeLogger(LoggerTreeNode loggerTreeNode, String logger) {
        log.debug("Including logger: " + logger);
        List<String> parentLoggerInLoggers = getParentLoggerInLoggers(getExcludedLoggers(), logger);
        if (parentLoggerInLoggers.size() > 0) {
            for (String s : parentLoggerInLoggers) {
                getExcludedLoggers().remove(s);
            }
            parentLoggerInLoggers = getParentLoggerInLoggers(getIncludedLoggers(), logger);
            for (String s : parentLoggerInLoggers) {
                getIncludedLoggers().remove(s);
            }
        } else {
            getIncludedLoggers().add(logger);
        }
        loggerTreeNode.setEnabled(true);
        LoggerTreeNode.setChildrenEnabled(loggerTreeNode, true);
        repaint();
    }

    /**
	 * Set the model for this tree. The given list of {@link LogEntry} objects is parsed
	 * for all loggers and the excluded and included loggers are taken into account.
	 *
	 * @param logEntries the list of current log entries
	 */
    public void setModel(List<LogEntry> logEntries) {
        setModel(createLoggersTreeModel(logEntries));
        for (String excludedLogger : getExcludedLoggers()) {
            LoggerTreeNode node = findNode(getModel(), excludedLogger);
            node.setEnabled(false);
            LoggerTreeNode.setChildrenEnabled(node, false);
        }
        for (String includedLogger : getIncludedLoggers()) {
            LoggerTreeNode node = findNode(getModel(), includedLogger);
            node.setEnabled(true);
            LoggerTreeNode.setChildrenEnabled(node, true);
        }
    }

    /**
	 * Parse the log entries and create a {@link TreeModel} out of the logger strings.
	 * The loggers are split at periods ('.').
	 *
	 * @param logEntries the log entries list
	 * @return the matching {@link TreeModel}
	 */
    public static TreeModel createLoggersTreeModel(List<LogEntry> logEntries) {
        LoggerTreeNode root = new LoggerTreeNode("Root");
        for (LogEntry logEntry : logEntries) {
            String logger = logEntry.getLogger();
            if (logger != null) {
                String[] packageNames = logger.split("\\.");
                LoggerTreeNode current = root;
                String parentPackages = "";
                for (String packageName : packageNames) {
                    String totalPackageName = packageName;
                    if (parentPackages.length() > 0) {
                        totalPackageName = parentPackages + "." + packageName;
                    }
                    boolean nodePresent = false;
                    for (int j = 0; j < current.getChildCount(); j++) {
                        LoggerTreeNode childAt = (LoggerTreeNode) current.getChildAt(j);
                        String fullPackageName = childAt.getUserObject();
                        if (totalPackageName.equals(fullPackageName)) {
                            current = childAt;
                            nodePresent = true;
                            break;
                        }
                    }
                    if (!nodePresent) {
                        LoggerTreeNode child = new LoggerTreeNode(totalPackageName);
                        current.add(child);
                        current = child;
                    }
                    parentPackages = totalPackageName;
                }
            }
        }
        return new DefaultTreeModel(root);
    }

    private LoggerTreeNode findNode(TreeModel model, String logger) {
        LoggerTreeNode result = null;
        LoggerTreeNode root = (LoggerTreeNode) model.getRoot();
        Enumeration enumeration = root.depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            LoggerTreeNode loggerTreeNode = (LoggerTreeNode) enumeration.nextElement();
            if (logger.equals(loggerTreeNode.getUserObject())) {
                result = loggerTreeNode;
                break;
            }
        }
        return result;
    }

    private List<String> getParentLoggerInLoggers(List<String> loggers, String logger) {
        List<String> result = new ArrayList<String>();
        for (String s : loggers) {
            if (s.startsWith(logger)) {
                result.add(s);
            }
        }
        return result;
    }
}
