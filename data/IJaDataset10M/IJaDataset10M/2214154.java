package plugin.lsttokens.race;

import org.junit.Test;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.Race;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractIntegerTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class LegsTokenTest extends AbstractIntegerTokenTestCase<Race> {

    static LegsToken token = new LegsToken();

    static CDOMTokenLoader<Race> loader = new CDOMTokenLoader<Race>(Race.class);

    @Override
    public Class<Race> getCDOMClass() {
        return Race.class;
    }

    @Override
    public CDOMLoader<Race> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<Race> getToken() {
        return token;
    }

    @Override
    public IntegerKey getIntegerKey() {
        return IntegerKey.LEGS;
    }

    @Override
    public boolean isNegativeAllowed() {
        return false;
    }

    @Override
    public boolean isZeroAllowed() {
        return true;
    }

    @Override
    public boolean isPositiveAllowed() {
        return true;
    }

    @Test
    public void dummyTest() {
    }
}
