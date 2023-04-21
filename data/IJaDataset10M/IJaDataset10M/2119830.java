package deprecated;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Administrador
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class DiscretizationMethods {

    public static final String[] KNOWN_METHODS_NAMES = new String[] { "CAIM", "Ameva", "CUM", "EFI", "EWI", "none" };

    public static final int CAIM = 0;

    public static final int Ameva = 1;

    public static final int CUM = 2;

    public static final int EFI = 3;

    public static final int EWI = 4;

    public static final int NONE = 5;

    public static final String[] KNOWN_SIMILARITY_METHODS = new String[] { "QSI", "IntervalKernel", "IKMulti", "DTW", "Euclidean" };

    public static final int QSI = 0;

    public static final int IntervalKernel = 1;

    public static final int IKMulti = 2;

    public static final int DTW = 3;

    public static final int Euclidean = 4;

    public static final double[] Preprocess_Series(double[] v, boolean seriesFixedLength, boolean Nor, boolean Tip, boolean Dif) {
        int longitud = v.length;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double total = 0;
        for (int pos = 0; pos < longitud; pos++) {
            if (v[pos] < min) min = v[pos];
            if (v[pos] > max) max = v[pos];
            total += v[pos];
        }
        double amplitud = max - min;
        double media = total / longitud;
        double suma = 0;
        if (Nor) {
            for (int i = 0; i < longitud; i++) v[i] = (v[i] - min) / amplitud;
        } else if (Tip) {
            for (int i = 0; i < longitud; i++) suma += Math.pow(v[i] - media, 2);
            if (suma > 0) {
                double desv = Math.sqrt(suma / (longitud - 1));
                for (int i = 0; i < longitud; i++) v[i] = (v[i] - media) / desv;
            }
        }
        if (Dif) longitud--;
        double[] aux = new double[longitud];
        if (Dif) for (int i = 0; i < longitud; i++) aux[i] = v[i + 1] - v[i]; else for (int i = 0; i < longitud; i++) aux[i] = v[i];
        return aux;
    }

    public static final void compute_CAIM_Ameva(List D, int metodo, int numClases, int numCortes, int[][] acumulados) {
        int[] mejor = new int[1];
        mejor[0] = 0;
        float tmpCriterion = 0;
        int paso = (metodo == Ameva) ? 10 * numClases : 1;
        float globalCriterion = 0;
        do {
            switch(metodo) {
                case CAIM:
                    tmpCriterion = nextMaxCAIM(D, numCortes, numClases, acumulados, mejor);
                    break;
                case Ameva:
                    tmpCriterion = nextMaxAmeva(D, numCortes, numClases, acumulados, mejor);
                    break;
            }
            ;
            if ((tmpCriterion > globalCriterion) || (paso < numClases)) {
                D.add(new Integer(mejor[0]));
                globalCriterion = tmpCriterion;
            } else if (tmpCriterion == globalCriterion) tmpCriterion = -9999999;
            paso++;
        } while ((tmpCriterion >= globalCriterion) || (paso < numClases));
    }

    /**
	 * Computes the best ChiSplit value obtained adding a new component to the
	 * limits list (D). Check all the not used values from the initial set.
	 */
    private static final float nextMaxAmeva(List D, int numCortes, int numClases, int[][] acu, int[] mejor) {
        float maxActual = -99999;
        for (int co = 1; co < numCortes; co++) {
            boolean vale = true;
            for (int k = 0; k < D.size(); k++) if (co == ((Integer) D.get(k)).intValue()) vale = false;
            if (vale) {
                LinkedList Daux = new LinkedList(D);
                Daux.add(new Integer(co));
                int numInt = Daux.size() - 1;
                Collections.sort(Daux);
                float valorChi = 0;
                long[][] matrizCA = new long[numClases + 1][numInt + 1];
                for (int icorte = 1; icorte < (numInt + 1); icorte++) {
                    for (int cla = 0; cla < numClases; cla++) {
                        int qi = 0;
                        for (int k = ((Integer) Daux.get(icorte - 1)).intValue(); k < ((Integer) Daux.get(icorte)).intValue(); k++) {
                            qi += acu[cla][k];
                        }
                        matrizCA[cla][icorte - 1] = qi;
                        matrizCA[cla][numInt] += qi;
                        matrizCA[numClases][icorte - 1] += qi;
                        matrizCA[numClases][numInt] += qi;
                    }
                }
                long totalN = matrizCA[numClases][numInt];
                for (int icorte = 0; icorte < numInt; icorte++) for (int cla = 0; cla < numClases; cla++) {
                    valorChi += Math.pow(matrizCA[cla][icorte], 2) / (matrizCA[cla][numInt] * matrizCA[numClases][icorte]);
                }
                valorChi = valorChi - 1;
                valorChi = valorChi * totalN / ((numClases - 1) * numInt);
                if (valorChi > maxActual) {
                    maxActual = valorChi;
                    mejor[0] = co;
                }
            }
        }
        return maxActual;
    }

    /**
	 * Order the first of two vectors in the indicated order, updating the
	 * other.
	 */
    public static final void Ordena_Vectores(double[] v1, String sOrder, int iLow1, int iHigh1, double[] v2) {
        int iLow2, iHigh2;
        double vItem1, vItem2, vItem3;
        iLow2 = iLow1;
        iHigh2 = iHigh1;
        vItem1 = v1[(int) ((iLow1 + iHigh1) / 2)];
        while (iLow2 < iHigh2) {
            if (sOrder.equals("A")) {
                while ((v1[iLow2] < vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] > vItem1) && (iHigh2 > iLow1)) iHigh2--;
            } else {
                while ((v1[iLow2] > vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] < vItem1) && (iHigh2 > iLow1)) iHigh2--;
            }
            if (iLow2 < iHigh2) {
                vItem2 = v1[iLow2];
                v1[iLow2] = v1[iHigh2];
                v1[iHigh2] = vItem2;
                vItem3 = v2[iLow2];
                v2[iLow2] = v2[iHigh2];
                v2[iHigh2] = vItem3;
            }
            if (iLow2 <= iHigh2) {
                iLow2 = iLow2 + 1;
                iHigh2 = iHigh2 - 1;
            }
        }
        if (iHigh2 > iLow1) Ordena_Vectores(v1, sOrder, iLow1, iHigh2, v2);
        if (iLow2 < iHigh1) Ordena_Vectores(v1, sOrder, iLow2, iHigh1, v2);
    }

    public static final void Ordena_Vectores(double[] v1, String sOrder, int iLow1, int iHigh1, int[] v2) {
        int iLow2, iHigh2;
        double vItem1, vItem2;
        int vItem3;
        iLow2 = iLow1;
        iHigh2 = iHigh1;
        vItem1 = v1[(int) ((iLow1 + iHigh1) / 2)];
        while (iLow2 < iHigh2) {
            if (sOrder.equals("A")) {
                while ((v1[iLow2] < vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] > vItem1) && (iHigh2 > iLow1)) iHigh2--;
            } else {
                while ((v1[iLow2] > vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] < vItem1) && (iHigh2 > iLow1)) iHigh2--;
            }
            if (iLow2 < iHigh2) {
                vItem2 = v1[iLow2];
                v1[iLow2] = v1[iHigh2];
                v1[iHigh2] = vItem2;
                vItem3 = v2[iLow2];
                v2[iLow2] = v2[iHigh2];
                v2[iHigh2] = vItem3;
            }
            if (iLow2 <= iHigh2) {
                iLow2 = iLow2 + 1;
                iHigh2 = iHigh2 - 1;
            }
        }
        if (iHigh2 > iLow1) Ordena_Vectores(v1, sOrder, iLow1, iHigh2, v2);
        if (iLow2 < iHigh1) Ordena_Vectores(v1, sOrder, iLow2, iHigh1, v2);
    }

    public static final void Ordena_Vectores(int[] v1, String sOrder, int iLow1, int iHigh1, int[] v2) {
        int iLow2, iHigh2;
        int vItem1, vItem2;
        int vItem3;
        iLow2 = iLow1;
        iHigh2 = iHigh1;
        vItem1 = v1[(int) ((iLow1 + iHigh1) / 2)];
        while (iLow2 < iHigh2) {
            if (sOrder.equals("A")) {
                while ((v1[iLow2] < vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] > vItem1) && (iHigh2 > iLow1)) iHigh2--;
            } else {
                while ((v1[iLow2] > vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] < vItem1) && (iHigh2 > iLow1)) iHigh2--;
            }
            if (iLow2 < iHigh2) {
                vItem2 = v1[iLow2];
                v1[iLow2] = v1[iHigh2];
                v1[iHigh2] = vItem2;
                vItem3 = v2[iLow2];
                v2[iLow2] = v2[iHigh2];
                v2[iHigh2] = vItem3;
            }
            if (iLow2 <= iHigh2) {
                iLow2 = iLow2 + 1;
                iHigh2 = iHigh2 - 1;
            }
        }
        if (iHigh2 > iLow1) Ordena_Vectores(v1, sOrder, iLow1, iHigh2, v2);
        if (iLow2 < iHigh1) Ordena_Vectores(v1, sOrder, iLow2, iHigh1, v2);
    }

    /**
	 * Computes the best CAIM value obtained adding a new component to the
	 * limits list (D). Check all the not used values from the initial set.
	 */
    private static final float nextMaxCAIM(List D, int numCortes, int numClases, int[][] acu, int[] amejor) {
        float maxActual = -99999;
        for (int co = 1; co < numCortes; co++) {
            boolean vale = true;
            for (int k = 0; k < D.size(); k++) if (co == ((Integer) D.get(k)).intValue()) vale = false;
            if (vale) {
                LinkedList Daux = new LinkedList(D);
                Daux.add(new Integer(co));
                Collections.sort(Daux);
                float valorCAIM = 0;
                for (int icorte = 1; icorte < Daux.size(); icorte++) {
                    float totalM = 0;
                    float MaxQ = 0;
                    for (int cla = 0; cla < numClases; cla++) {
                        int qi = 0;
                        for (int k = ((Integer) Daux.get(icorte - 1)).intValue(); k < ((Integer) Daux.get(icorte)).intValue(); k++) {
                            qi += acu[cla][k];
                        }
                        if (qi > MaxQ) MaxQ = qi;
                        totalM += qi;
                    }
                    valorCAIM += (Math.pow(MaxQ, 2) / totalM);
                }
                valorCAIM = valorCAIM / (Daux.size() - 1);
                if (valorCAIM > maxActual) {
                    maxActual = valorCAIM;
                    amejor[0] = co;
                }
            }
        }
        return maxActual;
    }

    /**
	 * Order the first of two vectors in the indicated order, updating the
	 * other.
	 */
    public static final void Ordena_Vectores(double[] v1, String sOrder, int iLow1, int iHigh1, String[] v2) {
        int iLow2, iHigh2;
        double vItem1, vItem2;
        String vItem3;
        iLow2 = iLow1;
        iHigh2 = iHigh1;
        vItem1 = v1[(int) ((iLow1 + iHigh1) / 2)];
        while (iLow2 < iHigh2) {
            if (sOrder.equals("A")) {
                while ((v1[iLow2] < vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] > vItem1) && (iHigh2 > iLow1)) iHigh2--;
            } else {
                while ((v1[iLow2] > vItem1) && (iLow2 < iHigh1)) iLow2++;
                while ((v1[iHigh2] < vItem1) && (iHigh2 > iLow1)) iHigh2--;
            }
            if (iLow2 < iHigh2) {
                vItem2 = v1[iLow2];
                v1[iLow2] = v1[iHigh2];
                v1[iHigh2] = vItem2;
                vItem3 = v2[iLow2];
                v2[iLow2] = v2[iHigh2];
                v2[iHigh2] = vItem3;
            }
            if (iLow2 <= iHigh2) {
                iLow2++;
                iHigh2--;
            }
        }
        if (iHigh2 > iLow1) Ordena_Vectores(v1, sOrder, iLow1, iHigh2, v2);
        if (iLow2 < iHigh1) Ordena_Vectores(v1, sOrder, iLow2, iHigh1, v2);
    }

    public static final void Compute_EWI(List D, int intervalos) {
        double minimo = ((Double) D.get(0)).doubleValue();
        double maximo = ((Double) D.get(1)).doubleValue();
        double incremento = Math.abs(maximo - minimo) / intervalos;
        if (minimo > maximo) minimo = maximo;
        for (int i = 1; i < intervalos; i++) {
            D.add(new Double(minimo + incremento * i));
        }
    }

    /**
	 * Computes the QSI value of two vectors.
	 */
    public static final double QSIndex(double[] pcVx, double[] pcVy) {
        int sizex = pcVx.length;
        int sizey = pcVy.length;
        int valor;
        double dvalor = 0;
        int[][] D = new int[sizex + 1][sizey + 1];
        for (int i = 0; i < (sizex + 1); i++) for (int j = 0; j < (sizey + 1); j++) D[i][j] = 0;
        for (int i = 1; i <= sizex; i++) for (int j = 1; j <= sizey; j++) {
            if (pcVx[i - 1] == pcVy[j - 1]) {
                D[i][j] = D[i - 1][j - 1] + 1;
            } else if (D[i - 1][j] > D[i][j - 1]) {
                D[i][j] = D[i - 1][j];
            } else {
                D[i][j] = D[i][j - 1];
            }
        }
        valor = D[sizex][sizey];
        if (sizey > sizex) dvalor = ((double) valor) / ((double) sizey); else dvalor = ((double) valor) / ((double) sizex);
        return ((double) dvalor);
    }

    /**
	 * Computes Euclidean Distance of two univariable series
	 * @param pcVx
	 * @param pcVy
	 * @return
	 */
    public static final double EuclideanSeries(double[] pfVx, double[] pfVy) {
        double valor = 0;
        int sizex;
        sizex = pfVx.length;
        for (int i = 0; i < sizex; i++) valor += Math.pow(pfVx[i] - pfVy[i], 2);
        return Math.sqrt(valor);
    }

    /**
	 * Computes the Dynamic Time Warping of two vectors
	 * @param pcVx
	 * @param pcVy
	 * @return
	 */
    public static final double DTW(double[] pfVx, double[] pfVy) {
        double min;
        int i, j;
        int sizex, sizey;
        int dir;
        sizex = pfVx.length;
        sizey = pfVy.length;
        double[][] D = new double[(sizex + 1)][sizey + 1];
        int[][] P = new int[(sizex + 1)][sizey + 1];
        for (i = 0; i <= sizex; ++i) for (j = 0; j <= sizey; ++j) {
            D[i][j] = 0.0;
            P[i][j] = 0;
        }
        D[1][1] = Math.pow((pfVx[0] - pfVy[0]), 2);
        P[1][1] = 1;
        for (i = 2; i <= sizex; i++) {
            D[i][1] = Math.pow((pfVx[i - 1] - pfVy[0]), 2) + D[i - 1][1];
            P[i][1] = i;
        }
        for (i = 2; i <= sizey; i++) {
            D[1][i] = Math.pow((pfVx[0] - pfVy[i - 1]), 2) + D[1][i - 1];
            P[1][i] = i;
        }
        for (i = 2; i <= sizex; i++) for (j = 2; j <= sizey; j++) {
            min = D[i - 1][j - 1];
            dir = 3;
            if (D[i - 1][j] < min) {
                min = D[i - 1][j];
                dir = 1;
                if (D[i][j - 1] < min) {
                    min = D[i][j - 1];
                    dir = 2;
                }
            } else if (D[i][j - 1] < min) {
                min = D[i][j - 1];
                dir = 2;
            }
            D[i][j] = Math.pow((pfVx[i - 1] - pfVy[j - 1]), 2) + (float) min;
            P[i][j] = P[i - (dir & 1)][j - ((dir & 2) / 2)] + 1;
        }
        return (Math.sqrt(D[sizex][sizey]) / P[sizex][sizey]);
    }

    public static final double Kernel_Intervalar(double[] dVx, double[] dVy, double[] discre, double paraSimil) {
        double lambda = paraSimil;
        int rangos = discre.length - 1;
        double[][] dist = new double[rangos][rangos];
        for (int i = 0; i < rangos; i++) for (int j = i; j < rangos; j++) if (i == j) dist[i][j] = 0; else {
            dist[i][j] = Math.pow((discre[j] + discre[j + 1]) / 2 - (discre[i] + discre[i + 1]) / 2, 2) + Math.pow((discre[j + 1] - discre[j]) / 2 - (discre[i + 1] - discre[i]) / 2, 2);
            dist[j][i] = dist[i][j];
        }
        double coste = 0.0;
        int sizex = dVx.length;
        for (int i = 0; i < sizex; i++) coste += Math.pow(lambda, dist[(int) dVx[i]][(int) dVy[i]]);
        return coste;
    }

    public static final void Compute_CUM(List D, int numIntervalos, int numClases, int numCortes, int[][] acumulados) {
        int num_acum = acumulados[0].length;
        double[][] acum = new double[num_acum][2];
        for (int i = 0; i < num_acum; i++) {
            for (int c = 0; c < numClases; c++) {
                acum[i][0] += acumulados[c][i];
            }
            acum[i][1] = Math.sqrt(acum[i][0]);
            if (i > 0) {
                acum[i][0] += acum[i - 1][0];
                acum[i][1] += acum[i - 1][1];
            }
        }
        double longitud = acum[num_acum - 1][1] / numIntervalos;
        int a1 = -1;
        for (int k = 1; k < numIntervalos; k++) {
            int a2 = find_root(acum, num_acum, a1 + 1, longitud * k);
            if (a1 == -1) a1 = 0;
            if (a2 <= a1) a2 = a1 + 1;
            if ((acum[a2][1] - k * longitud) > (k * longitud - acum[a2 - 1][1])) {
                D.add(new Integer(a2 - 1));
                a1 = a2 - 1;
            } else {
                D.add(new Integer(a2));
                a1 = a2;
            }
        }
    }

    private static int find_root(double datos[][], int num_datos, int inicial, double valor) {
        for (int i = inicial; i <= num_datos; i++) if (datos[i][1] > valor) {
            return i;
        }
        return -1;
    }

    public static final void Compute_EFI(List D, int numIntervalos, int numClases, int numCortes, int[][] acumulados) {
        int[] acum = new int[acumulados[0].length];
        for (int i = 0; i < acum.length; i++) {
            for (int c = 0; c < numClases; c++) acum[i] += acumulados[c][i];
            if (i > 0) acum[i] += acum[i - 1];
        }
        long total = acum[acum.length - 1];
        int pos_act = 0;
        for (int vuelta = 1; vuelta < numIntervalos; vuelta++) {
            double t;
            t = vuelta * ((double) total) / numIntervalos;
            long valor = (int) t;
            do {
                pos_act++;
            } while (acum[pos_act] < valor);
            D.add(new Integer(pos_act + 1));
            pos_act--;
        }
    }
}
