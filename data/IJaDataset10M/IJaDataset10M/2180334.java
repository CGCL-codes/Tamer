package merge_sort;

/**
 *
 * @author bm
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] dizi = new int[6];
        dizi[0] = 12;
        dizi[1] = 8;
        dizi[2] = 54;
        dizi[3] = 25;
        dizi[4] = 36;
        dizi[5] = 11;
    }

    public void merge(int i, int j) {
        int sayac = 0;
        int[] dizi = new int[6];
        if (i == j) return;
        int orta = (i + j) / 2;
        int[] tempdizi = new int[j - i + 1];
        merge(i, orta);
        merge(orta + 1, j);
        int k = i, m = orta + 1;
        for (k = i; k <= orta; k++) {
            for (m = orta + 1; m <= j; m++) {
                if (dizi[k] > dizi[m]) {
                    tempdizi[sayac] = dizi[m];
                    dizi[m] = -1;
                } else {
                    tempdizi[sayac] = dizi[k];
                    dizi[k] = -1;
                    k++;
                    sayac++;
                    if (k < orta) {
                        break;
                    }
                }
            }
            for (; m <= j; m++) {
                tempdizi[sayac] = dizi[m];
                sayac++;
            }
        }
        for (int s = i; s <= j; s++) {
            if (dizi[s] != -1) {
                tempdizi[sayac] = dizi[s];
                sayac++;
            }
        }
        sayac = 0;
        for (int s = i; s <= j; s++) {
            dizi[s] = tempdizi[sayac];
        }
    }
}
