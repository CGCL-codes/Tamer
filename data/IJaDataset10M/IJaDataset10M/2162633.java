package plugin.lsttokens.editcontext.equipment;

import pcgen.core.Equipment;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegerIntegrationTestCase;
import plugin.lsttokens.equipment.ReachToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class ReachIntegrationTest extends AbstractIntegerIntegrationTestCase<Equipment> {

    static ReachToken token = new ReachToken();

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
