package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG35_3 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG35_3() {
        super();
        try {
            setID("FG35_3");
            setTitle("carboxylic acid ester");
            addSubstructure("FG35_3", "[C;H1,$(C[#6])](=[OX1])[O][#6]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
