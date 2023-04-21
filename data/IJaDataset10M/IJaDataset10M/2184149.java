package net.sf.mzmine.modules.peaklistmethods.normalization.standardcompound;

import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.data.PeakListRow;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.BooleanParameter;
import net.sf.mzmine.parameters.parametertypes.ComboParameter;
import net.sf.mzmine.parameters.parametertypes.DoubleParameter;
import net.sf.mzmine.parameters.parametertypes.MultiChoiceParameter;
import net.sf.mzmine.parameters.parametertypes.PeakListsParameter;
import net.sf.mzmine.parameters.parametertypes.StringParameter;
import net.sf.mzmine.util.ExitCode;
import net.sf.mzmine.util.PeakMeasurementType;

public class StandardCompoundNormalizerParameters extends SimpleParameterSet {

    public static final PeakListsParameter peakList = new PeakListsParameter(1, 1);

    public static final StringParameter suffix = new StringParameter("Name suffix", "Suffix to be added to peak list name", "normalized");

    public static final ComboParameter<StandardUsageType> standardUsageType = new ComboParameter<StandardUsageType>("Normalization type", "Normalize intensities using ", StandardUsageType.values());

    public static final ComboParameter<PeakMeasurementType> peakMeasurementType = new ComboParameter<PeakMeasurementType>("Peak measurement type", "Measure peaks using ", PeakMeasurementType.values());

    public static final DoubleParameter MZvsRTBalance = new DoubleParameter("m/z vs RT balance", "Used in distance measuring as multiplier of m/z difference");

    public static final BooleanParameter autoRemove = new BooleanParameter("Remove original peak list", "If checked, the original peak list will be removed");

    public static final MultiChoiceParameter<PeakListRow> standardCompounds = new MultiChoiceParameter<PeakListRow>("Standard compounds", "List of peaks for choosing the normalization standards", new PeakListRow[0]);

    public StandardCompoundNormalizerParameters() {
        super(new Parameter[] { peakList, suffix, standardUsageType, peakMeasurementType, MZvsRTBalance, standardCompounds, autoRemove });
    }

    @Override
    public ExitCode showSetupDialog() {
        PeakList selectedPeakList[] = getParameter(peakList).getValue();
        PeakListRow rowChoices[];
        if (selectedPeakList.length == 1) {
            rowChoices = selectedPeakList[0].getRows();
        } else {
            rowChoices = new PeakListRow[0];
        }
        getParameter(StandardCompoundNormalizerParameters.standardCompounds).setChoices(rowChoices);
        return super.showSetupDialog();
    }
}
