package Sale;

import java.util.Vector;
import java.util.Enumeration;

/**
  * A StockFromValueCreator that restricts its input set to another Stock.
  *
  * <p>The StockItems to fill the new Stock are taken from another Stock.</p>
  *
  * <p>This algorithm does not perform any Backtracking. For an algorithm using Backtracking see
  * <a href="Sale.StockFromStockBT.html#_top_">StockFromStockBT</a>.</p>
  *
  * @see StockFromValueCreator
  * 
  * @author Steffen Zschaler
  * @version 0.5
  */
public class StockFromStock extends StockFromValueCreator {

    /**
    * The stock that contains the original StockItems.
    */
    protected Stock theOrgStock;

    /**
    * Construct a new StockFromStock-Object.
    *
    * @param civ the CatalogItemValue-object used to evaluate a CatalogItem.
    * @param theOrgStock the original Stock that contains the StockItems that can be used.
    */
    public StockFromStock(CatalogItemValue civ, Stock theOrgStock) {
        super(civ);
        this.theOrgStock = theOrgStock;
    }

    /**
    * Fills a Stock with a value.
    *
    * <p>Only items from <i>theOrgStock</i> can be used to fill the new Stock.</p>
    *
    * <p>The default implementationt does not perform any BackTracking. Thus it might
    *  not always find the best of possible solutions.</p>
    *
    * <p>E.g. if a customer needs to be paied $60 from a stock where there are only
    * items of $50 and $20 available, the algorithm will not succeed, because it always 
    * starts with the greatest value and goes on to smaller values then.</p>
    * 
    * @param theStock the Stock to be filled.
    * @param theValue the Value to be filled into <i>theStock</i>.
    *
    * @return the rest value that would not fit into the new Stock.
    */
    public float fillStock(Stock theStock, float theValue) {
        Catalog theCatalog = theStock.getCatalog();
        if (theCatalog != theOrgStock.getCatalog()) return theValue;
        Vector v = new Vector();
        {
            int lastIndex = -1;
            Enumeration e = theCatalog.elements();
            while (e.hasMoreElements()) {
                CatalogItem ci = (CatalogItem) e.nextElement();
                float flValue = theItemValuator.getValue(ci);
                int index = -1;
                for (int i = 0; i <= lastIndex; i++) {
                    if (flValue >= theItemValuator.getValue((CatalogItem) v.elementAt(i))) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) index = ++lastIndex; else lastIndex++;
                v.insertElementAt(ci, index);
            }
        }
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            CatalogItem ci = (CatalogItem) e.nextElement();
            float flValue = theItemValuator.getValue(ci);
            try {
                Enumeration orgStockObjects = theOrgStock.getObjects(ci.getKey());
                while ((theValue >= flValue) && (orgStockObjects.hasMoreElements())) {
                    theValue -= flValue;
                    try {
                        StockItem si = (StockItem) orgStockObjects.nextElement();
                        theStock.addItem(si);
                        theOrgStock.deleteItem(si);
                    } catch (Sale.NoSuchElementException ex) {
                    }
                }
            } catch (NoSuchElementException ex) {
            }
        }
        return theValue;
    }
}
