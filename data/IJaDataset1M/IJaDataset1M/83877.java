package javax.xml.xpath;

import java.util.List;

/**
 * An XPath extension function.
 *
 * @author (a href='mailto:dog@gnu.org'>Chris Burdess</a)
 * @since 1.3
 */
public interface XPathFunction {

    /**
   * Evaluate the function with the specified arguments.
   * @param args the list of arguments
   */
    Object evaluate(List args) throws XPathFunctionException;
}
