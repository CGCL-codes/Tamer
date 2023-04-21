package melting.approximativeMethods;

import java.util.HashMap;
import java.util.logging.Level;
import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.exceptions.NoExistingMethodException;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.methodInterfaces.SodiumEquivalentMethod;

/**
 * This class represents the approximative model. It implements the MelitngComputationMethod Interface.
 */
public class ApproximativeMode implements MeltingComputationMethod {

    /**
	 * environment containing the sequences, ion and agent concentrations and the options (default options and
	 * options entered by the user)
	 */
    protected Environment environment;

    /**
	 * RegisterMethods register : registers all the pattern computation methods implemented by Melting
	 */
    protected RegisterMethods register = new RegisterMethods();

    public ThermoResult computesThermodynamics() {
        OptionManagement.meltingLogger.log(Level.FINE, "\n Approximative method : ");
        return environment.getResult();
    }

    public boolean isApplicable() {
        boolean isApplicable = true;
        if (environment.getSequences().computesPercentMismatching() != 0) {
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The approximative mode formulas" + "cannot properly account for the presence of mismatches" + " and unpaired nucleotides.");
        }
        if (Integer.parseInt(environment.getOptions().get(OptionManagement.threshold)) >= environment.getSequences().getDuplexLength()) {
            if (environment.getOptions().get(OptionManagement.globalMethod).equals("def")) {
                isApplicable = false;
            }
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The approximative equations " + "were originally established for long DNA duplexes. (length superior to " + environment.getOptions().get(OptionManagement.threshold) + ").");
        }
        return isApplicable;
    }

    public void setUpVariables(HashMap<String, String> options) {
        this.environment = new Environment(options);
        if (isNaEqPossible()) {
            if (environment.getMg() > 0 || environment.getK() > 0 || environment.getTris() > 0) {
                SodiumEquivalentMethod method = this.register.getNaEqMethod(options);
                if (method != null) {
                    environment.setNa(method.computeSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(), environment.getDNTP()));
                } else {
                    throw new NoExistingMethodException("\n There are other ions than Na+ in the solution and no ion correction method is avalaible for this type of hybridization.");
                }
            }
        }
    }

    public RegisterMethods getRegister() {
        return register;
    }

    /**
	 * to check if a sodium equivalence is necessary. (when other ions are present)
	 * @return true if sodium equivalence is necessary.
	 */
    protected boolean isNaEqPossible() {
        return true;
    }
}
