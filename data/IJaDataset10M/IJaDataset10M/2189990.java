package plugin.lsttokens.spell;

import org.junit.Test;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.spell.Spell;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTypeSafeListTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class VariantsTokenTest extends AbstractTypeSafeListTestCase<Spell> {

    static VariantsToken token = new VariantsToken();

    static CDOMTokenLoader<Spell> loader = new CDOMTokenLoader<Spell>(Spell.class);

    @Override
    public Class<Spell> getCDOMClass() {
        return Spell.class;
    }

    @Override
    public CDOMLoader<Spell> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<Spell> getToken() {
        return token;
    }

    @Override
    public Object getConstant(String string) {
        return string;
    }

    @Override
    public char getJoinCharacter() {
        return '|';
    }

    @Override
    public ListKey<?> getListKey() {
        return ListKey.VARIANTS;
    }

    @Test
    public void dummyTest() {
    }

    @Override
    public boolean isClearDotLegal() {
        return false;
    }

    @Override
    public boolean isClearLegal() {
        return true;
    }

    @Override
    protected boolean requiresPreconstruction() {
        return false;
    }
}
