package net.sf.mzmine.modules.peaklistmethods.identification.dbsearch;

import net.sf.mzmine.data.IonizationType;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.ComboParameter;
import net.sf.mzmine.parameters.parametertypes.PeakListsParameter;

public class PeakListIdentificationParameters extends SimpleParameterSet {

    public static final PeakListsParameter peakLists = new PeakListsParameter();

    public static final ComboParameter<IonizationType> ionizationType = new ComboParameter<IonizationType>("Ionization type", "Ionization type", IonizationType.values());

    public PeakListIdentificationParameters() {
        super(new Parameter[] { peakLists, SingleRowIdentificationParameters.DATABASE, ionizationType, SingleRowIdentificationParameters.MAX_RESULTS, SingleRowIdentificationParameters.MZ_TOLERANCE, SingleRowIdentificationParameters.ISOTOPE_FILTER });
    }
}
