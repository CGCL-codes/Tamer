package translate;

import temp.Label;
import temp.Temp;
import tree.BasicExp;
import tree.BasicStm;
import tree.CONST;
import tree.ESEQ;
import tree.JUMP;
import tree.LABEL;
import tree.MOVE;
import tree.SEQ;
import tree.TEMP;

public class Cx extends Exp {

    BasicExp unEx() {
        Temp r = new Temp();
        Label t = new Label();
        Label f = new Label();
        Label join = new Label();
        return new ESEQ(new SEQ(unCx(t, f), new SEQ(new LABEL(t), new SEQ(new MOVE(new TEMP(r), new CONST(1)), new SEQ(new JUMP(join), new SEQ(new LABEL(f), new SEQ(new MOVE(new TEMP(r), new CONST(0)), new LABEL(join))))))), new TEMP(r));
    }

    BasicStm unNx() {
        Label join = new Label();
        return new SEQ(unCx(join, join), new LABEL(join));
    }

    BasicStm unCx(Label t, Label f) {
        return null;
    }
}
