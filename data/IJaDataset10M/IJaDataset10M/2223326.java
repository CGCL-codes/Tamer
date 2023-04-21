package de.sopra.view;

import de.sopra.controller.LocaleHandler;
import de.sopra.view.facade.FailsafeErrorInterface;
import javax.swing.JOptionPane;

/**
 * An Implementation of {@link FailsafeErrorInterface} for failsafe error
 * output.
 *
 * @author Falko K&ouml;tter
 */
public class FailSafeErrorNotifier implements FailsafeErrorInterface {

    /**
     * {@inheritDoc}
     */
    public void errorDisplay(String error) {
        if (error == null) {
            System.err.println(FATAL_ERROR_MESSAGE + '\n' + UNKNOWN);
            return;
        }
        try {
            error = LocaleHandler.getInstance().getMessage(error);
            String title = LocaleHandler.getInstance().getMessage("FATAL_ERROR_TITLE");
            try {
                JOptionPane.showMessageDialog(null, error, title, JOptionPane.ERROR_MESSAGE);
            } catch (Exception f) {
                System.err.println(error);
            }
        } catch (RuntimeException f) {
            System.err.println(FATAL_ERROR_MESSAGE + '\n' + error);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void errorDisplay(Throwable e) {
        if (e == null) {
            System.err.println(FATAL_ERROR_MESSAGE + '\n' + UNKNOWN);
            return;
        }
        try {
            String error = e.getLocalizedMessage();
            e.printStackTrace();
            if (!(error == null || error.isEmpty())) {
                String title = LocaleHandler.getInstance().getMessage("FATAL_ERROR_TITLE");
                try {
                    JOptionPane.showMessageDialog(null, error, title, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception f) {
                    System.err.println(title);
                    System.err.println(error);
                }
            }
        } catch (Exception f) {
            f.printStackTrace();
            System.err.println(FATAL_ERROR_MESSAGE + '\n' + e);
        }
    }
}
