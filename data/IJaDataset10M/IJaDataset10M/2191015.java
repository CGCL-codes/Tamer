package loengud.esimene.kaug;

/**
 * Inimiese andmete hoidmiseks andmestruktuur.
 * @author A
 *
 */
public class Inimene {

    /**
	 * Inimese nimi.
	 */
    String nimi;

    int vanus;

    boolean valge = true;

    @Override
    public String toString() {
        return "misse sinu asi on";
    }

    public static void main(String[] args) {
        Inimene mari = new Inimene();
        Inimene jyri = new Inimene();
        int n = 10;
        if (n > 0) {
        } else {
        }
        if (n < 0) {
            System.out.println("neg");
        }
        mari.nimi = "mari";
        mari.vanus = 21;
        jyri.nimi = "jyri";
        jyri.vanus = 22;
        mari = jyri;
        jyri.nimi = "ab";
        System.out.println(mari.nimi);
        int ab = 10;
        switch(ab) {
            case 10:
                System.out.println("k�mme");
                break;
        }
    }
}
