package sk.tuke.ess.lib.endcondition.language;

/**
 *
 * @author Marek
 */
public class LowerThan extends RelationExpression {

    public LowerThan(Operand operand_1, Operand operand_2) {
        super(operand_1, operand_2);
    }

    @Override
    public boolean evaluate() {
        if (super.evaluate()) {
            return true;
        }
        return operand_1.getValue() < operand_2.getValue();
    }
}
