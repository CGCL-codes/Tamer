package polr.server.object;

import org.simpleframework.xml.Element;

public class BagObject {

    @Element
    private int id;

    @Element
    private int quantity;

    public BagObject() {
    }

    public BagObject(int id, int q) {
        this.id = id;
        quantity = q;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getQuantityValue() {
        if (quantity < 10) return "0" + quantity; else return "" + quantity;
    }

    public void decreaseQuantity(int amount) {
        quantity = quantity - amount >= 0 ? quantity - amount : 0;
    }

    public void addQuantity(int amount) {
        if (quantity < 99) quantity = quantity + amount;
    }

    public int getId() {
        return id;
    }

    public String getIdValue() {
        if (id < 10) return "00" + id; else if (id < 100) return "0" + id; else return "" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BagObject) {
            BagObject b = (BagObject) o;
            if (id == b.getId()) return true; else return false;
        }
        return false;
    }
}
