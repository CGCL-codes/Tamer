package isp.apps.example.shoppingadmin.action;

import com.opensymphony.xwork.ActionSupport;
import isp.apps.example.shoppingadmin.domain.TotalSalesItem;
import isp.apps.example.shoppingcart.domain.CartItem;
import isp.apps.example.shoppingcart.domain.Item;
import isp.apps.example.shoppingcart.persistence.CartItemsDirectory;
import isp.apps.example.shoppingcart.persistence.factory.CartItemsPersistenceFactory;
import isp.apps.example.shoppingcart.persistence.ItemDirectory;
import isp.apps.example.shoppingcart.persistence.factory.ItemPersistenceFactory;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CalculateSalesDistributionAction extends ActionSupport {

    private List totalSalesList = new LinkedList();

    private List itemList;

    private CartItemsDirectory myCartDir = CartItemsPersistenceFactory.getCartItemsDirectory();

    private ItemDirectory myItemDir = ItemPersistenceFactory.getItemDirectory();

    private Integer totalVolume = new Integer(0);

    private BigDecimal totalSales = new BigDecimal(0);

    private BigDecimal totalTax = new BigDecimal(0);

    public CalculateSalesDistributionAction() {
    }

    public String execute() {
        BigDecimal tempSales, tempTax;
        int tempVolume;
        TotalSalesItem tsi;
        for (Iterator itemIter = itemList.iterator(); itemIter.hasNext(); ) {
            Item item = (Item) itemIter.next();
            List il = myCartDir.getCartItemsByItemId(item.getId());
            tempVolume = 0;
            tempSales = new BigDecimal(0);
            tempTax = new BigDecimal(0);
            for (Iterator cartItemIter = il.iterator(); cartItemIter.hasNext(); ) {
                CartItem ci = (CartItem) cartItemIter.next();
                tempSales = tempSales.add(ci.getCost().multiply(new BigDecimal(ci.getQuantity())));
                tempTax = tempTax.add(ci.getTax().multiply(new BigDecimal(ci.getQuantity())));
                tempVolume += ci.getQuantity();
            }
            tsi = new TotalSalesItem();
            tsi.setItem(item);
            tsi.setTotalSales(tempSales);
            tsi.setTotalVolume(new Integer(tempVolume));
            totalVolume = new Integer(totalVolume.intValue() + tempVolume);
            totalSales = totalSales.add(tempSales);
            totalTax = totalTax.add(tempTax);
            totalSalesList.add(tsi);
        }
        calculatePercents();
        return SUCCESS;
    }

    private void calculatePercents() {
        TotalSalesItem tsi;
        for (Iterator i = totalSalesList.iterator(); i.hasNext(); ) {
            tsi = (TotalSalesItem) i.next();
            if ((tsi.getTotalVolume().compareTo(new Integer(0)) == 0) || (tsi.getTotalSales().compareTo(new BigDecimal(0)) == 0)) {
                tsi.setSalesPercent(new BigDecimal(0));
                tsi.setVolumePercent(new BigDecimal(0));
                continue;
            }
            BigDecimal tempSales = tsi.getTotalSales().divide(totalSales, 4, BigDecimal.ROUND_HALF_UP);
            tempSales = tempSales.multiply(new BigDecimal(100));
            tsi.setSalesPercent(tempSales.setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal tempVolume = new BigDecimal(tsi.getTotalVolume()).divide(new BigDecimal(this.totalVolume), 4, BigDecimal.ROUND_HALF_UP);
            tempVolume = tempVolume.multiply(new BigDecimal(100));
            tsi.setVolumePercent(tempVolume.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    }

    public void validate() {
        if (itemList == null) addActionError("Item list \"items\" is empty or null");
    }

    public void setItems(List newItemList) {
        itemList = newItemList;
    }

    public void setTotalSalesList(List totalSalesList) {
        this.totalSalesList = totalSalesList;
    }

    public List getTotalSalesList() {
        return totalSalesList;
    }

    public void setTotalVolume(Integer totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Integer getTotalVolume() {
        return totalVolume;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }
}
