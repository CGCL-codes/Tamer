package plugin.lsttokens.editcontext.pcclass;

import pcgen.core.PCClass;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractStringIntegrationTestCase;
import plugin.lsttokens.pcclass.AbbToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class AbbIntegrationTest extends AbstractStringIntegrationTestCase<PCClass> {

    static AbbToken token = new AbbToken();

    static CDOMTokenLoader<PCClass> loader = new CDOMTokenLoader<PCClass>(PCClass.class);

    @Override
    public Class<PCClass> getCDOMClass() {
        return PCClass.class;
    }

    @Override
    public CDOMLoader<PCClass> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<PCClass> getToken() {
        return token;
    }

    @Override
    public boolean isClearLegal() {
        return false;
    }
}
