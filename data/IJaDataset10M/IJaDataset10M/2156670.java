package gov.lanl.Web;

import java.beans.*;
import nu.xom.*;

/**  COAS ObservationValue interface
 *
 * @author  dwf
 * @version
 */
public interface ObservationValue_ {

    /** Return DocumentFragment of Value
	 */
    public Element toDOM();

    /**
	 * return CORBA ObservationValue
	 */
    public org.omg.CORBA.Any[] toObservationValue();

    /**
	 * return String representation
	 */
    public String toString();
}
