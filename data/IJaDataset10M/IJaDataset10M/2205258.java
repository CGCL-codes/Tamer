package opsdebugger2.proxy;

import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import ops.KeyFilterQoSPolicy;
import ops.OPSObject;
import ops.Subscriber;
import ops.Topic;
import opsdebugger2.OPSDebugger2App;
import opsreflection.OPSFactory;

/**
 *
 * @author angr
 */
public class TopicSubscriberProxy extends ValueNotifier implements TreeModel, Runnable, Observer {

    private String topicName;

    private Object data;

    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

    private Subscriber subscriber;

    private Topic topic;

    private Vector<String> keys = new Vector<String>();

    private KeyFilterQoSPolicy keyFilter = new KeyFilterQoSPolicy();

    private String keyString;

    public TopicSubscriberProxy(String topicName) {
        this.topicName = topicName;
        OPSFactory opsf = OPSDebugger2App.getApplication().getActiveProject().getOPSFactory();
        topic = opsf.createTopic(topicName);
        String typeName = topic.getTypeID();
        data = opsf.createOPSObject(typeName);
    }

    @Override
    public synchronized boolean add(ValueListener e) {
        if (subscriber == null) {
            OPSFactory opsf = OPSDebugger2App.getApplication().getActiveProject().getOPSFactory();
            subscriber = opsf.createSubscriber(topicName);
            if (subscriber != null) {
                subscriber.addObserver(this);
                subscriber.start();
            }
        }
        return super.add(e);
    }

    public String getKeyString() {
        return keyString;
    }

    public void setKey(Vector<String> inKeys) {
        keyString = "";
        for (String string : inKeys) {
            keyString += " " + string;
        }
        if (inKeys.size() == 0) {
            subscriber.removeFilterQoSPolicy(keyFilter);
        } else {
            keyFilter.setKeys(inKeys);
            if (subscriber.getFilterQoSPolicies().size() == 0) {
                subscriber.addFilterQoSPolicy(keyFilter);
            }
        }
    }

    public Object getRoot() {
        return topic;
    }

    public Object getChild(Object parent, int index) {
        if (parent == topic) {
            return data;
        }
        try {
            if (isBasicType(parent.getClass().getFields()[index].get(parent))) {
                return parent.getClass().getFields()[index].getName();
            } else {
                if (parent.getClass().getFields()[index].get(parent) instanceof Vector) {
                    return parent.getClass().getFields()[index].getName();
                } else {
                    return parent.getClass().getFields()[index].get(parent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getChildCount(Object parent) {
        if (parent == topic) {
            return 1;
        }
        if (isBasicType(parent)) {
            return 0;
        } else {
            if (parent instanceof Vector) {
                return 0;
            }
        }
        try {
            return parent.getClass().getFields().length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public Topic getTopic() {
        return topic;
    }

    public boolean isLeaf(Object node) {
        if (isBasicType(node)) {
            return true;
        } else {
            if (node instanceof Vector<?>) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    private void notifyTreeModelListeners() {
        TreeModelEvent ev = new TreeModelEvent(data, new Object[] { data });
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeNodesChanged(ev);
        }
    }

    private boolean isBasicType(Object o) {
        if (o instanceof Integer || o instanceof Byte || o instanceof String || o instanceof Float || o instanceof Double || o instanceof Long || o instanceof Boolean) {
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(100);
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private int printFields(Field[] fields, Object o, int index, String parent) throws IllegalAccessException {
        for (int i = 0; i < fields.length; i++) {
            if (!isBasicType(fields[i].get(o))) {
                index++;
                if (fields[i].get(o) instanceof Vector<?>) {
                    Vector<?> vec = (Vector<?>) fields[i].get(o);
                    int arrIndex = 0;
                    for (Object elem : vec) {
                        index++;
                        if (!isBasicType(elem)) {
                            index = printFields(elem.getClass().getFields(), elem, index, parent + fields[i].getName() + " [" + arrIndex + "]");
                        } else {
                            notifyNewValue(parent + fields[i].getName() + " [" + arrIndex + "]", elem);
                        }
                        arrIndex++;
                    }
                } else {
                    index = printFields(fields[i].get(o).getClass().getFields(), fields[i].get(o), index, parent + fields[i].getName());
                }
            } else {
                notifyNewValue(fields[i].getName(), fields[i].get(o));
                index++;
            }
        }
        return index;
    }

    public void update(Observable o, Object arg) {
        data = arg;
        String receivedKey = ((OPSObject) arg).getKey();
        if (!keys.contains(receivedKey)) {
            getKeys().add(receivedKey);
        }
        Field[] fields = data.getClass().getFields();
        try {
            printFields(fields, data, 3, "");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public Vector<String> getKeys() {
        return keys;
    }
}
