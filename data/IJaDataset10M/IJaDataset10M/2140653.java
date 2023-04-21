package org.charvolant.tmsnet.protocol;

import org.charvolant.tmsnet.protocol.TMSNetType;
import org.charvolant.tmsnet.protocol.TMSNetElement;

/**
 * A test object for marshalling.
 * <p>
 * This is the simplest type of message
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@TMSNetElement(type = TMSNetType.EVENT, id = 2001)
public class TestEvent1 {
}
