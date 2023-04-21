package ho.module.teamAnalyzer.ui;

import ho.core.gui.IRefreshable;
import ho.core.gui.comp.panel.ImagePanel;
import ho.core.model.HOVerwaltung;
import ho.core.model.UserParameter;
import ho.core.module.config.ModuleConfig;
import ho.module.teamAnalyzer.SystemManager;
import ho.module.teamAnalyzer.manager.ReportManager;
import ho.module.teamAnalyzer.ui.controller.SimButtonListener;
import ho.module.teamAnalyzer.vo.Filter;
import ho.module.teamAnalyzer.vo.TeamLineup;
import ho.module.training.ui.comp.DividerListener;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class TeamAnalyzerPanel extends JPanel implements IRefreshable {

    /** The filters */
    public static Filter filter = new Filter();

    private static final long serialVersionUID = 1L;

    private JButton simButton = new JButton(HOVerwaltung.instance().getLanguageString("Simulate"));

    private RecapPanel recapPanel;

    private MainPanel mainPanel;

    private FilterPanel filterPanel;

    private RatingPanel ratingPanel;

    public TeamAnalyzerPanel() {
        SystemManager.initialize(this);
        initialize();
        simButton.addActionListener(new SimButtonListener(mainPanel.getMyTeamLineupPanel(), mainPanel.getOpponentTeamLineupPanel(), recapPanel));
        SystemManager.refreshData();
    }

    private void initialize() {
        filterPanel = new FilterPanel();
        recapPanel = new RecapPanel();
        mainPanel = new MainPanel();
        ratingPanel = new RatingPanel();
        setLayout(new BorderLayout());
        JPanel buttonPanel = new ImagePanel();
        buttonPanel.setLayout(new BorderLayout());
        simButton.setText(HOVerwaltung.instance().getLanguageString("Simulate"));
        buttonPanel.add(simButton, BorderLayout.CENTER);
        JSplitPane panel = new JSplitPane(0, ratingPanel, buttonPanel);
        panel.setDividerSize(1);
        panel.setResizeWeight(1);
        panel.setDividerLocation(UserParameter.instance().teamAnalyzer_LowerLefSplitPane);
        panel.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new DividerListener(DividerListener.teamAnalyzer_LowerLefSplitPane));
        JSplitPane leftPanel = new JSplitPane(0, filterPanel, panel);
        leftPanel.setDividerLocation(UserParameter.instance().teamAnalyzer_UpperLeftSplitPane);
        leftPanel.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new DividerListener(DividerListener.teamAnalyzer_UpperLeftSplitPane));
        JSplitPane mainPanel2 = new JSplitPane(1, leftPanel, mainPanel);
        mainPanel2.setDividerLocation(UserParameter.instance().teamAnalyzer_MainSplitPane);
        mainPanel2.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new DividerListener(DividerListener.teamAnalyzer_MainSplitPane));
        JSplitPane m_splitPane = new JSplitPane(0, mainPanel2, recapPanel);
        m_splitPane.setDividerLocation(UserParameter.instance().teamAnalyzer_BottomSplitPane);
        m_splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new DividerListener(DividerListener.teamAnalyzer_BottomSplitPane));
        add(m_splitPane, BorderLayout.CENTER);
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Returns the Filter Panel
     *
     * @return
     */
    public FilterPanel getFilterPanel() {
        return filterPanel;
    }

    /**
     * Returns the rating panel
     *
     * @return
     */
    public RatingPanel getRatingPanel() {
        return ratingPanel;
    }

    /**
     * Returns the recap panel
     *
     * @return
     */
    RecapPanel getRecapPanel() {
        return recapPanel;
    }

    /**
     * Returns the Simulate Button reference
     *
     * @return
     */
    JButton getSimButton() {
        return simButton;
    }

    public void refresh() {
    }

    public void reload() {
        TeamLineup lineup = ReportManager.getLineup();
        getFilterPanel().reload();
        if (lineup != null) {
            getMainPanel().reload(lineup, 0, 0);
            getRecapPanel().reload(lineup);
            getRatingPanel().reload(lineup);
        } else {
            getMainPanel().reload(null, 0, 0);
            getRecapPanel().reload(null);
            getRatingPanel().reload(null);
        }
        if (ModuleConfig.instance().getBoolean(SystemManager.ISLINEUP)) {
            getSimButton().setVisible(true);
        } else {
            getSimButton().setVisible(false);
        }
    }
}
