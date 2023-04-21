package net.sf.mzmine.modules.peaklistmethods.msms.msmsscore;

import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.MZToleranceParameter;
import net.sf.mzmine.parameters.parametertypes.MassListParameter;
import net.sf.mzmine.parameters.parametertypes.PercentParameter;

public class MSMSScoreParameters extends SimpleParameterSet {

    public static final MassListParameter massList = new MassListParameter();

    public static final MZToleranceParameter msmsTolerance = new MZToleranceParameter("MS/MS m/z tolerance", "Tolerance of the mass value to search (+/- range)");

    public static final PercentParameter msmsMinScore = new PercentParameter("MS/MS score threshold", "If the score for MS/MS is lower, discard this match");

    public MSMSScoreParameters() {
        super(new Parameter[] { massList, msmsTolerance, msmsMinScore });
    }
}
