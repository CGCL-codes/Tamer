package nts.typo;

import nts.base.Dimen;
import nts.io.CharCode;
import nts.node.Node;
import nts.node.AccKernNode;
import nts.node.HBoxNode;
import nts.node.HShiftNode;
import nts.node.FontMetric;
import nts.builder.Builder;
import nts.command.Token;
import nts.command.Command;
import nts.command.Prim;

public class AccentPrim extends BuilderPrim {

    public AccentPrim(String name) {
        super(name);
    }

    public final Action NORMAL = new Action() {

        public void exec(final Builder bld, Token src) {
            CharCode code = Token.makeCharCode(Prim.scanCharacterCode());
            if (code == CharCode.NULL) throw new RuntimeException("no char number scanned");
            FontMetric metric = getCurrFontMetric();
            Node node = metric.getCharNode(code);
            if (node == Node.NULL) charWarning(metric, code); else {
                Dimen x = metric.getDimenParam(FontMetric.DIMEN_PARAM_X_HEIGHT);
                Dimen s = metric.getDimenParam(FontMetric.DIMEN_PARAM_SLANT);
                Token tok = nextNonAssignment();
                Command cmd = meaningOf(tok);
                code = cmd.charCodeToAdd();
                if (code == CharCode.NULL) backToken(tok); else {
                    FontMetric nextMetric = getCurrFontMetric();
                    Node nextNode = nextMetric.getCharNode(code);
                    if (nextNode == Node.NULL) charWarning(nextMetric, code); else {
                        Dimen h = nextNode.getHeight();
                        if (!h.equals(x)) {
                            node = HBoxNode.packedOf(node);
                            node = HShiftNode.shiftingDown(node, x.minus(h));
                        }
                        Dimen a = node.getWidth();
                        Dimen delta = makeDelta(a, x, s, nextNode.getWidth(), h, nextMetric.getDimenParam(FontMetric.DIMEN_PARAM_SLANT));
                        bld.addNode(new AccKernNode(delta));
                        bld.addNode(node);
                        node = nextNode;
                        bld.addNode(new AccKernNode(delta.plus(a).negative()));
                    }
                }
                bld.addNode(node);
                bld.resetSpaceFactor();
            }
        }
    };

    private static Dimen makeDelta(Dimen a, Dimen x, Dimen s, Dimen w, Dimen h, Dimen t) {
        return Dimen.valueOf(w.minus(a).toDouble() / 2 + h.toDouble() * t.toDouble() - x.toDouble() * s.toDouble());
    }

    private static int spoints(Dimen d) {
        return d.toInt(Dimen.REPR_UNITY);
    }
}
