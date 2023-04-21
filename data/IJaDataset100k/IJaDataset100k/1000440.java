package gov.nist.javax.sip.header;

import java.util.Iterator;
import javax.sip.header.*;

/**
 * List of Unsupported headers.
 * @version 1.2 $Revision: 1.5 $ $Date: 2007/10/23 17:34:51 $
 * @author M. Ranganathan
 */
public class UnsupportedList extends SIPHeaderList<Unsupported> {

    private static final long serialVersionUID = -4052610269407058661L;

    /** Default Constructor
	 */
    public UnsupportedList() {
        super(Unsupported.class, UnsupportedHeader.NAME);
    }

    public Object clone() {
        UnsupportedList retval = new UnsupportedList();
        return retval.clonehlist(this.hlist);
    }
}
