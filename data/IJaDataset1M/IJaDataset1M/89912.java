package de.ios.kontor.cl.order;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.util.*;
import de.ios.framework.gui.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.cl.company.AddressView;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.main.co.*;
import de.ios.kontor.sv.address.co.*;

public class OrderView extends KontorView {

    protected static String CLASS_NAME = "de.ios.kontor.od.cl.OrderView";

    protected static String VIEW_TITLE = "order";

    protected static boolean DEBUG = true;

    protected int initialMode = VIEW_MODE;

    protected CardView cardview = new CardView();

    protected SimpleOrderView simpleOrderView = null;

    protected OrderLineEntriesView entriesListView = null;

    protected Customer customer = null;

    protected Iterator orders = null;

    protected KontorView parentView = null;

    /**
   * Creates a new view
   */
    public OrderView(KontorSession _session, Properties p) {
        super(_session, p);
        if (DEBUG) Debug.println(Debug.INFO, this, "constructor...");
        customer = null;
        orders = null;
        parentView = null;
    }

    /**
   * Creates a new view
   */
    public OrderView(KontorSession _session, Customer c, Iterator od, KontorView pv) {
        super(_session);
        if (DEBUG) Debug.println(Debug.INFO, this, "constructor...");
        customer = c;
        orders = od;
        parentView = pv;
    }

    /**
   *
   */
    public void setData() {
        simpleOrderView.setData();
    }

    /**
   *
   */
    public void getData() {
        simpleOrderView.getData();
    }

    /**
   * build the dialog
   */
    protected void createDialog() {
        if (DEBUG) Debug.println(Debug.INFO, this, "createDialog()...");
        simpleOrderView = new SimpleOrderView(getSession(), customer, orders, parentView).setInitialMode(initialMode);
        entriesListView = new OrderLineEntriesView(getSession(), null, null, simpleOrderView, 3).alwaysUpdate(true);
        cardview.addCard(simpleOrderView.getViewName(), simpleOrderView);
        cardview.addCard(entriesListView.getViewName(), entriesListView);
        setLayout(new BorderLayout());
        add("Center", cardview);
    }

    /**
   * initialisation
   */
    public void kvInit() {
        if (DEBUG) Debug.println(Debug.INFO, this, "init()...");
        super.kvInit();
        createDialog();
        cardview.init();
    }

    /**
   * applet start
   */
    public void kvStart() {
        if (DEBUG) Debug.println(Debug.INFO, this, "start()...");
        super.kvStart();
        cardview.start();
    }

    /**
   * applet stop
   */
    public void kvStop() {
        if (DEBUG) Debug.println(Debug.INFO, this, "stop()...");
        cardview.stop();
        super.kvStop();
    }

    /**
   * destruction
   */
    public void kvDestroy() {
        if (DEBUG) Debug.println(Debug.INFO, this, "destroy()...");
        cardview.destroy();
        super.kvDestroy();
    }

    /**
   * destroy():
   */
    public void destroy() {
        parentView = null;
        super.destroy();
    }

    /**
   * set the initial Mode
   */
    public OrderView setInitialMode(int mode) {
        initialMode = mode;
        return this;
    }

    /**
   * @return the currently showing Order.
   */
    public Order getCurrentOrder() {
        return simpleOrderView.getCurrentOrder();
    }

    /**
   * main method used for testing the view
   */
    public static void main(String argv[]) {
        new OrderView(null, null, null, null).isStandalone().systemExitOnDestroy(true).runWindow(argv);
    }

    /**
   * returns the views name
   */
    public String getViewName() {
        return getViewName(getDesc("order_viewname"));
    }
}
