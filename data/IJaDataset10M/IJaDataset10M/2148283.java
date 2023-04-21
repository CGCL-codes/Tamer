package com.sri.emo.wizard;

/**
 * Monitor that often delegates to logging packages, but can be extended
 * for more useful monitoring.
 *
 * @author Michael Rimov
 */
public interface WizardMonitor {

    /**
     * Debugging event for when a page is fired.
     *
     * @param src the source of the event.
     */
    void onEnterPage(WizardPage src);

    /**
     * Fired when forward id pressed on a wizard page.
     *
     * @param src the source page.
     */
    void onForward(WizardPage src);

    /**
     * Fired when back id is pressed on a wizard page.
     *
     * @param src the page source.
     */
    void onBack(WizardPage src);

    /**
     * Fired when the finish button is pressed on a wizard page.
     *
     * @param src the page source.
     */
    void onFinish(WizardPage src);

    /**
     * Fired when the cancel button is pressed on a wizard page.
     *
     * @param src the source of the event.
     */
    void onCancel(WizardPage src);

    /**
     * Fired when an error occurs while processing the wizard page.
     *
     * @param src   the source of the event.
     * @param error the Exeception.
     */
    void onError(WizardPage src, Throwable error);
}
