package ostf.gui.frame.monitor;

import javax.swing.JTabbedPane;

public class ActionStatsMonitors extends JTabbedPane {

    private static final long serialVersionUID = -1345579963817682725L;

    private ActionStatsPanel hitsPanel = null;

    private ActionStatsPanel failureRatePanel = null;

    private ActionStatsPanel responseTimePanel = null;

    private ActionStatsPanel throughputPanel = null;

    private ActionStatsTree actionStatsTree = null;

    public ActionStatsMonitors() {
        hitsPanel = new ActionStatsPanel("Action Hits", "number/minute");
        addTab("Hits", hitsPanel);
        failureRatePanel = new ActionStatsPanel("Action Failure Rate", "%");
        addTab("FailureRate", failureRatePanel);
        responseTimePanel = new ActionStatsPanel("Action Average Response Time", "milliseconds");
        addTab("ResponseTime", responseTimePanel);
        throughputPanel = new ActionStatsPanel("Action Troughput", "bytes/minute");
        addTab("Troughput", throughputPanel);
        actionStatsTree = new ActionStatsTree(this);
    }

    public ActionStatsTree getActionStatsTree() {
        return actionStatsTree;
    }

    public void monitorNode(ActionStatsNode node) {
        hitsPanel.getCollection().addSeries(node.getHitSeries());
        failureRatePanel.getCollection().addSeries(node.getFailureRateSeries());
        responseTimePanel.getCollection().addSeries(node.getResponseTimeSeries());
        throughputPanel.getCollection().addSeries(node.getThroughPutSeries());
    }

    public void unMonitorNode(ActionStatsNode node) {
        hitsPanel.getCollection().removeSeries(node.getHitSeries());
        failureRatePanel.getCollection().removeSeries(node.getFailureRateSeries());
        responseTimePanel.getCollection().removeSeries(node.getResponseTimeSeries());
        throughputPanel.getCollection().removeSeries(node.getThroughPutSeries());
    }

    public void clearActionStats() {
        actionStatsTree.clearActionStats(null);
        hitsPanel.getCollection().removeAllSeries();
        failureRatePanel.getCollection().removeAllSeries();
        responseTimePanel.getCollection().removeAllSeries();
        throughputPanel.getCollection().removeAllSeries();
    }
}
