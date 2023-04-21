package playground.lnicolas.convexhull;

import java.util.Vector;

public class floatVector extends Vector<Float> {

    public floatVector() {
        super(5, 10);
    }

    ;

    @Override
    public Float get(int i) {
        return elementAt(i);
    }

    ;

    public void set(int i, float value) {
        setElementAt(new Float(value), i);
    }

    ;

    public void append(float value) {
        addElement(new Float(value));
    }

    ;

    public String show() {
        String temp = new String("Size = " + size() + "\n");
        String temp2 = new String("\n");
        for (int i = 0; i < size(); i++) {
            temp2 = new String(i + ": " + get(i) + "   ");
            if ((i % 5) == 4) temp2 += "\n";
            temp += temp2;
        }
        ;
        return temp;
    }

    ;

    public void reverseHeapSort() {
        trimToSize();
        int n = size() - 1;
        int l, j, ir, i;
        float rra;
        l = (n >> 1) + 1;
        ir = n;
        for (; ; ) {
            if (l > 1) rra = get(--l); else {
                rra = get(ir);
                set(ir, get(1));
                if (--ir == 1) {
                    set(1, rra);
                    return;
                }
            }
            i = l;
            j = l << 1;
            while (j <= ir) {
                if (j < ir && get(j) > get(j + 1)) ++j;
                if (rra > get(j)) {
                    set(i, get(j));
                    j += (i = j);
                } else j = ir + 1;
            }
            set(i, rra);
        }
    }

    ;

    public void buildHeapFromTop(int n) {
        int i = 1, m;
        float top = get(1);
        while (2 * i < n) {
            m = 2 * i;
            if (get(m) < get(m + 1)) if (m < n) m++;
            if (top < get(m)) {
                set(i, get(m));
                i = m;
            } else break;
        }
        set(i, top);
    }

    ;

    public void buildHeapFromBelow(int n) {
        int i = n, m;
        float last = get(n);
        while (i / 2 > 0) {
            m = i / 2;
            if (get(m) < last) {
                set(i, get(m));
                i = m;
            } else break;
        }
        set(i, last);
    }

    ;
}

;
