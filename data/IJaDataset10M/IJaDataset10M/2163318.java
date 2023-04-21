package org.mcisb.kinetics.absorbance;

import java.io.*;
import org.mcisb.kinetics.*;
import org.mcisb.util.task.*;

/**
 * 
 * @author Neil Swainston
 */
public class AbsorbanceTask extends AbstractGenericBeanTask {

    @Override
    protected Serializable doTask() throws Exception {
        final KineticsExperimentSet experimentSet = (KineticsExperimentSet) bean.getProperty(org.mcisb.kinetics.PropertyNames.EXPERIMENT);
        experimentSet.setExperimentStudy(bean.getString(org.mcisb.kinetics.PropertyNames.PROJECT));
        experimentSet.setProperty(org.mcisb.kinetics.PropertyNames.STRAIN, bean.getString(org.mcisb.kinetics.PropertyNames.STRAIN));
        return new KineticsExperimentCalculator(experimentSet).calculate();
    }
}
