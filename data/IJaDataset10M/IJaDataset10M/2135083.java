package org.jbudget.gui.budget.wizard;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JLabel;
import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author petrov
 */
public class IntroductionPage extends WizardPage {

    public IntroductionPage() {
        String message = new String("Error: Resource Not Found.");
        java.net.URL url = ClassLoader.getSystemResource("docs/budget_wizard_intro.html");
        if (url != null) {
            try {
                StringBuffer buf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                while (reader.ready()) {
                    buf.append(reader.readLine());
                }
                message = buf.toString();
            } catch (IOException ex) {
                message = new String("IO Error.");
            }
        }
        JLabel label = new JLabel(message);
        setLayout(new BorderLayout());
        add(label, BorderLayout.NORTH);
    }

    /** Returns the description of the current wizard page. */
    public static final String getDescription() {
        return "Introduction.";
    }
}
