package be.vds.jtbdive.core.view.wizards;

import java.util.Map;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

public class FinisherWizardResultProvider implements WizardResultProducer {

    @Override
    public boolean cancel(Map arg0) {
        return true;
    }

    @Override
    public Object finish(Map arg0) throws WizardException {
        return arg0;
    }
}
