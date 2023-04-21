package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG6 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG6() {
        super();
        try {
            setID("FG6");
            setTitle("hydrazone");
            addSubstructure("FG6", "[$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2])][NX2]=[$([CX3]([#6])[#6]),$([CX3H][#6]),$([CX3H2])]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
