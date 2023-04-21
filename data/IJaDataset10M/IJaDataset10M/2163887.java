package org.xactor.test.perf.test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import junit.framework.TestCase;

public class ConvertToTxt extends TestCase {

    /** The directory that constains the serialized files. */
    private static final String SER_DIR = "/home/ivanneto/mestrado/dissertacao/graficos/serialized/cluster_new";

    private NumberFormat formatterEn;

    protected void setUp() throws Exception {
        formatterEn = DecimalFormat.getInstance(Locale.ENGLISH);
        formatterEn.setMinimumFractionDigits(2);
        formatterEn.setMaximumFractionDigits(2);
        formatterEn.setGroupingUsed(false);
    }

    public void test() throws Exception {
        File sourceDir = new File(SER_DIR);
        File destDir = new File(SER_DIR + "/txt");
        if (!destDir.exists()) destDir.mkdir();
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File file) {
                return !file.isDirectory() && file.getName().endsWith(".ser");
            }
        };
        File[] files = sourceDir.listFiles(fileFilter);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getAbsolutePath());
            FileInputStream fin = new FileInputStream(files[i]);
            ObjectInputStream ois = new ObjectInputStream(fin);
            DescriptiveStatistics stats = (DescriptiveStatistics) ois.readObject();
            ois.close();
            String fileName = files[i].getName();
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            PrintWriter pw = new PrintWriter(SER_DIR + "/txt/" + fileName + ".txt");
            for (int k = 1; k <= stats.getN(); k++) pw.println(k + "\t" + (stats.getElement(k - 1) / 1000000));
            pw.flush();
            pw.close();
        }
    }
}
