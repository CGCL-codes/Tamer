package googlechartwrapper.label;

import googlechartwrapper.AbstractPieChart;
import java.util.List;

/**
 * The interface for chart types which supports <a href="http://code.google.com/apis/chart/labels.html#pie_labels">
 * http://code.google.com/apis/chart/labels.html#pie_labels</a>
 * 
 * @author steffan
 * @version 03/17/09 
 * @see AbstractPieChart
 * @see PieChartLabel
 *
 */
public interface IPieChartLabelable {

    /**
	 * Adds an new PieChartLabel to the Chart.
	 * 
	 * @param pcl the new PieChartLabel
	 * 
	 * @throws IllegalArgumentException if you try to add <code>null</code>
	 */
    public void addPieChartLabel(PieChartLabel pcl);

    /**
	 * Returns a unmodifiable list of PieChartLabels.
	 * 
	 * @return list of PieChartLabel, can be empty.
	 */
    public List<PieChartLabel> getPieChartLabels();

    /**
	 * Removes an PieChartLabel at the given position.
	 * 
	 * @param index
	 * 
	 * @return the removed PieChartLabel
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
    public PieChartLabel removePieChartLabel(int index);

    /**
	 * Removes an given PieChartLabel object. 
	 * 
	 * @param pcl the PieChartLabel object in the list.
	 * 
	 * @return true if the remove was successful
	 */
    public boolean removePieChartLabel(DataPointLabel pcl);

    /**
	 * Removes all PieChartLabels in the list.
	 * 
	 */
    public void removePieChartLabels();
}
