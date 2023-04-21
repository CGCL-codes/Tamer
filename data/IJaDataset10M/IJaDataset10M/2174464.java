package com.rapidminer.gui.new_plotter.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.rapidminer.gui.new_plotter.configuration.DimensionConfig.PlotDimension;
import com.rapidminer.gui.new_plotter.configuration.ValueSource.SeriesUsageType;

/**
 * Contains a list of {@link GroupCellKeyAndData}.
 * 
 * @author Marius Helf
 *
 */
public class GroupCellSeriesData implements Iterable<GroupCellKeyAndData> {

    private List<GroupCellKeyAndData> groupCellSeriesData = new LinkedList<GroupCellKeyAndData>();

    public void addGroupCell(GroupCellKeyAndData groupCellKeyAndData) {
        groupCellSeriesData.add(groupCellKeyAndData);
    }

    public void clear() {
        groupCellSeriesData.clear();
    }

    public int groupCellCount() {
        return groupCellSeriesData.size();
    }

    public boolean isEmpty() {
        return groupCellSeriesData.isEmpty();
    }

    public Set<Double> getDistinctValues(SeriesUsageType usageType, PlotDimension dimension) {
        Set<Double> distinctValuesSet = new HashSet<Double>();
        for (GroupCellKeyAndData groupCellKeyAndData : groupCellSeriesData) {
            GroupCellData groupCellData = groupCellKeyAndData.getData();
            for (double value : groupCellData.getDataForUsageType(usageType).get(dimension)) {
                distinctValuesSet.add(value);
            }
        }
        return distinctValuesSet;
    }

    @Override
    public synchronized Iterator<GroupCellKeyAndData> iterator() {
        Iterator<GroupCellKeyAndData> i = null;
        synchronized (groupCellSeriesData) {
            i = groupCellSeriesData.iterator();
        }
        return i;
    }

    public GroupCellKeyAndData getGroupCellKeyAndData(int seriesIdx) {
        return groupCellSeriesData.get(seriesIdx);
    }
}
