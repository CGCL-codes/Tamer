package msidstock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maciej Piotrowski
 *
 *
 * TYLKO TESTOWA NIE RUSZAC!!!!!!!!!!!!!!!!!!
 *
 *
 *
 *
 */
public class FunkcjaCelu {

    Stock stock;

    int N;

    int M;

    double roznica = 0;

    double wydane = 0;

    double zarobione = 0;

    String[] compNames;

    float[] data;

    public FunkcjaCelu(Stock stock, int N) {
        try {
            this.stock = stock;
            this.N = N;
            this.M = stock.companies.size();
            compNames = stock.getCompaniesNames();
        } catch (Exception ex) {
            Logger.getLogger(FunkcjaCelu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double funkcja(double[][] MxN) {
        wydane = 0;
        zarobione = 0;
        roznica = 0;
        for (int wiersz = 0; wiersz < MxN.length; wiersz++) {
            try {
                data = stock.getCompanyFloatData(compNames[wiersz]);
                for (int kolumna = 0; kolumna <= MxN[wiersz].length; kolumna++) {
                    if (kolumna == 0) {
                        wydane = wydane + MxN[wiersz][kolumna] * data[kolumna];
                    } else {
                        if (kolumna == MxN[wiersz].length) {
                            zarobione = zarobione + MxN[wiersz][kolumna - 1] * data[kolumna];
                        } else {
                            roznica = (MxN[wiersz][kolumna] - MxN[wiersz][kolumna - 1]) * data[kolumna];
                            if (roznica > 0) {
                                wydane = wydane + roznica;
                            } else {
                                zarobione = zarobione - roznica;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        if (wydane == 0) {
            return 0;
        } else {
            return (zarobione - wydane) / wydane;
        }
    }

    public double funkcja(double[] MxN) {
        double temp[][] = new double[M][N];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                temp[i][j] = MxN[N * i + j];
            }
        }
        return funkcja(temp);
    }
}
