package Sale;

import java.io.Serializable;

/**
  * A CatalogItem.
  *
  * <p>CatalogItems describe available objects by their attributes. The only attribute
  * that is mandatory is a <I>key</I>(String), usually a name, but it could be an
  * ID-Number or anything. A CatalogItem can have an attribut assigned to be its
  * float-<I>value</I>, which will be returned by the getValue()-method.<p>
  *
  * <p>A CatalogItem can create its corresponding StockItem.</p>
  *
  * <p>A CatalogItem is Serializable.</p>
  *
  * <p>For Prototyping-usage DictionaryCatalogItem can be used.</p>
  *
  * @see Catalog
  * @see StockItem
  * @see DictionaryCatalogItem
  * @see CatalogItemValue
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public abstract class CatalogItem implements Serializable, Cloneable {

    private String key = null;

    /**
    * Construct a new CatalogItem.
    *
    * <p>Constructs a new CatalogItem with the key <I>key</I>.</p>
    *
    * @param key the key of the new CatalogItem.
    */
    public CatalogItem(String key) {
        super();
        setKey(key);
    }

    /**
    * Return the key of the CatalogItem.
    *
    * <p>Returns the key as set by setKey(). The key is a String that identifies the
    * CatalogItem. It can be a name or an ID-Number represented as String or anything
    * else that can be represented as a String.</p>
    *
    * @return the key identifying this CatalogItem.
    */
    public String getKey() {
        return key;
    }

    /**
    * Set the key of this CatalogItem.
    *
    * <p>This method is intended for use in subclasses only. You should be very careful
    * when publicising this method, as it does no checks whatsoever on access rights or
    * anything.</p>
    *
    * @param key the new key for this CatalogItem.
    */
    protected void setKey(String key) {
        this.key = key;
    }

    /**
    * Returns the value of this CatalogItem.
    *
    * <p>This method is used by CatalogItemValue. By default it returns 0.0f. You must
    * redefine it to return a sensible value for this CataloItem.</p>
    *
    * @return the value of this CatalogItem.
    */
    public float getValue() {
        return 0.0f;
    }

    /**
    * Creates a new StockItem for this CatalogItem.
    * 
    * <p>By default creates a StockItem without any additional information as it would
    * be used with a CountingStock. This default should be applicable with most Catalogs.
    * </p>
    *
    * @return a StockItem corresponding with this CatalogItem.
    */
    public StockItem createStockItem() {
        return new StockItem(this);
    }

    /**
    * Returns true if this CatalogItem equals o.
    *
    * <p>By default returns true if o is an instance of CatalogItem or any of its
    * subclasses. Must be redefined in subclasses.</p>
    * 
    * @param o the object to be compared.
    *
    * @return True, if this equals o.
    */
    public boolean equals(Object o) {
        return (o instanceof CatalogItem);
    }

    /**
    * Clone this CatalogItem. Must be overridden in subclasses.
    */
    public abstract Object clone();

    /**
    * Returns a short description of this CatalogItem.
    *
    * @return a short description of this CatalogItem.
    */
    public String toString() {
        return (getKey() + " : " + getValue());
    }
}
