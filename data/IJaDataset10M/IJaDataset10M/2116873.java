package org.vrspace.vrmlclient;

import org.vrspace.util.*;
import vrml.external.*;
import vrml.external.field.*;
import vrml.external.exception.*;
import java.net.URL;
import java.util.*;

/**
Dispatches events to the scene
*/
public class Dispatcher extends Observable {

    int debugLevel = 1;

    VRSpace applet;

    SceneManager mgr;

    Node[] nodes;

    boolean setViewpoint = true;

    Hashtable messageListeners;

    public Dispatcher(VRSpace applet, SceneManager mgr) {
        this.applet = applet;
        this.mgr = mgr;
    }

    public void reset() {
        messageListeners = new Hashtable();
    }

    public void dispatch(Message req) {
        try {
            debug(3, "Dispatching..." + req.getClassName() + " " + req.getId() + " " + req.getEventName() + " " + req.getEventValue());
            if (req.getClassName().equals("Add")) {
                if (req.getEventName().equals("className")) {
                    debug(1, "Adding " + req.getEventValue() + " " + req.getId());
                    if (req.getEventValue().equals("Transform")) {
                        mgr.addTransform(null, req.getId());
                    } else {
                        mgr.addNode(req.getEventValue(), req.getId());
                    }
                } else {
                    debug(0, "Illegal Add:" + req.getClassName() + " " + req.getId() + " " + req.getEventName() + " " + req.getEventValue());
                }
            } else if (req.getClassName().equals("Remove")) {
                if (req.getEventValue().equals("Transform")) {
                    Transform t = mgr.getTransform(req.getId());
                    if (t == null) {
                        debug(0, "Remove Transform " + req.getId() + " - it does not exist ?!");
                    } else {
                        debug(1, "Removing Transform " + req.getId());
                        mgr.removeTransform(null, req.getId());
                    }
                } else {
                    NodeManager nodeMgr = mgr.getNodeManager(req.getEventValue(), req.getId());
                    if (nodeMgr == null) {
                        debug(0, "Remove " + req.getEventValue() + " " + req.getId() + " - it does not exist ?!");
                    } else {
                        debug(1, "Removing " + req.getEventValue() + " " + req.getId());
                        mgr.removeNode(req.getEventValue(), req.getId());
                    }
                }
            } else if (req.getClassName().equals("Transform")) {
                Transform transform = mgr.getTransform(req.getId());
                try {
                    transform.setValue(req);
                } catch (NullPointerException e) {
                    Logger.logError("Transform " + req.getId() + " not found!");
                }
            } else if (req.getClassName().equals("YouAre")) {
                if (req.getEventName().equals("className")) {
                    Logger.logDebug("I Am Received: " + req.toString());
                    mgr.addMyUser(req.getEventValue(), req.getId());
                    applet.myClass = req.getEventValue();
                    applet.myId = req.getId();
                } else {
                    debug(0, "Illegal YouAre: " + req.toString());
                }
            } else if (req.getClassName().equals("Zone")) {
                Logger.logDebug("Zone Received: " + req.toString());
                applet.host = req.getEventName();
                StringTokenizer st = new StringTokenizer(req.getEventValue());
                applet.port = Integer.valueOf(st.nextToken()).intValue();
                applet.entryURL = new URL(st.nextToken());
            } else {
                debug(2, "Processing event...");
                long id = req.getId();
                if (id != 0) {
                    NodeManager nodeMgr = mgr.getNodeManager(req.getClassName(), req.getId());
                    if (nodeMgr == null) {
                    } else {
                        debug(2, "Got node manager");
                        if (nodeMgr.node == null) {
                            debug(2, "Request added to queue");
                            nodeMgr.addEvent(req);
                        } else {
                            debug(2, "Sending event...");
                            nodeMgr.send(req);
                        }
                    }
                } else {
                    debug(2, "Sending to nodes");
                    Hashtable nodeMgrs = mgr.getNodeManagers(req.getClassName());
                    if (nodeMgrs != null) {
                        Enumeration i = nodeMgrs.elements();
                        while (i.hasMoreElements()) {
                            ((NodeManager) i.nextElement()).send(req);
                        }
                    } else {
                        Logger.logError("Request for unknown node: " + req);
                    }
                    debug(2, "Sent");
                }
            }
        } catch (Throwable e) {
            Logger.logError("An error occurred while dispatching " + req, e);
        }
        String className = req.getClassName();
        long id = req.getId();
        String eventName = req.getEventName();
        String eventValue = req.getEventValue();
        Enumeration e = messageListeners.elements();
        while (e.hasMoreElements()) {
            Filter filter = (Filter) e.nextElement();
            if (filter.matches(className, id, eventName, eventValue)) {
                Vector listeners = filter.listeners;
                for (int i = 0; i < listeners.size(); i++) {
                    ((NodeManager) listeners.elementAt(i)).send(req);
                }
            }
        }
    }

    public void addListener(NodeManager nodeManager, String className, long id, String eventName, String eventValue) {
        Filter filter = new Filter(className, id, eventName, eventValue);
        String filterString = filter.toString();
        Filter oldFilter = (Filter) messageListeners.get(filterString);
        if (oldFilter != null) {
            filter = oldFilter;
        } else {
            messageListeners.put(filterString, filter);
        }
        filter.add(nodeManager);
    }

    public void removeListener(NodeManager nodeManager, String className, long id, String eventName, String eventValue) {
        String filterString = new Filter(className, id, eventName, eventValue).toString();
        Filter filter = (Filter) messageListeners.get(filterString);
        filter.remove(nodeManager);
    }

    public class Filter {

        String className;

        long id;

        String eventName;

        String eventValue;

        Vector listeners;

        public Filter(String className, long id, String eventName, String eventValue) {
            this.className = className;
            this.id = id;
            this.eventName = eventName;
            this.eventValue = eventValue;
        }

        public boolean matches(String className, long id, String eventName, String eventValue) {
            if ((this.className != null) && (!this.className.equals(className)) || (this.id != -1) && (this.id != id) || (this.eventName != null) && (!this.eventName.equals(eventName)) || (this.eventValue != null) && (!this.eventValue.equals(eventValue))) {
                return false;
            }
            return true;
        }

        public void add(NodeManager nodeManager) {
            if (listeners == null) {
                listeners = new Vector();
            }
            listeners.addElement(nodeManager);
        }

        public void remove(NodeManager nodeManager) {
            if (listeners == null) {
                listeners = new Vector();
            }
            listeners.removeElement(nodeManager);
        }
    }

    public void debug(String s) {
        debug(2, s);
    }

    public void debug(int level, String s) {
        if (level < debugLevel) {
            Logger.logDebug("Dispatcher(" + level + "): " + s);
        }
    }
}
