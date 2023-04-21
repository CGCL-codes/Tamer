package com.sri.emo.wizard.creation;

import com.sri.common.dbobj.ObjectNotFoundException;
import com.sri.emo.wizard.Wizard;
import com.sri.emo.wizard.WizardException;
import com.sri.emo.wizard.WizardMementoConverter;
import com.sri.emo.wizard.creation.model.CreationBeans;
import java.io.Serializable;

/**
 * Writes a CompletionWizard to and from a compact form. (A memento).  This
 * memento is then saved in state.  See the WizardMementoConverter interface
 * for more information.
 *
 * @author Michael Rimov
 * @version 1.0
 */
public class CreationMementoConverter extends CreationBuilder implements WizardMementoConverter {

    private static final long serialVersionUID = 1L;

    public CreationMementoConverter(CreationRepository completionRepository) {
        super(completionRepository);
    }

    /**
     * Re-fleshes out the wizard into usable forms.
     *
     * @param previouslyExternalized Serializable
     * @return Wizard
     * @throws WizardException
     */
    public Wizard fromMemento(final Serializable previouslyExternalized) throws WizardException {
        EmoCreationWizard cwiz = (EmoCreationWizard) previouslyExternalized;
        CreationBeans beans = null;
        try {
            beans = repository.findById(cwiz.getId());
        } catch (ObjectNotFoundException e) {
            cwiz.setCompletionBeans(beans);
            throw new WizardException("Could not find underlying wizard of id: " + cwiz.getId().toString(), e);
        }
        cwiz.setCompletionBeans(beans);
        return cwiz;
    }

    /**
     * Breaks the wizard down into minimal component state parts that can be
     * stored in the session or client.
     *
     * @param target Wizard the wizard being 'dehydrated'
     * @return Serializable -- an opaque object that should be serialized, stashed
     *         or something, but nothing else.
     * @throws WizardException
     */
    public Serializable toMemento(final Wizard target) throws WizardException {
        EmoCreationWizard cwiz = (EmoCreationWizard) target;
        cwiz.setCompletionBeans(null);
        return cwiz;
    }
}
