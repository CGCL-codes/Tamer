package plugin.lsttokens.editcontext.spell;

import pcgen.core.spell.Spell;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractTypeSafeListIntegrationTestCase;
import plugin.lsttokens.spell.CompsToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class CompsIntegrationTest extends AbstractTypeSafeListIntegrationTestCase<Spell> {

    static CompsToken token = new CompsToken();

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
    public boolean isClearLegal() {
        return true;
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
    public boolean isClearDotLegal() {
        return false;
    }

    @Override
    protected boolean requiresPreconstruction() {
        return false;
    }
}
