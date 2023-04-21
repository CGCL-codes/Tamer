package matching;

import java.util.ArrayList;
import java.util.Iterator;

public class match {

    private ArrayList<item> items;

    public match(ArrayList<item> _items) {
        items = _items;
    }

    public ArrayList<item> getMatches() {
        return items;
    }

    public int getMatchSize() {
        return items.size();
    }

    public item getItem(int i) {
        return items.get(i);
    }

    public String toString() {
        String a = "";
        for (int i = 0; i < items.size(); i++) a += items.get(i).toString();
        return a;
    }

    public int getSize() {
        return items.size();
    }

    public ArrayList<Integer> getIds() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        for (int i = 0; i < items.size(); i++) {
            a.add(items.get(i).getId());
        }
        return a;
    }

    public boolean compareIds(ArrayList<Integer> a) {
        int found;
        for (int i = 0; i < items.size(); i++) {
            found = 0;
            for (int j = 0; j < a.size(); j++) {
                if (items.get(i).getId() == a.get(j)) found++;
            }
            if (found == a.size()) return true;
        }
        return false;
    }

    public boolean hasId(int n) {
        Iterator<item> itr = items.iterator();
        while (itr.hasNext()) {
            item i = itr.next();
            if (i.getId() == n) return true;
        }
        return false;
    }

    public void removeById(int id) {
        Iterator<item> itr = items.iterator();
        while (itr.hasNext()) {
            item i = itr.next();
            if (i.getId() == id) items.remove(i);
        }
    }
}
