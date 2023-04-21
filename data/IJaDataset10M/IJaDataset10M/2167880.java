package net.sf.karatasi.desktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.karatasi.database.Database;
import net.sf.karatasi.database.DatabaseStatisticModel;
import org.jetbrains.annotations.NotNull;

/** The panel to display the statistic data of the database.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class DatabaseStatisticPanel extends JPanel implements ActionListener {

    /** The serialization Id. */
    private static final long serialVersionUID = -3301855276821453863L;

    /** For the localized texts. */
    @NotNull
    private final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder(GuiMain.class);

    /** The associated controller. */
    @NotNull
    private final DatabaseStatisticController controller;

    /** The database we are working with. */
    @NotNull
    private final Database database;

    /** The statistics data model for the database. */
    @NotNull
    private final DatabaseStatisticModel statisticsModel;

    /** The list of references to *_GRAPH constants of DatabaseStatisticsModel.
     * This list has exactly the same indexes that statisticsSelector displays.
     */
    @NotNull
    private final List<String> availableGraphs;

    /** The selector of the drawing type. This object is set to one of the *_GRAPH constants of DatabaseStatisticsModel. */
    private String activeGraphSelector = null;

    /** The central drawing panel. It provides a derived drawing routine that calls drawGraph. */
    @NotNull
    private final StatisticPanel centralPanel;

    /** The statistics selector. */
    @NotNull
    private final JComboBox statisticsSelector;

    /** The close button. */
    @NotNull
    private final JButton closeButton;

    /** Constructor.
     *
     * @param controller the associated controller.
     * @param database the database to be used.
     * @throws SQLException if database opening fails
     * @throws FileNotFoundException if database opening fails
     */
    public DatabaseStatisticPanel(@NotNull final DatabaseStatisticController controller, @NotNull final Database database) throws FileNotFoundException, SQLException {
        this.controller = controller;
        this.database = database;
        this.database.open();
        this.statisticsModel = new DatabaseStatisticModel(this.database);
        this.setLayout(new BorderLayout());
        final JLabel headlineLabel = new JLabel();
        final Font origFont = headlineLabel.getFont();
        headlineLabel.setFont(origFont.deriveFont(origFont.getStyle(), (float) (origFont.getSize() * 1.5)));
        headlineLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        headlineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headlineLabel.setText(String.format(actionBuilder.getString("showDatabaseStatistic.panel.headline"), database.getFullName()));
        this.add(headlineLabel, BorderLayout.NORTH);
        availableGraphs = statisticsModel.getAvailableGraphs();
        activeGraphSelector = availableGraphs.get(0);
        centralPanel = new StatisticPanel(this.statisticsModel, activeGraphSelector);
        this.add(centralPanel, BorderLayout.CENTER);
        final JPanel flowSouth = new JPanel();
        final Vector<String> graphSelectionList = new Vector<String>(availableGraphs.size());
        for (final String graphReference : availableGraphs) {
            graphSelectionList.add(actionBuilder.getString("showDatabaseStatistic." + graphReference + ".name"));
        }
        statisticsSelector = new JComboBox(graphSelectionList);
        statisticsSelector.addActionListener(this);
        flowSouth.add(statisticsSelector);
        closeButton = new JButton(actionBuilder.getString("showDatabaseStatistic.view.close"));
        closeButton.addActionListener(this);
        flowSouth.add(closeButton);
        this.add(flowSouth, BorderLayout.SOUTH);
    }

    /** {@inheritDoc} */
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == closeButton) {
            database.close();
            controller.closeButtonPressed();
        } else if (e.getSource() == statisticsSelector) {
            activeGraphSelector = availableGraphs.get(statisticsSelector.getSelectedIndex());
            centralPanel.switchGraph(activeGraphSelector);
        }
    }
}
