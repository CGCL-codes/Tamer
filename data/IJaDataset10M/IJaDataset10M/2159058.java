package net.assimilator.utilities.opmon;

import java.util.*;
import net.jini.config.Configuration;
import net.assimilator.core.OperationalString;
import net.assimilator.core.ServiceElement;

/**
 * Manages the state of a rendered OperationalString
 */
public class OpStringManager extends Observable implements Observer {

    private OperationalString opString;

    private Vector services = new Vector();

    private LinkedList events = new LinkedList();

    private Vector nested = new Vector();

    private String name;

    private int state;

    private Configuration config;

    public static final int ERROR = 1;

    public static final int WARNING = 2;

    public static final int OKAY = 3;

    /** 
     * Creates new OpStringManager
     * 
     * @param config - A Configuration object 
     */
    public OpStringManager(Configuration config) {
        this.state = ERROR;
        this.config = config;
    }

    public void initialize(OperationalString opString) {
        this.opString = opString;
        this.name = opString.getName();
        ServiceElement[] elems = opString.getServices();
        for (int i = 0; i < elems.length; i++) addServiceElement(elems[i]);
        events.add(new EventEntry(EventEntry.INFORMATION, "Created Operational String Manager " + name));
    }

    public void addServiceElement(ServiceElement sElem) {
        ServiceManager sm = getServiceManager(sElem);
        if (sm != null) return;
        ServiceManager serviceManager = new ServiceManager(sElem, config);
        serviceManager.setServiceCount();
        serviceManager.addObserver(this);
        services.add(serviceManager);
        serviceManager.seek();
        events.add(new EventEntry(EventEntry.INFORMATION, "Added Service [" + sElem.getName() + "] to Operational String " + name));
    }

    public void removeServiceElement(ServiceElement sElem) {
        ServiceManager sm = getServiceManager(sElem);
        if (sm != null) {
            services.remove(sm);
            sm.destroy();
            sm = null;
            setChanged();
            notifyObservers();
            clearChanged();
            events.add(new EventEntry(EventEntry.INFORMATION, "Removed Service [" + sElem.getName() + "] from " + "Operational String " + name));
        }
    }

    public void updateServiceElement(ServiceElement sElem) {
        ServiceManager sm = getServiceManager(sElem);
        if (sm != null) {
            sm.setServiceElement(sElem);
            sm.updateServiceCount();
            setChanged();
            notifyObservers();
            clearChanged();
            events.add(new EventEntry(EventEntry.INFORMATION, "Updated Service [" + sElem.getName() + "] in " + "Operational String " + name));
        } else {
        }
    }

    public void refresh(OperationalString newOpString) {
        ServiceElement[] sElems = newOpString.getServices();
        Vector notRefreshed = new Vector(services);
        for (int i = 0; i < sElems.length; i++) {
            try {
                ServiceManager sm = getServiceManager(sElems[i]);
                if (sm == null) addServiceElement(sElems[i]); else {
                    sm.setServiceElement(sElems[i]);
                    sm.updateServiceCount();
                    notRefreshed.remove(sm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Enumeration e = notRefreshed.elements(); e.hasMoreElements(); ) {
            ServiceManager sm = (ServiceManager) e.nextElement();
            sm.destroy();
        }
        opString = newOpString;
        setChanged();
        notifyObservers();
        clearChanged();
        events.add(new EventEntry(EventEntry.INFORMATION, "Updated Operational String " + name));
    }

    public void destroy() {
        for (Enumeration e = services.elements(); e.hasMoreElements(); ) {
            ServiceManager sm = (ServiceManager) e.nextElement();
            sm.destroy();
            sm = null;
        }
        services.clear();
        nested.clear();
        events.clear();
        state = ERROR;
    }

    public void addNestedOpString(OpStringManager nestedOpString) {
        nested.add(nestedOpString);
        nestedOpString.addObserver(this);
    }

    public Enumeration getNestedElements() {
        return (nested.elements());
    }

    public OperationalString getOperationalString() {
        return (opString);
    }

    /**
     * The update method is called since this object is an Observer of ServiceManager 
     * objects which are Observable. This method determines the overall state of the
     * OpStringManager through a straight forward process of tallying up
     * all the services that are contained by this OpStringManager and seeing if they've
     * been discovered or not.
     *
     * <p>The OpStringManager state is determined as follows:
     * <ul>
     * <li>If all the services have been discovered the OpStringManager state is OKAY</li> 
     * <li>If none of the services have been discovered the OpStringManager state is ERROR</li>
     * <li>If some of the services have been discovered the OpStringManager state is WARNING</li>
     * </ul>
     *
     * <p>Since the OpStringManager object extends from Observable, this method will also
     * notify observers that have subscribed for changes in it's state 
     *
     * @see Observer
     * @see Observable
     */
    public void update(Observable o, Object event) {
        int numDiscovered = 0;
        int serviceCount = 0;
        if (event != null) events.add(event);
        for (Enumeration e = services.elements(); e.hasMoreElements(); ) {
            ServiceManager sm = (ServiceManager) e.nextElement();
            serviceCount += sm.getServiceCount();
        }
        for (Enumeration e = services.elements(); e.hasMoreElements(); ) {
            boolean linkOkay = false;
            ServiceManager sm = (ServiceManager) e.nextElement();
            if (sm.getMinimum() == 0) continue;
            for (Iterator it = sm.getServiceData(); it.hasNext(); ) {
                ServiceData sd = (ServiceData) it.next();
                switch(sd.getState()) {
                    case ServiceManager.DISCOVERED:
                        linkOkay = true;
                        numDiscovered++;
                        break;
                    case ServiceManager.NOTDISCOVERED:
                        break;
                    case ServiceManager.FAILED:
                        break;
                    case ServiceManager.AMBIGUOUS:
                        break;
                }
            }
            if (!linkOkay) {
                state = ERROR;
                setChanged();
                notifyObservers();
                clearChanged();
                return;
            }
        }
        if (numDiscovered == serviceCount) state = OKAY; else if (numDiscovered == 0) state = ERROR; else state = WARNING;
        state = getState();
        setChanged();
        notifyObservers();
        clearChanged();
    }

    /**
     * Get the state of this Operational String. The Operational String state will represent
     * the 'weakest link in the chain', that is if this Operational String has nested
     * Operational Strings whose state is of lesser quality then the state of this 
     * Operational will reflect that. However if this Operational Strings state is of lesser
     * quality then the returned state is the state of this Operational String
     */
    public int getState() {
        int weakest = OKAY;
        for (Enumeration e = nested.elements(); e.hasMoreElements(); ) {
            OpStringManager nestedMgr = (OpStringManager) e.nextElement();
            if (nestedMgr.getState() <= weakest) weakest = nestedMgr.getState();
        }
        return (state < weakest ? state : weakest);
    }

    /**
     * Get the name of this operational string
     */
    public String getName() {
        return (name);
    }

    /**
     * @return An Enumeration that can be used over the ServiceManager objects contained
     * by this operational string
     */
    public Enumeration getElements() {
        return (services.elements());
    }

    /**
     * @return Number of services contained by this operational string
     */
    public int getSize() {
        return (services.size());
    }

    /**
     * @return an array of EventEntry elements for this operational string 
     * that have been logged
     */
    public EventEntry[] getEvents() {
        EventEntry[] ee = new EventEntry[events.size()];
        int i = 0;
        for (Iterator it = events.iterator(); it.hasNext(); ) {
            ee[i++] = (EventEntry) it.next();
        }
        return (ee);
    }

    /**
     * @return A ServiceManager which is managing the ServiceElement. 
     */
    public ServiceManager getServiceManager(ServiceElement sElem) {
        if (sElem != null) {
            for (Enumeration e = services.elements(); e.hasMoreElements(); ) {
                ServiceManager sm = (ServiceManager) e.nextElement();
                ServiceElement sElem1 = sm.getServiceElement();
                if (sElem.equals(sElem1)) return (sm);
            }
        }
        return (null);
    }
}
