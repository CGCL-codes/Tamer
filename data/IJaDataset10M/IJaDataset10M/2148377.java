package org.jdiameter.api.rf.events;

import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.app.AppRequestEvent;

/**
 * The ACR messages, indicated by the Command-Code field set to 271 is sent by the CTF to the CDF
 * in order to send charging information for the request bearer / subsystem / service. 
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface RfAccountingRequest extends AppRequestEvent {

    public static final String _SHORT_NAME = "ACR";

    public static final String _LONG_NAME = "Accounting-Request";

    public static final int code = 271;

    /**
   * @return Record type of request
   * @throws AvpDataException if result code avp is not integer
   */
    int getAccountingRecordType() throws AvpDataException;

    /**
   * @return record number
   * @throws AvpDataException if result code avp is not integer
   */
    long getAccountingRecordNumber() throws AvpDataException;
}
