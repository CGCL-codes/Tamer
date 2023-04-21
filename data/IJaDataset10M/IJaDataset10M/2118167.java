package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG22 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG22() {
        super();
        try {
            setID("FG22");
            setTitle("enolether");
            addSubstructure("FG22", "[$([CX3]([#6])[#6]),$([CX3H][#6]),$([CX3H2]),$([CX3](C=O)C=O),$([CX3H]C=O),$([CX3]([#6])C=O)]=[CX3;$(C(C=O)),$([C][#6]),$([CX3H1])]([O][#6])");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
