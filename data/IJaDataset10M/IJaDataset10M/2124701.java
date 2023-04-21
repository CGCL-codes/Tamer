package org.charvolant.tmsnet.command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.charvolant.tmsnet.protocol.TMSNetType;
import org.charvolant.tmsnet.protocol.TMSNetElement;

/**
 * A response to the {@link SetRecordDuration} command.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@XmlRootElement()
@TMSNetElement(type = TMSNetType.COMMAND, id = 33)
@XmlAccessorType(XmlAccessType.NONE)
public class SetRecordDurationResponse {
}
