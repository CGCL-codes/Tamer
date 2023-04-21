package plugin.lsttokens.template;

import org.junit.Test;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.PCTemplate;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractYesNoTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class RemovableTokenTest extends AbstractYesNoTokenTestCase<PCTemplate> {

    static RemovableToken token = new RemovableToken();

    static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>(PCTemplate.class);

    @Override
    public Class<PCTemplate> getCDOMClass() {
        return PCTemplate.class;
    }

    @Override
    public CDOMLoader<PCTemplate> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<PCTemplate> getToken() {
        return token;
    }

    @Override
    public ObjectKey<Boolean> getObjectKey() {
        return ObjectKey.REMOVABLE;
    }

    @Test
    public void dummyTest() {
    }
}
