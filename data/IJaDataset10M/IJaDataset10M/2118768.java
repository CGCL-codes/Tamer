package com.idna.dm.domain.comparator.impl;

import com.idna.dm.domain.Conclusion;
import com.idna.dm.domain.comparator.BasicExpressionComparator;

public class BasicExpressionNumericComparator extends AbstractBasicExpressionComparator {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7847716352183536767L;

    public static final BasicExpressionNumericComparator EQUALS = new BasicExpressionNumericComparator("EQUALS", "=");

    public static final BasicExpressionNumericComparator NOT_EQUALS = new BasicExpressionNumericComparator("NOT_EQUALS", "!=");

    public static final BasicExpressionNumericComparator LESS_THAN = new BasicExpressionNumericComparator("LESS_THAN", "<");

    public static final BasicExpressionNumericComparator LESS_THAN_OR_EQUAL = new BasicExpressionNumericComparator("LESS_THAN_OR_EQUAL", "<=");

    public static final BasicExpressionNumericComparator GREATER_THAN = new BasicExpressionNumericComparator("GREATER_THAN", ">");

    public static final BasicExpressionNumericComparator GREATER_THAN_OR_EQUAL = new BasicExpressionNumericComparator("GREATER_THAN_OR_EQUAL", "=>");

    private BasicExpressionNumericComparator(String comparatorNumberSymbol) {
        super(comparatorNumberSymbol);
    }

    private BasicExpressionNumericComparator(String comparatorNumberSymbol, String underlyingSymbol) {
        super(comparatorNumberSymbol, underlyingSymbol);
    }

    @Override
    public Conclusion doComparison(Object leftHandOperand, Object rightHandOperand) {
        int leftNumber = Integer.valueOf(leftHandOperand.toString());
        int rightNumber = Integer.valueOf(rightHandOperand.toString());
        if (this == EQUALS) {
            return Conclusion.newRootCauseConclusion((leftNumber == rightNumber));
        } else if (this == NOT_EQUALS) {
            return Conclusion.newRootCauseConclusion((leftNumber != rightNumber));
        } else if (this == LESS_THAN) {
            return Conclusion.newRootCauseConclusion((leftNumber < rightNumber));
        } else if (this == LESS_THAN_OR_EQUAL) {
            return Conclusion.newRootCauseConclusion((leftNumber <= rightNumber));
        } else if (this == GREATER_THAN) {
            return Conclusion.newRootCauseConclusion((leftNumber > rightNumber));
        } else if (this == GREATER_THAN_OR_EQUAL) {
            return Conclusion.newRootCauseConclusion((leftNumber >= rightNumber));
        }
        throw new IllegalStateException(String.format("This state [%s] is not possible.", comparatorSymbol));
    }

    @Override
    public BasicExpressionComparator[] getValues() {
        return new BasicExpressionComparator[] { EQUALS, NOT_EQUALS, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL };
    }

    public enum NumericComparatorMappedToDBKey {

        EQUALS("1", "="), NOT_EQUALS("4", "!="), LESS_THAN("5", "<"), LESS_THAN_OR_EQUAL("6", "<="), GREATER_THAN("7", ">"), GREATER_THAN_OR_EQUAL("8", "=>");

        private String id;

        private String display;

        private NumericComparatorMappedToDBKey(String id, String display) {
            this.id = id;
            this.display = display;
        }

        public String getId() {
            return id;
        }

        public String getComperatorId() {
            return getId();
        }

        public String getDisplay() {
            return display;
        }
    }
}
