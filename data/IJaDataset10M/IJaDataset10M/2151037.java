package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG33_5 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG33_5() {
        super();
        try {
            setID("FG33_5");
            setTitle("aryl fluoride");
            addSubstructure("FG33_5", "[cX3][F]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
