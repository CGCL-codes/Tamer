package nts.noad;

import nts.base.Dimen;
import nts.io.Log;
import nts.io.Loggable;
import nts.io.CntxLog;
import nts.io.CntxLoggable;
import nts.io.CharCode;
import nts.node.Node;
import nts.node.Box;
import nts.node.HBoxNode;
import nts.node.HShiftNode;
import nts.node.TreatNode;
import nts.node.MathWordBuilder;

public class CharField extends Field implements Loggable, CntxLoggable {

    public static final CharField NULL = null;

    private byte fam;

    private CharCode code;

    public CharField(byte fam, CharCode code) {
        this.fam = fam;
        this.code = code;
    }

    public void addOn(Log log) {
        log.addEsc("fam").add(fam).add(' ').add(code);
    }

    public void addOn(Log log, CntxLog cntx) {
        addOn(log);
    }

    public void addOn(Log log, CntxLog cntx, char p) {
        cntx.addOn(log, this, p);
    }

    public boolean isJustChar() {
        return true;
    }

    public Dimen skewAmount(Converter conv) {
        return conv.skewAmount(fam, code);
    }

    public Node convertedBy(Converter conv) {
        return conv.fetchCharNode(fam, code);
    }

    public Box fittingTo(Converter conv, Dimen width) {
        return conv.fetchFittingWidthBox(fam, code, width);
    }

    public Dimen xHeight(Converter conv) {
        return conv.getXHeight(fam);
    }

    public Node cleanBox(Converter conv, byte how) {
        return packedWithItalCorr(conv.fetchCharNode(fam, code, how));
    }

    private static Node packedWithItalCorr(Node node) {
        if (node == Node.NULL) return HBoxNode.EMPTY;
        Box box = HBoxNode.packedOf(node);
        Dimen ital = node.getItalCorr();
        return (ital == Dimen.NULL) ? box : box.pretendingWidth(box.getWidth().plus(ital));
    }

    protected static class CharOperator implements Operator {

        private final Node node;

        private final Dimen shift;

        public CharOperator(Node node, Dimen shift) {
            this.node = node;
            this.shift = shift;
        }

        public Node getNodeToBeLimited() {
            Node box = packedWithItalCorr(node);
            return (shift.isZero()) ? box : HBoxNode.packedOf(HShiftNode.shiftingDown(box, shift));
        }

        public Egg getEggToBeScripted(byte spType) {
            return new OperatorEgg(node, shift, spType);
        }

        public Dimen getItalCorr() {
            return (node == Node.NULL) ? Dimen.NULL : node.getItalCorr();
        }
    }

    private static Operator makeOperator(Node node, Converter conv) {
        Dimen middle = conv.getDimPar(DP_AXIS_HEIGHT);
        Dimen shift = ((node == Node.NULL) ? Dimen.ZERO : node.getHeight().max(Dimen.ZERO).minus(node.getDepth().max(Dimen.ZERO)).halved()).minus(middle);
        return new CharOperator(node, shift);
    }

    public Operator makeOperator(Converter conv, boolean larger) {
        return makeOperator((larger) ? conv.fetchLargerNode(fam, code) : conv.fetchCharNode(fam, code), conv);
    }

    public MathWordBuilder getMathWordBuilder(Converter conv, TreatNode proc) {
        return conv.getWordBuilder(fam, proc);
    }

    public byte wordFamily() {
        return fam;
    }

    public void contributeToWord(MathWordBuilder word) {
        word.add(code);
    }

    public Operator takeLastOperator(MathWordBuilder word, Converter conv, boolean larger) {
        return makeOperator((larger) ? word.takeLastLargerNode() : word.takeLastNode(), conv);
    }
}
