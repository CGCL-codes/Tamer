package org.jcpsim.examples.simple;

import java.util.Locale;

public class VentilationOfRcLung {

    double PEEP = 5.0;

    double Freq = 12.0;

    double Ti = 40.0;

    double Tp = 10.0;

    double TV = 400.0;

    double R = 10.0;

    double C = 40.0;

    double cycleTime = 60.0 / Freq;

    double tInsp = cycleTime * Ti / 100.0;

    double tInspFlow = cycleTime * (Ti - Tp) / 100.0;

    double qsoll = TV / tInspFlow;

    double timeInCycle;

    int Phase;

    double Presp;

    double q;

    double V;

    double Plung;

    public int getPhase(double t) {
        timeInCycle = t % cycleTime;
        if (timeInCycle > tInsp) return 3;
        if (timeInCycle > tInspFlow) return 2;
        return 1;
    }

    public VentilationOfRcLung() {
        double deltaT = 0.01;
        Plung = PEEP;
        for (double t = 0.0; t <= 10.0; t += deltaT) {
            switch(getPhase(t)) {
                case 1:
                    Presp = qsoll * R / 1000.0 + Plung;
                    break;
                case 2:
                    Presp = Plung;
                    break;
                case 3:
                    Presp = PEEP;
                    break;
            }
            q = (Presp - Plung) / (R / 1000.0);
            V = Plung * C;
            Plung += deltaT * q / C;
            System.out.printf(Locale.US, "%8.4f, %8.4f, %8.4f, %8.4f, %8.4f\n", t, Presp, Plung, V, q);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        new VentilationOfRcLung();
    }
}
