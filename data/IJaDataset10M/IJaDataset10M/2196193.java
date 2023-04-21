package plugin.lsttokens.choose;

import org.junit.Test;
import pcgen.cdom.base.CDOMObject;
import pcgen.core.Equipment;
import pcgen.core.WeaponProf;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.QualifierToken;
import plugin.lsttokens.ChooseLst;
import plugin.lsttokens.testsupport.AbstractChooseTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class EquipmentTokenTest extends AbstractChooseTokenTestCase<CDOMObject, Equipment> {

    static ChooseLst token = new ChooseLst();

    static EquipmentToken subtoken = new EquipmentToken();

    static CDOMTokenLoader<CDOMObject> loader = new CDOMTokenLoader<CDOMObject>(CDOMObject.class);

    @Override
    public Class<Equipment> getCDOMClass() {
        return Equipment.class;
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
    public Class<Equipment> getTargetClass() {
        return Equipment.class;
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
    protected QualifierToken<WeaponProf> getPCQualifier() {
        return null;
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
