package plugin.lsttokens.deprecated;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.ChooseInformation;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ComplexParseResult;
import pcgen.rules.persistence.token.ErrorParsingWrapper;
import pcgen.rules.persistence.token.ParseResult;
import pcgen.rules.persistence.token.PostDeferredToken;
import pcgen.util.Logging;

public class FeatEqToken extends ErrorParsingWrapper<CDOMObject> implements CDOMSecondaryToken<CDOMObject>, PostDeferredToken<CDOMObject> {

    public String getTokenName() {
        return "FEATEQ";
    }

    public String getParentToken() {
        return "CHOOSE";
    }

    public ParseResult parseToken(LoadContext context, CDOMObject obj, String value) {
        if (value == null) {
            return new ParseResult.Fail("CHOOSE:" + getTokenName() + " requires additional arguments");
        }
        if (value.indexOf(',') != -1) {
            return new ParseResult.Fail("CHOOSE:" + getTokenName() + " arguments may not contain , : " + value);
        }
        if (value.indexOf('[') != -1) {
            return new ParseResult.Fail("CHOOSE:" + getTokenName() + " arguments may not contain [] : " + value);
        }
        if (value.charAt(0) == '|') {
            return new ParseResult.Fail("CHOOSE:" + getTokenName() + " arguments may not start with | : " + value);
        }
        if (value.charAt(value.length() - 1) == '|') {
            return new ParseResult.Fail("CHOOSE:" + getTokenName() + " arguments may not end with | : " + value);
        }
        if (value.indexOf("||") != -1) {
            return new ParseResult.Fail("CHOOSE:" + getTokenName() + " arguments uses double separator || : " + value);
        }
        Logging.deprecationPrint("CHOOSE:FEAT= has been deprecated, " + "please use a CHOOSE of the " + "appropriate type with the FEAT= primitive, " + "e.g. CHOOSE:WEAPONPROFICIENCY|FEAT=Weapon Focus");
        ParseResult pr = ParseResult.SUCCESS;
        if (value.indexOf('|') != -1) {
            ComplexParseResult cpr = new ComplexParseResult();
            cpr.addWarningMessage("CHOOSE:" + getTokenName() + " will ignore arguments: " + value.substring(value.indexOf('|') + 1));
            pr = cpr;
        }
        context.obj.put(obj, ObjectKey.FEATEQ_STRING, context.ref.getCDOMReference(Ability.class, AbilityCategory.FEAT, value));
        return pr;
    }

    public String[] unparse(LoadContext context, CDOMObject cdo) {
        return null;
    }

    public Class<CDOMObject> getTokenClass() {
        return CDOMObject.class;
    }

    public Class<CDOMObject> getDeferredTokenClass() {
        return CDOMObject.class;
    }

    public boolean process(LoadContext context, CDOMObject obj) {
        CDOMSingleRef<Ability> ref = obj.get(ObjectKey.FEATEQ_STRING);
        if (ref != null) {
            Ability ab = ref.resolvesTo();
            if (ab.get(ObjectKey.FEATEQ_STRING) != null) {
                process(context, ab);
            }
            ChooseInformation<?> info = ab.get(ObjectKey.CHOOSE_INFO);
            if (info == null) {
                Logging.errorPrint("Feat " + ref.getLSTformat() + " was referred to in " + obj.getClass().getSimpleName() + " " + obj.getKeyName() + " but it was not a FEAT with CHOOSE");
                return false;
            }
            context.unconditionallyProcess(obj, "CHOOSE", info.getName() + "|FEAT=" + ref.getLSTformat());
            obj.remove(ObjectKey.FEATEQ_STRING);
        }
        return true;
    }
}
