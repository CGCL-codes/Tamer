package com.san.stock;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.san.stock.IciciDirectTrader;
import com.san.stock.StockItem;
import junit.framework.TestCase;

public class IciciDirectTraderTest extends TestCase {

    private static Log logger = LogFactory.getLog(IciciDirectTraderTest.class);

    public void testNotify() {
        IciciDirectTrader trader = new IciciDirectTrader();
        List<StockCall> stockItem = new ArrayList<StockCall>();
        trader.notify(stockItem);
    }
}
