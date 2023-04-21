package plugin.lsttokens.deprecated;

import pcgen.cdom.base.CDOMObject;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ComplexParseResult;
import pcgen.rules.persistence.token.ErrorParsingWrapper;
import pcgen.rules.persistence.token.ParseResult;

public class SAListToken extends ErrorParsingWrapper<CDOMObject> implements CDOMSecondaryToken<CDOMObject> {

    public String getTokenName() {
        return "SALIST";
    }

    public String getParentToken() {
        return "CHOOSE";
    }

    public ParseResult parseToken(LoadContext context, CDOMObject obj, String value) {
        ComplexParseResult cpr = new ComplexParseResult();
        cpr.addWarningMessage("CHOOSE:SALIST has been deprecated.  " + "If you are looking for a replacement function, " + "please contact the PCGen team for support");
        return cpr;
    }

    public String[] unparse(LoadContext context, CDOMObject cdo) {
        return null;
    }

    public Class<CDOMObject> getTokenClass() {
        return CDOMObject.class;
    }
}
