package Sale;

import java.util.Enumeration;
import java.util.Vector;

/**
  * A storing stock.
  *
  * <p> A StoringStock will store a list of StockItems for each CatalogItem instead of
  * merely counting occurences.</p>
  *
  * @see CountingStock
  * @see Stock
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public class StoringStock extends Stock {

    /**
    * Construct a new StoringStock.
    *
    * @param name the name of the Stock.
    * @param theCatalog the Catalog this Stock refers to.
    * @param dc the DictionaryCreator to create the Stocks Dictionary-Implementation.
    */
    public StoringStock(String name, Catalog theCatalog, DictionaryCreator dc) {
        super(name, theCatalog, dc);
    }

    /**
    * Construct a new StoringStock using a default Dictionary-Implementation.
    *
    * @param name the name of the Stock.
    * @param theCatalog the Catalog this Stock refers to.
    */
    public StoringStock(String name, Catalog theCatalog) {
        super(name, theCatalog);
    }

    /**
    * Construct a new Stock from a value.
    *
    * <p>The value <I>theValue</I> is represented as exactly as possible by a collection
    * of StockItems as prescribed by the given Catalog. The rest value can be asked from
    * the <I>getGapValue()</I>-method.</p>
    *
    * @param name the name of this Stock.
    * @param theCatalog the Catalog this Stock refers to.
    * @param dc the DictionaryCreator to create the Stocks Dictionary-Implementation.
    * @param theSumValue the value to be represented.
    * @param sfvc the algorithm to be used when creating the Stock.
    *
    * @see Stock#getGapValue
    * @see Stock#Stock(String,Sale.Catalog,Sale.DictionaryCreator,float,Sale.StockFromValueCreator)
    */
    public StoringStock(String name, Catalog theCatalog, DictionaryCreator dc, float theSumValue, StockFromValueCreator sfvc) {
        super(name, theCatalog, dc, theSumValue, sfvc);
    }

    /**
    * Add an item to the Stock.
    *
    * <p>Each item is referred to by the key of its corresponding CatalogItem.</p>
    *
    * @param si the item to be added to the stock.
    *
    * @exception Sale.NoSuchElementException if <i>si</i> does not correspond to any CatalogItem in this Stock's Catalog.
    */
    public synchronized void addItem(StockItem si) throws NoSuchElementException {
        if (!getCatalog().containsKey(si.getKey())) throw new NoSuchElementException("Stock: " + getName() + " Key: " + si.getKey());
        Vector v = (Vector) getItemContainer().get(si.getKey());
        if (v == null) {
            v = new Vector();
            getItemContainer().put(si.getKey(), v);
        }
        v.addElement(si);
        si.setStock(this);
    }

    /**
    * Add the contents of one Stock to this Stock. 
    *
    * <p><STRONG>Attention: </STRONG>The original Stock is not affected, as the clone-method is
    * used for each StockItem.</p>
    *
    * @param st the Stock to be added.
    *
    * @exception DifferentCatalogException if st's Catalog is different from the Catalog of this Stock.
    */
    public synchronized void addStock(Stock st) throws Sale.DifferentCatalogException {
        if (st.getCatalog() != getCatalog()) throw new DifferentCatalogException();
        Enumeration e = st.elements();
        while (e.hasMoreElements()) {
            try {
                addItem((StockItem) ((StockItem) e.nextElement()).clone());
            } catch (NoSuchElementException ex) {
                throw new DifferentCatalogException();
            }
        }
    }

    /**
    * Subtract a Stock from this.
    *
    * <p>Deletes all the item in <i>st</i> from this Stock. An exception is thrown, if
    * <i>st</i> is not fully contained in this Stock.</p>
    *
    * @param st the Stock to be subtracted
    *
    * @exception DifferentCatalogException if st's Catalog is different from the Catalog of this Stock.
    * @exception StockNotContainedException if st is not contained in this Stock.
    * 
    * @see #containsStock
    */
    public synchronized void subtractStock(Stock st) throws DifferentCatalogException, StockNotContainedException {
        if (st.getCatalog() != getCatalog()) throw new DifferentCatalogException();
        if (!containsStock(st)) throw new StockNotContainedException();
        Enumeration e = st.elements();
        while (e.hasMoreElements()) {
            try {
                deleteItem((StockItem) ((StockItem) e.nextElement()).clone());
            } catch (NoSuchElementException ex) {
                throw new StockNotContainedException();
            }
        }
    }

    /**
    * Delete the item <I>key</I> which was added last.
    *
    * <p>Returns the deleted item or null if no item was deleted.</p>
    *
    * @param key the key of the item to be deleted.
    *
    * @return the item that was deleted.
    *
    * @exception Sale.NoSuchElementException if <I>key</I> was not found in the Stock.
    */
    public synchronized StockItem deleteItem(String key) throws Sale.NoSuchElementException {
        Vector v = (Vector) getItemContainer().get(key);
        if (v != null) return (deleteItem((StockItem) v.lastElement())); else throw new NoSuchElementException("Stock: " + getName() + " Key: " + key);
    }

    /**
    * Delete an item from the Stock.
    *
    * <p>If the Stock contains an item equal to <I>si</I> it will be deleted from the stock.</p>
    *
    * @param si the StockItem to be deleted.
    * 
    * @return the StockItem that was deleted.
    *
    * @exception NoSuchElementException if <i>si</i> was not found in the Stock.
    */
    public synchronized StockItem deleteItem(StockItem si) throws Sale.NoSuchElementException {
        Vector v = (Vector) getItemContainer().get(si.getKey());
        int nIndex = 0;
        if ((v != null) && ((nIndex = v.indexOf(si)) != -1)) {
            StockItem siRemove = (StockItem) v.elementAt(nIndex);
            v.removeElementAt(nIndex);
            if (v.isEmpty()) getItemContainer().remove(si.getKey());
            siRemove.setStock(null);
            return (siRemove);
        } else throw new NoSuchElementException("In deleteItem: Stock: " + getName() + " Key: " + si.getKey());
    }

    /**
    * Count StockItems that refer to a CatalogItem.
    *
    * <p>Returns the number of objects that refer to CatalogItem <I>key</I>.</p>
    *
    * @param key the key of the CatalogItem.
    *
    * @return the number of StockItems referring key.
    *
    * @exception NoSuchElementException if <i>key</i> does not exist in the Catalog.
    */
    public synchronized int countObjects(String key) throws NoSuchElementException {
        if (!getCatalog().containsKey(key)) throw new NoSuchElementException("Stock: " + getName() + " Key: " + key);
        Vector v = (Vector) getItemContainer().get(key);
        return ((v != null) ? v.size() : 0);
    }

    /**
    * Returns all StockItems referring to a key.
    *
    * @param key the key to be referred.
    *
    * @return an Enumeration of StockItems that refer to <I>key</I>.
    *
    * @exception NoSuchElementException if <i>key</i> does not exist in the Catalog.
    */
    public synchronized Enumeration getObjects(String key) throws NoSuchElementException {
        Vector v = (Vector) getItemContainer().get(key);
        if (v == null) throw new NoSuchElementException("Stock: " + getName() + " Key: " + key);
        return (v.elements());
    }

    /**
    * Tests whether a Stock is entirely contained in this Stock. A Stock <i>A</i> is thought to be
    * entirely  contained in a Stock <i>B</i> if all of <i>A</i>'s items can be found in <i>B</i>.
    *
    * @param s the Stock to be tested.
    *
    * @return true if <i>s</i> is entirely contained in this Stock.
    */
    public synchronized boolean containsStock(Stock s) {
        if (s.getCatalog() == getCatalog()) {
            Enumeration e = s.elements();
            while (e.hasMoreElements()) {
                try {
                    if (!containsItem((StockItem) e.nextElement())) return false;
                } catch (NoSuchElementException ex) {
                    return false;
                }
            }
            return true;
        } else return false;
    }

    /**
    * Tests if this Stock contains an item equal to <i>si</i>. To determine equality the
    * <a href="Sale.StockItem.html#equals">equals()</a>-method is used.
    *
    * @param si the StockItem to be tested for.
    *
    * @return true, if an item equal to <i>si</i> is contained in this Stock.
    *
    * @exception NoSuchElementException if si does not correspond to any CatalogItem in this Stock's Catalog.
    */
    public synchronized boolean containsItem(StockItem si) throws NoSuchElementException {
        if (!getCatalog().containsKey(si.getKey())) throw new NoSuchElementException("Stock: " + getName() + " Key: " + si.getKey());
        Vector v = (Vector) getItemContainer().get(si.getKey());
        return ((v != null) && (v.indexOf(si) != -1));
    }

    /**
    * Create a new StoringStock with the same name and Catalog as this one. The new Stock
    * will have a Hashtable for Dictionary-Implementation independantly of the
    * Dictionary-Implementation of <i>this</i>.
    */
    protected Stock createPeer() {
        return new StoringStock(getName(), getCatalog(), new DictionaryCreator());
    }
}
