package gnu.xml.validation.relaxng;

import org.relaxng.datatype.Datatype;
import org.relaxng.datatype.DatatypeLibrary;

/**
 * A RELAX NG value element.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class ValuePattern extends Pattern {

    DatatypeLibrary datatypeLibrary;

    Datatype type;

    String ns;

    String value;
}
