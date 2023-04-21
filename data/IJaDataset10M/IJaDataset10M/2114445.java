package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG54 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG54() {
        super();
        try {
            setID("FG54");
            setTitle("urea");
            addSubstructure("FG54", "[$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2])]C(=O)[$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2])]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
