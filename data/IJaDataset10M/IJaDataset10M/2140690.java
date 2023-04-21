package plugin.lsttokens.choose.subtoken;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.PrimitiveChoiceSet;
import pcgen.cdom.inst.CDOMLanguage;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.ChoiceSetToken;

public class LanguageToken extends AbstractToken implements ChoiceSetToken<CDOMObject> {

    private static final Class<CDOMLanguage> LANGUAGE_CLASS = CDOMLanguage.class;

    @Override
    public String getTokenName() {
        return "LANGUAGE";
    }

    public PrimitiveChoiceSet<?> parse(LoadContext context, CDOMObject obj, String value) throws PersistenceLayerException {
        return context.getChoiceSet(LANGUAGE_CLASS, value);
    }

    public Class<CDOMObject> getTokenClass() {
        return CDOMObject.class;
    }
}
