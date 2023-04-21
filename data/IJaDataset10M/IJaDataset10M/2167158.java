package Methodes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe permettant de résoudre le problème avec la méthode résolution cvx
 * @author aurelie
 *
 */
public class Prob_Cvx extends Methode {

    /** Fichier de résultat de la résolution avec CVX sous MatLab */
    private String fichier;

    /** Constantes nombre de jours.	 */
    private static int NBJOUR = 7;

    /** Constantes sur le nombre de centrales. */
    private static int NBCENTRALE = 5;

    /** Matrice 5x7 de production pour chaque centrale pour chaque jour. */
    private double[][] production = new double[NBCENTRALE][NBJOUR];

    /** Matrice de la demande moyenne pour chaque jour. */
    private double[] demande = new double[NBJOUR];

    /** Tableau contenant la capacité max de la centrale1 pour chaque jour. */
    private double[] maxCentrale1 = new double[NBJOUR];

    /** Tableau contenant la capacité max de la centrale2 pour chaque jour. */
    private double[] maxCentrale2 = new double[NBJOUR];

    /** Tableau contenant la capacité max de la centrale3 pour chaque jour. */
    private double[] maxCentrale3 = new double[NBJOUR];

    /** Tableau contenant la capacité max de la centrale4 pour chaque jour. */
    private double[] maxCentrale4 = new double[NBJOUR];

    /** Tableau contenant la capacité max de la centrale5 pour chaque jour. */
    private double[] maxCentrale5 = new double[NBJOUR];

    /** Cout minimum trouve grâce à CVX. */
    private double coutMinimum;

    /** Temps d'exécution du cvx. */
    private double temps;

    /**
	 * Constructeur indiquant le fichier résultat à lire.
	 * @param fic : fichier résultats
	 */
    public Prob_Cvx(String fic) {
        fichier = fic;
    }

    /**
	 * Méthode lançant la résolution (pour ce mode de résolution il s'agit uniquement de récupèrer les 
	 * informations contenues dans le fichier résultat.txt).
	 */
    public void solve() {
        try {
            this.lireFichier();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        solution = new Solution(production, maxCentrale1, maxCentrale2, maxCentrale3, maxCentrale4, maxCentrale5, coutMinimum, temps);
    }

    /**
	 * Methode de vérification de l'extraction des données de résolution.
	 */
    private void afficherResult() {
        System.out.println("cout minium");
        System.out.println(coutMinimum);
        System.out.println("Temps d'execution");
        System.out.println(temps);
        System.out.println("Production centrales");
        for (int i = 0; i < 7; i++) {
            System.out.println("jour " + i + " : ");
            for (int j = 0; j < 5; j++) {
                System.out.print("centrale " + j + " : ");
                System.out.println(production[j][i]);
            }
        }
        System.out.println("demande");
        for (int i = 0; i < 7; i++) {
            System.out.println("jour " + i + " : " + demande[i]);
        }
    }

    /**
	 * Methode permettant d'extraires toutes les données de résolutions du cvx
	 * @throws NumberFormatException
	 * @throws IOException
	 */
    private void lireFichier() throws NumberFormatException, IOException {
        String strLine = "";
        int i = 0;
        int j = 0;
        BufferedReader fp = new BufferedReader(new FileReader(fichier));
        while ((strLine = fp.readLine()) != null) {
            if (strLine.equals("cout minimum")) {
                strLine = fp.readLine();
                coutMinimum = Double.parseDouble(strLine);
            } else if (strLine.equals("matrice x")) {
                while (!((strLine = fp.readLine()).equals("demande"))) {
                    production[i][j] = Double.parseDouble(strLine);
                    i++;
                    if (i > 4) {
                        j++;
                        i = 0;
                    }
                }
                i = j = 0;
                while (!(strLine = fp.readLine()).equals("capacite max centrale 1")) {
                    demande[i] = Double.parseDouble(strLine);
                    i++;
                }
                i = j = 0;
                while (!(strLine = fp.readLine()).equals("capacite max centrale 2")) {
                    maxCentrale1[i] = Double.parseDouble(strLine);
                    i++;
                }
                i = j = 0;
                while (!(strLine = fp.readLine()).equals("capacite max centrale 3")) {
                    maxCentrale2[i] = Double.parseDouble(strLine);
                    i++;
                }
                i = j = 0;
                while (!(strLine = fp.readLine()).equals("capacite max centrale 4")) {
                    maxCentrale3[i] = Double.parseDouble(strLine);
                    i++;
                }
                i = j = 0;
                while (!(strLine = fp.readLine()).equals("capacite max centrale 5")) {
                    maxCentrale4[i] = Double.parseDouble(strLine);
                    i++;
                }
                i = j = 0;
                while (!(strLine = fp.readLine()).equals("temps")) {
                    maxCentrale5[i] = Double.parseDouble(strLine);
                    i++;
                }
            }
            if (!strLine.equals("temps")) temps = Double.parseDouble(strLine);
        }
    }

    /**
	 * getter de la matrice de production
	 * @return la matrice 5x7 de production
	 */
    public double[][] getProduction() {
        return production;
    }

    /**
	 * Renvoie la production en fonction de la centrale et du jour
	 * @param i la centrale 
	 * @param j le jour
	 * @return la production 
	 */
    public double getProduction(int i, int j) {
        return this.production[i][j];
    }

    /**
	 * getter de demande
	 * @returnla demande moyenne de chaque jour
	 */
    public double[] getDemande() {
        return demande;
    }

    /**
	 * get de la capacité max de la centrale1
	 * @return les capacités max pour chaque jour de la centrale1
	 */
    public double[] getMaxCentrale1() {
        return maxCentrale1;
    }

    /**
	 * Retourn la capacité max pour un jour donné
	 * @param i le jour
	 * @return la capacité max de la centrale
	 */
    public double getMaxCentrale1(int i) {
        return maxCentrale1[i];
    }

    /**
	 * get de la capacité max de la centrale2
	 * @return les capacités max pour chaque jour de la centrale2
	 */
    public double[] getMaxCentrale2() {
        return maxCentrale2;
    }

    /**
	 * Retourn la capacité max pour un jour donné
	 * @param i le jour
	 * @return la capacité max
	 */
    public double getMaxCentrale2(int i) {
        return maxCentrale2[i];
    }

    /**
	 * get de la capacité max de la centrale3
	 * @return les capacités max pour chaque jour de la centrale3
	 */
    public double[] getMaxCentrale3() {
        return maxCentrale3;
    }

    /**
	 * Retourn la capacité max pour un jour donné
	 * @param i le jour
	 * @return la capacité max
	 */
    public double getMaxCentrale3(int i) {
        return maxCentrale3[i];
    }

    /**
	 * get de la capacité max de la centrale4
	 * @return les capacités max pour chaque jour de la centrale4
	 */
    public double[] getMaxCentrale4() {
        return maxCentrale4;
    }

    /**
	 * Retourn la capacité max pour un jour donné
	 * @param i le jour
	 * @return la capacité max
	 */
    public double getMaxCentrale4(int i) {
        return maxCentrale4[i];
    }

    /**
	 * get de la capacité max de la centrale5
	 * @return les capacités max pour chaque jour de la centrale5
	 */
    public double[] getMaxCentrale5() {
        return maxCentrale5;
    }

    /**
	 * Retourn la capacité max pour un jour donné
	 * @param i le jour
	 * @return la capacité max
	 */
    public double getMaxCentrale5(int i) {
        return maxCentrale5[i];
    }

    /**
	 * Getter
	 * @return le cout minimum trouvé au problème
	 */
    public double getCoutMinimum() {
        return coutMinimum;
    }

    /**
	 * Getter
	 * @return le temps d'exécution du cvx
	 */
    public double getTemps() {
        return temps;
    }
}
