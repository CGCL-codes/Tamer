package saadadb.prdconfiguration;

import saadadb.products.Product;
import saadadb.util.Messenger;

/**
 *This class serves for describing the constraints in "pathname" mode
 *The constraint is on the name of the parent file of a tested file (product)
 *@author Millan Patrick
 *@version $Id: PathName.java 118 2012-01-06 14:33:51Z laurent.mistahl $
 *@see ProductIdentificationConstraint
 */
public class PathName implements ProductIdentificationConstraint {

    /**Name of the product identification constraint in the case this one is the pathname**/
    private String name;

    /**Constructor
     *@param String the constraint name in pathname 
     */
    public PathName(String name) {
        this.name = name;
    }

    /**This method confirms integrity of this constraint in a configuration
     * and sending message in case of no validation
     *@return boolean true or false if this constraint is confirmed
     */
    public boolean isValid() {
        if (name == null) {
            Messenger.printMsg(Messenger.ERROR, getClass().getName() + ": Configuration invalid");
            return false;
        }
        return true;
    }

    /**This method confirms integrity of a product with this configuration constraint
     * and sending message in case of no validation
     *@param Product that we want to validate
     *@return boolean true or false if this constraint is confirmed
     */
    public boolean valid(Product product) {
        if (!(product.getParent().indexOf(name) >= 0)) {
            Messenger.printMsg(Messenger.ERROR, getClass().getName() + ": " + name + " invalid in " + product.getName());
            return false;
        }
        return true;
    }

    /**Returns the constraint name in pathname
     *@return String the constraint name
     */
    public String getConstraintType() {
        return name;
    }
}
