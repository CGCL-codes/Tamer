package math.calcul;

public class Binaire {

    private final double[] representTab;

    public Binaire(String bS) {
        representTab = new double[6];
        int lS = bS.length();
        for (int i = 0; i < 6; i++) {
            if (i < lS) {
                representTab[i] = bS.charAt(i) == '0' ? 0 : 1;
            }
        }
    }

    public double[] repTab() {
        return representTab;
    }
}
