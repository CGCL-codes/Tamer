package de.toolforge.googlechartwrapper.label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.toolforge.googlechartwrapper.util.AppendableFeature;
import de.toolforge.googlechartwrapper.util.IExtendedFeatureAppender;
import de.toolforge.googlechartwrapper.util.IFeatureAppender;

/**
 * Collects all axis label containers and appends it to the url.
 * @author martin
 *
 */
public class AxisLabelAppender implements IExtendedFeatureAppender {

    List<AxisLabelContainer> axis = new ArrayList<AxisLabelContainer>();

    public String getFeaturePrefix() {
        return "chxt";
    }

    public List<AppendableFeature> getAppendableFeatures(List<? extends IFeatureAppender> otherAppenders) {
        if (axis.size() < 1) {
            return new ArrayList<AppendableFeature>();
        }
        StringBuffer axisTypes = new StringBuffer(axis.size() + 1);
        for (int i = 0; i < axis.size(); i++) {
            axisTypes.append(axis.get(i).getType().getType());
            axisTypes.append(",");
        }
        StringBuffer axisLabels = new StringBuffer(axis.size() * 5 + 1);
        for (int i = 0; i < axis.size(); i++) {
            AxisLabelContainer sum = axis.get(i);
            if (sum.getLabels().size() > 0) {
                if (sum.isUseLabels()) {
                    axisLabels.append(i);
                    axisLabels.append(":|");
                    for (AxisLabel label : sum.getLabels()) {
                        axisLabels.append(label.getLabel());
                        axisLabels.append("|");
                    }
                }
            }
        }
        StringBuffer axisLabelsPos = new StringBuffer(axis.size() * 5 + 1);
        for (int i = 0; i < axis.size(); i++) {
            AxisLabelContainer sum = axis.get(i);
            if (sum.isUseLabelPositions()) {
                if (sum.getLabels().size() > 0) {
                    axisLabelsPos.append(i);
                    axisLabelsPos.append(",");
                    for (AxisLabel label : sum.getLabels()) {
                        axisLabelsPos.append(label.getPos());
                        axisLabelsPos.append(",");
                    }
                    axisLabelsPos.replace(axisLabelsPos.length() - 1, axisLabelsPos.length(), "");
                    axisLabelsPos.append("|");
                }
            }
        }
        StringBuffer axisRange = new StringBuffer(axis.size() * 5 + 1);
        for (int i = 0; i < axis.size(); i++) {
            AxisLabelContainer sum = axis.get(i);
            if (sum.getAxisRange() != null) {
                axisRange.append(i);
                axisRange.append(",");
                axisRange.append(sum.getAxisRange().getLower());
                axisRange.append(",");
                axisRange.append(sum.getAxisRange().getUpper());
                if (sum.getAxisRange().getInterval() != null) {
                    axisRange.append(",");
                    axisRange.append(sum.getAxisRange().getInterval().intValue());
                }
                axisRange.append("|");
            }
        }
        StringBuffer axisStyle = new StringBuffer(axis.size() * 5 + 1);
        for (int i = 0; i < axis.size(); i++) {
            AxisLabelContainer sum = axis.get(i);
            if (sum.getAxisStyle() != null) {
                axisStyle.append(i);
                axisStyle.append(",");
                axisStyle.append((sum.getAxisStyle().getColor().getMatchingColorHexValue()));
                if (sum.getAxisStyle().getFontSize() > 0) {
                    axisStyle.append(",");
                    axisStyle.append(sum.getAxisStyle().getFontSize());
                    if (sum.getAxisStyle().isAlignmentUsed()) {
                        axisStyle.append(",");
                        axisStyle.append(sum.getAxisStyle().getAlignment());
                    }
                }
                axisStyle.append("|");
            }
        }
        List<AppendableFeature> features = new ArrayList<AppendableFeature>();
        features.add(new AppendableFeature(axisTypes.substring(0, axisTypes.length() - 1), getFeaturePrefix()));
        if (axisLabels.length() > 1) {
            features.add(new AppendableFeature(axisLabels.substring(0, axisLabels.length() - 1), "chxl"));
        }
        if (axisLabelsPos.length() > 1) {
            features.add(new AppendableFeature(axisLabelsPos.substring(0, axisLabelsPos.length() - 1), "chxp"));
        }
        if (axisRange.length() > 1) {
            features.add(new AppendableFeature(axisRange.substring(0, axisRange.length() - 1), "chxr"));
        }
        if (axisStyle.length() > 1) {
            features.add(new AppendableFeature(axisStyle.substring(0, axisStyle.length() - 1), "chxs"));
        }
        return features;
    }

    public void addAxis(AxisLabelContainer axis) {
        this.axis.add(axis);
    }

    public boolean removeAxis(AxisLabelContainer axis) {
        return this.axis.remove(axis);
    }

    public AxisLabelContainer removeAxis(int index) {
        return axis.remove(index);
    }

    public void removeAll() {
        for (int i = 0; i < axis.size(); ) {
            axis.remove(i);
        }
    }

    public List<AxisLabelContainer> getList() {
        return Collections.unmodifiableList(axis);
    }
}
