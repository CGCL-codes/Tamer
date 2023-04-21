package org.blogtrader.platform.core.persistence;

import java.util.List;
import org.blogtrader.platform.core.analysis.chart.handledchart.AbstractHandledChart;
import org.blogtrader.platform.core.analysis.descriptor.Descriptors;
import org.blogtrader.platform.core.analysis.indicator.Indicator;
import org.blogtrader.platform.core.data.dataserver.AbstractQuoteDataServer;
import org.blogtrader.platform.core.data.dataserver.AbstractTickerDataServer;
import org.blogtrader.platform.core.stock.Quote;

/**
 *
 * @author Caoyuan Deng
 */
public abstract class PersistenceManager {

    private static PersistenceManager defalut;

    public static PersistenceManager getDefalut() {
        if (defalut == null) {
            defalut = new NetBeansPersistenceManager();
        }
        return defalut;
    }

    public abstract void saveDescriptors(Descriptors descriptors);

    public abstract Descriptors restoreDescriptors(String stockSymbol);

    public abstract void restoreProperties();

    public abstract void saveProperties();

    public abstract List<Indicator> getAllAvailableIndicators();

    public abstract List<AbstractHandledChart> getAllAvailableHandledChart();

    public abstract List<AbstractQuoteDataServer> getAllAvailableQuoteDataServers();

    public abstract List<AbstractTickerDataServer> getAllAvailableTickerDataServers();

    public abstract void saveQuotes(String symbol, List<Quote> quotes);

    public abstract List<Quote> restoreQuotes(String symbol);

    public abstract void deleteQuotes(String symbol, long fromTime, long toTime);

    public abstract void shutdown();
}
