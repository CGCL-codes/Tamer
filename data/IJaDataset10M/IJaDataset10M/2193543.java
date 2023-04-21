package net.sf.jmoney.pricehistory.external;

import java.util.Date;
import net.sf.jmoney.model2.Commodity;

public interface IPriceFetcher {

    boolean canProcess(Commodity commodity);

    Long getPrice(Date date);
}
