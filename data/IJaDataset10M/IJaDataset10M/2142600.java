package org.fressia.lang.parser;

/**
 * Action option info used by the semnatic checker.
 *
 * @author Alvaro Egana
 *
 */
public class Option extends SemanticContext {

    private boolean optional;

    private int allowedInstances;

    private int expectedTypeId;

    private String mappingParamName;

    public Option(String optionName, String textualName, String mappingParamName, int expectedTypeId, boolean optional) {
        this(optionName, textualName, mappingParamName, expectedTypeId, optional, Integer.MAX_VALUE);
    }

    public Option(String optionName, String textualName, String mappingParamName, int expectedTypeId, boolean optional, int allowedInstances) {
        super(optionName, textualName);
        this.optional = optional;
        this.allowedInstances = allowedInstances;
        this.expectedTypeId = expectedTypeId;
        this.mappingParamName = mappingParamName;
        if ((getAllowedInstances() > 1) && (expectedTypeId != Variable.TYPE_STR_ID)) {
            throw new SemanticsConfiguration.SemanticContextException(getTextualName() + " ('" + Variable.getTypeName(expectedTypeId) + "')" + ": Multiple instances are allowed for '" + Variable.getTypeName(Variable.TYPE_STR_ID) + "' options only.");
        }
    }

    public boolean isOptional() {
        return optional;
    }

    public int getAllowedInstances() {
        return allowedInstances;
    }

    public int getExpectedTypeId() {
        return expectedTypeId;
    }

    public String getMappingParamName() {
        return mappingParamName;
    }
}
