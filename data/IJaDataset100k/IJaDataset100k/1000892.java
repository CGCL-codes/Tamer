package de.uni_leipzig.lots.webfrontend.formbeans.page;

import de.uni_leipzig.lots.webfrontend.formbeans.Wizard;

/**
 * A page in a wizard.
 *
 * @author Alexander Kiel
 * @version $Id: Page.java,v 1.6 2007/10/23 06:30:38 mai99bxd Exp $
 */
public interface Page {

    Wizard getWizard();

    void setWizard(Wizard wizard);

    boolean isCommitted();

    void setCommitted(boolean commited);

    boolean isActivated();

    /**
     * Changes the activation status of this page.
     *
     * @param activated
     */
    void setActivated(boolean activated);
}
