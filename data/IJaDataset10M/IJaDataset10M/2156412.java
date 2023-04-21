package plugin.lsttokens;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.PCTemplate;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractGlobalStringTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class TempdescLstTest extends AbstractGlobalStringTokenTestCase {

    static CDOMPrimaryToken<CDOMObject> token = new TempdescLst();

    static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>(PCTemplate.class);

    @Override
    public CDOMLoader<PCTemplate> getLoader() {
        return loader;
    }

    @Override
    public Class<PCTemplate> getCDOMClass() {
        return PCTemplate.class;
    }

    @Override
    public CDOMPrimaryToken<CDOMObject> getToken() {
        return token;
    }

    @Override
    public StringKey getStringKey() {
        return StringKey.TEMP_DESCRIPTION;
    }
}
