package org.mcisb.ui.kinetics;

import java.util.*;
import javax.swing.*;
import org.mcisb.kinetics.*;
import org.mcisb.kinetics.memo.*;
import org.mcisb.ui.app.*;

/**
 * 
 * @author Neil Swainston
 */
public class SabioKineticsArchiver implements Archiver {

    @Override
    public void archive(final KineticsExperimentSet experimentSet, final Map<String, double[]> modelNameToInitialRates) throws Exception {
        final App app = new KineticsExperimentWriterApp(new JFrame(), experimentSet, modelNameToInitialRates, new SabioKineticsExperimentWriterTask());
        app.show();
    }
}
