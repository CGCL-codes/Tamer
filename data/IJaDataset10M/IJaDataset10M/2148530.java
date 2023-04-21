package org.docflower.xml.typeinfo.restrictioncheckers;

import java.util.*;
import org.apache.ws.commons.schema.*;
import org.docflower.util.BaseStatus;
import org.docflower.util.IMessageLocalizer;
import org.docflower.util.XmlMessages;
import org.docflower.xml.*;

public class EnumerationRestrictionChecker extends BaseRestrictionChecker {

    private Set<String> allowedValues = new HashSet<String>();

    @Override
    public void init(XmlSchemaFacet facet, String baseSimpleTypeName) {
        XmlSchemaEnumerationFacet ef = (XmlSchemaEnumerationFacet) facet;
        getAllowedValues().add(ef.getValue().toString());
        if (hasProblemMessage()) {
            initProblemInfo(facet, BaseStatus.Severity.FATAL_ERROR, XmlMessages.getString("EnumerationRestrictionChecker.ValueNotAllowed"));
        }
    }

    @Override
    public boolean check(String valueStr, Object value, List<BaseStatus> problemDetailsList, IMessageLocalizer messageLocalizer) {
        boolean result = getAllowedValues().contains(valueStr);
        if (!result) {
            problemDetailsList.add(new StatusDetail(getProblemSeverity(), getProblemMessage(messageLocalizer)));
        }
        return result;
    }

    public Set<String> getAllowedValues() {
        return allowedValues;
    }
}
