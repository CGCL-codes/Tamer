package maltcms.ui.wizard;

import java.util.Map;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;

/**
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class Main implements WizardPage.WizardResultProducer {

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String[] args) {
        Main m = new Main();
        Wizard w = WizardPage.createWizard(new Class[] { FileInputOutputPane.class, AnchorDefinitionPane.class, PreprocessingPane.class, AlignmentPane.class, VisualizationPane.class }, m);
        Map gatheredSettings = (Map) WizardDisplayer.showWizard(w);
    }

    public Object finish(Map arg0) throws WizardException {
        for (Object key : arg0.keySet()) {
            System.out.println(key + " = " + arg0.get(key));
        }
        return arg0;
    }

    public boolean cancel(Map arg0) {
        return true;
    }
}
