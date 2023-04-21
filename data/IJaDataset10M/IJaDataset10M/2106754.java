package org.sinaxe.components;

import org.sinaxe.*;
import org.sinaxe.util.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.geom.Point2D;

public class SinaxeSubscriber implements SinaxeComponent, SinaxeDataTypes {

    private SinaxeVariable oldvalueVariable;

    private SinaxeVariable newvalueVariable;

    private SinaxeQuery watchQuery;

    private SinaxeQuery listQuery;

    private SinaxeSubscription listSubscription;

    private SinaxeOutPort changedOutPort;

    private SinaxeOutPort insertedOutPort;

    private SinaxeOutPort removedOutPort;

    private SinaxeInPort loadInPort;

    private ArrayList watchSubscriptions = new ArrayList();

    private String type;

    public SinaxeSubscriber() {
    }

    protected void watch(Object context) {
        SinaxeSubscription newWatch = watchQuery.newSubscription();
        if (type.equals("string")) {
            newWatch.getString(context);
        } else if (type.equals("boolean")) {
            newWatch.getBoolean(context);
        } else if (type.equals("number")) {
            newWatch.getNumber(context);
        } else if (type.equals("node")) {
            newWatch.getNode(context);
        } else if (type.equals("nodes")) {
            newWatch.getNodes(context);
        }
        final Object finalContext = context;
        newWatch.addSinaxeSubscriptionListener(new SinaxeSubscriptionListener() {

            public void valueChanged(SinaxeSubscriptionEvent event) {
                oldvalueVariable.setValue(event.getOldValue());
                newvalueVariable.setValue(event.getNewValue());
                changedOutPort.send(finalContext);
            }

            public void nodeChanged(SinaxeSubscriptionEvent event) {
                oldvalueVariable.setValue(event.getOldValue());
                newvalueVariable.setValue(event.getNewValue());
                changedOutPort.send(event.getNewValue());
            }

            public void nodesInserted(SinaxeSubscriptionEvent event) {
                insertedOutPort.send(event.getNodes());
            }

            public void nodesChanged(SinaxeSubscriptionEvent event) {
                changedOutPort.send(event.getNodes());
            }

            public void nodesRemoved(SinaxeSubscriptionEvent event) {
                removedOutPort.send(event.getNodes());
            }
        });
        watchSubscriptions.add(newWatch);
    }

    protected void releaseList() {
        for (int i = 0; i < watchSubscriptions.size(); i++) ((SinaxeSubscription) watchSubscriptions.get(i)).releaseContext();
        watchSubscriptions.clear();
    }

    protected void loadList(List contextList) {
        for (int i = 0; i < contextList.size(); i++) watch(contextList.get(i));
    }

    protected void load(Object context) {
        if (listQuery != null) {
            listSubscription.releaseContext();
            releaseList();
            loadList(listSubscription.getNodes(context));
        } else watch(context);
    }

    public void sinaxeInit(SinaxeRuntime runtime, SinaxeProperty properties) throws SinaxeException {
        properties.require("type");
        type = properties.getValue("type");
        if (type.equals("string")) {
        } else if (type.equals("boolean")) {
        } else if (type.equals("number")) {
        } else if (type.equals("node")) {
        } else if (type.equals("nodes")) {
        } else {
            runtime.issueError("Invalid type '" + type + "'!");
        }
        oldvalueVariable = runtime.registerVariable("oldvalue");
        newvalueVariable = runtime.registerVariable("newvalue");
        watchQuery = runtime.getQuery("watch", true);
        listQuery = runtime.getQuery("list", false);
        if (listQuery != null) {
            listSubscription = listQuery.newSubscription();
            listSubscription.addSinaxeSubscriptionListener(new DefaultSinaxeSubscriptionListener() {

                public void nodesInserted(SinaxeSubscriptionEvent event) {
                    loadList(event.getNodes());
                }

                public void nodesRemoved(SinaxeSubscriptionEvent event) {
                    releaseList();
                    loadList(listSubscription.getNodes(listSubscription.getContext()));
                }
            });
        }
        changedOutPort = runtime.registerOutPort("changed", DT_ANY);
        insertedOutPort = runtime.registerOutPort("inserted", DT_ANY);
        removedOutPort = runtime.registerOutPort("removed", DT_ANY);
        loadInPort = runtime.registerInPort("load", DT_ANY);
        loadInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object context) {
                load(context);
            }
        });
    }

    public void sinaxeExit() throws SinaxeException {
    }

    public Component sinaxeGetComponent() {
        return null;
    }

    public String sinaxeGetDocumentation() {
        return null;
    }

    public Object sinaxePointToContext(java.awt.Point source) {
        return null;
    }
}
