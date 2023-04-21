package bouttime.wizard.fileinput.text;

import bouttime.dao.Dao;
import bouttime.fileinput.TextFileInput;
import bouttime.fileinput.TextFileInputConfig;
import bouttime.wizard.fileinput.common.*;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;

/**
 * A class to implement a wizard for receiving Wrestler data from a text file.
 */
public class TextInputWizard {

    private Dao dao;

    public TextInputWizard(Dao dao) {
        this.dao = dao;
    }

    /**
     * Run the wizard.
     */
    public void runWizard() {
        TextFileInputConfig config = this.dao.getTextFileInputConfig();
        WizardPage[] pages = new WizardPage[] { new ColumnSelector(config), new FieldSeparatorSelector(config), new FileSelector() };
        TextFileInput fi = new TextFileInput();
        FileInputWizardResultProducer rp = new FileInputWizardResultProducer(fi, dao);
        Wizard wizard = WizardPage.createWizard(pages, rp);
        WizardDisplayer.showWizard(wizard);
    }
}
