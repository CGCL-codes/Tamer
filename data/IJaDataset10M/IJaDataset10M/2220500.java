package org.jdiameter.api.acc.events;

import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.app.AppRequestEvent;

/**
 * An Account Request is a request from a client to a server
 * 
 * @version 1.5.1 Final
 * 
 * @author erick.svenson@yahoo.com
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AccountRequest extends AppRequestEvent {

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

    /**
   * 
   * @param recordType
   * @throws AvpDataException
   */
    void setAccountingRecordType(int recordType) throws AvpDataException;

    /**
   * 
   * @param number
   * @throws AvpDataException
   */
    void setAccountingRecordNumber(long number) throws AvpDataException;
}
