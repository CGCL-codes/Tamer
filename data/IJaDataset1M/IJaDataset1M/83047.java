package examples.littlewizard;

import dakside.hacc.dakwizard.DefaultWizardModel;
import dakside.hacc.dakwizard.WizardStep;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * A sample wizard model with a linear path
 * @author takaji
 */
public class MyWizardModel extends DefaultWizardModel {

    private String[] holder = null;

    public MyWizardModel() {
        String wizardString = ResourceBundle.getBundle("examples.littlewizard.resources.MyWizardModel").getString("intro");
        WizardStep[] s = new WizardStep[] { new StaticTextStep(this, null, wizardString), new InputStep(this, "Name"), new InputStep(this, "Address"), new InputStep(this, "Birthday"), new InputStep(this, "Note") };
        initPath(s);
        this.holder = new String[this.linearPath.size()];
    }

    void keyin(String msg) {
        this.holder[currentStep] = msg;
    }

    /**
     * @return the holder
     */
    public String[] getHolder() {
        return Arrays.copyOf(holder, holder.length);
    }
}
