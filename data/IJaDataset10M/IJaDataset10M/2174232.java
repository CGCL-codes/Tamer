package com.servengine.ecommerce.ejb;

import com.servengine.portal.Portal;
import com.servengine.user.User;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

@Local
public interface EcommerceManagerLocal {

    public List<ItemCategory> getRootCategories(String portalid);

    public ItemCategory getCategory(String portalid, Integer categoryId);

    public void persist(ItemCategory category);

    public void removeItem(String portalid, Integer id);

    public void removeCategory(String portalid, Integer id);

    public ItemSelectField getSelectField(String portalid, Integer id);

    public void removeExtraField(String portalid, Integer id);

    public Invoice getInvoice(User user, Integer id);

    public Item getItem(String portalid, Integer id);

    public void storeExtraFieldValues(String portalid, Integer itemId, Map<Integer, String> values);

    public void persist(Item item);

    public List<ItemSelectField> getSelectFields(String portalid);

    public void removeSelectField(String portalid, Integer id);

    public ItemSelectField getItemSelectField(String portalid, Integer id);

    public ItemExtraField getItemExtraField(String portalid, Integer id);

    public void persist(ItemSelectField field);

    public void removeItemSelectFieldOption(String portalid, Integer id);

    public void persist(ItemSelectFieldOption option);

    public void storeSelectFieldOptionValues(String portalid, Integer itemId, Map<Integer, Float> optionPrices, Map<Integer, Float> optionWeights, Map<Integer, String> optionComments);

    public void removeItemSelectFieldOptionValue(String portalid, Integer id);

    public void persist(PurchaseOrder order);

    public void storeOrderStatuses(String portalid, Map<Integer, String> names, Map<Integer, String> descriptions, Integer initialStatusId);

    public void persist(PurchaseOrderStatus status);

    public void removeOrderStatus(String portalid, Integer id);

    public List<PurchaseOrderStatus> getOrderStatuses(String portalid);

    public PurchaseOrderStatus getInitialStatus(String portalid);

    public void persist(PurchaseOrderItem item);

    public Invoice createInvoice(PurchaseOrder order);

    public void merge(Invoice invoice);

    public PurchaseOrder getOrder(String portalid, Integer id);

    public Invoice getInvoice(Portal portal, Integer id);

    public ItemSelectFieldOptionValue getItemSelectFieldOptionValue(Item item, Integer id);

    public Collection<Item> getItems(String portalid, boolean all, String searchstring, Integer categoryId, int maxPrice, int minPrice, int minimumAmount, Map<Integer, String> extraFields);

    public List<Item> getRandomItems(String portalid, Integer categoryId, int maxResults);

    public List<Item> getFeaturedItems(String portalid);

    public void persist(ItemExtraField field);

    public long getItemCount(Integer categoryId);

    public List<PurchaseOrder> getUserOrders(User user);

    public List<Invoice> getInvoices(Portal portal);

    public Invoice duplicate(Invoice invoice);

    public PurchaseOrder duplicate(PurchaseOrder invoice);

    public void removeInvoice(Invoice invoiceExpurganda);

    public List<Invoice> getUnpaidInvoices(Portal portal);
}
