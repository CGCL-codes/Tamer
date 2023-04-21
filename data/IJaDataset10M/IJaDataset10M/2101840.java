package org.tn5250j.event;

/**
 * The listener interface for those interested in
 * receiving Wizard events.
 */
public interface WizardListener {

    /**
    * Invoked <i>before</i> advancing to the next page.
    * Calling <code>e.setAllowChange(false)</code> will prevent the
    * next page from being advanced too.
    * Check the <code>e.isLastPage()</code> to see if you are on the
    * last page.
    */
    public void nextBegin(WizardEvent e);

    /**
    * Invoked after advancing to the next page.
    */
    public void nextComplete(WizardEvent e);

    /**
    * Invoked <i>before</i> advancing to the previous page.
    * Calling <code>e.setAllowChange(false)</code> will prevent the
    * previous page from being advanced too.
    */
    public void previousBegin(WizardEvent e);

    /**
    * Invoked after advancing to the previous page.
    */
    public void previousComplete(WizardEvent e);

    /**
    * Invoked if a "finish" action is triggered.
    */
    public void finished(WizardEvent e);

    /**
    * Invoked if the Cancel action is triggered.
    */
    public void canceled(WizardEvent e);

    /**
    * Invoked if the Help action is triggered.
    */
    public void help(WizardEvent e);
}
