package junit.classification;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Random;
import javax.management.MBeanServer;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.tree.RandomTree;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;
import net.sf.javaml.utils.Statistics;
import org.apache.commons.math.stat.StatUtils;
import org.junit.Test;

public class TestRandomTreeSize {

    @Test
    public void testKNN() {
        Random rg = new Random(System.currentTimeMillis());
        try {
            double[] value = new double[20];
            double[] dataval = new double[value.length];
            long mem = 0;
            for (int i = 0; i < value.length; i++) {
                Dataset data = FileHandler.loadDataset(new File("devtools/data/colon.csv.gz"), 0, ",");
                long dataMem = checkMem();
                dataval[i] = dataMem;
                RandomTree rt = new RandomTree(5, rg);
                rt.buildClassifier(data);
                mem = checkMem();
                value[i] = mem;
            }
            System.out.println("Mean data memory size: " + StatUtils.mean(dataval));
            System.out.println("Mean RT memory size: " + StatUtils.mean(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long checkMem() {
        try {
            System.gc();
            Thread.currentThread().sleep(100);
            System.runFinalization();
            Thread.currentThread().sleep(100);
            System.gc();
            Thread.currentThread().sleep(500);
            System.gc();
            Thread.currentThread().sleep(100);
            System.runFinalization();
            Thread.currentThread().sleep(100);
            System.gc();
            Thread.currentThread().sleep(100);
            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            return totalMemory - freeMemory;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
