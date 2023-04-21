package assem;

import temp.Label;

public class LABEL extends Instr {

    public Label label;

    public LABEL(String a, Label l) {
        assem = a;
        use = null;
        def = null;
        jumps = null;
        label = l;
    }
}
