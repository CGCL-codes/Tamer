package plugin.lsttokens.choose;

import org.junit.Test;
import pcgen.cdom.base.CDOMObject;
import pcgen.core.Race;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.QualifierToken;
import plugin.lsttokens.ChooseLst;
import plugin.lsttokens.testsupport.AbstractChooseTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.qualifier.race.PCToken;

public class RaceTokenTest extends AbstractChooseTokenTestCase<CDOMObject, Race> {

    static ChooseLst token = new ChooseLst();

    static RaceToken subtoken = new RaceToken();

    static CDOMTokenLoader<CDOMObject> loader = new CDOMTokenLoader<CDOMObject>(CDOMObject.class);

    @Override
    public Class<Race> getCDOMClass() {
        return Race.class;
    }

    @Override
    public CDOMLoader<CDOMObject> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<CDOMObject> getToken() {
        return token;
    }

    @Override
    public CDOMSecondaryToken<?> getSubToken() {
        return subtoken;
    }

    @Override
    public Class<Race> getTargetClass() {
        return Race.class;
    }

    @Test
    public void testEmpty() {
    }

    @Override
    protected boolean allowsQualifier() {
        return true;
    }

    @Override
    protected String getChoiceTitle() {
        return subtoken.getDefaultTitle();
    }

    @Override
    protected QualifierToken<Race> getPCQualifier() {
        return new PCToken();
    }

    @Override
    protected boolean isTypeLegal() {
        return true;
    }

    @Override
    protected boolean isAllLegal() {
        return true;
    }
}
