package com.bbn.vessel.author.addStrategyWizard;

import java.util.List;
import com.bbn.vessel.author.util.wizard.Choice;
import com.bbn.vessel.author.util.wizard.WizardPage;

/**
 * Choice items that add more choices can implement this interface and their
 * products will be incorporated in the multiple choice options.
 *
 * @author RTomlinson
 */
public interface AdditionDetail {

    /**
     * @return the Choice corresponding to this addition
     */
    Choice getChoice();

    /**
     * Add pages to the wizard to carry out the addition
     *
     * the pages will be added after the current page
     */
    void addPages();

    /**
     * get the pages needed wizard to carry out the addition
     *
     * most of the time you will want to call addPages instead
     * @return the pages needed wizard to carry out the addition
     */
    List<WizardPage> getPages();
}
