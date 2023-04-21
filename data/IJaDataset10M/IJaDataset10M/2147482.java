package plugin.lsttokens.editcontext.pcclass;

import java.net.URISyntaxException;
import pcgen.core.PCClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegerIntegrationTestCase;
import plugin.lsttokens.pcclass.KnownspellsfromspecialtyToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class KnownSpellsFromSpecialtyIntegrationTest extends AbstractIntegerIntegrationTestCase<PCClass> {

    static KnownspellsfromspecialtyToken token = new KnownspellsfromspecialtyToken();

    static CDOMTokenLoader<PCClass> loader = new CDOMTokenLoader<PCClass>(PCClass.class);

    @Override
    public void setUp() throws PersistenceLayerException, URISyntaxException {
        super.setUp();
        prefix = "CLASS:";
    }

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
    public boolean isNegativeAllowed() {
        return false;
    }

    @Override
    public boolean isZeroAllowed() {
        return false;
    }

    @Override
    public boolean isPositiveAllowed() {
        return true;
    }

    @Override
    public boolean doesOverwrite() {
        return true;
    }
}
