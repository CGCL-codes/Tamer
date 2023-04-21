package org.argus.jmxmodel;

import java.util.ArrayList;
import java.util.List;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.argus.event.MBeanEvent;

public class MBean extends AbstractNode {

    private List<MBeanConstructor> constructors;

    private List<MBeanOperation> operations;

    private List<MBeanAttribute> attributes;

    private List<MBeanNotification> notifications;

    private String classType;

    public MBean(String name) {
        super(null, name, null);
    }

    public MBean(String name, String description) {
        super(null, name, description);
    }

    public MBean(Domain parent, String name, String classType, String description) {
        super(parent, name, description);
        constructors = new ArrayList<MBeanConstructor>();
        operations = new ArrayList<MBeanOperation>();
        attributes = new ArrayList<MBeanAttribute>();
        notifications = new ArrayList<MBeanNotification>();
        this.classType = classType;
    }

    public void setAllUnmarked() {
        setMark(false);
        for (MBeanConstructor temp : constructors) {
            temp.setAllUnmarked();
        }
        for (MBeanOperation temp : operations) {
            temp.setAllUnmarked();
        }
        for (MBeanAttribute temp : attributes) {
            temp.setAllUnmarked();
        }
        for (MBeanNotification temp : notifications) {
            temp.setAllUnmarked();
        }
    }

    public void removeUnmarked() {
        for (int i = 0; i < constructors.size(); i++) {
            MBeanConstructor temp = getMBeanConstructor(i);
            if (temp.isMark()) {
                temp.removeUnmarked();
            } else {
                removeConstructor(i);
                i--;
            }
        }
        for (int i = 0; i < operations.size(); i++) {
            MBeanOperation temp = getMBeanOperation(i);
            if (temp.isMark()) {
                temp.removeUnmarked();
            } else {
                removeOperation(i);
                i--;
            }
        }
        for (int i = 0; i < attributes.size(); i++) {
            MBeanAttribute temp = getMBeanAttribute(i);
            if (temp.isMark()) {
                temp.removeUnmarked();
            } else {
                removeAttribute(i);
                i--;
            }
        }
        for (int i = 0; i < notifications.size(); i++) {
            MBeanNotification temp = getMBeanNotification(i);
            if (temp.isMark()) {
                temp.removeUnmarked();
            } else {
                removeNotification(i);
                i--;
            }
        }
    }

    public MBeanAttribute getMBeanAttribute(String s) {
        for (MBeanAttribute att : attributes) {
            if (att.getName().equals(s)) {
                return att;
            }
        }
        return null;
    }

    public MBeanConstructor getMBeanConstructor(String s) {
        for (MBeanConstructor temp : constructors) {
            if (temp.getName().equals(s)) {
                return temp;
            }
        }
        return null;
    }

    public MBeanOperation getMBeanOperation(String s) {
        for (MBeanOperation temp : operations) {
            if (temp.getName().equals(s)) {
                return temp;
            }
        }
        return null;
    }

    public MBeanNotification getMBeanNotification(String s) {
        for (MBeanNotification temp : notifications) {
            if (temp.getName().equals(s)) {
                return temp;
            }
        }
        return null;
    }

    public String getClassType() {
        return classType;
    }

    public Domain getParent() {
        return (Domain) parent;
    }

    public void setParent(Domain parent) {
        this.parent = parent;
    }

    public synchronized MBeanConstructor getMBeanConstructor(int i) {
        return constructors.get(i);
    }

    public synchronized int getConstructorSize() {
        return constructors.size();
    }

    public synchronized void addConstructor(MBeanConstructor constructor) {
        constructors.add(constructor);
        fireEvent(new MBeanEvent(constructor, MBeanEvent.ADDED));
    }

    public synchronized void removeConstructor(int i) {
        MBeanConstructor constructor = constructors.remove(i);
        fireEvent(new MBeanEvent(constructor, MBeanEvent.REMOVED));
    }

    public synchronized MBeanOperation getMBeanOperation(int i) {
        return operations.get(i);
    }

    public synchronized int getOperationSize() {
        return operations.size();
    }

    public synchronized void addOperation(MBeanOperation object) {
        operations.add(object);
        fireEvent(new MBeanEvent(object, MBeanEvent.ADDED));
    }

    public synchronized void removeOperation(int i) {
        MBeanOperation object = operations.remove(i);
        fireEvent(new MBeanEvent(object, MBeanEvent.REMOVED));
    }

    public synchronized MBeanAttribute getMBeanAttribute(int i) {
        return attributes.get(i);
    }

    public synchronized int getAttributeSize() {
        return attributes.size();
    }

    public synchronized void addAttribute(MBeanAttribute object) {
        attributes.add(object);
        fireEvent(new MBeanEvent(object, MBeanEvent.ADDED));
    }

    public synchronized void removeAttribute(int i) {
        MBeanAttribute object = attributes.remove(i);
        fireEvent(new MBeanEvent(object, MBeanEvent.REMOVED));
    }

    public synchronized MBeanNotification getMBeanNotification(int i) {
        return notifications.get(i);
    }

    public synchronized int getNotificationSize() {
        return notifications.size();
    }

    public synchronized void addNotification(MBeanNotification object) {
        notifications.add(object);
        fireEvent(new MBeanEvent(object, MBeanEvent.ADDED));
    }

    public synchronized void removeNotification(int i) {
        MBeanNotification object = notifications.remove(i);
        fireEvent(new MBeanEvent(object, MBeanEvent.REMOVED));
    }

    public String toString() {
        try {
            ObjectName on = new ObjectName(name);
            if (on.getKeyProperty("Name") != null) {
                return "Name=" + on.getKeyProperty("Name");
            } else if (on.getKeyProperty("name") != null) {
                return "name=" + on.getKeyProperty("name");
            } else if (on.getKeyProperty("Type") != null) {
                return "Type=" + on.getKeyProperty("Type");
            } else if (on.getKeyProperty("type") != null) {
                return "type=" + on.getKeyProperty("type");
            } else {
                return "Name=No name set";
            }
        } catch (MalformedObjectNameException e) {
            return super.toString();
        }
    }
}
