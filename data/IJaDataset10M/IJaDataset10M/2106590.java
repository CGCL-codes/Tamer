package log4j.ui;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import prisms.arch.PrismsSession;
import prisms.ui.tree.DataTreeNode;

/** Displays the entire hierarchy of Log4j {@link Logger}s to the user */
public class LoggerTree extends prisms.ui.tree.DataTreeMgrPlugin {

    static final Logger log = Logger.getLogger(LoggerTree.class);

    private java.util.HashSet<Logger> theLoggers;

    @Override
    public void initPlugin(PrismsSession session, prisms.arch.PrismsConfig config) {
        setSelectionMode(SelectionMode.SINGLE);
        super.initPlugin(session, config);
        theLoggers = new java.util.HashSet<Logger>();
        Logger rootlog = org.apache.log4j.Logger.getRootLogger();
        setRoot(new LoggerNode(this, null, false, rootlog));
        checkClearable();
        session.addEventListener("userAuthorityChanged", new prisms.arch.event.PrismsEventListener() {

            public void eventOccurred(PrismsSession session2, prisms.arch.event.PrismsEvent evt) {
                if (getSession().getUser().equals(evt.getProperty("user"))) checkClearable();
            }
        });
    }

    @Override
    public void initClient() {
        checkLoggers();
        super.initClient();
    }

    @Override
    public void processEvent(JSONObject evt) {
        checkLoggers();
        super.processEvent(evt);
    }

    void checkClearable() {
        LoggerNode root = (LoggerNode) getRoot();
        boolean clearable = getSession().getPermissions().has("Edit Loggers");
        if (root.getAvailableActions().length == 0 && clearable) root.addAction(new javax.swing.AbstractAction("Clear Saved Logger Settings") {

            public void actionPerformed(ActionEvent evt) {
                String msg = "Are you sure you want to clear all saved logger settings? Logging" + " levels will be reverted to the defaults after the server is restarted.";
                getSession().getUI().confirm(msg, new prisms.ui.UI.ConfirmListener() {

                    public void confirmed(boolean confirm) {
                        if (!confirm) return;
                        int cleared;
                        try {
                            cleared = getSession().getApp().getEnvironment().getLogger().clearLoggerConfigs();
                        } catch (prisms.arch.PrismsException e) {
                            log.error("Could not clear saved logger settings", e);
                            getSession().getUI().error("Could not clear saved logger settings: " + e);
                            return;
                        }
                        getSession().getUI().info(cleared + " setting" + (cleared == 1 ? "" : "s") + " cleared");
                    }
                });
            }
        }); else if (root.getAvailableActions().length > 0 && !clearable) root.removeAction(root.getAvailableActions()[0]);
    }

    private void checkLoggers() {
        java.util.Enumeration<Logger> logs = org.apache.log4j.LogManager.getCurrentLoggers();
        while (logs.hasMoreElements()) {
            Logger next = logs.nextElement();
            if (!theLoggers.contains(next)) {
                theLoggers.add(next);
                addLogger(next);
            }
        }
    }

    @Override
    public void setSelection(DataTreeNode[] nodes, boolean fromUser) {
        super.setSelection(nodes, fromUser);
        if (nodes.length != 1 || !(nodes[0] instanceof LoggerNode)) return;
        Logger logger = ((LoggerNode) nodes[0]).getLogger();
        getSession().setProperty(log4j.app.Log4jProperties.selectedLogger, logger);
    }

    void addLogger(Logger logger) {
        String[] path = logger.getName().split("\\.");
        addLogger((LoggerNode) getRoot(), logger, path, 0);
    }

    void addLogger(LoggerNode subtree, Logger logger, String[] path, int pathIndex) {
        LoggerNode toUse = null;
        for (prisms.ui.tree.DataTreeNode child : subtree.getChildren()) {
            String[] childPath = ((LoggerNode) child).getLogger().getName().split("\\.");
            if (childPath[pathIndex].equals(path[pathIndex])) {
                toUse = (LoggerNode) child;
                break;
            }
        }
        if (pathIndex == path.length - 1) {
            if (toUse == null) {
                toUse = new LoggerNode(this, subtree, subtree.hasPublicActions(), logger);
                subtree.setChildren(prisms.util.ArrayUtils.add(subtree.getChildren(), toUse));
            }
            return;
        }
        if (toUse == null) {
            Logger intermediateLog;
            StringBuilder name = new StringBuilder();
            for (int p = 0; p < pathIndex + 1; p++) {
                name.append(path[p]);
                if (p != pathIndex) name.append(".");
            }
            intermediateLog = org.apache.log4j.Logger.getLogger(name.toString());
            toUse = new LoggerNode(this, subtree, subtree.hasPublicActions(), intermediateLog);
            subtree.setChildren(prisms.util.ArrayUtils.add(subtree.getChildren(), toUse));
        }
        addLogger(toUse, logger, path, pathIndex + 1);
    }
}
