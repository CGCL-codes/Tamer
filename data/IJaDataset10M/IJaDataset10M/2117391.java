package resultactor.auto;

import org.junit.Test;
import pcgen.cdom.base.ChooseResultActor;
import pcgen.core.ShieldProf;
import plugin.lsttokens.auto.ShieldProfToken;
import resultactor.testsupport.AbstractResultActorTest;

public class ShieldProfTokenTest extends AbstractResultActorTest<ShieldProf> {

    static ShieldProfToken cra = new ShieldProfToken();

    @Test
    public void testEmpty() {
    }

    @Override
    public ChooseResultActor getActor() {
        return cra;
    }

    @Override
    public Class<ShieldProf> getCDOMClass() {
        return ShieldProf.class;
    }

    @Override
    public boolean isGranted() {
        return false;
    }
}
