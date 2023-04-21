package tethyspict.wizard.database;

import java.awt.Dimension;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPanelProvider;

public class BaseDatabaseWizardProvider extends WizardPanelProvider {

    public static final String[] steps = { BaseWelcomePage.NAME, BaseNewOrExistingPage.NAME };

    public static final String[] descriptions = { BaseWelcomePage.DESCRIPTION, BaseNewOrExistingPage.DESCRIPTION };

    public static final String title = "Establish connection to database";

    protected BaseDatabaseWizardProvider() {
        super(title, steps, descriptions);
    }

    @Override
    protected JComponent createPanel(WizardController controller, String id, Map settings) {
        if (id.equals(BaseWelcomePage.NAME)) {
            return new BaseWelcomePage();
        } else if (id.equals(BaseNewOrExistingPage.NAME)) {
            return new BaseNewOrExistingPage();
        } else {
            throw new Error("Unknown ID " + id);
        }
    }
}
