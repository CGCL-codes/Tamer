package bioweka.core.properties.indices;

import bioweka.core.Supervised;

/**
 * Class attribute property for components that consider the class value of 
 * instances.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.2 $
 */
public class SupervisedProperty extends AbstractIndexProperty implements Supervised {

    /**
     * Unique class identifier
     */
    private static final long serialVersionUID = 4049634585975665461L;

    /**
     * Default index of the attribute holding the class value.
     */
    public static final String CLASS_ATTR_DEFAULT_INDEX = "last";

    /**
     * Description of the class attribute property.
     */
    public static final String CLASS_ATTR_TIP_TEXT = "Sets the index of the class attribute.";

    /**
     * Option flag for setting the class attribute index.
     */
    public static final String CLASS_ATTR_OPTION_FLAG = "C";

    /**
     * Name of the class attribute property.
     */
    public static final String CLASS_ATTR_PROPERTY_NAME = "classAttribute";

    /**
     * Initializes the class attribute property.
     */
    public SupervisedProperty() {
        super(CLASS_ATTR_PROPERTY_NAME, CLASS_ATTR_DEFAULT_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    public String tipText() {
        return CLASS_ATTR_TIP_TEXT;
    }

    /**
     * {@inheritDoc}
     */
    public String optionFlag() {
        return CLASS_ATTR_OPTION_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    public String getClassAttribute() {
        return getIndex();
    }

    /**
     * {@inheritDoc}
     */
    public void setClassAttribute(String classAttribute) throws Exception {
        setIndex(classAttribute);
    }

    /**
     * {@inheritDoc}
     */
    public String classAttributeTipText() {
        return CLASS_ATTR_TIP_TEXT;
    }
}
