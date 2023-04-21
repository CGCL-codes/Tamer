package sale;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import users.*;
import log.*;
import sale.multiwindow.*;
import sale.events.*;
import data.NameContext;
import data.NameContextException;
import data.DataBasket;
import data.Stock;
import data.Catalog;
import data.DuplicateKeyException;
import util.*;

/**
  * The central class in a SalesPoint application, responsible for central management tasks and for
  * persistence.
  *
  * <p>There is only one instance of the Shop class per application, and you can obtain, or change this central,
  * singleton instance through calls to {@link #getTheShop} or {@link #setTheShop}, resp.</p>
  *
  * <p>The Shop will manage the application's display, creating and removing customers' displays as necessary.
  * Also, the Shop will offer a central MenuSheet, from which the user can select certain central,
  * administrative actions, like shutdown, loadup, creating and removing SalesPoints, etc. This MenuSheet can,
  * of course, be adapted. See {@link #createShopMenuSheet}, if you're interested in this.</p>
  *
  * <p>The Shop can make persistent the entire current state of the application by calling just one method:
  * {@link #makePersistent}.</p>
  *
  * <p>The Shop serves as a {@link ProcessContext} for remote and background {@link SaleProcess processes},
  * which will be equipped with a {@link NullDisplay}. To find out about running processes at the Shop, see
  * {@link #runProcess} and {@link #runBackgroundProcess}.</p>
  *
  * <p>The Shop will maintain a list of customers currently active in the Shop and it will associate a
  * {@link Display display} to each of these. Only customers that are currently active in the Shop will have a
  * display associated to them. They will then hand this display on to the SalesPoint at which they are being
  * serviced, so that any communication that becomes necessary while servicing the customer will be made
  * through the customer's display.</p>
  *
  * @author Steffen Zschaler
  * @version 2.0 28/05/1999
  * @since v2.0
  */
public class Shop extends Object implements CustomerQueueListener, SerializableListener {

    /**
    * The SalesPoints that belong to the system.
    *
    * @serial
    */
    protected List m_lspSalesPoints = new LinkedList();

    /**
    * The monitor synchronizing access to the list of SalesPoints.
    */
    private transient Object m_oSalesPointsLock;

    /**
    * Return the monitor synchronizing access to the list of SalesPoints.
    *
    * @override Never
    */
    protected final Object getSalesPointsLock() {
        if (m_oSalesPointsLock == null) {
            m_oSalesPointsLock = new Object();
        }
        return m_oSalesPointsLock;
    }

    /**
    * The current SalesPoint.
    *
    * @serial
    */
    private SalesPoint m_spCurrent = null;

    /**
    * Flag indicating whether calls to {@link #setCurrentSalesPoint} are to have an effect or not. Used for
    * optimization reasons.
    *
    * @serial
    */
    private int m_nCurrentSalesPointIsAdjusting = 0;

    /**
    * A ProcessContext for one remote or background process.
    */
    protected static class ProcessHandle implements ProcessContext {

        /**
      * The process for which this is the context.
      *
      * @serial
      */
        protected SaleProcess m_p;

        /**
      * The display to be used. Defaults to {@link NullDisplay#s_ndGlobal}.
      *
      * @serial
      */
        protected Display m_d = NullDisplay.s_ndGlobal;

        /**
      * The user to be used as the current user for the process.
      *
      * @serial
      */
        protected User m_usr;

        /**
      * The DataBasket to be used.
      *
      * @serial
      */
        protected DataBasket m_db;

        /**
      * Create a new ProcessHandle.
      */
        public ProcessHandle(SaleProcess p, Display d, User usr, DataBasket db) {
            super();
            if (d != null) {
                m_d = d;
            }
            m_usr = usr;
            m_p = p;
            m_p.attach(db);
            m_p.attach(this);
        }

        public void setFormSheet(SaleProcess p, FormSheet fs) throws InterruptedException {
            if (fs != null) {
                fs.attach(p);
            }
            m_d.setFormSheet(fs);
        }

        public void popUpFormSheet(SaleProcess p, FormSheet fs) throws InterruptedException {
            if (fs != null) {
                fs.attach(p);
            }
            m_d.popUpFormSheet(fs);
        }

        public void setMenuSheet(SaleProcess p, MenuSheet ms) {
            if (ms != null) {
                ms.attach(p);
            }
            m_d.setMenuSheet(ms);
        }

        public boolean hasUseableDisplay(SaleProcess p) {
            return m_d.isUseableDisplay();
        }

        public void log(SaleProcess p, Loggable la) throws IOException {
            Shop.getTheShop().log(la);
        }

        public User getCurrentUser(SaleProcess p) {
            return m_usr;
        }

        public Stock getStock(String sName) {
            return Shop.getTheShop().getStock(sName);
        }

        public Catalog getCatalog(String sName) {
            return Shop.getTheShop().getCatalog(sName);
        }

        public void processStarted(SaleProcess p) {
        }

        public void processFinished(SaleProcess p) {
            p.detachContext();
            synchronized (Shop.getTheShop().getProcessesLock()) {
                Shop.getTheShop().m_lphProcesses.remove(this);
            }
        }

        /**
      * Suspend the process that is handled by this ProcessHandle.
      *
      * @override Never
      */
        public void suspend() throws InterruptedException {
            m_p.suspend();
        }

        /**
      * Resume the process that is handled by this ProcessHandle.
      *
      * @override Never
      */
        public void resume() {
            m_p.resume();
        }

        /**
      * Check whether the process that is handled by this ProcessHandle can be quitted.
      *
      * <p>The default implementation simply calls
      * <pre>
      *   m_p.{@link SaleProcess#canQuit canQuit (fContextDestroy)};
      * </pre>
      *
      * Called by {@link #canShutdown}.</p>
      *
      * @override Sometimes
      */
        public boolean canShutdown(boolean fContextDestroy) {
            return m_p.canQuit(fContextDestroy);
        }
    }

    /**
    * All remote or background processes currently running on this Shop, represented by their
    * {@link ProcessHandle process handles}.
    *
    * @serial
    */
    protected List m_lphProcesses = new LinkedList();

    /**
    * The monitor synchronizing access to the list of processes.
    */
    private transient Object m_oProcessesLock;

    /**
    * Return the monitor synchronizing access to the list of processes.
    *
    * @override Never
    */
    protected final Object getProcessesLock() {
        if (m_oProcessesLock == null) {
            m_oProcessesLock = new Object();
        }
        return m_oProcessesLock;
    }

    /**
    * The global catalogs.
    *
    * @serial
    */
    private Map m_mpCatalogs = new HashMap();

    /**
    * The monitor synchronizing access to the Catalogs.
    */
    private transient Object m_oCatalogsLock;

    /**
    * Return the monitor synchronizing access to the Catalogs.
    *
    * @override Never
    */
    private final Object getCatalogsLock() {
        if (m_oCatalogsLock == null) {
            m_oCatalogsLock = new Object();
        }
        return m_oCatalogsLock;
    }

    private final NameContext m_ncCatalogContext = new NameContext() {

        public void checkNameChange(DataBasket db, String sOldName, String sNewName) throws NameContextException {
            if (db != null) {
                throw new NameContextException("Rollback/commit of name changes of global Catalogs not yet implemented.");
            }
            if (m_mpCatalogs.containsKey(sNewName)) {
                throw new NameContextException("Name already spent!");
            }
        }

        public void nameHasChanged(DataBasket db, String sOldName, String sNewName) {
            m_mpCatalogs.put(sNewName, m_mpCatalogs.remove(sOldName));
        }

        public Object getNCMonitor() {
            return getCatalogsLock();
        }
    };

    /**
    * The global Stocks.
    *
    * @serial
    */
    private Map m_mpStocks = new HashMap();

    /**
    * The monitor synchronizing access to the Stocks.
    */
    private transient Object m_oStocksLock;

    /**
    * Return the monitor synchronizing access to the Stocks.
    *
    * @override Never
    */
    private final Object getStocksLock() {
        if (m_oStocksLock == null) {
            m_oStocksLock = new Object();
        }
        return m_oStocksLock;
    }

    private final NameContext m_ncStockContext = new NameContext() {

        public void checkNameChange(DataBasket db, String sOldName, String sNewName) throws NameContextException {
            if (db != null) {
                throw new NameContextException("Rollback/commit of name changes of global Stocks not yet implemented.");
            }
            if (m_mpStocks.containsKey(sNewName)) {
                throw new NameContextException("Name already spent!");
            }
        }

        public void nameHasChanged(DataBasket db, String sOldName, String sNewName) {
            m_mpStocks.put(sNewName, m_mpStocks.remove(sOldName));
        }

        public Object getNCMonitor() {
            return getStocksLock();
        }
    };

    /**
    * The current state of the Shop. One of {@link #DEAD}, {@link #RUNNING} or {@link #SUSPENDED}.
    *
    * @serial
    */
    private int m_nShopState = DEAD;

    /**
    * The monitor synchronizing access to the Shop's state.
    */
    private transient Object m_oStateLock;

    /**
    * Return the monitor synchronizing access to the Shop's state.
    *
    * @override Never
    */
    private final Object getStateLock() {
        if (m_oStateLock == null) {
            m_oStateLock = new Object();
        }
        return m_oStateLock;
    }

    /**
    * The UserManager managing all customers currently active in the Shop.
    *
    * @serial
    */
    protected UserManager m_umCurrentUsers = new UserManager();

    /**
    * The Frame containing the main Shop display.
    */
    protected transient JFrame m_jfShopFrame = null;

    /**
    * The Shop's status frame.
    */
    protected transient JFrame m_jfStatusFrame = null;

    /**
    * The title of the Shop's frame.
    *
    * @serial
    */
    protected String m_sShopFrameTitle = "Shop";

    /**
    * The title of the Shop's status frame.
    *
    * @serial
    */
    protected String m_sStatusFrameTitle = "SalesPoint Status Frame";

    /**
    * Temporary helper variable to be able to insert the MultiWindow MenuSheet into the Shop's menu.
    */
    private transient MenuSheet m_msMultiWindowMenu;

    /**
    * The Timer used by this Shop for managing the simulation time.
    *
    * @serial
    */
    protected Timer m_trTimer;

    /**
    * Objects that where registered to be made persistent.
    *
    * @serial
    */
    protected Map m_mpToPersistify = new HashMap();

    /**
    * The monitor synchronizing access to the persistent objects.
    */
    private transient Object m_oPersistifyLock = null;

    /**
    * @return the monitor synchronizing access to the persistent objects.
    *
    * @overide Never
    */
    private final Object getPersistifyLock() {
        if (m_oPersistifyLock == null) {
            m_oPersistifyLock = new Object();
        }
        return m_oPersistifyLock;
    }

    /**
    * First writes the default serializable fields, then calls {@link #onSaveFrames}.
    */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        util.Debug.print("Writing Shop!", -1);
        synchronized (getPersistifyLock()) {
            oos.defaultWriteObject();
        }
        onSaveFrames(oos);
        util.Debug.print("Finished writing Shop.", -1);
    }

    /**
    * First reads the default serializable fields, then calls {@link #onLoadFrames}.
    */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        util.Debug.print("Loading Shop...", -1);
        setTheShop(this);
        synchronized (getPersistifyLock()) {
            ois.defaultReadObject();
        }
        onLoadFrames(ois);
        util.Debug.print("Finished loading Shop.", -1);
    }

    /**
    * Sole constructor to enforce singleton pattern.
    */
    protected Shop() {
    }

    /**
    * Return the UserManager managing all users currently active.
    *
    * <p>You must not use this UserManager's add, create or remove methods, but instead
    * use the appropriate methods provided by the Shop. The UserManager should only be used
    * to log customers in at SalesPoint's or other objects.</p>
    *
    * @override Never
    *
    * @see #m_umCurrentUsers
    * @see #addActiveCustomer
    * @see #removeActiveCustomer
    */
    public UserManager getCurrentUsers() {
        return m_umCurrentUsers;
    }

    /**
    * Add a Customer to the list of currently active users for the Shop.
    *
    * <p>Only customers that are currently active in a Shop can have a display associated
    * with them. When you add a new customer to the list of currently active users, a new
    * display will be created and attached to the customer.</p>
    *
    * <p>If the customer is not known in the
    * {@link users.UserManager#getGlobalUM global UserManager} or the user known there is
    * different from this customer, an {@link users.UnknownUserException} will be thrown.
    * </p>
    *
    * @override Never
    *
    * @param cust the customer to be added to the list of currently active customers
    * @param spStart the SalesPoint at which the customer is to start.
    * @param fMakeCurrent if true, the given SalesPoint will be made the current SalesPoint and
    * the given customer's display will receive the focus.
    *
    * @exception users.UnknownUserException if the user was not known at the global
    * UserManager
    */
    public void addActiveCustomer(Customer cust, SalesPoint spStart, boolean fMakeCurrent) {
        if (getCurrentUsers().getUser(cust.getName()) != null) {
            return;
        }
        if (UserManager.getGlobalUM().getUser(cust.getName()) == cust) {
            cust.attach(createCustomerDisplay(cust));
            getCurrentUsers().addUser(cust);
        } else {
            throw new users.UnknownUserException("In addActiveCustomer: Customer unknown at global user manager.");
        }
        cust.goToSalesPoint(spStart, fMakeCurrent);
    }

    /**
    * Remove a customer from the list of currently active users.
    *
    * <p>The customer's display will be detached from the customer and closed.</p>
    *
    * @override Never
    *
    * @param cust the customer to remove from the list of currently active customers.
    */
    public void removeActiveCustomer(Customer cust) {
        cust.goToSalesPoint(null, false);
        removeCustomerDisplay(cust.detachDisplay());
        getCurrentUsers().deleteUser(cust.getName());
    }

    /**
    * Create and return a new Customer display.
    *
    * @override Never
    *
    * @param cust the customer for whom to create the display.
    *
    * @return the new display for the customer.
    */
    protected Display createCustomerDisplay(Customer cust) {
        MultiWindow mwMain = (MultiWindow) getShopFrame();
        MultiWindowHandle mwh = (MultiWindowHandle) mwMain.getNewHandle();
        mwh.setDisplayCaption(cust.getName());
        return mwh;
    }

    /**
    * Remove a customer's display.
    *
    * <p>At the time of the call, the display will already have been detached from the
    * customer</p>
    *
    * @override Never
    *
    * @param d the display to remove.
    */
    protected void removeCustomerDisplay(Display d) {
        ((MultiWindow) getShopFrame()).closeDisplay(d);
    }

    /**
    * Notification method informing the Shop that a customer was queued at a SalesPoint.
    *
    * <p>This method is <code>public</code> as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void onCustomerQueued(CustomerQueueEvent e) {
        if (e.getSalesPoint() == getCurrentSalesPoint()) {
            MultiWindow mw = (MultiWindow) getShopFrame();
            Display dSelected = mw.getSelectedDisplay();
            setCurrentSalesPoint(e.getSalesPoint());
            mw.setSelectedDisplay(dSelected);
        }
    }

    /**
    * Notification method informing the Shop that a customer was unqueued from a SalesPoint.
    *
    * <p>This method is <code>public</code> as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void onCustomerUnQueued(CustomerQueueEvent e) {
        if (e.getSalesPoint() == getCurrentSalesPoint()) {
            MultiWindow mw = (MultiWindow) getShopFrame();
            mw.makeInVisible(e.getAffectedCustomer().getDisplay());
        }
    }

    /**
    * Notification method informing the Shop that a customer became the current customer at a SalesPoint.
    *
    * <p>This method is <code>public</code> as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void onCustomerLoggedOn(SalesPoint sp, Customer cust) {
        if (sp == getCurrentSalesPoint()) {
            setCurrentSalesPoint(sp, cust);
        }
    }

    /**
    * Notification method informing the Shop that the current customer was unqueued from a SalesPoint.
    *
    * <p>This method is <code>public</code> as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void onCustomerLoggedOff(SalesPoint sp, Customer cust) {
    }

    /**
    * Add a SalesPoint to the Shop.
    *
    * @override Never Instead, override {@link #onSalesPointAdded}.
    *
    * @param sp the SalesPoint to be added.
    */
    public void addSalesPoint(SalesPoint sp) {
        synchronized (getStateLock()) {
            if (getShopState() != RUNNING) {
                try {
                    sp.suspend();
                } catch (InterruptedException e) {
                }
            }
            synchronized (getSalesPointsLock()) {
                sp.attachStatusDisplay(createStatusDisplay(sp));
                m_lspSalesPoints.add(sp);
                onSalesPointAdded(sp);
            }
        }
    }

    /**
    * Hook method performing additional work when a SalesPoint was added.
    *
    * @override Sometimes Make sure to call the super class's method if overriding this method.
    *
    * @param sp the SalesPoint that was removed from the Shop.
    */
    protected void onSalesPointAdded(final SalesPoint sp) {
        sp.addCustomerQueueListener(this);
        MenuSheet ms = ((MultiWindow) getShopFrame()).getCurrentMenuSheet();
        if (ms != null) {
            ms = (MenuSheet) ms.getTaggedItem(SET_CURRENT_SP_TAG);
            if (ms != null) {
                ms.add(new MenuSheetItem(sp.getName(), "__TAG:_SALESPOINT_SELECTOR_" + sp.getName(), new Action() {

                    public void doAction(SaleProcess p, SalesPoint _sp) throws IOException {
                        Shop.getTheShop().setCurrentSalesPoint(sp);
                    }
                }));
            }
        }
        setCurrentSalesPoint(sp);
        sp.logSalesPointOpened();
    }

    /**
    * Remove a SalesPoint from the Shop.
    *
    * <p>Prior to being removed from the Shop, the SalesPoint will be
    * {@link SalesPoint#suspend suspended}.</p>
    *
    * @override Never Instead, override {@link #onSalesPointRemoved}.
    *
    * @param sp the SalesPoint to be removed
    */
    public void removeSalesPoint(SalesPoint sp) {
        try {
            sp.suspend();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        synchronized (getSalesPointsLock()) {
            removeStatusDisplay(sp.detachStatusDisplay());
            m_lspSalesPoints.remove(sp);
            onSalesPointRemoved(sp);
        }
    }

    /**
    * Hook method called when a SalesPoint was removed from the Shop.
    *
    * @override Sometimes Make sure to call the super class's method if you override this method.
    *
    * @param sp the SalesPoint that was removed from the Shop.
    */
    protected void onSalesPointRemoved(SalesPoint sp) {
        sp.removeCustomerQueueListener(this);
        if (getCurrentSalesPoint() == sp) {
            if (m_lspSalesPoints.size() > 0) {
                setCurrentSalesPoint((SalesPoint) m_lspSalesPoints.get(0));
            } else {
                setCurrentSalesPoint(null);
            }
        }
        MenuSheet ms = ((MultiWindow) getShopFrame()).getCurrentMenuSheet();
        if (ms != null) {
            ms = (MenuSheet) ms.getTaggedItem(SET_CURRENT_SP_TAG);
            if (ms != null) {
                ms.remove("__TAG:_SALESPOINT_SELECTOR_" + sp.getName());
            }
        }
        sp.logSalesPointClosed();
    }

    /**
    * Create a status display for a SalesPoint.
    *
    * @override Never
    *
    * @param sp the SalesPoint for which to create the status display.
    */
    protected Display createStatusDisplay(SalesPoint sp) {
        MultiWindowHandle mwh = (MultiWindowHandle) ((MultiWindow) getStatusFrame()).getNewHandle();
        mwh.setDisplayCaption(sp.getName());
        ((MultiWindow) getStatusFrame()).makeVisible(mwh);
        return mwh;
    }

    /**
    * Close a status display.
    *
    * @override Never
    *
    * @param d the status display to be closed.
    */
    protected void removeStatusDisplay(Display d) {
        ((MultiWindow) getStatusFrame()).closeDisplay(d);
    }

    /**
    * Get a list of all SalesPoints in the Shop.
    *
    * <p>The list is backed by the SalesPoint's queue, but is immutable.</p>
    *
    * @override Never
    */
    public List getSalesPoints() {
        synchronized (getSalesPointsLock()) {
            return Collections.unmodifiableList(m_lspSalesPoints);
        }
    }

    /**
    * Make a SalesPoint the current SalesPoint.
    *
    * <p>If a SalesPoint is the current SalesPoint the windows representing customers queueing
    * at this SalesPoint will be displayed in some special way.</p>
    *
    * <p>By default the current customer of the SalesPoint will get the focus.</p>
    *
    * @override Never
    *
    * @param sp the SalesPoint to be the current SalesPoint from now on.
    */
    public void setCurrentSalesPoint(SalesPoint sp) {
        setCurrentSalesPoint(sp, ((sp != null) ? (sp.getCurrentCustomer()) : (null)));
    }

    /**
    * Make a SalesPoint the current SalesPoint.
    *
    * <p>If a SalesPoint is the current SalesPoint the windows representing customers queueing
    * at this SalesPoint will be displayed in some special way.</p>
    *
    * @override Never
    *
    * @param sp the SalesPoint to be the current SalesPoint from now on.
    * @param cust the customer whose display shall get the focus.
    */
    public void setCurrentSalesPoint(SalesPoint sp, Customer cust) {
        if (isCurrentSalesPointAdjusting()) {
            return;
        }
        MultiWindow mw = (MultiWindow) getShopFrame();
        List ldDisplays = new LinkedList();
        Display dSelected = null;
        if (sp != null) {
            for (Iterator i = sp.getCustomers().iterator(); i.hasNext(); ) {
                Customer c = (Customer) i.next();
                ldDisplays.add(c.getDisplay());
                if (c == cust) {
                    dSelected = c.getDisplay();
                }
            }
        }
        m_spCurrent = sp;
        mw.makeVisible(ldDisplays, dSelected);
        setShopFrameTitle(m_sShopFrameTitle);
    }

    /**
    * Set a flag that can be used to optimize setCurrentSalesPoint calls. As long as this flag is set, i.e.
    * {@link #isCurrentSalesPointAdjusting} returns true, calls to {@link #setCurrentSalesPoint} will have no
    * effect. You can reset the flag by calling {@link #resetCurrentSalesPointIsAdjusting}.
    *
    * @override Never
    */
    public void setCurrentSalesPointIsAdjusting() {
        ++m_nCurrentSalesPointIsAdjusting;
    }

    /**
    * Reset a flag that can be used to optimize setCurrentSalesPoint calls. There must be one call to
    * <code>resetCurrentSalesPointIsAdjusting</code> for each call to {@link #setCurrentSalesPointIsAdjusting}.
    * Calls to this function must be followed by an explicit call to {@link #setCurrentSalesPoint}.
    *
    * @override Never
    */
    public void resetCurrentSalesPointIsAdjusting() {
        --m_nCurrentSalesPointIsAdjusting;
    }

    /**
    * Return whether or not calls to {@link #setCurrentSalesPoint(sale.SalesPoint)} have any effect.
    *
    * @override Never
    */
    public boolean isCurrentSalesPointAdjusting() {
        return m_nCurrentSalesPointIsAdjusting > 0;
    }

    /**
    * Return the current SalesPoint of the Shop.
    *
    * @override Never
    */
    public SalesPoint getCurrentSalesPoint() {
        return m_spCurrent;
    }

    /**
    * Run a process on the Shop.
    *
    * @override Never
    *
    * @param p the process to be run.
    * @param d the display to be used by the Shop. This can be <code>null</code>, then, a {@link NullDisplay}
    * will be used.
    * @param usr the user to be the current user for the process.
    * @param db the DataBasket to be used by the process.
    */
    public void runProcess(SaleProcess p, Display d, User usr, DataBasket db) {
        synchronized (getStateLock()) {
            synchronized (getProcessesLock()) {
                m_lphProcesses.add(new ProcessHandle(p, d, usr, db));
                if (getShopState() == RUNNING) {
                    p.start();
                } else {
                    try {
                        p.suspend();
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }
    }

    /**
    * Run a background process on the Shop. A background process does not have a display. You can use
    * background processes to perform tasks that do not need user communication.
    *
    * @override Never
    *
    * @param p the process to be run.
    * @param usr the user to be the current user for the process.
    * @param db the DataBasket to be used by the process.
    */
    public void runBackgroundProcess(SaleProcess p, User usr, DataBasket db) {
        runProcess(p, null, usr, db);
    }

    /**
    * Start the Shop.
    *
    * <p>This method must not be called after load up.</p>
    *
    * @override Never
    */
    public void start() {
        synchronized (getStateLock()) {
            if (getShopState() == DEAD) {
                JFrame jf = getShopFrame();
                jf.pack();
                jf.setVisible(true);
                m_nShopState = SUSPENDED;
                resume();
            }
        }
    }

    /**
    * Suspend a running Shop. Suspending the Shop includes suspending all SalesPoints currently in the Shop.
    *
    * @override Never
    */
    public void suspend() {
        synchronized (getStateLock()) {
            if (getShopState() == RUNNING) {
                synchronized (getProcessesLock()) {
                    for (Iterator i = m_lphProcesses.iterator(); i.hasNext(); ) {
                        try {
                            ((ProcessHandle) i.next()).suspend();
                        } catch (InterruptedException ie) {
                        }
                    }
                }
                synchronized (getSalesPointsLock()) {
                    for (Iterator i = m_lspSalesPoints.iterator(); i.hasNext(); ) {
                        try {
                            ((SalesPoint) i.next()).suspend();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                m_nShopState = SUSPENDED;
            }
        }
    }

    /**
    * Resume a suspended Shop. Resuming includes resuming all SalesPoints currently in the Shop.
    *
    * @override Never
    */
    public void resume() {
        synchronized (getStateLock()) {
            if (getShopState() == SUSPENDED) {
                synchronized (getProcessesLock()) {
                    for (Iterator i = m_lphProcesses.iterator(); i.hasNext(); ) {
                        ((ProcessHandle) i.next()).resume();
                    }
                }
                synchronized (getSalesPointsLock()) {
                    for (Iterator i = m_lspSalesPoints.iterator(); i.hasNext(); ) {
                        ((SalesPoint) i.next()).resume();
                    }
                }
                m_nShopState = RUNNING;
            }
        }
    }

    /**
    * Close the Shop and quit the application.
    *
    *
    * <p>This method is linked to the &quot;Quit&quot; item in the Shop's MenuSheet as well as to the close
    * window gesture for the Shop frame.</p>
    *
    * @override Sometimes By default implemented as:
    * <pre>
    * if (Shop.{@link #getTheShop getTheShop()}.{@link #shutdown shutdown (true)}) {
    *   System.exit (0);
    * };
    * </pre>
    */
    public void quit() {
        if (Shop.getTheShop().shutdown(true)) {
            System.exit(0);
        }
        ;
    }

    /**
    * Close the Shop.
    *
    * <p>Calling this method will stop any processes currently running on any SalesPoints in
    * the Shop after calling {@link #canShutdown} to check whether shutdown is permitted at
    * the moment. The method must therefore not be called from within a process !</p>
    *
    * @override Never
    *
    * @param fPersistify if true, the current state of the Shop will be made persistent prior
    * to actually closing the Shop.
    *
    * @return true if the shutdown was successful.
    */
    public boolean shutdown(boolean fPersistify) {
        synchronized (getSalesPointsLock()) {
            synchronized (getProcessesLock()) {
                boolean fRunning = (getShopState() == RUNNING);
                if (!canShutdown(fPersistify)) {
                    return false;
                }
                if (fPersistify) {
                    try {
                        makePersistent();
                    } catch (CancelledException ce) {
                        if (fRunning) {
                            resume();
                        }
                        return false;
                    } catch (Throwable t) {
                        System.err.println("Exception occurred while making persistent: " + t);
                        t.printStackTrace();
                        if (fRunning) {
                            resume();
                        }
                        return false;
                    }
                }
                clearInternalStructures();
                m_nShopState = DEAD;
                return true;
            }
        }
    }

    /**
    * Check whether shutdown can be permitted in the current state of the system.
    *
    * <p>In this method you can assume that you are the owner of {@link #getSalesPointsLock()}
    * and {@link #getProcessesLock()}, so that you can access the list of SalesPoints and the
    * list of processes without extra synchronization.</p>
    *
    * <p>The default implementation will first {@link #suspend} the Shop, should
    * {@link #getShopState its state} be {@link #RUNNING}. It will then check all processes running on the
    * Shop. If no such processes exist or if all of them confirm that shut down is possible, it will call the
    * {@link SalesPoint#canQuit} method of any {@link SalesPoint} in the system, passing
    * <code>!fPersistify</code> as the parameter. If all SalesPoints return true, the Shop stays suspended and
    * <code>canShutdown</code> returns true. In any other case, the Shop will be {@link #resume resumed} again,
    * and false will be returned.</p>
    *
    * <p>This method is usually not called directly, but if you do, make sure to call it
    * with synchronization on {@link #getSalesPointsLock()} and {@link #getProcessesLock()}.</p>
    *
    * @override Sometimes
    *
    * @param fPersistify if true, the Shop's state will be made persistent before shutdown.
    *
    * @return true to indicate shutdown is OK; false otherwise.
    */
    protected boolean canShutdown(boolean fPersistify) {
        boolean fRunning = (getShopState() == RUNNING);
        if (fRunning) {
            suspend();
        }
        boolean fCanQuit = true;
        for (Iterator i = m_lphProcesses.iterator(); i.hasNext() && fCanQuit; ) {
            fCanQuit = ((ProcessHandle) i.next()).canShutdown(!fPersistify);
        }
        for (Iterator i = m_lspSalesPoints.iterator(); i.hasNext() && fCanQuit; ) {
            fCanQuit = ((SalesPoint) i.next()).canQuit(!fPersistify);
        }
        if (!fCanQuit) {
            if (fRunning) {
                resume();
            }
            return false;
        }
        return true;
    }

    /**
    * Return the Shop's state, being one of {@link #DEAD}, {@link #RUNNING} or {@link #SUSPENDED}.
    *
    * @override Never
    */
    public int getShopState() {
        return m_nShopState;
    }

    /**
    * Make the current state of the Shop persistent.
    *
    * @override Never
    *
    * @exception IOException if an error occurred.
    * @exception CancelledException if the retrieval of the persistance stream was cancelled by the user.
    */
    public synchronized void makePersistent() throws IOException, CancelledException {
        boolean fRunning = (getShopState() == RUNNING);
        if (fRunning) {
            suspend();
        }
        try {
            OutputStream osStream = retrievePersistanceOutStream();
            synchronized (getSalesPointsLock()) {
                synchronized (getProcessesLock()) {
                    ObjectOutputStream oosOut = new ObjectOutputStream(osStream);
                    oosOut.writeObject(this);
                    oosOut.writeObject(UserManager.getGlobalUM());
                    oosOut.writeObject(User.getGlobalPassWDGarbler());
                    oosOut.flush();
                    oosOut.close();
                    osStream.close();
                }
            }
        } finally {
            if (fRunning) {
                resume();
            }
        }
    }

    /**
    * Save the Shop's main frame's and the status frame's state to the given stream.
    *
    * @override Never
    *
    * @param oos the Stream to save to
    *
    * @exception IOException if an error occurred while saving the frames' states.
    */
    protected void onSaveFrames(ObjectOutputStream oos) throws IOException {
        ((MultiWindow) getShopFrame()).save(oos);
        ((MultiWindow) getStatusFrame()).save(oos);
    }

    /**
    * Restore the Shop's state from a Stream.
    *
    * <p><strong>Attention:</strong> Any old reference to the Shop is invalid afterwards. The new Shop can be
    * acquired through {@link #getTheShop Shop.getTheShop()}.</p>
    *
    * @override Never
    *
    * @exception IOException if an exception occurred while loading
    * @exception ClassNotFoundException if an exception occurred while loading
    * @exception CancelledException if the user cancels loading.
    */
    public synchronized void restore() throws IOException, ClassNotFoundException, CancelledException {
        InputStream isStream = retrievePersistanceInStream();
        if (!shutdown(false)) {
            throw new CancelledException();
        }
        synchronized (getSalesPointsLock()) {
            synchronized (getProcessesLock()) {
                ObjectInputStream oisIn = new ObjectInputStream(isStream);
                oisIn.readObject();
                UserManager.setGlobalUM((UserManager) oisIn.readObject());
                User.setGlobalPassWDGarbler((users.PassWDGarbler) oisIn.readObject());
                oisIn.close();
                isStream.close();
            }
        }
        synchronized (getTheShop().getStateLock()) {
            getTheShop().m_nShopState = SUSPENDED;
            getTheShop().resume();
        }
    }

    /**
    * Load the Shop's main frame's and the status frame's state from the given stream.
    *
    * @override Never
    *
    * @param ois the Stream to load from
    *
    * @exception IOException if an error occurred while loading the frames' states.
    * @exception ClassNotFoundException if an error occurred while loading the frames' states.
    */
    protected void onLoadFrames(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ((MultiWindow) getShopFrame()).load(ois);
        ((MultiWindow) getStatusFrame()).load(ois);
    }

    /**
    * Helper method creating the dialog in which the user can select the persistence file.
    *
    * @override Never
    */
    private JFileChooser getChooser() {
        JFileChooser jfcChooser = new JFileChooser();
        jfcChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File fToAccept) {
                if (fToAccept == null) return false;
                if (fToAccept.isDirectory()) return true;
                StringTokenizer stName = new StringTokenizer(fToAccept.getName(), ".");
                if (stName.hasMoreTokens()) stName.nextToken(); else return false;
                String sSuffix = null;
                while (stName.hasMoreTokens()) {
                    sSuffix = stName.nextToken();
                }
                if (sSuffix != null) return (sSuffix.toLowerCase().equals("prs")); else return false;
            }

            public String getDescription() {
                return "Persistance Files (*.prs)";
            }
        });
        jfcChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        jfcChooser.setMultiSelectionEnabled(false);
        return jfcChooser;
    }

    /**
    * Retrieve the stream to which the Shop's state is to be written.
    *
    * @override Sometimes The default implementation allows the user to select a file name and creates a stream
    * for the specified file.
    *
    * @exception IOException if an exception occurred while creating the stream
    * @exception CancelledException if the user cancelled the save process.
    */
    protected OutputStream retrievePersistanceOutStream() throws IOException, CancelledException {
        javax.swing.JFileChooser jfcChooser = getChooser();
        File fFile = null;
        do {
            if (jfcChooser.showSaveDialog(null) == JFileChooser.CANCEL_OPTION) throw new CancelledException("File choosing cancelled.");
            fFile = jfcChooser.getSelectedFile();
            if (fFile == null) throw new CancelledException("No file selected.");
            if (!jfcChooser.getFileFilter().accept(fFile) && !fFile.exists()) fFile = new File(fFile.getParent(), fFile.getName() + ".prs");
            if ((jfcChooser.accept(fFile)) && (!fFile.exists())) {
                switch(JOptionPane.showConfirmDialog(null, fFile.getAbsolutePath() + " does not exist.\nCreate?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    case JOptionPane.NO_OPTION:
                        fFile = null;
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        throw new CancelledException("File choosing cancelled.");
                    case JOptionPane.YES_OPTION:
                        fFile.createNewFile();
                }
            }
        } while (!jfcChooser.getFileFilter().accept(fFile) || fFile.isDirectory());
        return new java.io.FileOutputStream(fFile);
    }

    /**
    * Retrieve the stream from which to read the Shop's state.
    *
    * @override Sometimes The default implementation allows the user to select a file name and creates a stream
    * for the specified file.
    *
    * @exception IOException if an exception occurred while creating the stream
    * @exception CancelledException if the user cancelled the save process.
    */
    protected InputStream retrievePersistanceInStream() throws IOException, CancelledException {
        javax.swing.JFileChooser jfcChooser = getChooser();
        do {
            jfcChooser.getSelectedFile();
            if (jfcChooser.showOpenDialog(null) == javax.swing.JFileChooser.CANCEL_OPTION) throw new CancelledException("File choosing cancelled.");
        } while (!jfcChooser.getSelectedFile().exists());
        return new java.io.FileInputStream(jfcChooser.getSelectedFile());
    }

    /**
    * Set an object to be persistent. The object can be accessed at the given key.
    *
    * @override Never
    *
    * @param oKey the key at which the object can be accessed.
    * @param oToPersistify the object that is to be made persistent.
    *
    * @return the object previously stored at that key.
    */
    public Object setObjectPersistent(Object oKey, Object oToPersistify) {
        synchronized (getPersistifyLock()) {
            Object oReturn = m_mpToPersistify.remove(oKey);
            m_mpToPersistify.put(oKey, oToPersistify);
            return oReturn;
        }
    }

    /**
    * Set an object to be no longer persistent.
    *
    * @override Never
    *
    * @param oKey the key at which the object can be accessed.
    *
    * @return the object that was made transient.
    */
    public Object setObjectTransient(Object oKey) {
        synchronized (getPersistifyLock()) {
            return m_mpToPersistify.remove(oKey);
        }
    }

    /**
    * Get a persistent object.
    *
    * @override Never
    *
    * @param oKey the key that describes the object.
    *
    * @return the persistent object.
    */
    public Object getPersistentObject(Object oKey) {
        synchronized (getPersistifyLock()) {
            return m_mpToPersistify.get(oKey);
        }
    }

    /**
    * Get an iterator of all persistent objects. You can use the iterator's remove() method to make objects
    * transient.
    *
    * @override Never
    */
    public Iterator getPersistentObjects() {
        synchronized (getPersistifyLock()) {
            return m_mpToPersistify.values().iterator();
        }
    }

    /**
    * Clear the internal structures maintained by the Shop, thus finishing off shutdown.
    *
    * @override Never
    */
    protected void clearInternalStructures() {
        synchronized (getSalesPointsLock()) {
            m_lspSalesPoints.clear();
        }
        synchronized (getProcessesLock()) {
            m_lphProcesses.clear();
        }
        if (m_jfShopFrame != null) {
            m_jfShopFrame.setVisible(false);
            m_jfShopFrame.dispose();
            m_jfShopFrame = null;
        }
        if (m_jfStatusFrame != null) {
            m_jfStatusFrame.setVisible(false);
            m_jfStatusFrame.dispose();
            m_jfStatusFrame = null;
        }
    }

    /**
    * Get the Shop's main frame.
    *
    * <p>The main Shop frame will be the frame in which the Shop's menu gets displayed. By
    * default, also the customers' displays will be shown in this frame, using InternalFrames.</p>
    *
    * <p>By default this creates a {@link sale.multiwindow.MultiWindow} with the title that you specified
    * in a call to {@link #setShopFrameTitle}.</p>
    *
    * <p>When the Shop frame is set to visible, all customer displays of all currently active
    * customers must be shown as well.</p>
    *
    * @override Never
    */
    protected JFrame getShopFrame() {
        if (m_jfShopFrame == null) {
            MultiWindow mw = new MultiWindow(m_sShopFrameTitle, MultiWindow.TABBED);
            m_msMultiWindowMenu = mw.getMultiWindowMenuSheet();
            MenuSheet ms = createShopMenuSheet();
            m_msMultiWindowMenu = null;
            mw.setMenuSheet(ms);
            mw.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            mw.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    new Thread("Shop closer") {

                        public void run() {
                            Shop.getTheShop().quit();
                        }
                    }.start();
                }
            });
            m_jfShopFrame = mw;
        }
        return m_jfShopFrame;
    }

    /**
    * Set the Shop frame's title. Initially, this is &quot;Shop&quot;.
    *
    * @override Never
    *
    * @param sTitle the new title.
    */
    public void setShopFrameTitle(String sTitle) {
        m_sShopFrameTitle = sTitle;
        SalesPoint sp = getCurrentSalesPoint();
        getShopFrame().setTitle(sTitle + (((sp != null) && (sp.getName() != null)) ? (" - " + sp.getName()) : ("")));
    }

    /**
    * Get the {@link javax.swing.JFrame JFrame} to be used when displaying SalesPoint status
    * information.
    *
    * @override Never
    */
    protected JFrame getStatusFrame() {
        if (m_jfStatusFrame == null) {
            MultiWindow mw = new MultiWindow(m_sStatusFrameTitle, MultiWindow.TABBED);
            mw.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            m_jfStatusFrame = mw;
        }
        return m_jfStatusFrame;
    }

    /**
    * Set the status frame's title. Initially, this is &quot;SalesPoint Status Frame&quot;.
    *
    * @override Never
    *
    * @param sTitle the new title.
    */
    public void setStatusFrameTitle(String sTitle) {
        m_sStatusFrameTitle = sTitle;
        getStatusFrame().setTitle(sTitle);
    }

    /**
    * Set the status frame's visibility.
    *
    * @override Never
    */
    public void setStatusFrameVisible(boolean fVisible) {
        getStatusFrame().setVisible(fVisible);
    }

    /**
    * Get the status frame's visibility.
    *
    * @override Never
    */
    public boolean isStatusFrameVisible() {
        return getStatusFrame().isVisible();
    }

    /**
    * Create and return the Shop's main MenuSheet.
    *
    * <p>The default implementation will provide two MenuSheets in the Shop's MenuSheet:</p>
    *
    * <table border>
    *   <tr>
    *     <th>MenuSheet (name/tag)</th>
    *     <th>Item text</th>
    *     <th>Item tag</th>
    *     <th>Item action</th>
    *     <th>Comments</th>
    *   </tr>
    *   <tr>
    *     <td rowspan=7>Shop {@link #SHOP_MENU_TAG}</td>
    *     <td>Set current SalesPoint</td>
    *     <td>{@link #SET_CURRENT_SP_TAG}</td>
    *     <td>{@link #setCurrentSalesPoint setCurrentSalesPoint()}.</td>
    *     <td>This is a Sub-MenuSheet that shows all the SalesPoints in the Shop. The user can click the one
    *         he or she wants to select. As long as this MenuSheet is found in the Shop's MenuSheet, it will
    *         be updated by calls to {@link #addSalesPoint} and {@link #removeSalesPoint}.
    *     </td>
    *   </tr>
    *   <tr>
    *     <td>Toggle status frame</td>
    *     <td>{@link #TOGGLE_STATUS_FRAME_TAG}</td>
    *     <td>Toggle the status frame's visibility.</td>
    *     <td></td>
    *   </tr>
    *   <tr>
    *     <td><i>Separator</i></td>
    *     <td>{@link #SEPARATOR_ONE_TAG}</td>
    *     <td></td>
    *     <td></td>
    *   </tr>
    *   <tr>
    *     <td>Load...</td>
    *     <td>{@link #LOAD_TAG}</td>
    *     <td>Load a persistent Shop image.</td>
    *     <td></td>
    *   </tr>
    *   <tr>
    *     <td>Save...</td>
    *     <td>{@link #SAVE_TAG}</td>
    *     <td>Save current Shop state to create a persistant Shop image.</td>
    *     <td></td>
    *   </tr>
    *   <tr>
    *     <td><i>Separator</i></td>
    *     <td>{@link #SEPARATOR_TWO_TAG}</td>
    *     <td></td>
    *     <td></td>
    *   </tr>
    *   <tr>
    *     <td>Quit</td>
    *     <td>{@link #QUIT_SHOP_TAG}</td>
    *     <td>{@link #quit}.</td>
    *     <td></td>
    *   </tr>
    *   <tr>
    *     <td>MultiWindow {@link sale.multiwindow.MultiWindow#MULTIWINDOW_MENU_TAG}</td>
    *     <td>see {@link sale.multiwindow.MultiWindow#getMultiWindowMenuSheet}</td>
    *     <td></td>
    *     <td></td>
    *   </tr>
    * </table>
    *
    * @override Sometimes
    */
    protected MenuSheet createShopMenuSheet() {
        MenuSheet ms = new MenuSheet("Shop Menu");
        MenuSheet ms2 = new MenuSheet("Shop", SHOP_MENU_TAG, 'S');
        ms2.add(new MenuSheet("Set current SalesPoint", SET_CURRENT_SP_TAG));
        ms2.add(new MenuSheetItem("Toggle status frame", TOGGLE_STATUS_FRAME_TAG, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                Shop.getTheShop().setStatusFrameVisible(!Shop.getTheShop().isStatusFrameVisible());
            }
        }));
        ms2.add(new MenuSheetSeparator(SEPARATOR_ONE_TAG));
        MenuSheetItem msi1 = new MenuSheetItem("Load...", LOAD_TAG, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) throws Throwable {
                try {
                    Shop.getTheShop().restore();
                } catch (CancelledException cexc) {
                    JOptionPane.showMessageDialog(null, cexc.getMessage(), "Loading cancelled", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        msi1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        msi1.setMnemonic('L');
        ms2.add(msi1);
        MenuSheetItem msi2 = new MenuSheetItem("Save...", SAVE_TAG, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) throws Throwable {
                try {
                    Shop.getTheShop().makePersistent();
                } catch (CancelledException cexc) {
                    JOptionPane.showMessageDialog(null, cexc.getMessage(), "Saving cancelled", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        msi2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        msi2.setMnemonic('S');
        ms2.add(msi2);
        ms2.add(new MenuSheetSeparator(SEPARATOR_TWO_TAG));
        MenuSheetItem msi3 = new MenuSheetItem("Quit", QUIT_SHOP_TAG, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                Shop.getTheShop().quit();
            }
        });
        msi3.setMnemonic('Q');
        ms2.add(msi3);
        ms.add(ms2);
        if (m_msMultiWindowMenu != null) {
            ms.add(m_msMultiWindowMenu);
        }
        return ms;
    }

    /**
    * Get the Shop's timer. If no timer has been set using {@link #setTimer}, the default timer will be a
    * {@link StepTimer} with a {@link Step} time.
    *
    * @override Never
    *
    * @return the Shop's Timer
    */
    public Timer getTimer() {
        if (m_trTimer == null) m_trTimer = new StepTimer();
        return m_trTimer;
    }

    /**
    * Set the Shop's Timer.
    *
    * @override Never
    *
    * @param trTimer the Timer to be used from now on
    */
    public void setTimer(Timer trTimer) {
        m_trTimer = trTimer;
    }

    /**
    * Log a piece of information to the global log file.
    *
    * @override Never
    *
    * @param la the information to be logged.
    *
    * @exception IOException on any error while logging.
    */
    public void log(Loggable la) throws IOException {
        Log.getGlobalLog().log(la);
    }

    /**
    * Add a Stock to the global list of Stocks. The Stock can later be identified by its name.
    *
    * @override Never
    *
    * @param st the Stock to be added to the global list of Stocks.
    *
    * @exception DuplicateKeyException if a Stock of the same name already exists in the global list of Stocks.
    */
    public void addStock(Stock st) throws DuplicateKeyException {
        synchronized (getStocksLock()) {
            if (m_mpStocks.containsKey(st.getName())) {
                throw new DuplicateKeyException(st.getName());
            }
            m_mpStocks.put(st.getName(), st);
            st.attach(m_ncStockContext);
        }
    }

    /**
    * Remove a Stock from the global list of Stocks.
    *
    * @override Never
    *
    * @param sName the name of the Stock to be removed.
    *
    * @return the removed Stock, if any.
    */
    public Stock removeStock(String sName) {
        synchronized (getStocksLock()) {
            Stock st = (Stock) m_mpStocks.remove(sName);
            if (st != null) {
                st.detachNC();
            }
            return st;
        }
    }

    /**
    * Look up a Stock in the global Stock list.
    *
    * @override Never
    *
    * @param sName the name of the Stock to be looked up.
    *
    * @return the Stock, if any.
    */
    public Stock getStock(String sName) {
        synchronized (getStocksLock()) {
            return (Stock) m_mpStocks.get(sName);
        }
    }

    /**
    * Add a Catalog to the global table of Catalogs. The Catalog will be identifiable by its name.
    *
    * @override Never
    *
    * @param c the Catalog to be added to the global list of Catalogs
    *
    * @exception DuplicateKeyException if a Catalog of the same name already existed in the global list of
    * Catalogs.
    */
    public void addCatalog(Catalog c) throws DuplicateKeyException {
        synchronized (getCatalogsLock()) {
            if (m_mpCatalogs.containsKey(c.getName())) {
                throw new DuplicateKeyException(c.getName());
            }
            m_mpCatalogs.put(c.getName(), c);
            c.attach(m_ncCatalogContext);
        }
    }

    /**
    * Remove a catalog from the global table of Catalogs.
    *
    * @override Never
    *
    * @param sName the name of the Catalog to be removed.
    *
    * @return the Catalog that was removed, if any.
    */
    public Catalog removeCatalog(String sName) {
        synchronized (getCatalogsLock()) {
            Catalog c = (Catalog) m_mpCatalogs.remove(sName);
            if (c != null) {
                c.detachNC();
            }
            return c;
        }
    }

    /**
    * Get a Catalog from the global list of Catalogs.
    *
    * @override Never
    *
    * @param sName the name of the Catalog to be returned.
    *
    * @return the associated Catalog, if any.
    */
    public Catalog getCatalog(String sName) {
        synchronized (getCatalogsLock()) {
            return (Catalog) m_mpCatalogs.get(sName);
        }
    }

    /**
    * Constant marking the Shop's state. DEAD means the Shop was either shut down or not started yet.
    */
    public static final int DEAD = 0;

    /**
    * Constant marking the Shop's state. RUNNING means the Shop was started and neither suspended nor shutdown.
    */
    public static final int RUNNING = 1;

    /**
    * Constant marking the Shop's state. SUSPENDED means the Shop was {@link #suspend suspended}.
    */
    public static final int SUSPENDED = 2;

    /**
    * MenuSheetObject tag marking the entire Shop MenuSheet.
    */
    public static final String SHOP_MENU_TAG = "__TAG:_SHOP_MENU_";

    /**
    * MenuSheetObject tag marking the &quot;Set Current SalesPoint&quot; item.
    */
    public static final String SET_CURRENT_SP_TAG = "__TAG:_SHOP_SET_CURRENT_SALESPOINT_";

    /**
    * MenuSheetObject tag marking the &quot;Toggle Status Frame&quot; item.
    */
    public static final String TOGGLE_STATUS_FRAME_TAG = "__TAG:_SHOP_TOGGLE_STATUS_FRAME_";

    /**
    * MenuSheetObject tag marking the first separator.
    */
    public static final String SEPARATOR_ONE_TAG = "__TAG:_SHOP_SEPARATOR_1_";

    /**
    * MenuSheetObject tag marking the &quot;Load...&quot; item.
    */
    public static final String LOAD_TAG = "__TAG:_SHOP_LOAD_";

    /**
    * MenuSheetObject tag marking the &quot;Save...&quot; item.
    */
    public static final String SAVE_TAG = "__TAG:_SHOP_SAVE_";

    /**
    * MenuSheetObject tag marking the second separator.
    */
    public static final String SEPARATOR_TWO_TAG = "__TAG:_SHOP_SEPARATOR_2_";

    /**
    * MenuSheetObject tag marking the &quot;Quit&quot; item.
    */
    public static final String QUIT_SHOP_TAG = "__TAG:_SHOP_QUIT_";

    /**
    * The singleton instance of the Shop, that is used throughout the entire application.
    */
    private static Shop s_shTheShop;

    /**
    * The monitor used to synchronized access to the singleton.
    */
    private static Object s_oShopLock = new Object();

    /**
    * Get the global, singleton Shop instance.
    */
    public static Shop getTheShop() {
        synchronized (s_oShopLock) {
            if (s_shTheShop == null) {
                setTheShop(new Shop());
            }
            return s_shTheShop;
        }
    }

    /**
    * Set the global, singleton Shop instance.
    *
    * <p>This method will only have an effect the next time, {@link #getTheShop} gets called.
    * So to avoid inconsistency, use this method only in the beginning of your program, to
    * install an instance of a subclass of Shop as the global, singleton Shop instance.</p>
    *
    * @param shTheShop the new global, singleton Shop instance
    */
    public static void setTheShop(Shop shTheShop) {
        synchronized (s_oShopLock) {
            s_shTheShop = shTheShop;
        }
    }
}
