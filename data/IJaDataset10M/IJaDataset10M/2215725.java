package org.kablink.teaming.gwt.client.widgets;

import org.kablink.teaming.gwt.client.GwtTeaming;
import org.kablink.teaming.gwt.client.GwtTeamingMessages;
import org.kablink.teaming.gwt.client.util.GwtClientHelper;
import org.kablink.teaming.gwt.client.util.MilestoneStats;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * Base class for a group of objects that draws graphs based on the
 * statistics from a MilestoneStats object.
 * 
 * @author jwootton@novell.com
 */
public abstract class MilestoneGraphBase extends VibeFlowPanel {

    private FlexCellFormatter m_gridCellFormatter;

    private FlexTable m_grid;

    private MilestoneStats m_milestoneStats;

    private String m_gridStyles;

    protected GwtTeamingMessages m_messages = GwtTeaming.getMessages();

    /**
	 * Constructor method.
	 */
    public MilestoneGraphBase(MilestoneStats milestoneStats, String gridStyles, boolean showLegend) {
        super();
        setMilestoneStatistics(milestoneStats);
        setGridStyles(gridStyles);
        addStyleName("milestoneGraphs-stats");
        renderGraph(showLegend);
    }

    /**
	 * Adds a colored bar segment to a grid.
	 * 
	 * @param count
	 * @param percent
	 * @param style
	 * @param message
	 */
    protected void addBarSegment(int count, int percent, String style, String message) {
        String width;
        InlineLabel il;
        int cell;
        if (count == 0) {
            return;
        }
        width = (percent + "%");
        il = new InlineLabel(width);
        il.addStyleName("milestoneGraphs-statsBarSegment");
        il.setWordWrap(false);
        il.setTitle(message);
        try {
            cell = m_grid.getCellCount(0);
        } catch (Exception ex) {
            cell = 0;
        }
        m_grid.setWidget(0, cell, il);
        m_gridCellFormatter.setWidth(0, cell, width);
        m_gridCellFormatter.addStyleName(0, cell, style);
    }

    /**
	 * Adds a statistic bar to a graph.
	 * 
	 * @param vp
	 * @param style
	 * @param message
	 */
    protected void addLegendBar(String style, String message) {
        VibeFlowPanel fp;
        VibeFlowPanel colorBox;
        InlineLabel il;
        fp = new VibeFlowPanel();
        fp.addStyleName("milestoneGraphs-statsLegendBar");
        add(fp);
        colorBox = new VibeFlowPanel();
        colorBox.addStyleName("milestoneGraphs-statsLegendBox " + style);
        fp.add(colorBox);
        il = new InlineLabel(message);
        il.addStyleName("milestoneGraphs-statsLegendLabel");
        fp.add(il);
    }

    /**
	 * 
	 */
    protected final MilestoneStats getMilestoneStatistics() {
        return m_milestoneStats;
    }

    /**
	 * Renders the graph.
	 * 
	 * This is implemented by the classes that extend this class to
	 * render their graph.
	 * 
	 * @param showLegend
	 */
    protected abstract void render(boolean showLegend);

    private void renderGraph(boolean showLegend) {
        renderGrid();
        render(showLegend);
    }

    private void renderGrid() {
        m_grid = new FlexTable();
        if (GwtClientHelper.hasString(m_gridStyles)) {
            m_grid.addStyleName(m_gridStyles);
        }
        m_grid.setCellPadding(0);
        m_grid.setCellSpacing(0);
        add(m_grid);
        m_gridCellFormatter = m_grid.getFlexCellFormatter();
    }

    private void setGridStyles(String gridStyles) {
        m_gridStyles = gridStyles;
    }

    /**
	 * 
	 */
    private void setMilestoneStatistics(MilestoneStats milestoneStats) {
        m_milestoneStats = milestoneStats;
    }
}
