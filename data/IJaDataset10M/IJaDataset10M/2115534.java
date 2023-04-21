package spellcast.questions;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.easymock.MockControl;
import spellcast.beings.IWizard;

/**
 * SelectTargetQuestion Test Case.
 *
 * @author Barrie Treloar
 * @version $Revision: 120 $
 */
public class SelectTargetQuestionTest extends TestCase {

    public void testSelectTarget() throws Exception {
        MockControl theWizardMockControl = MockControl.createControl(IWizard.class);
        IWizard theWizard = (IWizard) theWizardMockControl.getMock();
        theWizardMockControl.replay();
        MockControl theOpponentWizardMockControl = MockControl.createControl(IWizard.class);
        IWizard theOpponentWizard = (IWizard) theOpponentWizardMockControl.getMock();
        theOpponentWizardMockControl.replay();
        MockControl theTargetableMockControl = MockControl.createControl(IRequiresTarget.class);
        IRequiresTarget theTargetable = (IRequiresTarget) theTargetableMockControl.getMock();
        theTargetable.setTarget(theOpponentWizard);
        theTargetableMockControl.replay();
        List<IWizard> targets = new ArrayList<IWizard>();
        targets.add(theWizard);
        targets.add(theOpponentWizard);
        IQuestion OUT = new SelectTargetQuestion(theTargetable, targets);
        assertEquals("question.selectTarget", OUT.getQuestion());
        OUT.setAnswer(theOpponentWizard);
        OUT.execute(null);
        theWizardMockControl.verify();
        theOpponentWizardMockControl.verify();
        theTargetableMockControl.verify();
    }
}
