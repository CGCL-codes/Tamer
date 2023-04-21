package net.sf.mzmine.modules.peaklistmethods.dataanalysis.projectionplots;

import net.sf.mzmine.parameters.ParameterSet;
import org.jfree.chart.labels.XYZToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

public class ProjectionPlotToolTipGenerator implements XYZToolTipGenerator {

    private ColoringType coloringType;

    private enum LabelMode {

        FileName, FileNameAndParameterValue
    }

    private LabelMode labelMode;

    ProjectionPlotToolTipGenerator(ParameterSet parameters) {
        try {
            coloringType = parameters.getParameter(ProjectionPlotParameters.coloringType).getValue();
        } catch (IllegalArgumentException exeption) {
            coloringType = ColoringType.NOCOLORING;
        }
        if (coloringType.equals(ColoringType.NOCOLORING)) labelMode = LabelMode.FileName;
        if (coloringType.equals(ColoringType.COLORBYFILE)) labelMode = LabelMode.FileName;
        if (coloringType.isByParameter()) labelMode = LabelMode.FileNameAndParameterValue;
    }

    private String generateToolTip(ProjectionPlotDataset dataset, int item) {
        switch(labelMode) {
            case FileName:
            default:
                return dataset.getRawDataFile(item);
            case FileNameAndParameterValue:
                String ret = dataset.getRawDataFile(item) + "\n";
                ret += coloringType.getParameter().getName() + ": ";
                int groupNumber = dataset.getGroupNumber(item);
                Object paramValue = dataset.getGroupParameterValue(groupNumber);
                if (paramValue != null) ret += paramValue.toString(); else ret += "N/A";
                return ret;
        }
    }

    public String generateToolTip(XYDataset dataset, int series, int item) {
        if (dataset instanceof ProjectionPlotDataset) return generateToolTip((ProjectionPlotDataset) dataset, item); else return null;
    }

    public String generateToolTip(XYZDataset dataset, int series, int item) {
        if (dataset instanceof ProjectionPlotDataset) return generateToolTip((ProjectionPlotDataset) dataset, item); else return null;
    }
}
