package calclipse.core.comps.disp.tokens;

import calclipse.core.disp.DisplayDoc;
import calclipse.core.gui.GUI;
import calclipse.core.gui.PropertyOperand;
import calclipse.lib.math.mp.MPUtil;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

public class PixelSize extends Operator {

    public PixelSize() {
        super("pixel_size", RPNTokenType.LUOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final Object o = op1.getValue();
        if (o instanceof DisplayDoc) {
            return new PixelSizeOperand((DisplayDoc) o);
        }
        throw new RPNException(MathMessages.getInvalidDataType());
    }

    private static final class PixelSizeOperand extends PropertyOperand<DisplayDoc, Integer> {

        public PixelSizeOperand(final DisplayDoc d) {
            super("", d);
        }

        @Override
        protected Object get(final DisplayDoc widget) {
            return new Double(widget.getDisplay().getPixelSize());
        }

        @Override
        protected Integer validate(final Object value) throws RPNException {
            final int i = MPUtil.toInt(value);
            if (i < 1) {
                throw new RPNException(MathMessages.getInvalidSize());
            }
            return i;
        }

        @Override
        protected void set(final DisplayDoc widget, final Integer value) {
            widget.getDisplay().setPixelSize(value);
            GUI.getWin(widget).pack();
        }
    }
}
