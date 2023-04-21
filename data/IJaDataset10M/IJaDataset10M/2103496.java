package uk.co.christhomson.coherence.viewer.sample.loader;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import uk.co.christhomson.coherence.viewer.sample.objects.InstrumentPrice;
import uk.co.christhomson.coherence.viewer.sample.objects.InstrumentPriceKey;
import uk.co.christhomson.coherence.viewer.sample.objects.PriceSource;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class InstrumentPriceBulkLoader {

    private NamedCache cache = null;

    public static final void main(String[] args) {
        String cacheName = args[0];
        NamedCache cache = CacheFactory.getCache(cacheName);
        InstrumentPriceBulkLoader loader = new InstrumentPriceBulkLoader(cache);
        loader.loadAll();
    }

    public InstrumentPriceBulkLoader(NamedCache cache) {
        this.cache = cache;
    }

    public void generatePrices(String ticker, PriceSource source) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2010, Calendar.JANUARY, 1, 0, 0, 0);
        Random rand = new Random();
        Map<InstrumentPriceKey, InstrumentPrice> prices = new HashMap<InstrumentPriceKey, InstrumentPrice>();
        double price = rand.nextInt(1000);
        for (int i = 0; i < 10; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            Date date = cal.getTime();
            price = price + ((rand.nextDouble() - 0.5) * (price / 10));
            InstrumentPriceKey key = new InstrumentPriceKey(ticker, date, source);
            InstrumentPrice value = new InstrumentPrice(price);
            System.out.println(key);
            System.out.println(value);
            prices.put(key, value);
        }
        cache.putAll(prices);
    }

    public void loadAll() {
        generatePrices("VOD.L", PriceSource.BLOOMBERG);
        generatePrices("VOD.L", PriceSource.REUTERS);
        generatePrices("RBS.L", PriceSource.BLOOMBERG);
        generatePrices("RBS.L", PriceSource.REUTERS);
        generatePrices("BT.L", PriceSource.BLOOMBERG);
        generatePrices("BT.L", PriceSource.REUTERS);
        generatePrices("IBM.N", PriceSource.BLOOMBERG);
        generatePrices("IBM.N", PriceSource.REUTERS);
        generatePrices("III.L", PriceSource.BLOOMBERG);
        generatePrices("III.L", PriceSource.REUTERS);
    }
}
