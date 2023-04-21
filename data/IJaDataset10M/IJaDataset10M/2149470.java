package uk.ac.ebi.intact.psimitab.mock;

import psidev.psi.mi.tab.mock.PsimiTabMockBuilder;
import psidev.psi.mi.tab.model.Interactor;
import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.intact.psimitab.model.ExtendedInteractor;
import uk.ac.ebi.intact.psimitab.IntactBinaryInteraction;

/**
 * Mock builder for IntAct MITAB interactions.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.2
 */
public class IntactPsimiTabMockBuilder extends PsimiTabMockBuilder {

    public IntactPsimiTabMockBuilder() {
    }

    @Override
    protected Interactor buildInteractor() {
        return new ExtendedInteractor();
    }

    @Override
    protected BinaryInteraction buildInteraction(Interactor a, Interactor b) {
        return new IntactBinaryInteraction((ExtendedInteractor) a, (ExtendedInteractor) b);
    }
}
