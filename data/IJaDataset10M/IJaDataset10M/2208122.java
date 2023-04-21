package diet.parameters.ui;

import diet.parameters.ExperimentSettings;

/**
 *
 * @author user
 */
public class ParameterUITesting {

    /** Creates a new instance of ParameterUITesting */
    public ParameterUITesting() {
        ExperimentSettings em = new ExperimentSettings();
        em.populateWithTestData();
        JExperimentParametersFrame jepf = new JExperimentParametersFrame(em);
    }

    public static void main(String[] args) {
        ParameterUITesting pi = new ParameterUITesting();
    }
}
