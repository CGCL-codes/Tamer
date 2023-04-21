package org.charvolant.tmsnet.command;

import javax.xml.bind.annotation.XmlRootElement;
import org.charvolant.tmsnet.protocol.TMSNetType;
import org.charvolant.tmsnet.protocol.TMSNetElement;

/**
 * Get information about what is currently playing.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@TMSNetElement(type = TMSNetType.COMMAND, id = 31)
@XmlRootElement()
public class GetCurrentChannel {
}
