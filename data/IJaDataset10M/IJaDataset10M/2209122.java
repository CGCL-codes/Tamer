package hello;

import java.util.List;
import java.util.ArrayList;

public class Bouquet {

    List<Flower> flowers = new ArrayList<Flower>();

    public boolean add(String name) {
        return flowers.add(new Flower(name));
    }

    public void clear() {
        flowers.clear();
    }

    public boolean contains(Object arg0) {
        return flowers.contains(arg0);
    }

    public boolean isEmpty() {
        return flowers.isEmpty();
    }

    public int size() {
        return flowers.size();
    }
}
