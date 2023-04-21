package jneural;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.lang.Math.*;
import javax.vecmath.*;

public class sommem {

    public static class CapaSOM implements Serializable {

        GMatrix GMPesos;

        GVector GVSortides;

        int NumNeuron;
    }

    ;

    public static class SOM implements java.io.Serializable {

        int t;

        int NumLearns;

        boolean AutoLearnSpeed;

        int NFiles;

        int NCols;

        CapaSOM Entrades;

        int Guanyador;

        CapaSOM Sortides;

        boolean PropagEscalar;

        double MinSort;

        double MaxSort;

        double ka;

        int DeltaF;

        int DeltaC;

        GVector GVvectent;

        GVector GVVectsal;

        public int Actualitzar() {
            NumLearns++;
            if (AutoLearnSpeed) ka = 0.01f / (double) Math.log((double) NumLearns * 100);
            PropagarEscalar();
            int Unitat = 0;
            int Act = Guanyador;
            int fg = Guanyador / NCols;
            int cg = Guanyador % NCols;
            int f1 = fg;
            f1 = Math.max(0, f1);
            int f2 = fg + DeltaF;
            f2 = Math.min(NFiles - 1, f2);
            int c1 = cg;
            c1 = Math.max(0, c1);
            int c2 = cg + DeltaC;
            c2 = Math.min(NCols - 1, c2);
            int i, j, k;
            for (i = c1; i <= c2; i++) {
                for (j = f1; j <= f2; j++) {
                    Unitat = i + j * NCols;
                    GVector ActPesos = new GVector(Entrades.NumNeuron);
                    Sortides.GMPesos.getRow(Unitat, ActPesos);
                    GVector sub = new GVector(ActPesos.getSize());
                    sub.sub(Entrades.GVSortides, ActPesos);
                    sub.scale(ka);
                    ActPesos.add(ActPesos, sub);
                    Sortides.GMPesos.setRow(Unitat, ActPesos);
                }
            }
            return Guanyador;
        }

        SOM() {
            AutoLearnSpeed = true;
            PropagEscalar = true;
            ka = 0.01f;
            DeltaC = 1;
            DeltaF = 1;
            Entrades = new CapaSOM();
            Sortides = new CapaSOM();
            Entrades.NumNeuron = 0;
            Sortides.NumNeuron = 0;
            NumLearns = 0;
            MaxSort = 1.0;
            MinSort = -1.0;
        }

        public void SetEntrades(Double[] Entr, int NumEntr) {
            int i;
            if (NumEntr != Entrades.NumNeuron && NumEntr != 0) {
            }
            for (i = 0; i < Entrades.NumNeuron; i++) {
                Entrades.GVSortides.setElement(i, Entr[i]);
            }
        }

        public void Init(int NumEntr, int NumFil, int NumCol, double MaxValor, double MinValor) {
            int i, j;
            Entrades.GVSortides = new GVector(NumEntr);
            Entrades.NumNeuron = NumEntr;
            Sortides.GVSortides = new GVector(NumFil * NumCol);
            Sortides.NumNeuron = NumFil * NumCol;
            Sortides.GMPesos = new GMatrix(Sortides.NumNeuron, Entrades.NumNeuron);
            NFiles = NumFil;
            NCols = NumCol;
            MaxSort = MaxValor;
            MinSort = MinValor;
            for (i = 0; i < Sortides.NumNeuron; i++) {
                for (j = 0; j < NumEntr; j++) {
                    Sortides.GMPesos.setElement(i, j, (double) (Math.random() * 2 - 1));
                }
            }
        }

        public int NumNeuron(int NumFil, int NumCol) {
            return (NumFil * NCols + NumCol);
        }

        public void PropagarEscalar() {
            double Minim = -10000000000.0f, mag;
            int i;
            Guanyador = 0;
            Sortides.GVSortides.mul(Sortides.GMPesos, Entrades.GVSortides);
        }

        public int NumEntrades() {
            return Entrades.NumNeuron;
        }
    }

    ;
}

;
