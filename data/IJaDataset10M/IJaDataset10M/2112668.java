package ca.sqlpower.wabit.swingui.report;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import ca.sqlpower.wabit.report.Report;
import ca.sqlpower.wabit.swingui.WabitSwingSession;
import ca.sqlpower.wabit.swingui.report.selectors.SelectorsPanel;

public class ReportPanel extends LayoutPanel {

    private SelectorsPanel dashboardPanel;

    private final JSplitPane splitPane;

    private final Report report;

    private static final Preferences prefs = Preferences.userRoot();

    private static final String SEPARATOR_POSITION_PREFERENCES_KEY = "Wabit.ReportPanel.SeparatorPosition";

    private static final Double DEFAULT_SEPARATOR_POSITION = 0.5d;

    private final Runnable reportRefresher = new Runnable() {

        public void run() {
            refreshDataAction.actionPerformed(null);
        }
    };

    public ReportPanel(WabitSwingSession session, final Report report) {
        super(session, report);
        this.report = report;
        this.dashboardPanel = new SelectorsPanel(report, reportRefresher);
        this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.splitPane.setTopComponent(super.getSourceComponent());
        this.splitPane.setBottomComponent(dashboardPanel);
        this.splitPane.setOneTouchExpandable(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                splitPane.setDividerLocation(prefs.getDouble(SEPARATOR_POSITION_PREFERENCES_KEY, DEFAULT_SEPARATOR_POSITION));
                splitPane.addPropertyChangeListener("dividerLocation", new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent e) {
                        Number value = (Number) e.getNewValue();
                        prefs.putDouble(SEPARATOR_POSITION_PREFERENCES_KEY, value.doubleValue() / splitPane.getBounds().height);
                    }
                });
            }
        });
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        this.dashboardPanel.cleanup();
    }

    /**
	 * We override this component because we want to add a more complex component
	 * that will display the source stuff but also the dashboard controls.
	 */
    public JComponent getSourceComponent() {
        return this.splitPane;
    }
}
