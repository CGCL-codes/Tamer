package org.localstorm.mcc.ejb.cashflow.impl.ticker.connector.institutions;

import org.localstorm.mcc.ejb.cashflow.impl.ticker.connector.Html2XmlUrlConnector;
import org.localstorm.mcc.ejb.cashflow.impl.ticker.connector.PriceXPath;
import org.localstorm.mcc.ejb.cashflow.impl.ticker.connector.i18n.RussianPriceParser;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: LocalStorm
 * Date: Feb 26, 2011
 * Time: 1:10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sberbank extends Html2XmlUrlConnector {

    private static final String URL = "http://sbrf.ru/moscow/ru/";

    private static final Map<String, PriceXPath> TICKETS = new HashMap<String, PriceXPath>();

    static {
        TICKETS.put("SBRF/USD", new PriceXPath("//TABLE[@class='table1_eggs4']/TBODY/TR[2]/TD[5]/text()", "//TABLE[@class='table1_eggs4']/TBODY/TR[2]/TD[3]/text()"));
        TICKETS.put("SBRF/EUR", new PriceXPath("//TABLE[@class='table1_eggs4']/TBODY/TR[3]/TD[5]/text()", "//TABLE[@class='table1_eggs4']/TBODY/TR[3]/TD[3]/text()"));
        TICKETS.put("SBRF/GLD", new PriceXPath("//TABLE[@class='table2_eggs4']/TBODY/TR[2]/TD[5]/text()", "//TABLE[@class='table2_eggs4']/TBODY/TR[2]/TD[3]/text()"));
        TICKETS.put("SBRF/SLVR", new PriceXPath("//TABLE[@class='table2_eggs4']/TBODY/TR[3]/TD[5]/text()", "//TABLE[@class='table2_eggs4']/TBODY/TR[3]/TD[3]/text()"));
    }

    public Sberbank() {
        super(URL, TICKETS, new RussianPriceParser());
    }
}
