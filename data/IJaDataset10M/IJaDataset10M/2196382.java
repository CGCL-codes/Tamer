package org.jdiameter.api.app;

import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;

/**
 * Basic class for application specific answer event (Sx, Rx, Gx)
 * 
 * @version 1.5.1 Final
 * 
 * @author erick.svenson@yahoo.com
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>

 */
public interface AppAnswerEvent extends AppEvent {

    /**
   * Return result code (or experimental if present) AVP of answer message
   * @return result code (or experimental if present) AVP of answer message
   * @throws AvpDataException if result code avp absent
   */
    Avp getResultCodeAvp() throws AvpDataException;
}
