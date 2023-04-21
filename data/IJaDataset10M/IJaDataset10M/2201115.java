package calclipse.lib.math.mp.steps;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import calclipse.lib.math.mp.MPConstants;
import calclipse.lib.math.mp.MPUtil;
import calclipse.lib.math.rpn.Constant;
import calclipse.lib.math.rpn.Delimitation;
import calclipse.lib.math.rpn.Fragment;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNParserStep;
import calclipse.lib.math.rpn.RPNTokenStream;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.lib.math.util.Indexer;
import calclipse.msg.MathMessages;

/**
 * Support for indices.
 * @author T. Sommerland
 */
public class IndexProcessor implements RPNParserStep {

    private static final int DELEGATOR_PRIORITY = 1000;

    private static final int ACCUMULATOR_PRIORITY = -1000;

    private final Delimitation delim = MPConstants.PAREN_DELIMITATION;

    private final Accumulator accumulator = new Accumulator();

    private final Delegator delegator = new Delegator();

    private final Indexer indexer;

    public IndexProcessor(final Indexer indexer) {
        this.indexer = indexer;
    }

    /**
     * Uses the {@link calclipse.lib.math.util.Indexer#DEFAULT}.
     */
    public IndexProcessor() {
        this(Indexer.DEFAULT);
    }

    private void processBuffer(final List<Fragment> buffer) {
        final Deque<Boolean> flags = new ArrayDeque<Boolean>();
        for (int i = buffer.size() - 1; i >= 0; i--) {
            final Fragment frag = buffer.get(i);
            if (delim.readCloser(frag.getToken())) {
                flags.add(Boolean.FALSE);
            } else if (delim.readSeparator(frag.getToken()) && !flags.isEmpty()) {
                frag.setToken(accumulator);
                flags.removeLast();
                flags.add(Boolean.TRUE);
            } else if (delim.readOpener(frag.getToken()) && !flags.isEmpty() && flags.removeLast()) {
                buffer.add(i, new Fragment(delegator, frag.getPosition()));
            }
        }
    }

    @Override
    public void read(final RPNTokenStream s) throws RPNException {
        s.getBuffer().add(s.getInput());
        s.skip();
    }

    @Override
    public void end(final RPNTokenStream s) throws RPNException {
        processBuffer(s.getBuffer());
        for (final Fragment frag : s.getBuffer()) {
            s.write(frag);
        }
    }

    private final class Accumulator extends Operator {

        public Accumulator() {
            super(delim.getSeparators()[0], RPNTokenType.BOPERATOR, ACCUMULATOR_PRIORITY);
        }

        @Override
        public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
            final Object o1 = op1.getValue();
            final Object o2 = op2.getValue();
            IndexList list = null;
            if (o1 instanceof IndexList) {
                list = (IndexList) o1;
            } else {
                list = new IndexList();
                list.add(MPUtil.toIndex(o1));
            }
            list.add(MPUtil.toIndex(o2));
            return new Constant(list);
        }
    }

    private final class Delegator extends Operator {

        public Delegator() {
            super(delim.getOpener(), RPNTokenType.BOPERATOR, DELEGATOR_PRIORITY);
        }

        @Override
        public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
            final Object o2 = op2.getValue();
            if (o2 instanceof IndexList) {
                final IndexList list = (IndexList) o2;
                final int[] indices = new int[list.size()];
                for (int i = 0; i < indices.length; i++) {
                    indices[i] = list.get(i);
                }
                return indexer.get(op1, indices);
            }
            throw new RPNException(MathMessages.getInvalidDataType());
        }
    }

    private static final class IndexList extends ArrayList<Integer> {

        private static final long serialVersionUID = 1L;

        public IndexList() {
        }
    }
}
