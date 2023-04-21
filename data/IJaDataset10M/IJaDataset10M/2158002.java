package org.mcisb.kinetics;

import java.io.*;
import java.util.*;
import org.mcisb.kinetics.absorbance.*;
import org.mcisb.ontology.sbo.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class KineticsExperimentCalculator {

    /**
	 * 
	 */
    private final KineticsExperimentSet experimentSet;

    /**
	 * 
	 */
    protected boolean calculated = false;

    /**
	 *
	 * @param experimentSet
	 */
    public KineticsExperimentCalculator(final KineticsExperimentSet experimentSet) {
        this.experimentSet = experimentSet;
    }

    /**
	 * 
	 * @return Serializable
	 * @throws java.lang.Exception
	 */
    public Serializable calculate() throws java.lang.Exception {
        if (!calculated) {
            return doCalculation();
        }
        return null;
    }

    /**
	 * 
	 *
	 * @return Serializable
	 * @throws java.lang.Exception
	 */
    private Serializable doCalculation() throws java.lang.Exception {
        final int FIRST = 0;
        final java.util.HashMap<String, KineticsCalculator> modelNameToKineticsCalculator = new java.util.HashMap<String, KineticsCalculator>();
        for (Iterator<SBMLDocument> iterator = experimentSet.getDocuments().iterator(); iterator.hasNext(); ) {
            final Model model = iterator.next().getModel();
            final AbsorbanceUtils absorbanceUtils = new AbsorbanceUtils(experimentSet.getAbsorptionCoefficient(model.getId()), experimentSet.getExperimentProtocol().getDouble(org.mcisb.kinetics.absorbance.PropertyNames.PATH_LENGTH));
            double modifierConcentration = NumberUtils.UNDEFINED;
            for (int l = 0; l < model.getNumReactions(); l++) {
                final Reaction reaction = model.getReaction(l);
                final boolean isBackground = (reaction.getNumModifiers() == 0);
                modifierConcentration = (reaction.getNumModifiers() > 0) ? model.getSpecies(reaction.getModifier(FIRST).getSpecies()).getInitialConcentration() : NumberUtils.UNDEFINED;
                for (int m = 0; m < reaction.getNumReactants(); m++) {
                    final Species reactant = model.getSpecies(reaction.getReactant(m).getSpecies());
                    final double[] absorbanceData = experimentSet.getAbsorbanceData(reactant.getId());
                    if (absorbanceData.length > 0) {
                        absorbanceUtils.addData(absorbanceData, isBackground, reactant.getInitialConcentration());
                    }
                }
            }
            final KineticsCalculator calculator = new KineticsCalculator(experimentSet.getTimepoints(), absorbanceUtils.getSubstrateConcentrations(), absorbanceUtils.getProductConcentrations(), modifierConcentration, experimentSet.getConsiderHillCoefficient(model.getId()));
            modelNameToKineticsCalculator.put(model.getName(), calculator);
            for (int l = 0; l < model.getNumReactions(); l++) {
                final Reaction reaction = model.getReaction(l);
                final KineticLaw kineticLaw = reaction.getKineticLaw();
                kineticLaw.unsetListOfLocalParameters();
                final LocalParameter kcatParameter = KineticsUtils.addParameter(kineticLaw, SboUtils.CATALYTIC_RATE_CONSTANT, org.mcisb.kinetics.PropertyNames.KCAT_UNIT);
                kcatParameter.setValue(calculator.getValue(SboUtils.CATALYTIC_RATE_CONSTANT));
                experimentSet.addCondition(kcatParameter, org.mcisb.util.PropertyNames.ERROR, Double.toString(calculator.getError(SboUtils.CATALYTIC_RATE_CONSTANT)));
                final LocalParameter kmParameter = KineticsUtils.addParameter(kineticLaw, SboUtils.MICHAELIS_CONSTANT, org.mcisb.kinetics.PropertyNames.CONCENTRATION_UNIT);
                kmParameter.setValue(calculator.getValue(SboUtils.MICHAELIS_CONSTANT));
                experimentSet.addCondition(kmParameter, org.mcisb.util.PropertyNames.ERROR, Double.toString(calculator.getError(SboUtils.MICHAELIS_CONSTANT)));
                final LocalParameter hillCoefficientParameter = KineticsUtils.addParameter(kineticLaw, SboUtils.HILL_COEFFICIENT);
                hillCoefficientParameter.setValue(calculator.getValue(SboUtils.HILL_COEFFICIENT));
                experimentSet.addCondition(hillCoefficientParameter, org.mcisb.util.PropertyNames.ERROR, Double.toString(calculator.getError(SboUtils.HILL_COEFFICIENT)));
            }
        }
        calculated = true;
        return modelNameToKineticsCalculator;
    }
}
