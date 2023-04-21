package pedro.soa.alerts;

import pedro.system.PedroResources;
import pedro.mda.model.*;
import java.util.ArrayList;

public class ListFieldMatchingCriterion extends MatchingCriterion {

    private int comparedValue;

    private String comparedChildType;

    private FieldOperator operator;

    public ListFieldMatchingCriterion() {
        comparedChildType = PedroResources.EMPTY_STRING;
    }

    public FieldOperator getOperator() {
        return operator;
    }

    public boolean matches(DataFieldModel dataFieldModel) {
        ListFieldModel listFieldModel = (ListFieldModel) dataFieldModel;
        int childCount;
        if (comparedChildType == null) {
            ArrayList children = listFieldModel.getChildren();
            return matches(children.size());
        } else {
            RecordModelUtility recordModelUtility = new RecordModelUtility();
            ArrayList childrenOfType = recordModelUtility.getListChildren(listFieldModel, comparedChildType);
            return matches(childrenOfType.size());
        }
    }

    /**
     * Get the value of comparedValue.
     *
     * @return value of comparedValue.
     */
    public int getComparedValue() {
        return comparedValue;
    }

    /**
     * Get the value of comparedChildType.
     *
     * @return value of comparedChildType.
     */
    public String getComparedChildType() {
        return comparedChildType;
    }

    public String getDescription() {
        StringBuffer description = new StringBuffer();
        description.append(getFieldName());
        description.append(":");
        description.append("Number of ");
        if (comparedChildType != null) {
            description.append(comparedChildType);
            description.append(" ");
        }
        description.append("subrecords ");
        String operatorPhrase = FieldOperator.getPhraseForOperator(operator);
        description.append(operatorPhrase);
        description.append(" ");
        description.append(String.valueOf(comparedValue));
        return description.toString();
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }

    /**
     * Set the value of comparedValue.
     *
     * @param comparedValue Value to assign to comparedValue.
     */
    public void setComparedValue(int comparedValue) {
        this.comparedValue = comparedValue;
    }

    /**
     * Set the value of comparedChildType.
     *
     * @param comparedChildType Value to assign to comparedChildType.
     */
    public void setComparedChildType(String comparedChildType) {
        this.comparedChildType = comparedChildType;
    }

    private boolean matches(int childCount) {
        if (operator == FieldOperator.EQUALS) {
            return (childCount == comparedValue);
        } else if (operator == FieldOperator.GREATER_THAN) {
            return (childCount > comparedValue);
        } else if (operator == FieldOperator.GREATER_THAN_EQUALS) {
            return (childCount >= comparedValue);
        } else if (operator == FieldOperator.LESS_THAN) {
            return (childCount < comparedValue);
        } else if (operator == FieldOperator.LESS_THAN_EQUALS) {
            return (childCount <= comparedValue);
        } else {
            return (childCount != comparedValue);
        }
    }
}
