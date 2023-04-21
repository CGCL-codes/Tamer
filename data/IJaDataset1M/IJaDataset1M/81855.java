package tethyspict.wizard.database;

import javax.swing.JLabel;
import org.netbeans.spi.wizard.WizardPage;

public class ExistingTethysChooseDBPage extends WizardPage {

    public static final String NAME = "choosetethys";

    public static final String DESCRIPTION = "Choose existing Tethys";

    public ExistingTethysChooseDBPage() {
        this.build();
    }

    private void build() {
        this.add(new JLabel("TODO: EXISTING TETHYS"));
    }
}
