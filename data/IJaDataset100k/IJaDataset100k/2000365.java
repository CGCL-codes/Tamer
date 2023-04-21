package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG56 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG56() {
        super();
        try {
            setID("FG56");
            setTitle("thiourea");
            addSubstructure("FG56", "[$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2])]C(=S)[$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2])]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
