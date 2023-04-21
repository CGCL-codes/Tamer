package auxillary;

import experiment.Dataset;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Funkcje pomocnicze zwi�zane z bibliotek� WEKA.
 * 
 * @author Mateusz Markowicz
 * @author Marta Sta�ska
 */
public class WekaAux {

    /**
	 * Zwraca Instances (WEKA), na podstawie wskazanego zbioru osobnik�w. 
	 * @param dataset Zbi�r osobnik�w.
	 * @param w Wagi osobnik�w. Je�li null przyjmuje dla wszystkich 1.0.
	 * @param classInts Klasy osobnik�w. Je�li null nie uwzgl�dnia klas osobnik�w. 
	 * @param idxs Indeksy osobnik�w (mo�liwe powt�rzenia). Je�li null przyjmuje
	 * kolejno�� z dataset.
	 * @return Instancja klasy Instances.
	 */
    public static Instances getInstances(Dataset dataset, double[] w, double[] classInts, int[] idxs) {
        int newD = dataset.getD();
        if (classInts != null) newD += 1;
        Instances ret = new Instances("tmp1", fVector(newD), dataset.getN());
        for (int i = 0; i < dataset.getN(); i++) {
            int idx;
            Double wi, classIntsI;
            if (idxs == null) idx = i; else idx = idxs[i];
            if (w == null) wi = 1.0; else wi = w[idx];
            if (classInts == null) classIntsI = null; else classIntsI = classInts[idx];
            ret.add(getInstance(dataset.getInd(idx), wi, classIntsI, ret));
        }
        if (classInts != null) ret.setClassIndex(dataset.getD());
        return ret;
    }

    /**
	 * Zwraca Instance (WEKA).
	 * @param xi Wektor cech osobnika.
	 * @param w Waga osobnika.
	 * @param classInt Klasa osobnika. Je�eli null nie uwzgl�dnia klasy.
	 * @param insts Instancja Instances do kt�rej nale�e� ma osobnik. Je�li null
	 * umieszcza w nowo utworzonym Instances.
	 * @return
	 */
    public static Instance getInstance(double[] xi, double w, Double classInt, Instances insts) {
        int D = xi.length;
        double[] attr;
        if (classInt != null) {
            attr = new double[D + 1];
            attr[D] = classInt;
        } else {
            attr = new double[D];
        }
        if (insts == null) {
            insts = new Instances("tmp2", fVector(attr.length), 1);
            if (classInt != null) insts.setClassIndex(D);
        }
        for (int i = 0; i < D; i++) attr[i] = xi[i];
        Instance ret = new Instance(w, attr);
        ret.setDataset(insts);
        return ret;
    }

    private static FastVector fVector(int d) {
        FastVector ret = new FastVector(d);
        for (int i = 0; i < d; i++) ret.addElement(new Attribute(i + ""));
        return ret;
    }
}
