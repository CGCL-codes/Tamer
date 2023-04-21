package data;

/**
  * Basic implementation of the {@link DataBasketCondition} interface. You can use this
  * class as a basis for implementing more sophisticated queries.
  *
  * @author Steffen Zschaler
  * @version 2.0 14/06/1999
  * @since v2.0
  */
public class DataBasketConditionImpl extends Object implements DataBasketCondition {

    /**
    * The source condition.
    *
    * @serial
    */
    protected DataBasketEntrySource m_dbesSource;

    /**
    * The destination condition.
    *
    * @serial
    */
    protected DataBasketEntryDestination m_dbedDest;

    /**
    * The main key condition.
    *
    * @serial
    */
    protected String m_sMainKey;

    /**
    * The secondary key condition.
    *
    * @serial
    */
    protected String m_sSecondaryKey;

    /**
    * The value condition.
    *
    * @serial
    */
    protected Object m_oValue;

    /**
    * Create a new DataBasketConditionImpl.
    *
    * @param sMainKey the value for {@link #m_sMainKey}.
    * @param sSecondaryKey the value for {@link #m_sSecondaryKey}.
    * @param dbesSource the value for {@link #m_dbesSource}.
    * @param dbedDest the value for {@link #m_dbedDest}.
    * @param oValue the value for {@link #m_oValue}.
    */
    public DataBasketConditionImpl(String sMainKey, String sSecondaryKey, DataBasketEntrySource dbesSource, DataBasketEntryDestination dbedDest, Object oValue) {
        super();
        m_sMainKey = sMainKey;
        m_sSecondaryKey = sSecondaryKey;
        m_dbesSource = dbesSource;
        m_dbedDest = dbedDest;
        m_oValue = oValue;
    }

    /**
    * @return {@link #m_dbesSource}.
    *
    * @override Never Instead set the value of {@link #m_dbesSource}.
    */
    public DataBasketEntrySource getSource() {
        return m_dbesSource;
    }

    /**
    * @return {@link #m_dbedDest}.
    *
    * @override Never Instead set the value of {@link #m_dbedDest}.
    */
    public DataBasketEntryDestination getDestination() {
        return m_dbedDest;
    }

    /**
    * @return {@link #m_oValue}.
    *
    * @override Never Instead set the value of {@link #m_oValue}.
    */
    public Object getValue() {
        return m_oValue;
    }

    /**
    * @return {@link #m_sMainKey}.
    *
    * @override Never Instead set the value of {@link #m_sMainKey}.
    */
    public String getMainKey() {
        return m_sMainKey;
    }

    /**
    * @return {@link #m_sSecondaryKey}.
    *
    * @override Never Instead set the value of {@link #m_sSecondaryKey}.
    */
    public String getSecondaryKey() {
        return m_sSecondaryKey;
    }

    /**
    * As a default, always returns true.
    *
    * @override Sometimes
    */
    public boolean match(DataBasketEntry dbe) {
        return true;
    }

    /**
    * A DataBasketCondition matching all items in a DataBasket.
    */
    public static final DataBasketCondition ALL_ENTRIES = new DataBasketConditionImpl(null, null, null, null, null);

    /**
    * A DataBasketCondition that matches all entries that describe StockItem movements.
    */
    public static final DataBasketCondition ALL_STOCK_ITEMS = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, null, null, null, null);

    /**
    * A DataBasketCondition that matches all entries that describe CatalogItem movements.
    */
    public static final DataBasketCondition ALL_CATALOG_ITEMS = new DataBasketConditionImpl(CATALOG_ITEM_MAIN_KEY, null, null, null, null);

    /**
    * A DataBasketCondition that matches all entries that describe StockItems being taken from the
    * given Stock.
    */
    public static final DataBasketCondition allStockItemsWithSource(Stock stSource) {
        return new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, null, stSource, null, null);
    }

    /**
    * A DataBasketCondition that matches all entries that describe StockItems being entered into the
    * given Stock.
    */
    public static final DataBasketCondition allStockItemsWithDest(Stock stDest) {
        return new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, null, null, stDest, null);
    }

    /**
    * A DataBasketCondition that matches all entries that describe CatalogItems being taken from the
    * given Catalog.
    */
    public static final DataBasketCondition allCatalogItemsWithSource(Catalog cSource) {
        return new DataBasketConditionImpl(CATALOG_ITEM_MAIN_KEY, null, cSource, null, null);
    }

    /**
    * A DataBasketCondition that matches all entries that describe CatalogItems being entered into the
    * given Catalog.
    */
    public static final DataBasketCondition allCatalogItemsWithDest(Catalog cDest) {
        return new DataBasketConditionImpl(CATALOG_ITEM_MAIN_KEY, null, null, cDest, null);
    }

    /**
    * A DataBasketCondition that matches exactly one given CatalogItem.
    */
    public static final DataBasketCondition specificCatalogItem(CatalogItem ci) {
        return new DataBasketConditionImpl(CATALOG_ITEM_MAIN_KEY, ci.getName(), null, null, ci);
    }

    /**
    * A DataBasketCondition that matches exactly one given StockItem.
    */
    public static final DataBasketCondition specificStockItem(StockItem si) {
        return new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, si.getName(), null, null, si);
    }
}
