package jung.ext.predicates;

import jung.ext.utils.BasicUtils;
import jung.refact.FatalException;

public class UnderConstructionPredicate extends ContainsUserDataKeyPredicate {

    private static final String ID = UnderConstructionPredicate.class.getName();

    protected static final KeyType UNDER_CONSTRUCTION = KeyType.UNDER_CONSTRUCTION;

    private static UnderConstructionPredicate instance;

    public UnderConstructionPredicate() {
        super(UNDER_CONSTRUCTION);
    }

    /**
   * Use this function to get a general, arbitrary or singleton instance 
   * of this class. In this case a general instance is provided.
   * @return
   */
    public static UnderConstructionPredicate getInstance() throws FatalException {
        if (instance == null) instance = new UnderConstructionPredicate();
        return instance;
    }

    public String getName() {
        return BasicUtils.afterDot(ID);
    }
}
