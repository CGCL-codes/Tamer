package org.jpox.samples.types.arraylist;

import java.io.Serializable;
import java.util.Random;
import org.jpox.samples.types.container.ListHolder;

/**
 * Container object for FK ArrayList tests.
 *
 * @version $Revision: 1.1 $    
 **/
public class ArrayList2 implements ListHolder {

    private static Random r = new Random(0);

    private int identifierA;

    private String identifierB;

    java.util.ArrayList items = new java.util.ArrayList();

    public ArrayList2() {
        identifierA = r.nextInt();
        identifierB = String.valueOf(r.nextInt());
    }

    public java.util.Collection getItems() {
        return items;
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public int getNoOfItems() {
        return items.size();
    }

    public void addItem(Object item) {
        items.add(item);
    }

    public void addItems(java.util.Collection c) {
        items.addAll(c);
    }

    public void removeItem(Object item) {
        items.remove(item);
    }

    public void removeItems(java.util.Collection c) {
        items.removeAll(c);
    }

    public void retainItems(java.util.Collection c) {
        items.retainAll(c);
    }

    public void removeItem(int position) {
        if (position < items.size() && position >= 0) {
            items.remove(position);
        }
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getIdentifierA() {
        return identifierA;
    }

    public void setIdentifierA(int identifierA) {
        this.identifierA = identifierA;
    }

    public String getIdentifierB() {
        return identifierB;
    }

    public void setIdentifierB(String identifierB) {
        this.identifierB = identifierB;
    }

    public void setItems(java.util.Collection items) {
        this.items = (java.util.ArrayList) items;
    }

    public boolean contains(Object value) {
        return items.contains(value);
    }

    public boolean containsAll(java.util.Collection values) {
        return items.containsAll(values);
    }

    public void addItem(Object item, int position) {
        this.items.add(position, item);
    }

    public String toString() {
        return getClass().getName() + " : [" + items.size() + " items]";
    }

    public static class Oid implements Serializable {

        public int identifierA;

        public String identifierB;

        public Oid() {
        }

        public Oid(String s) {
            java.util.StringTokenizer token = new java.util.StringTokenizer(s, "::");
            s = token.nextToken();
            s = token.nextToken();
            this.identifierA = Integer.valueOf(s).intValue();
            s = token.nextToken();
            this.identifierB = s;
        }

        public String toString() {
            return this.getClass().getName() + "::" + identifierA + "::" + identifierB;
        }

        public int hashCode() {
            if (identifierB != null) {
                return identifierA ^ identifierB.hashCode();
            } else {
                return identifierA;
            }
        }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid) other;
                return k.identifierA == this.identifierA && k.identifierB.equals(this.identifierB);
            }
            return false;
        }
    }
}
