package calclipse.core.comps.gui.tokens;

import javax.swing.SwingUtilities;
import calclipse.core.gui.Doc;
import calclipse.core.gui.GUI;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Constant;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

public class ToBack extends Operator {

    public ToBack() {
        super("to_back", RPNTokenType.LUOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final Object o = op1.getValue();
        if (o instanceof Doc) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    GUI.getWin((Doc) o).toBack();
                }
            });
            return Constant.VOID;
        }
        throw new RPNException(MathMessages.getInvalidDataType());
    }
}
