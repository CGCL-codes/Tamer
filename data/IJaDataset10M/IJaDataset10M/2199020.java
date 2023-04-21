package uk.ac.ebi.intact.application.editor.util;

import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.model.clone.IntactClonerException;
import uk.ac.ebi.intact.model.clone.IntactCloner;
import uk.ac.ebi.intact.context.IntactContext;
import org.joda.time.DateTime;
import java.util.Date;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: EditorIntactCloner.java 12056 2008-09-03 14:02:33Z prem_intact $
 */
public class EditorIntactCloner extends IntactCloner {

    public EditorIntactCloner() {
        setExcludeACs(true);
    }

    @Override
    public BioSource cloneBioSource(BioSource bioSource) throws IntactClonerException {
        return bioSource;
    }

    @Override
    public Institution cloneInstitution(Institution institution) throws IntactClonerException {
        return institution;
    }

    @Override
    protected AnnotatedObject cloneAnnotatedObjectCommon(AnnotatedObject<?, ?> ao, AnnotatedObject clone) throws IntactClonerException {
        if (clone == null) {
            return null;
        }
        if (clone instanceof Interaction) {
            Interaction interaction = (Interaction) clone;
            interaction.getExperiments().clear();
        } else if (clone instanceof Experiment) {
            Experiment experiment = (Experiment) clone;
            experiment.getInteractions().clear();
        }
        if (ao == clone) {
            return ao;
        }
        return super.cloneAnnotatedObjectCommon(ao, clone);
    }
}
