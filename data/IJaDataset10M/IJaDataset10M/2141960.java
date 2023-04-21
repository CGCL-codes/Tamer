package org.silentsquare.permutation;

import java.util.Iterator;

public class Permutation1 implements Iterator<String> {

    private int N;

    private byte[] elements;

    private int max;

    private int cur;

    private Permutation1 p1;

    public Permutation1(int n) {
        this.N = n;
        elements = new byte[N];
        for (int i = 0; i < N; i++) elements[i] = 1;
        max = N - 1;
        cur = 0;
    }

    private Permutation1(byte[] elements) {
        this.elements = elements;
        for (; cur < elements.length; cur++) {
            if (elements[cur] == 1) {
                break;
            }
        }
        for (max = elements.length - 1; max >= 0; max--) {
            if (elements[max] == 1) {
                break;
            }
        }
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == 1) {
                N++;
            }
        }
    }

    public boolean hasNext() {
        if (cur > max) return false; else return true;
    }

    public String next() {
        if (N == 1) return String.valueOf(cur++); else {
            if (p1 == null) {
                byte[] newElements = elements.clone();
                newElements[cur] = 0;
                p1 = new Permutation1(newElements);
            }
            String rv = cur + " " + p1.next();
            if (!p1.hasNext()) {
                p1 = null;
                for (++cur; cur < elements.length; cur++) {
                    if (elements[cur] == 1) {
                        break;
                    }
                }
            }
            return rv;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Permutation1 p = new Permutation1(6);
        int c = 0;
        while (p.hasNext()) {
            c++;
            System.out.println(p.next());
        }
        System.out.println(c + " permutations.");
    }
}
