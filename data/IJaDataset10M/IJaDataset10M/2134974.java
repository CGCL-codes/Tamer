package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG78_2 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG78_2() {
        super();
        try {
            setID("FG78_2");
            setTitle("sulfinic acid ester");
            addSubstructure("FG78_2", "[SX3](=[OX1])([#6])([O][#6])");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
