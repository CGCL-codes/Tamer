package uk.ac.ebi.intact.sanity.rules.feature;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import uk.ac.ebi.intact.mocks.InstitutionMock;
import uk.ac.ebi.intact.mocks.components.Q9QXS6ComponentMock;
import uk.ac.ebi.intact.mocks.cvFeatureType.MutationDecreasingMock;
import uk.ac.ebi.intact.mocks.experiments.ButkevitchMock;
import uk.ac.ebi.intact.mocks.interactions.Cja1Dbn1Mock;
import uk.ac.ebi.intact.model.Component;
import uk.ac.ebi.intact.model.Feature;
import uk.ac.ebi.intact.model.Interaction;
import uk.ac.ebi.intact.model.Range;
import uk.ac.ebi.intact.sanity.commons.rules.GeneralMessage;
import java.util.Collection;

/**
 * MutationAnalysisFeature Tester.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 * @since 2.0
 */
public class MutationAnalysisFeatureTest {

    @Test
    public void check() throws Exception {
        Interaction interaction = Cja1Dbn1Mock.getMock(ButkevitchMock.getMock());
        Component component = Q9QXS6ComponentMock.getMock(interaction);
        Feature feature = new Feature(InstitutionMock.getMock(), "feature", component, MutationDecreasingMock.getMock());
        Range range = new Range(InstitutionMock.getMock(), 1, 1, 1, 1, "");
        feature.addRange(range);
        MutationAnalysisFeature rule = new MutationAnalysisFeature();
        Collection<GeneralMessage> messages = rule.check(feature);
        assertEquals(0, messages.size());
    }
}
