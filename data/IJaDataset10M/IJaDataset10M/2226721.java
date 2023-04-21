package uk.co.ordnancesurvey.rabbitparser.metadata.impl;

import uk.co.ordnancesurvey.rabbitparser.metadata.IParsedPartDefinition;
import uk.co.ordnancesurvey.rabbitparser.metadata.element.IZeroOrMorePartDefinition;
import uk.co.ordnancesurvey.rabbitparser.metadata.impl.base.BaseContainsSinglePartDefinition;

/**
 * Default implemetation of a {@link IZeroOrMorePartDefinition}
 * 
 * @author rdenaux
 * 
 */
public class ZeroOrMorePartDefinition extends BaseContainsSinglePartDefinition implements IZeroOrMorePartDefinition {

    public ZeroOrMorePartDefinition(IParsedPartDefinition contained) {
        super(contained);
    }

    public IParsedPartDefinition getRepeatedPart() {
        return getContained();
    }

    public String getAsBNF() {
        return "(" + getContained().getAsBNF() + ")*";
    }
}
