package org.dpes.apriori;

import org.dpes.apriori.Krotki.Krotka;

public class IndeksBitowy {

    /**
	 * for 32bit processors 32, 64 for 64
	 */
    static final int PROC_CMP_SIZE = 32;

    private static int bitRowCount[][];

    private int[] indeks;

    public IndeksBitowy(int n) {
        if (n <= 0) throw new RuntimeException("zla wartosc dla indeksu bitowego, musi byc >0!");
        setIndeks(new int[findCellOffset(n) + 1]);
    }

    public IndeksBitowy(int[] bity) {
        setIndeks(bity);
    }

    public IndeksBitowy(IndeksBitowy ib) {
        this.setIndeks(new int[ib.podajWielkoscIndeksu()]);
        System.arraycopy(ib.getIndeks(), 0, this.getIndeks(), 0, ib.podajWielkoscIndeksu());
    }

    private int findCellOffset(int n) {
        if (n % PROC_CMP_SIZE == 0) return n / PROC_CMP_SIZE - 1; else return n / PROC_CMP_SIZE;
    }

    private int findRowOffset(int n) {
        if (n % PROC_CMP_SIZE == 0) return 0; else return PROC_CMP_SIZE - (n % PROC_CMP_SIZE);
    }

    public void setUpRow(int n) {
        int cellOffset = findCellOffset(n);
        getIndeks()[cellOffset] = getIndeks()[cellOffset] | get32bitMask(findRowOffset(n));
    }

    public void setUpRow(Krotka krotka) {
        setUpRow(krotka.getValue());
    }

    public int get32bitMask(int n) {
        if (n < 0 || n > 31) throw new RuntimeException("invalid 32bit mask offset! (" + n + ")");
        int mask = 1;
        mask <<= n;
        return mask;
    }

    public int getRowValue(int n) {
        int result = getIndeks()[findCellOffset(n)];
        result &= get32bitMask(findRowOffset(n));
        if (result != 0) return 1;
        return 0;
    }

    private static int getCellCount(int n) {
        if (bitRowCount == null) {
            bitRowCount = new int[0x0000ffff + 1][1];
            for (int i = 0; i <= 0x0000ffff; i++) {
                int sum = 0;
                int current = i;
                while (current > 0) {
                    sum += current % 2;
                    current /= 2;
                }
                bitRowCount[i][0] = sum;
            }
        }
        int left = n;
        left <<= 16;
        left >>>= 16;
        int right = n;
        right >>>= 16;
        return bitRowCount[left][0] + bitRowCount[right][0];
    }

    public int countRows() {
        int sum = 0;
        for (int i = 0; i < getIndeks().length; i++) {
            sum += getCellCount(getIndeks()[i]);
        }
        return sum;
    }

    public IndeksBitowy join(IndeksBitowy prawy) {
        if (this.podajWielkoscIndeksu() != prawy.podajWielkoscIndeksu()) throw new RuntimeException("indexes aren't the same size!");
        for (int i = 0; i < this.getIndeks().length; i++) {
            this.getIndeks()[i] &= prawy.getIndeks()[i];
        }
        return this;
    }

    public void sum(IndeksBitowy prawy) {
        if (this.podajWielkoscIndeksu() != prawy.podajWielkoscIndeksu()) throw new RuntimeException("indexes aren't the same size!");
        for (int i = 0; i < this.getIndeks().length; i++) {
            this.getIndeks()[i] |= prawy.getIndeks()[i];
        }
    }

    @Override
    public boolean equals(Object obj) {
        for (int i = 0; i < getIndeks().length; i++) {
            if (getIndeks()[i] != ((IndeksBitowy) obj).getIndeks()[i]) return false;
        }
        return true;
    }

    public int podajWielkoscIndeksu() {
        return getIndeks().length;
    }

    private void setIndeks(int[] indeks) {
        this.indeks = indeks;
    }

    public int[] getIndeks() {
        return indeks;
    }
}
