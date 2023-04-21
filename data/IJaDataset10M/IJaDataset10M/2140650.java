package datascheme;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import util.util;

public class IdStringMatcher {

    private HashMap<Integer, RankedElement> Elements;

    public IdStringMatcher() {
        this.Elements = new HashMap<Integer, RankedElement>();
    }

    public IdStringMatcher(HashMap<Integer, RankedElement> Elements) {
        this.Elements = Elements;
    }

    public String getElementString(Integer Id) {
        RankedElement temp = this.Elements.get(Id);
        if (temp == null) return "Element does not exist";
        return this.Elements.get(Id).getName();
    }

    public Integer getElementPos(Integer Id) {
        return this.Elements.get(Id).getPos();
    }

    public RankedElement getElement(Integer Id) {
        return this.Elements.get(Id);
    }

    public void setElements(HashMap<Integer, RankedElement> Elements) {
        this.Elements = Elements;
    }

    public HashMap<Integer, RankedElement> getElements() {
        return Elements;
    }

    public void addElement(Integer Id, RankedElement Element) {
        this.Elements.put(Id, Element);
    }

    public Integer addElement(RankedElement Element) {
        Random generator = new Random();
        int newId = generator.nextInt();
        while (this.Elements.containsKey(newId)) newId = generator.nextInt();
        this.Elements.put(newId, Element);
        return newId;
    }

    public Integer getMaxId() {
        Integer i = 0;
        Integer t = 0;
        Iterator<Integer> it = Elements.keySet().iterator();
        while (it.hasNext()) {
            t = it.next();
            if (i < t) i = t;
        }
        return i;
    }

    public void removeElement(Integer id) {
        this.Elements.remove(id);
    }

    /** Returns -1 if Element not found */
    public Integer getId(String Element) {
        Iterator<Integer> i = this.Elements.keySet().iterator();
        int counter = 0;
        while (i.hasNext()) {
            counter = (Integer) i.next();
            if (this.Elements.get(counter).getName().equals(Element)) return counter;
        }
        return -1;
    }

    public Boolean isElement(String lg) {
        return this.Elements.containsValue(lg);
    }

    public Boolean isId(Integer id) {
        return this.Elements.containsValue(id);
    }

    public Object[] getDBObject(Integer id) {
        Object[] dbe = new Object[3];
        RankedElement elem = this.Elements.get(id);
        dbe[0] = id;
        dbe[1] = elem.getName();
        dbe[0] = elem.getPos();
        return dbe;
    }

    public String toString() {
        String result = null;
        Iterator<Integer> i = this.Elements.keySet().iterator();
        int counter = 0;
        while (i.hasNext()) {
            counter = (Integer) i.next();
            result = result + "Key: " + counter + " Element: " + this.Elements.get(counter) + util.NewLine;
        }
        if (result == null) return "No Elements in this IdStringMatcher";
        return result;
    }
}
