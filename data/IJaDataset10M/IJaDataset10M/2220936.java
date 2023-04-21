package data.filters;

import data.*;

/**
  * CountingStockFilter that filters MoneyBags.
  *
  * @author Steffen Zschaler
  * @version 2.0 19/08/1999
  * @since v2.0
  */
public abstract class MoneyBagFilter extends CountingStockFilter implements MoneyBag {

    /**
    * Create a new MoneyBagFilter.
    *
    * @param mbSource the MoneyBag to be filtered.
    */
    public MoneyBagFilter(MoneyBag mbSource) {
        super(mbSource);
    }
}
