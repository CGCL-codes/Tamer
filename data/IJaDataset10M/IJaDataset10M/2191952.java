package br.gov.demoiselle.pedidos.view.managedbean;

import java.sql.Date;
import java.util.Collection;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import br.gov.demoiselle.pedidos.bean.Category;
import br.gov.demoiselle.pedidos.bean.Inventory;
import br.gov.demoiselle.pedidos.bean.Order;
import br.gov.demoiselle.pedidos.business.IInventoryBC;
import br.gov.demoiselle.pedidos.business.IOrderBC;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.view.faces.controller.AbstractManagedBean;

/**
 * This class is the main resource for managing the user interface.  It
 * all of the bound properties for the JSF UI.
 * 
 * @author Gordon Yorke
 */
public class InventoryMB extends AbstractManagedBean {

    @Injection
    protected IInventoryBC inventoryService;

    @Injection
    protected IOrderBC orderService;

    protected Category[] categories;

    protected Inventory[] inventory;

    protected Inventory[] criticalInventory;

    protected String currentCategory;

    protected int minQuantity = 5;

    protected long orderedItemId;

    protected Order currentOrder;

    protected static final String UPDATE_OPERATION = "Update";

    protected static final String CREATE_OPERATION = "Create";

    protected String orderEntryOperation;

    protected String orderModificationResult;

    protected static final String SUCCESS = "Success";

    public InventoryMB() {
    }

    public Category[] getCategories() {
        try {
            if (this.categories == null) {
                Collection<Category> tempCollection = inventoryService.getCategories();
                this.categories = tempCollection.toArray(new Category[tempCollection.size()]);
                this.currentCategory = this.categories[0].getName();
            }
        } catch (RuntimeException ex) {
            handleException(ex);
        }
        return this.categories;
    }

    public Inventory[] getInventoryForCategory() {
        try {
            if (this.inventory == null) {
                Collection<Inventory> tempCollection = inventoryService.getInventoryForCategoryMaxQuantity(this.currentCategory, Integer.MAX_VALUE);
                this.inventory = tempCollection.toArray(new Inventory[tempCollection.size()]);
            }
        } catch (RuntimeException ex) {
            handleException(ex);
        }
        return this.inventory;
    }

    public void changeCategory(ActionEvent actionEvent) {
        this.inventory = null;
        this.currentCategory = (String) actionEvent.getComponent().getAttributes().get("value");
        this.inventory = null;
        this.criticalInventory = null;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void minQuantityUpdated(ValueChangeEvent valueChangeEvent) {
        this.minQuantity = ((Integer) valueChangeEvent.getNewValue()).intValue();
        this.criticalInventory = null;
    }

    public Inventory[] getCriticalInventoryForCategory() {
        try {
            if (this.criticalInventory == null) {
                Collection<Inventory> tempCollection = inventoryService.getInventoryForCategoryMaxQuantity(this.currentCategory, this.minQuantity);
                this.criticalInventory = tempCollection.toArray(new Inventory[tempCollection.size()]);
            }
        } catch (RuntimeException ex) {
            handleException(ex);
        }
        return this.criticalInventory;
    }

    public Order[] getShippedOrdersForCurrentItem() {
        try {
            Collection<Order> tempCollection = orderService.getShippedOrdersForItem(this.orderedItemId);
            return tempCollection.toArray(new Order[tempCollection.size()]);
        } catch (RuntimeException ex) {
            handleException(ex);
        }
        return new Order[0];
    }

    public Order[] getPendingOrdersForCurrentItem() {
        try {
            Collection<Order> tempCollection = orderService.getPendingOrdersForItem(this.orderedItemId);
            return tempCollection.toArray(new Order[tempCollection.size()]);
        } catch (RuntimeException ex) {
            handleException(ex);
        }
        return new Order[0];
    }

    public void itemSelected(ActionEvent actionEvent) {
        this.orderedItemId = ((Long) ((UIParameter) actionEvent.getComponent().getFacet("extraParameter")).getValue()).longValue();
    }

    public void orderUpdate(ActionEvent actionEvent) {
        try {
            long orderId = ((Long) ((UIParameter) actionEvent.getComponent().getFacet("extraParameter")).getValue()).longValue();
            this.currentOrder = orderService.getOrderById(orderId);
            this.orderEntryOperation = UPDATE_OPERATION;
        } catch (RuntimeException ex) {
            handleException(ex);
        }
    }

    public void orderRemoved(ActionEvent actionEvent) {
        long orderId = ((Long) ((UIParameter) actionEvent.getComponent().getFacet("extraParameter")).getValue()).longValue();
        try {
            orderService.requestCancelOrder(orderId);
            this.orderModificationResult = "Success";
        } catch (OptimisticLockException ex) {
            this.orderModificationResult = "Failed to " + this.orderEntryOperation.toLowerCase() + " Order.  Order status has changed since last viewed";
        } catch (Exception ex) {
            this.orderModificationResult = "Failed to " + this.orderEntryOperation.toLowerCase() + " Order.  An unexpected Error ocurred: " + ex.toString();
        }
    }

    public void createNewOrder(ActionEvent actionEvent) {
        try {
            this.currentOrder = new Order();
            this.currentOrder.setOrderInitiated(new Date(System.currentTimeMillis()));
            this.currentOrder.setItem(orderService.getItemById(this.orderedItemId));
            this.orderEntryOperation = CREATE_OPERATION;
        } catch (RuntimeException ex) {
            handleException(ex);
        }
    }

    public void setOrderedItemId(long orderedItem) {
        this.orderedItemId = orderedItem;
    }

    public long getOrderedItemId() {
        return orderedItemId;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void completeOrderUpdate(ActionEvent actionEvent) {
        try {
            if (this.orderEntryOperation == UPDATE_OPERATION) {
                orderService.alterOrderQuantity(this.currentOrder.getOrderId(), this.currentOrder.getQuantity());
            } else {
                orderService.createNewOrder(this.currentOrder);
            }
            this.orderModificationResult = SUCCESS;
        } catch (RollbackException ex) {
            if (ex.getCause() instanceof OptimisticLockException) {
                this.orderModificationResult = "Failed to " + this.orderEntryOperation.toLowerCase() + " Order.  Order status has changed since last viewed";
            } else {
                this.orderModificationResult = "Failed to " + this.orderEntryOperation.toLowerCase() + " Order.  An unexpected Error ocurred: " + ex.toString();
            }
        } catch (Exception ex) {
            this.orderModificationResult = "Failed to " + this.orderEntryOperation.toLowerCase() + " Order.  An unexpected Error ocurred: " + ex.toString();
        }
    }

    public void setOrderEntryOperation(String orderEntryOperation) {
        this.orderEntryOperation = orderEntryOperation;
    }

    public String getOrderEntryOperation() {
        return orderEntryOperation;
    }

    public void setOrderModificationResult(String orderModificationResult) {
        this.orderModificationResult = orderModificationResult;
    }

    public String getOrderModificationResult() {
        return orderModificationResult;
    }

    public void refreshInventoryManagement(ActionEvent actionEvent) {
        this.inventory = null;
        this.criticalInventory = null;
    }

    public boolean getSuccess() {
        return orderModificationResult == SUCCESS;
    }

    public void setSuccess() {
    }

    protected void handleException(RuntimeException ex) {
        StringBuffer details = new StringBuffer();
        Throwable causes = ex;
        while (causes.getCause() != null) {
            details.append(ex.getMessage());
            details.append("    Caused by:");
            details.append(causes.getCause().getMessage());
            causes = causes.getCause();
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), details.toString());
        FacesContext.getCurrentInstance().addMessage("errorTag", message);
    }
}
