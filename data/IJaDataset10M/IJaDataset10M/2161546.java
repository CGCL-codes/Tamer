package bexee.ecs;

/**
 * This class is used to override the default constructor of the
 * <code>org.apache.ecs.xml.XML</code> in order to set some properties. These
 * properties are common for all usages of <code>org.apache.ecs.xml.XML</code>
 * in bexee.
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:21 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public class XML extends org.apache.ecs.xml.XML {

    private static final boolean MUST_CLOSE_ELEMENT = true;

    private static final boolean PRETTY_PRINT_ENABLED = false;

    private static final int TAB_LEVEL = 1;

    /**
     * Create a new <code>XML</code> instance with the given
     * <code>elementName</code>.
     */
    public XML(String elementName) {
        super(elementName, MUST_CLOSE_ELEMENT);
        setTabLevel(TAB_LEVEL);
        setPrettyPrint(PRETTY_PRINT_ENABLED);
    }
}
