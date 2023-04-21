package plugin.lsttokens.equipment;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.Equipment;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractStringTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class FumbleRangeTokenTest extends AbstractStringTokenTestCase<Equipment> {

    static FumblerangeToken token = new FumblerangeToken();

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
    protected boolean isClearLegal() {
        return true;
    }

    @Override
    public StringKey getStringKey() {
        return StringKey.FUMBLE_RANGE;
    }
}
