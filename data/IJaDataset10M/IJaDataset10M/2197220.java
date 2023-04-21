package com.hisham.parking;

import com.hisham.transaction.*;
import com.hisham.creditcard.*;
import com.hisham.powerpark.*;

/**
 *
 * <p>Title: Web Services for Parking</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Ali Hisham Malik
 * @version 2.0
 */
public interface IParkingTransactionInfo extends ITransactionInfo, ParkingConstants {

    String getOrderNumber();

    String getIpAddress();

    CcTransactionInfo getCcTransactionInfo();

    PpTransactionInfo getPpTransactionInfo();
}
