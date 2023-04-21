package melting.ionCorrection.mixedNaMgCorrections;

import java.util.logging.Level;
import melting.Environment;
import melting.configuration.OptionManagement;
import melting.correctionMethods.EntropyCorrection;
import melting.ionCorrection.magnesiumCorrections.Tan06MagnesiumCorrection;
import melting.ionCorrection.magnesiumCorrections.Tan07MagnesiumCorrection;
import melting.ionCorrection.sodiumCorrections.Tan06SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.Tan07SodiumCorrection;

/**
 * This class represents the mixed (Na,Mg) correction model tanmix08. It extends EntropyCorrection.
 * 
 * Richard Owczarzy, Bernardo G Moreira, Yong You, Mark A 
 * Behlke, Joseph A walder, "Predicting stability of DNA duplexes in solutions
 * containing magnesium and monovalent cations", 2008, Biochemistry, 47, 5336-5353.
*/
public class Tan07MixedNaMgCorrection extends EntropyCorrection {

    /**
	 * String entropyCorrection : formula for the entropy correction
	 */
    protected static String entropyCorrection = "delta S(Na, Mg) = delta S(Na = 1M) - 3.22 x ((duplexLength - 1) x (x1 x g1 + x2 x g2) + g12)";

    /**
	 * String x1Formula : formula representing the fractional contribution of Na+ ions
	 */
    protected static String x1Formula = "x1 = Na / (Na + (8.1 - 32.4 / duplexLength) x (5.2 - ln(Na)) x Mg)";

    /**
	 * String x2Formula : formula representing the fractional contribution of Mg2+ ions
	 */
    protected static String x2Formula = "x2 = 1 - x1";

    /**
	 * String gFormula : function associated with the electrostatic folding free energy per base stack.
	 */
    protected static String gFormula = "g12 = -0.6 x x1 x x2 x ln(Na) x ln((1 / x1 - 1) x Na) / duplexLength";

    @Override
    public boolean isApplicable(Environment environment) {
        boolean isApplicable = super.isApplicable(environment);
        if (environment.getMg() == 0) {
            OptionManagement.meltingLogger.log(Level.WARNING, " \n The magnesium concentration must be a positive numeric value.");
            isApplicable = false;
        } else if (environment.getMg() < 0.1 && environment.getMg() > 0.3) {
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The mixed Na/Mg correction of Zhi-Jie Tan et al. (2007)" + "is reliable for magnesium concentrations between 0.1M and 0.3M.");
        }
        if (environment.getNa() < 0 && environment.getNa() > 1) {
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The mixed Na/Mg correction of Zhi-Jie Tan et al. (2007)" + "is reliable for sodium concentrations between 0M and 1M.");
        }
        if (environment.getHybridization().equals("rnarna") == false || environment.getHybridization().equals("dnadna") == false) {
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The magnesium correction of Zhi-Jie Tan et al. (2007)is originally established for " + "RNA or DNA duplexes.");
        }
        return isApplicable;
    }

    @Override
    protected double correctEntropy(Environment environment) {
        OptionManagement.meltingLogger.log(Level.FINE, "\n The magnesium correction from Zhi-Jie Tan et al. (2007) : ");
        OptionManagement.meltingLogger.log(Level.FINE, entropyCorrection);
        double Na = environment.getNa();
        double Mg = environment.getMg() - environment.getDNTP();
        double duplexLength = (double) environment.getSequences().getDuplexLength();
        double x1 = Na / (Na + (8.1 - 32.4 / duplexLength) * (5.2 - Math.log(Na)) * Mg);
        double x2 = 1 - x1;
        double g12 = -0.6 * x1 * x2 * Math.log(Na) * Math.log((1 / x1 - 1) * Na) / duplexLength;
        double g1 = 0;
        double g2 = 0;
        if (environment.getHybridization().equals("rnarna")) {
            g1 = Tan07SodiumCorrection.calculateFreeEnergyPerBaseStack(environment);
            g2 = Tan07MagnesiumCorrection.computeFreeEnergyPerBaseStack(environment);
        } else if (environment.getHybridization().equals("dnadna")) {
            g1 = Tan06SodiumCorrection.calculateFreeEnergyPerBaseStack(environment);
            g2 = Tan06MagnesiumCorrection.computeFreeEnergyPerBaseStack(environment);
        }
        double entropy = -3.22 * ((duplexLength - 1) * (x1 * g1 + x2 * g2) + g12);
        OptionManagement.meltingLogger.log(Level.FINE, "and where : ");
        OptionManagement.meltingLogger.log(Level.FINE, gFormula);
        OptionManagement.meltingLogger.log(Level.FINE, x1Formula);
        OptionManagement.meltingLogger.log(Level.FINE, x2Formula);
        return entropy;
    }
}
