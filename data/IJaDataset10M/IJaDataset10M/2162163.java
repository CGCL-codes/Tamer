package generators;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SimpleDataset;
import net.sf.javaml.core.SimpleInstance;

/**
 * This class can generate a dataset with non-spherical shaped clusters to test
 * the time complexity and performance of clustering algorithms on datasets that
 * contain non-spherical clusters .
 * 
 * Dataset contains 3 clusters, with respectively 1080, 250 and 200
 * instances each, 1530 over-all.
 * 
 * All instances have dimension 2
 * 
 * All datasets are normalized with the values in each dimension between 0 and
 * 1.
 * 
 * All instances have no class set.
 * 
 * The values in each dimension within a cluster are Gaussian distributed with a
 * standard deviation of 0.1.
 * 
 * @author Andreas De Rijcke
 * 
 */
public class SSclusters2Dataset3D {

    public static void main(String[] args) {
        write(createNd(), "syntheticdatasets/3D_special_shape2.data");
    }

    private static double[] createVec(double x, double y, double clusterSpread, Random rg) {
        double x1 = rg.nextGaussian() * clusterSpread + x;
        double y1 = rg.nextGaussian() * clusterSpread + y;
        double[] vec = new double[3];
        vec[0] = (float) x1;
        vec[1] = (float) y1;
        vec[2] = (float) 1;
        return vec;
    }

    private static Dataset createNd() {
        Dataset out = new SimpleDataset();
        Random rg = new Random(System.currentTimeMillis());
        double[] vec = new double[3];
        int dataPerPoint = 150;
        double clusterspread = 0.030;
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.15, 0.38, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.135, 0.29, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.16, 0.22, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.20, 0.18, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.25, 0.15, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.35, 0.16, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.4, 0.18, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.45, 0.27, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.5, 0.37, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.5, 0.48, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.47, 0.56, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.40, 0.64, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.36, 0.73, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.38, 0.8, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.45, 0.87, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.55, 0.85, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.62, 0.83, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint; i++) {
            vec = createVec(0.7, 0.81, clusterspread, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        int dataPerPoint2 = 100;
        double clusterspread2 = 0.020;
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.6, 0.70, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.64, 0.67, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.68, 0.64, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.72, 0.61, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.76, 0.58, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.80, 0.55, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint2; i++) {
            vec = createVec(0.84, 0.52, clusterspread2, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        int dataPerPoint3 = 60;
        double clusterspread3 = 0.020;
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.90, 0.17, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.86, 0.18, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.82, 0.20, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.80, 0.25, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.82, 0.30, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.86, 0.32, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        for (int i = 0; i < dataPerPoint3; i++) {
            vec = createVec(0.90, 0.33, clusterspread3, rg);
            out.addInstance(new SimpleInstance(vec));
        }
        return out;
    }

    private static void write(Dataset data, String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            for (int i = 0; i < data.size(); i++) {
                Instance tmp = data.getInstance(i);
                out.print(tmp.getValue(0));
                for (int j = 1; j < tmp.size(); j++) out.print("\t" + tmp.getValue(j));
                out.println();
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
