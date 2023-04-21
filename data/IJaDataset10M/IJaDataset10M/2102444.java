package resultactor.auto;

import org.junit.Test;
import pcgen.cdom.base.ChooseResultActor;
import pcgen.core.Language;
import plugin.lsttokens.auto.LangToken;
import resultactor.testsupport.AbstractResultActorTest;

public class LangTokenTest extends AbstractResultActorTest<Language> {

    static LangToken cra = new LangToken();

    @Test
    public void testEmpty() {
    }

    @Override
    public ChooseResultActor getActor() {
        return cra;
    }

    @Override
    public Class<Language> getCDOMClass() {
        return Language.class;
    }

    @Override
    public boolean isGranted() {
        return false;
    }
}
