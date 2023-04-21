package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG84_1 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG84_1() {
        super();
        try {
            setID("FG84_1");
            setTitle("boronic acid");
            addSubstructure("FG84_1", "[BX3]([#6])([OH])([OH])");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
