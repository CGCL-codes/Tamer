package freemind.controller.filter.condition;

import freemind.main.Resources;
import freemind.main.Tools;
import freemind.main.XMLElement;

abstract class CompareConditionAdapter extends NodeCondition {

    static final String IGNORE_CASE = "ignore_case";

    static final String VALUE = "value";

    private String conditionValue;

    private boolean ignoreCase;

    CompareConditionAdapter(String value, boolean ignoreCase) {
        super();
        this.conditionValue = value;
        this.ignoreCase = ignoreCase;
    }

    protected int compareTo(String nodeValue) throws NumberFormatException {
        try {
            int i2 = Integer.parseInt(conditionValue);
            int i1 = Integer.parseInt(nodeValue);
            return i1 < i2 ? -1 : (i1 == i2 ? 0 : 1);
        } catch (NumberFormatException fne) {
        }
        ;
        double d2;
        try {
            d2 = Double.parseDouble(conditionValue);
        } catch (NumberFormatException fne) {
            return ignoreCase ? nodeValue.compareToIgnoreCase(conditionValue) : nodeValue.compareTo(conditionValue);
        }
        ;
        double d1 = Double.parseDouble(nodeValue);
        return Double.compare(d1, d2);
    }

    public void saveAttributes(XMLElement child) {
        super.saveAttributes(child);
        child.setAttribute(VALUE, conditionValue);
        child.setAttribute(IGNORE_CASE, Tools.BooleanToXml(ignoreCase));
    }

    public String createDescription(String attribute, int comparationResult, boolean succeed) {
        String simpleCondition;
        switch(comparationResult) {
            case -1:
                simpleCondition = succeed ? ConditionFactory.FILTER_LT : ConditionFactory.FILTER_GE;
                break;
            case 0:
                simpleCondition = Resources.getInstance().getResourceString(succeed ? ConditionFactory.FILTER_IS_EQUAL_TO : ConditionFactory.FILTER_IS_NOT_EQUAL_TO);
                break;
            case 1:
                simpleCondition = succeed ? ConditionFactory.FILTER_GT : ConditionFactory.FILTER_LE;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ConditionFactory.createDescription(attribute, simpleCondition, conditionValue, ignoreCase);
    }
}
