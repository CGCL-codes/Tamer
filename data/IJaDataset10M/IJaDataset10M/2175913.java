package org.happy.commons.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.happy.commons.sort.algorithms.QuickSortParallel_1x0;
import org.happy.commons.util.ArraysHelper;
import org.happy.commons.util.comparators.Comparator_1x0;

/**
 * benchmark, which compares standard java sorting algorithm to the
 * happy-commons start JVM with comparable parameters
 * -Xmx2G -Xms2G -Xverify:none -XX:+UseParNewGC
 */
public class SortBenchmark {

    /**
	 * @param args
	 * @throws IOException
	 */
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException {
        int minElementNumber = (int) 1e5;
        int maxElementNumber = (int) 3e6;
        int deltaElementNumber = (int) 3e5;
        int repeats = 100;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        String fileName = "./temp/sort-statistics.xls";
        FileOutputStream fileOut = new FileOutputStream(fileName);
        int rowIndex = 5;
        int collumnIndex = 5;
        HSSFRow row1 = sheet.createRow(rowIndex++);
        row1.createCell(collumnIndex).setCellValue("number of elements");
        HSSFRow row2 = sheet.createRow(rowIndex++);
        row2.createCell(collumnIndex).setCellValue("time of Happy-Sort/time of Java-Sort");
        for (int n = minElementNumber; n <= (maxElementNumber + deltaElementNumber); n += deltaElementNumber) {
            collumnIndex++;
            Integer[] a = ArraysHelper.createRandomArray(n, 0, 2 * n);
            double speedup = testPerformance(a, repeats);
            System.out.println("array-size=" + n + "; speedup=" + speedup);
            row1.createCell(collumnIndex).setCellValue(n);
            row2.createCell(collumnIndex).setCellValue(speedup);
        }
        wb.write(fileOut);
        fileOut.close();
        System.out.println("finished");
    }

    /**
	 * executes the performance test
	 * 
	 * @param a
	 * @param repeats
	 *            number of repeats for to sort the array
	 * @return the speedup timeJavaSort/timeHappySort
	 */
    public static double testPerformance(Integer[] a, int repeats) {
        Comparator<Integer> comparator = new Comparator_1x0<Integer>();
        long startTime;
        long timeJavaSort = 0;
        long timeHappySort = 0;
        QuickSortParallel_1x0 sorter = new QuickSortParallel_1x0();
        for (int i = 0; i < repeats; i++) {
            Integer[] a1 = a.clone();
            Integer[] a2 = a.clone();
            startTime = System.nanoTime();
            Arrays.sort(a2, comparator);
            timeJavaSort += System.nanoTime() - startTime;
            startTime = System.nanoTime();
            sorter.sort(a1, comparator);
            timeHappySort += System.nanoTime() - startTime;
        }
        return (double) timeJavaSort / (double) timeHappySort;
    }
}
