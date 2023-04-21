package weka.gui.beans;

import java.util.EventObject;
import weka.associations.AssociationRules;

/**
 * Class encapsulating a set of association rules.
 * 
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision: 6511 $
 */
public class BatchAssociationRulesEvent extends EventObject {

    /** For serialization */
    private static final long serialVersionUID = 6332614648885439492L;

    /** The encapsulated rules */
    protected AssociationRules m_rules;

    /**
   * Creates a new <code>BatchAssociationRulesEvent</code> instance.
   * 
   * @param source the source object.
   * @param rules the association rules.
   */
    public BatchAssociationRulesEvent(Object source, AssociationRules rules) {
        super(source);
        m_rules = rules;
    }

    /**
   * Get the encapsulated association rules.
   * 
   * @return the encapsulated association rules.
   */
    public AssociationRules getRules() {
        return m_rules;
    }
}
