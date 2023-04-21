package net.sf.mzmine.modules.peaklistmethods.dataanalysis.clustering;

import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.data.PeakListRow;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.clustering.em.EMClusterer;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.clustering.farthestfirst.FarthestFirstClusterer;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.clustering.hierarchical.HierarClusterer;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.clustering.simplekmeans.SimpleKMeansClusterer;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.projectionplots.ProjectionPlotParameters;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.ComboParameter;
import net.sf.mzmine.parameters.parametertypes.ModuleComboParameter;
import net.sf.mzmine.parameters.parametertypes.PeakListsParameter;
import net.sf.mzmine.util.ExitCode;

public class ClusteringParameters extends SimpleParameterSet {

    public static final PeakListsParameter peakLists = new PeakListsParameter();

    private static ClusteringAlgorithm algorithms[] = new ClusteringAlgorithm[] { new EMClusterer(), new FarthestFirstClusterer(), new SimpleKMeansClusterer(), new HierarClusterer() };

    public static final ModuleComboParameter<ClusteringAlgorithm> clusteringAlgorithm = new ModuleComboParameter<ClusteringAlgorithm>("Clustering algorithm", "Select the algorithm you want to use for clustering", algorithms);

    public static final ComboParameter<ClusteringDataType> typeOfData = new ComboParameter<ClusteringDataType>("Type of data", "Specify the type of data used for the clustering: samples or variables", ClusteringDataType.values());

    public ClusteringParameters() {
        super(new Parameter[] { peakLists, ProjectionPlotParameters.peakMeasurementType, ProjectionPlotParameters.dataFiles, ProjectionPlotParameters.rows, clusteringAlgorithm, typeOfData });
    }

    @Override
    public ExitCode showSetupDialog() {
        PeakList selectedPeakList[] = getParameter(peakLists).getValue();
        RawDataFile dataFileChoices[];
        if (selectedPeakList.length == 1) {
            dataFileChoices = selectedPeakList[0].getRawDataFiles();
        } else {
            dataFileChoices = new RawDataFile[0];
        }
        PeakListRow rowChoices[];
        if (selectedPeakList.length == 1) {
            rowChoices = selectedPeakList[0].getRows();
        } else {
            rowChoices = new PeakListRow[0];
        }
        getParameter(ProjectionPlotParameters.dataFiles).setChoices(dataFileChoices);
        getParameter(ProjectionPlotParameters.rows).setChoices(rowChoices);
        return super.showSetupDialog();
    }
}
