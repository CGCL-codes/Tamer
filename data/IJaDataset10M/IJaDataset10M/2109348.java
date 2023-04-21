package plugin.lsttokens.choose.subtoken;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.PrimitiveChoiceSet;
import pcgen.cdom.inst.CDOMAbility;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.ChoiceSetToken;

public class AbilityToken extends AbstractToken implements ChoiceSetToken<CDOMObject> {

    private static final Class<CDOMAbility> ABILITY_CLASS = CDOMAbility.class;

    @Override
    public String getTokenName() {
        return "ABILITY";
    }

    public PrimitiveChoiceSet<?> parse(LoadContext context, CDOMObject obj, String value) throws PersistenceLayerException {
        return context.getChoiceSet(ABILITY_CLASS, value);
    }

    public Class<CDOMObject> getTokenClass() {
        return CDOMObject.class;
    }
}
