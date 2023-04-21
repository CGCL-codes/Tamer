package examSpring2007.Q1;

public class DStock {

    Item i;

    DStock next;

    public DStock(Item _i, DStock _n) {
        i = _i;
        next = _n;
    }

    public DStock add(Item i, DStock s) {
        return new DStock(i, s);
    }

    public void print() {
        DStock tmp = this;
        boolean done = false;
        while (!done) {
            if (tmp.i.number_of_stock > 0) tmp.i.print();
            if (tmp.next == null) {
                done = true;
            } else {
                tmp = tmp.next;
            }
        }
    }

    public int no_of_sofas() {
        DStock tmp = this;
        boolean done = false;
        int sofaCount = 0;
        while (!done) {
            if (tmp.i instanceof Sofa && tmp.i.number_of_stock > 0) {
                sofaCount = sofaCount + tmp.i.number_of_stock;
            }
            if (tmp.next == null) {
                done = true;
            } else {
                tmp = tmp.next;
            }
        }
        return sofaCount;
    }
}
