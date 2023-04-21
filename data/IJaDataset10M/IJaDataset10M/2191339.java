package serveur;

public class Gen_Matrice {

    public Gen_Matrice() {
    }

    public static int[][] GenrateMatrix(int taillematric) {
        int a = 0;
        int[][] tab = new int[taillematric][(taillematric * 2)];
        {
            for (int i = 0; i < taillematric; i++) {
                for (int j = 0; j < (taillematric * 2); j++) {
                    a = (int) (Math.random() * 20 - 10);
                    tab[i][j] = a;
                }
            }
            return tab;
        }
    }
}
