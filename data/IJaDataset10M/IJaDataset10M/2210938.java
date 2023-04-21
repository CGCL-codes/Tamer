package phex.xml.sax.rules;

import phex.xml.sax.DElement;
import phex.xml.sax.DSubElementList;

public class DConsequencesList extends DSubElementList<DConsequence> implements DElement {

    public static final String ELEMENT_NAME = "consequences-list";

    public DConsequencesList() {
        super(ELEMENT_NAME);
    }
}
