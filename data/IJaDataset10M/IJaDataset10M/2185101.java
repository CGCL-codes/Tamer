package domain.store;

import java.sql.Date;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;
import db.DataAccessLayer;
import db.GlobalDataAccessObject;
import domain.Location;
import domain.Report;
import domain.operation.OrderOperation;
import domain.priority.Priority;
import domain.service.Service;
import domain.service.ServiceShift;
import domain.store.product.Product;
import domain.store.user.Role;
import domain.store.user.User;

public class Store extends Observable {

    public static final String SERVICE_UPDATED = "Service Updated";

    public static final String WAREHOUSE_UPDATED = "Warehouse Updated";

    public static final String PENDING_ORDERS_UPDATED = "Pending Orders Updated";

    private GlobalDataAccessObject dataAccess = null;

    private static Store instance = null;

    private Vector<Service> requestedServices = null;

    private Vector<OrderOperation> orders = null;

    private Vector<OrderOperation> waitingOrders = null;

    private Vector<OrderOperation> pendingOrders = null;

    private Vector<Product> allProducts = null;

    private Vector<Service> allServices = null;

    private Vector<Location> allLocations = null;

    private Priority[] allPriorities = null;

    private int currentOrderOperationId = -10;

    private User curUser = null;

    private Date curDate = null;

    public static synchronized Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    private Store() {
        dataAccess = DataAccessLayer.getDAOObject();
        requestedServices = new Vector<Service>();
        orders = new Vector<OrderOperation>();
        waitingOrders = new Vector<OrderOperation>();
        pendingOrders = new Vector<OrderOperation>();
        allPriorities = dataAccess.getAllPriorities();
        initOrderOperations();
        initProducts();
        initServices();
        initLocations();
    }

    private void initLocations() {
        allLocations = new Vector<Location>();
        Location[] l = dataAccess.getAllLocations();
        for (Location location : l) {
            allLocations.add(location);
        }
    }

    private void initProducts() {
        allProducts = new Vector<Product>();
        Product[] p = dataAccess.getAllProducts();
        for (Product product : p) {
            allProducts.add(product);
        }
    }

    private void initServices() {
        allServices = new Vector<Service>();
        Service[] s = dataAccess.getAllServices();
        for (Service service : s) {
            allServices.add(service);
        }
    }

    private void initOrderOperations() {
        OrderOperation[] orderOperations = DataAccessLayer.getDAOObject().getAllOrderOperations();
        for (OrderOperation orderOperation : orderOperations) {
            if (orderOperation.isFinished()) {
                orders.add(orderOperation);
                Service[] services = orderOperation.getServices();
                for (Service service : services) {
                    requestedServices.add(service);
                }
            } else {
                if (orderOperation.hasRequestedProducts()) {
                    waitingOrders.add(orderOperation);
                } else {
                    pendingOrders.add(orderOperation);
                }
            }
        }
    }

    public synchronized int getNextOrderOperationId() {
        if (currentOrderOperationId == -10) {
            currentOrderOperationId = DataAccessLayer.getDAOObject().getLastOrderOperationId();
        }
        return ++currentOrderOperationId;
    }

    /**
	 * Sat�� ekran�n� model'i taraf�ndan kullan�lacakt�r. Sat�� i�lemi bittikten
	 * sonra bu method �a��r�larak Veritaban�na eri�im sa�lanacakt�r.
	 */
    public void finishOrderOperation(OrderOperation order) {
        String notifyType = null;
        if (order.isFinished()) {
            Service[] serviceArray = order.getServices();
            for (Service service : serviceArray) {
                requestedServices.add(service);
            }
            notifyType = SERVICE_UPDATED;
            orders.add(order);
            System.err.println(order.getId() + ". order has been added to finished queue");
        } else {
            waitingOrders.add(order);
            System.err.println(order.getId() + ". order has been added to waiting queue");
            notifyType = WAREHOUSE_UPDATED;
        }
        DataAccessLayer.getDAOObject().addOrder(order);
        setChanged();
        notifyObservers(notifyType);
    }

    @Override
    public void notifyObservers(Object arg) {
        System.err.println("Store - observerCount : " + countObservers());
        super.notifyObservers(arg);
    }

    public void updatePendingOrder(OrderOperation orderOperation) {
        String notifyType = null;
        Service[] serviceArray = orderOperation.getServices();
        for (Service service : serviceArray) {
            requestedServices.add(service);
        }
        notifyType = SERVICE_UPDATED;
        pendingOrders.remove(orderOperation);
        orders.add(orderOperation);
        System.err.println("updatePendingOrder-> " + orderOperation.getId() + ". order has been added to finished queue");
        DataAccessLayer.getDAOObject().updatePendingOrder(orderOperation);
        setChanged();
        notifyObservers(notifyType);
    }

    public void updateWaitingOrder(OrderOperation orderOperation) {
        String notifyType = null;
        Iterator<Product> it = orderOperation.getRequestedProducts();
        waitingOrders.remove(orderOperation);
        if (it.hasNext()) {
            notifyType = WAREHOUSE_UPDATED;
            waitingOrders.add(orderOperation);
            System.err.println("updateWaitingOrder-> " + orderOperation.getId() + ". order has been added to waiting queue");
        } else {
            notifyType = PENDING_ORDERS_UPDATED;
            pendingOrders.add(orderOperation);
            System.err.println("updateWaitingOrder-> " + orderOperation.getId() + ". order has been added to pending queue");
        }
        DataAccessLayer.getDAOObject().updateWaitingOrder(orderOperation);
        setChanged();
        notifyObservers(notifyType);
    }

    public Service[] getRequestedServices() {
        int len = requestedServices.size();
        Service[] serviceArray = new Service[len];
        return (Service[]) requestedServices.toArray(serviceArray);
    }

    public OrderOperation[] getWaitingOrderOperations() {
        return (OrderOperation[]) toOrderOpeartionArray(waitingOrders);
    }

    public OrderOperation[] getPendingOperations() {
        return (OrderOperation[]) toOrderOpeartionArray(pendingOrders);
    }

    public OrderOperation[] getOrderOperations() {
        return toOrderOpeartionArray(orders);
    }

    @SuppressWarnings("unchecked")
    private OrderOperation[] toOrderOpeartionArray(Vector vector) {
        int len = vector.size();
        OrderOperation[] objectArray = new OrderOperation[len];
        return (OrderOperation[]) vector.toArray(objectArray);
    }

    public User getCurrentUser() {
        if (curUser == null) {
            curUser = new User("1123435", "admin", "admin", "admin", new Role("Admin"));
        }
        return curUser;
    }

    public void setCurrentUser(String userName) {
        curUser = dataAccess.getUser(userName);
    }

    public Date getCurrentDate() {
        if (curDate == null) {
            curDate = Date.valueOf("2009-01-01");
        }
        return curDate;
    }

    public Product[] getAllProducts() {
        int size = allProducts.size();
        Product[] p = new Product[size];
        return allProducts.toArray(p);
    }

    public Service[] getAllServices() {
        int size = allServices.size();
        Service[] s = new Service[size];
        return allServices.toArray(s);
    }

    public ServiceShift[] getAvailableServiceShifts(Service currentService) {
        return dataAccess.getAvailableServiceShifts(currentService);
    }

    public Location[] getAllLocations() {
        int size = allLocations.size();
        Location[] l = new Location[size];
        return allLocations.toArray(l);
    }

    public Priority[] getAllPriorities() {
        return allPriorities;
    }

    public Report[] getReports() {
        Report[] reports = new Report[6];
        reports[0] = dataAccess.getMoneyReceivedDailyReport();
        reports[1] = dataAccess.getMoneyReceivedWeeklyReport();
        reports[2] = dataAccess.getMoneyReceivedMonthyReport();
        reports[3] = dataAccess.getNumberOfOrdersDaily();
        reports[4] = dataAccess.getNumberOfOrdersWeekly();
        reports[5] = dataAccess.getNumberOfOrdersMonthly();
        return reports;
    }

    public void addNewLocation(Location location) {
        dataAccess.addLocation(location);
        initLocations();
    }

    public void addNewPriority(Priority priority) {
        dataAccess.addPriority(priority);
        allPriorities = dataAccess.getAllPriorities();
    }

    public void addNewService(Service service) {
        dataAccess.addService(service);
        initServices();
    }

    public void logout() {
        curUser = null;
    }

    public void updateProduct() {
        initProducts();
    }
}
