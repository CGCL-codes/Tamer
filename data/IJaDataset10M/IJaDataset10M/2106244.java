package fr.brgm.exows.filter.gml.operators.logic;

import java.util.ArrayList;
import java.util.List;
import fr.brgm.exows.filter.gml.objects.EnvelopeType;
import fr.brgm.exows.filter.gml.operators.OpsType;

/**
 * @author BRGM
 * @version $Id$
 */
public class BinaryLogicOpType extends LogicOpsType {

    static final String _TYPE = "Binary Logic Operator";

    public BinaryLogicOpType() {
        super();
        setType(_TYPE);
    }

    private List<OpsType> operators = new ArrayList<OpsType>();

    public void addOperator(OpsType operator) {
        operators.add(operator);
    }

    public List<OpsType> getOperators() {
        return operators;
    }

    public void setOperators(List<OpsType> operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        for (OpsType operator : getOperators()) {
            sb.append(operator.toString());
        }
        return sb.toString();
    }

    @Override
    public EnvelopeType getBBOX() {
        for (OpsType operator : getOperators()) {
            if (operator.getBBOX() != null) return operator.getBBOX();
        }
        return null;
    }
}
