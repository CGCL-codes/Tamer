package se.sics.tasim.tac04.agents;

import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Logger;
import se.sics.isl.transport.Transportable;
import se.sics.isl.util.ArrayUtils;
import se.sics.tasim.aw.Agent;
import se.sics.tasim.aw.Message;
import se.sics.tasim.props.BOMBundle;
import se.sics.tasim.props.ComponentCatalog;
import se.sics.tasim.props.DeliveryNotice;
import se.sics.tasim.props.DeliverySchedule;
import se.sics.tasim.props.FactoryStatus;
import se.sics.tasim.props.InventoryStatus;
import se.sics.tasim.props.OfferBundle;
import se.sics.tasim.props.OrderBundle;
import se.sics.tasim.props.ProductionSchedule;
import se.sics.tasim.props.RFQBundle;
import se.sics.tasim.props.SimulationStatus;
import se.sics.tasim.props.StartInfo;

public class DummyManufacturer extends Agent {

    private static final int OFFER_RFQ_ID = 0;

    private static final int OFFER_PRODUCT_ID = 1;

    private static final int OFFER_QUANTITY = 2;

    private static final int OFFER_DUEDATE = 3;

    private static final int OFFER_PARTS = 4;

    private static final int ORDER_ID = 0;

    private static final int ORDER_OFFER_ID = 1;

    private static final int ORDER_PARTS = 2;

    private BOMBundle bomBundle;

    private ComponentCatalog catalog;

    private String factory;

    private int factoryCapacity;

    private int lowFactoryCapacity;

    private int hiFactoryCapacity;

    private int daysBeforeVoid = 5;

    private boolean isInitialized = false;

    private int rfqCounter = 0;

    private InventoryStatus currentInventory;

    private ProductionSchedule lastDayProduction;

    private DeliveryNotice[] deliveries = new DeliveryNotice[10];

    private int deliveryCount = 0;

    private InventoryStatus componentDemand = new InventoryStatus();

    private int[] customerOffers = new int[200 * OFFER_PARTS];

    private String[] customerOfferReceiver = new String[200];

    private int customerOfferCounter = 0;

    private double priceDiscountFactor = 0.3;

    private int[] customerOrders = new int[100];

    private int customerOrderStartIndex = 0;

    private int customerOrderCounter = 0;

    private int currentDate;

    private int lastBidDueDate = -1;

    private Hashtable supplyTable = new Hashtable();

    private Random random = new Random();

    private Logger log = Logger.getLogger("global");

    public DummyManufacturer() {
    }

    private void checkInitialized() {
        this.isInitialized = this.bomBundle != null && this.catalog != null && this.factory != null;
    }

    protected void simulationSetup() {
        this.log = Logger.getLogger(DummyManufacturer.class.getName() + '.' + getName());
        log.fine("dummy " + getName() + " simulationSetup");
    }

    protected void simulationFinished() {
    }

    protected void messageReceived(Message message) {
        Transportable content = message.getContent();
        if (content instanceof InventoryStatus) {
            this.currentInventory = (InventoryStatus) content;
        } else if (content instanceof OfferBundle) {
            OfferBundle offers = (OfferBundle) content;
            OrderBundle orders = new OrderBundle();
            for (int i = 0, n = offers.size(); i < n; i++) {
                orders.addOrder(getNextID(), offers.getOfferID(i));
            }
            sendMessage(message.createReply(orders));
        } else if (content instanceof RFQBundle) {
            if (isInitialized) {
                String sender = message.getSender();
                RFQBundle rfq = (RFQBundle) content;
                OfferBundle offer = new OfferBundle();
                for (int i = 0, n = rfq.size(); i < n; i++) {
                    int dueDate = rfq.getDueDate(i);
                    if ((dueDate - currentDate) >= 6 && (dueDate <= lastBidDueDate)) {
                        int productID = rfq.getProductID(i);
                        int resPrice = rfq.getReservePricePerUnit(i);
                        int bomProductIndex = bomBundle.getIndexFor(productID);
                        int basePrice = bomProductIndex >= 0 ? bomBundle.getProductBasePrice(bomProductIndex) : 0;
                        basePrice = (int) (basePrice * 0.90);
                        if (resPrice > basePrice) {
                            int quantity = rfq.getQuantity(i);
                            int offeredPrice = (int) (basePrice + (resPrice - basePrice) * (1.0 - random.nextDouble() * priceDiscountFactor));
                            int offerID = addCustomerOffer(sender, rfq.getRFQID(i), productID, quantity, dueDate);
                            offer.addOffer(offerID, rfq, i, offeredPrice);
                        }
                    }
                }
                if (offer.size() > 0) {
                    sendMessage(message.createReply(offer));
                }
            }
        } else if (content instanceof OrderBundle) {
            addCustomerOrders((OrderBundle) content);
            for (int i = 0, n = componentDemand.getProductCount(); i < n; i++) {
                int quantity = componentDemand.getQuantity(i);
                if (quantity > 0) {
                    int productID = componentDemand.getProductID(i);
                    String[] suppliers = catalog.getSuppliersForProduct(productID);
                    if (suppliers != null) {
                        int supIndex = random.nextInt(suppliers.length);
                        RFQBundle b = (RFQBundle) supplyTable.get(suppliers[supIndex]);
                        if (b == null) {
                            b = new RFQBundle();
                            supplyTable.put(suppliers[supIndex], b);
                        }
                        int rfqID = ++rfqCounter;
                        b.addRFQ(rfqID, productID, quantity, currentDate);
                        componentDemand.addInventory(productID, -quantity);
                    } else {
                        log.severe("no suppliers for product " + productID);
                    }
                }
            }
            sendMessages(supplyTable);
            supplyTable.clear();
        } else if (content instanceof DeliveryNotice) {
            addDelivery((DeliveryNotice) content);
        } else if (content instanceof SimulationStatus) {
            if (factory != null && currentInventory != null) {
                sendSchedules();
            }
            currentDate = ((SimulationStatus) content).getCurrentDate() + 1;
        } else if (content instanceof ComponentCatalog) {
            this.catalog = (ComponentCatalog) content;
            checkInitialized();
        } else if (content instanceof BOMBundle) {
            this.bomBundle = (BOMBundle) content;
            checkInitialized();
        } else if (content instanceof StartInfo) {
            StartInfo info = (StartInfo) content;
            this.lastBidDueDate = info.getNumberOfDays() - 2;
            this.daysBeforeVoid = info.getAttributeAsInt("customer.daysBeforeVoid", this.daysBeforeVoid);
            this.factory = info.getAttribute("factory.address");
            this.factoryCapacity = info.getAttributeAsInt("factory.capacity", 2000);
            this.lowFactoryCapacity = (int) (0.2 * this.factoryCapacity);
            this.hiFactoryCapacity = (int) (0.9 * this.factoryCapacity);
            checkInitialized();
        }
    }

    private int addCustomerOffer(String receiver, int rfqID, int productID, int quantity, int dueDate) {
        int index = customerOfferCounter * OFFER_PARTS;
        if (index >= customerOffers.length) {
            customerOffers = ArrayUtils.setSize(customerOffers, index + 200 * OFFER_PARTS);
            customerOfferReceiver = (String[]) ArrayUtils.setSize(customerOfferReceiver, customerOfferCounter + 200);
        }
        customerOffers[index + OFFER_RFQ_ID] = rfqID;
        customerOffers[index + OFFER_PRODUCT_ID] = productID;
        customerOffers[index + OFFER_QUANTITY] = quantity;
        customerOffers[index + OFFER_DUEDATE] = dueDate;
        customerOfferReceiver[customerOfferCounter] = receiver;
        return customerOfferCounter++;
    }

    private void addCustomerOrders(OrderBundle bundle) {
        int orderCount = bundle.size();
        int index = customerOrderStartIndex + customerOrderCounter * ORDER_PARTS;
        if ((index + orderCount * ORDER_PARTS) >= customerOrders.length) {
            int[] tmp = (customerOrderStartIndex > (ORDER_PARTS * (orderCount + 100))) ? customerOrders : new int[index + ORDER_PARTS * (orderCount + 100)];
            System.arraycopy(customerOrders, customerOrderStartIndex, tmp, 0, customerOrderCounter * ORDER_PARTS);
            customerOrders = tmp;
            customerOrderStartIndex = 0;
            index = customerOrderCounter * ORDER_PARTS;
        }
        for (int i = 0; i < orderCount; i++) {
            int orderID = bundle.getOrderID(i);
            int offerID = bundle.getOfferID(i);
            int offerIndex = offerID * OFFER_PARTS;
            int dueDate = customerOffers[offerIndex + OFFER_DUEDATE];
            if (dueDate < 0) {
                log.severe("offer " + offerID + " for new order " + orderID + " has already been handled");
            } else {
                int insertIndex = index;
                int previousOfferID;
                while (insertIndex > customerOrderStartIndex && ((previousOfferID = customerOrders[insertIndex - ORDER_PARTS + ORDER_OFFER_ID]) >= 0) && ((customerOffers[previousOfferID * OFFER_PARTS + OFFER_DUEDATE]) > dueDate)) {
                    customerOrders[insertIndex + ORDER_ID] = customerOrders[insertIndex - ORDER_PARTS + ORDER_ID];
                    customerOrders[insertIndex + ORDER_OFFER_ID] = previousOfferID;
                    insertIndex -= ORDER_PARTS;
                }
                customerOrders[insertIndex + ORDER_ID] = orderID;
                customerOrders[insertIndex + ORDER_OFFER_ID] = offerID;
                index += ORDER_PARTS;
                customerOrderCounter++;
                int productID = customerOffers[offerIndex + OFFER_PRODUCT_ID];
                int quantity = customerOffers[offerIndex + OFFER_QUANTITY];
                int[] components = bomBundle.getComponentsForProductID(productID);
                if (components != null) {
                    for (int j = 0, m = components.length; j < m; j++) {
                        componentDemand.addInventory(components[j], quantity);
                    }
                }
            }
        }
    }

    private void addDelivery(DeliveryNotice notice) {
        if (deliveryCount == deliveries.length) {
            deliveries = (DeliveryNotice[]) ArrayUtils.setSize(deliveries, deliveryCount + 20);
        }
        deliveries[deliveryCount++] = notice;
    }

    private void sendSchedules() {
        InventoryStatus inventory = new InventoryStatus(this.currentInventory);
        if (deliveryCount > 0) {
            for (int i = 0, n = deliveryCount; i < n; i++) {
                DeliveryNotice notice = deliveries[i];
                deliveries[i] = null;
                for (int j = 0, m = notice.size(); j < m; j++) {
                    inventory.addInventory(notice.getProductID(j), notice.getQuantity(j));
                }
            }
            deliveryCount = 0;
        }
        if (lastDayProduction != null) {
            for (int i = 0, n = lastDayProduction.size(); i < n; i++) {
                inventory.addInventory(lastDayProduction.getProductID(i), lastDayProduction.getQuantity(i));
            }
            lastDayProduction = null;
        }
        ProductionSchedule production = new ProductionSchedule();
        DeliverySchedule delivery = new DeliverySchedule();
        int latestDueDate = currentDate - daysBeforeVoid + 2;
        int orderEndIndex = customerOrderStartIndex + customerOrderCounter * ORDER_PARTS;
        int freeCapacity = factoryCapacity;
        for (int index = customerOrderStartIndex; index < orderEndIndex; index += ORDER_PARTS) {
            int offerID = customerOrders[index + ORDER_OFFER_ID];
            if (offerID >= 0) {
                int orderID = customerOrders[index + ORDER_ID];
                int offerIndex = offerID * OFFER_PARTS;
                int productID = customerOffers[offerIndex + OFFER_PRODUCT_ID];
                int quantity = customerOffers[offerIndex + OFFER_QUANTITY];
                int dueDate = customerOffers[offerIndex + OFFER_DUEDATE];
                int inventoryQuantity = inventory.getInventoryQuantity(productID);
                int pidIndex;
                int cyclesReq;
                int[] components;
                if ((currentDate >= (dueDate - 1)) && (inventoryQuantity >= quantity)) {
                    delivery.addDelivery(productID, quantity, orderID, customerOfferReceiver[offerID]);
                    inventory.addInventory(productID, -quantity);
                    customerOffers[offerIndex + OFFER_RFQ_ID] = -1;
                    customerOrders[index + ORDER_OFFER_ID] = -1;
                } else if (dueDate <= latestDueDate) {
                    log.info("cancelling to late order " + orderID + " (dueDate=" + dueDate + ",date=" + currentDate + ')');
                    customerOffers[offerIndex + OFFER_RFQ_ID] = -1;
                    customerOrders[index + ORDER_OFFER_ID] = -1;
                    components = bomBundle.getComponentsForProductID(productID);
                    if (components != null) {
                        for (int j = 0, m = components.length; j < m; j++) {
                            componentDemand.addInventory(components[j], -quantity);
                        }
                    }
                } else if (inventoryQuantity >= quantity) {
                    inventory.addInventory(productID, -quantity);
                } else if ((pidIndex = bomBundle.getIndexFor(productID)) < 0) {
                    log.warning("could not produce for unknown product " + productID);
                } else if ((cyclesReq = bomBundle.getAssemblyCyclesRequired(pidIndex)) <= 0) {
                } else if ((quantity * cyclesReq) > freeCapacity) {
                } else if ((components = bomBundle.getComponents(pidIndex)) == null) {
                } else if (hasAvailableComponents(components, quantity - inventoryQuantity, inventory)) {
                    freeCapacity -= quantity * cyclesReq;
                    for (int j = 0, m = components.length; j < m; j++) {
                        inventory.addInventory(components[j], -(quantity - inventoryQuantity));
                    }
                    if (inventoryQuantity > 0) {
                        inventory.addInventory(productID, -inventoryQuantity);
                    }
                    production.addProduction(productID, quantity - inventoryQuantity);
                }
            }
        }
        if (freeCapacity < lowFactoryCapacity) {
            this.priceDiscountFactor = 0.05;
        } else if (freeCapacity >= hiFactoryCapacity) {
            this.priceDiscountFactor = 0.3;
        } else {
            this.priceDiscountFactor = 0.2;
        }
        while (customerOrderStartIndex < orderEndIndex && customerOrders[customerOrderStartIndex + ORDER_OFFER_ID] < 0) {
            customerOrderStartIndex += ORDER_PARTS;
            customerOrderCounter--;
        }
        if (delivery.size() > 0) {
            sendMessage(factory, delivery);
        }
        if (production.size() > 0) {
            lastDayProduction = production;
            sendMessage(factory, production);
        }
    }

    private boolean hasAvailableComponents(int[] components, int quantity, InventoryStatus inventory) {
        for (int j = 0, m = components.length; j < m; j++) {
            if (inventory.getInventoryQuantity(components[j]) < quantity) {
                return false;
            }
        }
        return true;
    }
}
