package net.sf.yahoocsv.logic;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.yahoocsv.common.spring.BeanAttributeRequiredException;
import net.sf.yahoocsv.dao.PriceDao;
import net.sf.yahoocsv.domain.PriceBar;

public class PriceLogicImpl implements PriceLogic {

    private Cache priceCache;

    private PriceDao priceDao;

    public List<PriceBar> getPrices(String ticker) {
        List<PriceBar> list;
        Element element = priceCache.getQuiet(ticker);
        if (element != null) {
            list = (List<PriceBar>) element.getValue();
            return list;
        }
        list = priceDao.getPrices(ticker);
        element = new Element(ticker, list);
        priceCache.putQuiet(element);
        return list;
    }

    public PriceBar getPrice(String ticker, Date date) {
        List<PriceBar> priceBars = getPrices(ticker);
        PriceBar key = new PriceBar();
        key.setDate(date);
        Collections.sort(priceBars, PriceComparator.dateCompare);
        int index = Collections.binarySearch(priceBars, key, PriceComparator.dateCompare);
        if (index < 0) {
            return null;
        }
        return priceBars.get(index);
    }

    public List<PriceBar> getPrices(String ticker, Date from, Date to) {
        List<PriceBar> prices = getPrices(ticker);
        if (from == null || to == null) return prices;
        Collections.sort(prices, PriceComparator.dateCompare);
        if (from.after(to)) {
            Date tmp = from;
            from = to;
            to = tmp;
        }
        PriceBar key = new PriceBar();
        key.setDate(from);
        int fromIndex = Collections.binarySearch(prices, key, PriceComparator.dateCompare);
        if (fromIndex < 0) {
            fromIndex = -(fromIndex + 1);
        }
        key.setDate(to);
        int toIndex = Collections.binarySearch(prices, key, PriceComparator.dateCompare);
        if (toIndex < 0) {
            toIndex = -(toIndex + 1);
        }
        ;
        ArrayList<PriceBar> list = new ArrayList<PriceBar>();
        list.addAll(prices.subList(fromIndex, toIndex));
        return list;
    }

    public void afterPropertiesSet() throws Exception {
        if (priceCache == null) {
            throw new BeanAttributeRequiredException(this, "priceCache");
        }
        if (priceDao == null) {
            throw new BeanAttributeRequiredException(this, "priceDao");
        }
    }

    public void setPriceCache(Cache priceCache) {
        this.priceCache = priceCache;
    }

    public void setPriceDao(PriceDao priceDao) {
        this.priceDao = priceDao;
    }
}
