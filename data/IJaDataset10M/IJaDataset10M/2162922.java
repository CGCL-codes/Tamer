package actor.choose;

import org.junit.Test;
import pcgen.cdom.base.PersistentChoiceActor;
import pcgen.cdom.identifier.SpellSchool;
import plugin.lsttokens.choose.SchoolsToken;
import actor.testsupport.AbstractPersistentChoiceActorTestCase;

public class SchoolsTokenTest extends AbstractPersistentChoiceActorTestCase<SpellSchool> {

    static SchoolsToken pca = new SchoolsToken();

    @Test
    public void testEmpty() {
    }

    @Override
    public PersistentChoiceActor<SpellSchool> getActor() {
        return pca;
    }

    @Override
    protected SpellSchool getObject() {
        return context.ref.constructNowIfNecessary(SpellSchool.class, ITEM_NAME);
    }
}
