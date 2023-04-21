package plugin.lsttokens.editcontext.equipment;

import pcgen.core.Equipment;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegerIntegrationTestCase;
import plugin.lsttokens.equipment.AccheckToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class AccheckIntegrationTest extends AbstractIntegerIntegrationTestCase<Equipment> {

    static AccheckToken token = new AccheckToken();

    static CDOMTokenLoader<Equipment> loader = new CDOMTokenLoader<Equipment>(Equipment.class);

    @Override
    public Class<Equipment> getCDOMClass() {
        return Equipment.class;
    }

    @Override
    public CDOMLoader<Equipment> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<Equipment> getToken() {
        return token;
    }

    @Override
    public boolean isNegativeAllowed() {
        return true;
    }

    @Override
    public boolean isZeroAllowed() {
        return true;
    }

    @Override
    public boolean isPositiveAllowed() {
        return false;
    }

    @Override
    public boolean doesOverwrite() {
        return true;
    }
}
