package sequime.DotPlot;

import javax.swing.event.ChangeListener;
import org.knime.base.node.viz.plotter.scatter.*;
import org.knime.base.node.viz.plotter.columns.*;
import org.knime.base.node.viz.plotter.props.ScatterPlotterAppearanceTab;
import org.knime.core.data.DataValue;

/**
 * Stores all tabs inside the view.
 * @author micha
 *
 */
public class DotPlotterProperties extends ScatterPlotterProperties {

    protected DotPlotterFilterTab filterTab;

    public DotPlotterProperties() {
        this(new Class[] { DataValue.class }, new Class[] { DataValue.class });
    }

    public DotPlotterProperties(final Class[] allowedXTypes, final Class[] allowedYTypes) {
        super(allowedXTypes, allowedYTypes);
        this.filterTab = new DotPlotterFilterTab();
        this.addTab(this.filterTab.getDefaultName(), this.filterTab);
    }

    /**
     * Adds a new listener to panel controlling filter size.
     * @param listener New ChangeListener.
     */
    public void addFilterSizeChangeListener(final ChangeListener listener) {
        this.filterTab.addFilterSizeChangeListener(listener);
    }

    /**
     * @return Returns the current filter size.
     */
    public int getFilterSize() {
        return this.filterTab.getFilterSize();
    }
}
