package net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters;

import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.cropper.CropFilter;
import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.mean.MeanFilter;
import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.resample.ResampleFilter;
import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.savitzkygolay.SGFilter;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.BooleanParameter;
import net.sf.mzmine.parameters.parametertypes.ModuleComboParameter;
import net.sf.mzmine.parameters.parametertypes.RawDataFilesParameter;
import net.sf.mzmine.parameters.parametertypes.StringParameter;

public class ScanFiltersParameters extends SimpleParameterSet {

    public static final ScanFilter rawDataFilters[] = { new SGFilter(), new MeanFilter(), new CropFilter(), new ResampleFilter() };

    public static final RawDataFilesParameter dataFiles = new RawDataFilesParameter();

    public static final StringParameter suffix = new StringParameter("Suffix", "This string is added to filename as suffix", "filtered");

    public static final ModuleComboParameter<ScanFilter> filter = new ModuleComboParameter<ScanFilter>("Filter", "Raw data filter", rawDataFilters);

    public static final BooleanParameter autoRemove = new BooleanParameter("Remove source file after filtering", "If checked, original file will be removed and only filtered version remains");

    public ScanFiltersParameters() {
        super(new Parameter[] { dataFiles, suffix, filter, autoRemove });
    }
}
