package de.fraunhofer.ipsi.ipsixq.sequencetypes;

import de.fraunhofer.ipsi.xquery.context.StaticContext;
import de.fraunhofer.ipsi.xquery.datamodel.Sequence;
import de.fraunhofer.ipsi.xquery.datamodel.TextNode;
import de.fraunhofer.ipsi.xquery.sequencetypes.SequenceType;

public class TextType extends NodeType {

    public static final TextType TYPE = new TextType();

    /**
	 * Singleton constructor.
	 */
    private TextType() {
    }

    /**
	 * Method matches
	 *
	 * @param    any                 an AnyType
	 * @param    context             a  StaticContext
	 *
	 * @return   a boolean
	 *
	 * @throws   TypeError
	 *
	 */
    public boolean matches(Sequence any, StaticContext context) {
        boolean result = false;
        if (any.size() == 1) {
            any = any.get(0);
        }
        if (any instanceof TextNode) {
            result = true;
        }
        return result;
    }

    /**
	 * Method toString
	 *
	 * @return   a String
	 *
	 */
    public String toString() {
        return "text()";
    }
}
