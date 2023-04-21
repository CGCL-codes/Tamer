package plugin.lsttokens.campaign;

import org.junit.Test;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Campaign;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTypeSafeListTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class GamemodeTokenTest extends AbstractTypeSafeListTestCase<Campaign> {

    static GamemodeToken token = new GamemodeToken();

    static CDOMTokenLoader<Campaign> loader = new CDOMTokenLoader<Campaign>(Campaign.class);

    @Override
    public Class<Campaign> getCDOMClass() {
        return Campaign.class;
    }

    @Override
    public CDOMLoader<Campaign> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<Campaign> getToken() {
        return token;
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
    public ListKey<?> getListKey() {
        return ListKey.GAME_MODE;
    }

    @Test
    public void dummyTest() {
    }

    @Override
    public boolean isClearDotLegal() {
        return false;
    }

    @Override
    public boolean isClearLegal() {
        return false;
    }

    @Override
    protected boolean requiresPreconstruction() {
        return false;
    }

    @Override
    public void testReplacementInputs() throws PersistenceLayerException {
    }

    @Override
    public void testReplacementInputsTwo() throws PersistenceLayerException {
    }

    @Override
    public void testValidInputMultList() throws PersistenceLayerException {
    }

    @Override
    protected ConsolidationRule getConsolidationRule() {
        return ConsolidationRule.OVERWRITE;
    }
}
