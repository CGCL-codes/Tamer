package net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.cropper;

import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.ScanFilterSetupDialog;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.RangeParameter;
import net.sf.mzmine.util.ExitCode;

public class CropFilterParameters extends SimpleParameterSet {

    public static final RangeParameter mzRange = new RangeParameter("m/z range", "m/z boundary of the cropped region", MZmineCore.getConfiguration().getMZFormat());

    public CropFilterParameters() {
        super(new Parameter[] { mzRange });
    }

    public ExitCode showSetupDialog() {
        ScanFilterSetupDialog dialog = new ScanFilterSetupDialog(this, CropFilter.class);
        dialog.setVisible(true);
        return dialog.getExitCode();
    }
}
