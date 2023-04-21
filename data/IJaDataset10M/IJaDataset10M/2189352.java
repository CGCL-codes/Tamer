package melting.patternModels.cricksPair;

import java.util.logging.Level;
import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.MethodNotApplicableException;
import melting.sequences.NucleotidSequences;

/**
 * This class represents the nearest neighbor model sug95. It extends CricksNNMethod.
 * 
 * Sugimoto et al. (1995). Biochemistry 34 : 11211-11216
 */
public class Sugimoto95 extends CricksNNMethod {

    /**
	 * String defaultFileName : default name for the xml file containing the thermodynamic parameters for each Crick's pair
	 */
    public static String defaultFileName = "Sugimoto1995nn.xml";

    @Override
    public boolean isApplicable(Environment environment, int pos1, int pos2) {
        boolean isApplicable = true;
        if (environment.getHybridization().equals("dnarna") == false && environment.getHybridization().equals("rnadna") == false) {
            isApplicable = false;
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The model of Sugimoto et al. (1995)" + "is established for hybrid DNA/RNA sequences.");
        }
        isApplicable = super.isApplicable(environment, pos1, pos2);
        if (environment.isSelfComplementarity()) {
            throw new MethodNotApplicableException("\n The thermodynamic parameters of Sugimoto et al. (1995)" + "are established for hybrid DNA/RNA sequences and they can't be self complementary sequence.");
        }
        return isApplicable;
    }

    @Override
    public ThermoResult computeThermodynamics(NucleotidSequences sequences, int pos1, int pos2, ThermoResult result) {
        OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is  from Sugimoto et al (1995).");
        OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
        double enthalpy = result.getEnthalpy();
        double entropy = result.getEntropy();
        Thermodynamics NNValue;
        for (int i = pos1; i <= pos2 - 1; i++) {
            NNValue = this.collector.getNNvalue("d" + sequences.getSequenceNNPair(i), "r" + sequences.getComplementaryNNPair(i));
            OptionManagement.meltingLogger.log(Level.FINE, "d" + sequences.getSequenceNNPair(i) + "/" + "r" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());
            enthalpy += NNValue.getEnthalpy();
            entropy += NNValue.getEntropy();
        }
        result.setEnthalpy(enthalpy);
        result.setEntropy(entropy);
        return result;
    }

    @Override
    public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2) {
        boolean isMissing = false;
        for (int i = pos1; i <= pos2 - 1; i++) {
            if (this.collector.getNNvalue("d" + sequences.getSequenceNNPair(i), "r" + sequences.getComplementaryNNPair(i)) == null) {
                OptionManagement.meltingLogger.log(Level.WARNING, "\n The thermodynamic parameters for d" + sequences.getSequenceNNPair(i) + "/r" + sequences.getComplementaryNNPair(i) + "are missing.");
                isMissing = true;
            }
        }
        return isMissing;
    }

    @Override
    public void initialiseFileName(String methodName) {
        super.initialiseFileName(methodName);
        if (this.fileName == null) {
            this.fileName = defaultFileName;
        }
    }
}
