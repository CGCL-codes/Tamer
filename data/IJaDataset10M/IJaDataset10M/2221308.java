package michaelacceptors.rules;

import toxTree.tree.rules.StructureAlertAmbit;
import ambit2.smarts.query.SMARTSException;

public class Rule13A extends StructureAlertAmbit {

    private static final long serialVersionUID = 0;

    public Rule13A() {
        super();
        try {
            id = "13A";
            setTitle("Para-quinone");
            addSubstructure(getTitle(), "C1=CC(=O)C=CC1=O");
            examples[0] = "";
            examples[1] = "C1=CC(=O)C=CC1=O";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
