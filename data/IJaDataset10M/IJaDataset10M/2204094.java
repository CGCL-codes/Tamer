package org.isakiev.wic.demo.statistics.comparisons;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.isakiev.wic.MatrixProvider;
import org.isakiev.wic.demo.ImageLoader;
import org.isakiev.wic.demo.statistics.CompressionManager;
import org.isakiev.wic.demo.statistics.CompressionResult;
import org.isakiev.wic.demo.statistics.DataEntry;
import org.isakiev.wic.demo.statistics.DataSet;
import org.isakiev.wic.demo.statistics.presentations.*;
import org.isakiev.wic.geometry.CoordinateMatrix;
import org.isakiev.wic.j2kfacade.EncoderParameters;

public class AllMatricesComparison {

    private static AllMatricesComparisonResult calculateResult() {
        AllMatricesComparisonResult result = new AllMatricesComparisonResult();
        String imageName = "resources/lena64.png";
        result.setImageName(imageName);
        BufferedImage image = ImageLoader.loadImage(imageName);
        List<CoordinateMatrix> matrices = MatrixProvider.getAdmissibleMatrices(7);
        for (CoordinateMatrix matrix : matrices) {
            result.getMatrixNames().add(matrix.toString());
        }
        final int maxSmoothnessValue = 7;
        final int decompositionLevelsNumber = 1;
        for (int d = 1; d <= decompositionLevelsNumber; d++) {
            EncoderParameters params = EncoderParameters.LOSSLESS;
            params.setDecompositionLevelsNumber(d);
            for (int n = 1; n <= maxSmoothnessValue; n++) {
                for (int k = n + 1; k <= maxSmoothnessValue; k++) {
                    AllMatricesComparisonResult.Entry entry = new AllMatricesComparisonResult.Entry();
                    entry.setDecompositionSteps(d);
                    entry.setN(n);
                    entry.setK(k);
                    for (CoordinateMatrix matrix : matrices) {
                        entry.getResults().add(CompressionManager.checkWICCompression(image, matrix, n, k, params));
                    }
                    result.getEntries().add(entry);
                }
            }
        }
        return result;
    }

    private static final boolean FORCE_RESULT_CALCULATION = false;

    public static void execute() {
        AllMatricesComparisonResult result = (AllMatricesComparisonResult) SerializationHelper.read(AllMatricesComparisonResult.class);
        if (result == null || FORCE_RESULT_CALCULATION) {
            result = calculateResult();
            SerializationHelper.write(result);
        }
        DataSet fileSizeDataSet = new DataSet("��������� �������� ������ ������ ��� ��������� ������ ���������", "��������� ���������", "������ �����, ����", result.getMatrixNames().size());
        for (String matrixName : result.getMatrixNames()) {
            fileSizeDataSet.getSeriesNames().add(matrixName);
        }
        for (AllMatricesComparisonResult.Entry resultEntry : result.getEntries()) {
            DataEntry entry = new DataEntry("n = " + resultEntry.getN() + "; k = " + resultEntry.getK());
            for (CompressionResult res : resultEntry.getResults()) {
                entry.getValues().add(res.getSize());
            }
            fileSizeDataSet.getEntries().add(entry);
        }
        new LineChartResultPresentation(fileSizeDataSet);
        DataSet psnrDataSet = new DataSet("��������� PSNR ��� ��������� ������ ���������", "��������� ���������", "PSNR, ��", result.getMatrixNames().size());
        for (String matrixName : result.getMatrixNames()) {
            psnrDataSet.getSeriesNames().add(matrixName);
        }
        for (AllMatricesComparisonResult.Entry resultEntry : result.getEntries()) {
            DataEntry entry = new DataEntry("n = " + resultEntry.getN() + "; k = " + resultEntry.getK());
            for (CompressionResult res : resultEntry.getResults()) {
                entry.getValues().add(res.getPsnr());
            }
            psnrDataSet.getEntries().add(entry);
        }
        new LineChartResultPresentation(psnrDataSet);
        DataSet timeDataSet = new DataSet("��������� ������� ������ ��� ��������� ������ ���������", "��������� ���������", "����� ������, ��", result.getMatrixNames().size());
        for (String matrixName : result.getMatrixNames()) {
            timeDataSet.getSeriesNames().add(matrixName);
        }
        for (AllMatricesComparisonResult.Entry resultEntry : result.getEntries()) {
            DataEntry entry = new DataEntry("n = " + resultEntry.getN() + "; k = " + resultEntry.getK());
            for (CompressionResult res : resultEntry.getResults()) {
                entry.getValues().add(res.getTime() > 0 ? res.getTime() / 1000000 : 0);
            }
            timeDataSet.getEntries().add(entry);
        }
        new LineChartResultPresentation(timeDataSet);
    }

    public static class AllMatricesComparisonResult {

        private String imageName;

        private List<String> matrixNames = new ArrayList<String>();

        private List<Entry> entries = new ArrayList<Entry>();

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public List<String> getMatrixNames() {
            return matrixNames;
        }

        public void setMatrixNames(List<String> matrixNames) {
            this.matrixNames = matrixNames;
        }

        public List<Entry> getEntries() {
            return entries;
        }

        public void setEntries(List<Entry> entries) {
            this.entries = entries;
        }

        public static class Entry {

            private int n;

            private int k;

            private int decompositionSteps;

            private List<CompressionResult> results = new ArrayList<CompressionResult>();

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public int getK() {
                return k;
            }

            public void setK(int k) {
                this.k = k;
            }

            public int getDecompositionSteps() {
                return decompositionSteps;
            }

            public void setDecompositionSteps(int decompositionSteps) {
                this.decompositionSteps = decompositionSteps;
            }

            public List<CompressionResult> getResults() {
                return results;
            }

            public void setResults(List<CompressionResult> results) {
                this.results = results;
            }
        }
    }
}
