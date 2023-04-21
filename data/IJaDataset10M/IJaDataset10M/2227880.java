package plugin.lsttokens.subclass;

import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.SubClass;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractIntegerTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class ProhibitcostTokenTest extends AbstractIntegerTokenTestCase<SubClass> {

    static ProhibitcostToken token = new ProhibitcostToken();

    static CDOMTokenLoader<SubClass> loader = new CDOMTokenLoader<SubClass>(SubClass.class);

    @Override
    public Class<SubClass> getCDOMClass() {
        return SubClass.class;
    }

    @Override
    public CDOMLoader<SubClass> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<SubClass> getToken() {
        return token;
    }

    @Override
    public IntegerKey getIntegerKey() {
        return IntegerKey.PROHIBIT_COST;
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
}
