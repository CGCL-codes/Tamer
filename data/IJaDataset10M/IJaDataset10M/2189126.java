package plugin.lsttokens.spell;

import org.junit.Test;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.spell.Spell;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTypeSafeListTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class DurationTokenTest extends AbstractTypeSafeListTestCase<Spell> {

    static DurationToken token = new DurationToken();

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
        return ListKey.DURATION;
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

    @Test
    public void testGoodParentheses() throws PersistenceLayerException {
        assertTrue(parse("(first)"));
    }

    @Test
    public void testBadParentheses() throws PersistenceLayerException {
        assertFalse("Missing end paren should have been flagged.", parse("(first"));
        assertFalse("Missing start paren should have been flagged.", parse("first)"));
        assertFalse("Missing start paren should have been flagged.", parse("(fir)st)"));
        assertFalse("Out of order parens should have been flagged.", parse(")(fir(st)"));
    }
}
