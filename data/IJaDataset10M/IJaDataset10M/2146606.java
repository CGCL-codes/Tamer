package trader;

import java.io.Serializable;
import trader.gui.*;

public class BrokerViewImpl implements BrokerView, Serializable {

    private final transient BrokerGui gui;

    private BrokerModel model;

    private BrokerController controller;

    /** Creates a new instance of BrokerViewImpl */
    public BrokerViewImpl(BrokerModel model, BrokerGui gui) {
        System.out.println("Creating BrokerViewImpl");
        try {
            this.model = model;
            this.model.addChangeListener(this);
        } catch (Exception e) {
            System.out.println("BrokerViewImpl constructor " + e);
        }
        this.gui = gui;
    }

    public void addUserGestureListener(BrokerController b) throws BrokerException {
        System.out.println("BrokerViewImpl.addUserGestureListener " + b);
        controller = b;
        gui.addController(controller);
    }

    public void showDisplay(Object display) throws BrokerException {
        System.out.println("BrokerViewImpl.showDisplay " + display);
        gui.displayObject(display);
    }

    public void handleCustomerChange(Customer cust) throws BrokerException {
        System.out.println("BrokerViewImpl.processCustomer " + cust);
        gui.displayObject(cust);
    }
}
