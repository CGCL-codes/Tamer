package pl.edu.zut.wi.vsl.modules.steganalysis.bsm;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.ArrayList;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import org.apache.log4j.Logger;
import pl.edu.zut.wi.vsl.commons.StegoImage;
import pl.edu.zut.wi.vsl.commons.steganalysis.SteganalysisException;
import pl.edu.zut.wi.vsl.commons.steganalysis.SteganalyticTechnique;
import pl.edu.zut.wi.vsl.commons.utils.FileUtility;
import pl.edu.zut.wi.vsl.commons.utils.ImageUtility;

/**
 * Performs steganalysis using binary similarity measure (BSM), where 
 * features are obtained from the spatial domain representation of the image
 * and support ArrayList machine (SVM) is used as classificator.
 * <p>
 * This technique was originally proposed by Ismail Avcibas et al. at
 * Uludag University in Bursa, Turkey. For more information visit his webpage 
 * {@link http://www20.uludag.edu.tr/~avcibas/} 
 * <p>
 * Implemented as described in "Image steganalysis with Binary Similarity 
 * Measures" by Ismail Avcibas, Mehdi Kharrazi, Nasir Memon and Bulent Sankur.
 * <p>
 *
 * @author Michal Wegrzyn
 */
public class BsmImpl implements SteganalyticTechnique {

    private static BsmImpl bs;

    private static Logger logger = Logger.getLogger(BsmImpl.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StegoImage si = null;
        bs = new BsmImpl();
        switch(args.length) {
            case 3:
                bs.createDataFile(args[0], args[1], args[2]);
                break;
            case 7:
                try {
                    BufferedImage bi = ImageUtility.readImage(args[0]);
                    si = new StegoImage(bi, args[0]);
                } catch (IllegalArgumentException e) {
                    logger.error("Could not create stegoimage.", e);
                    System.exit(1);
                } catch (NullPointerException e) {
                    logger.error("Could not create stegoimage.", e);
                    System.exit(1);
                } catch (IOException e) {
                    logger.error("Could not create stegoimage.", e);
                    System.exit(1);
                }
                LinkedHashMap<String, String> o = new LinkedHashMap<String, String>();
                o.put("model", args[1]);
                o.put("scaling params", args[2]);
                o.put("predict probability", args[3]);
                o.put("test data", args[4]);
                o.put("scaled test data", args[5]);
                o.put("svm output", args[6]);
                try {
                    bs.analyse(si, o);
                } catch (SteganalysisException e) {
                    logger.error("Error occured during steganalysis", e);
                    System.exit(1);
                }
                break;
            case 1:
                if (args[0].equals("--help") || args[0].equals("-help") || args[0].equals("?") || args[0].equals("/?")) {
                    printUsage();
                }
                break;
            default:
                System.out.println("Unsupported option");
                BsmImpl.printUsage();
                break;
        }
    }

    /**
     * Prints usage to console.
     */
    public static void printUsage() {
        System.out.println("Usage: \n" + "Steganalysis: \n" + "vsl-module-steganalysis-bsm <path to image> <model> <scaling params> \n" + "                            <predict probability> <test data> \n" + "                            <scaled test data> <svm output> \n" + "model - path to the file that contains output model file produced by libsvm\n" + "scaling params - path to the file that contains scaling parameters created \n" + "during scaling training data for SVM \n" + "predict probability - whether to predict probability estimates, 0 or 1; \n" + "for one-class SVM only 0 is supported\n" + "test data - path to a file that will store test data for a classifier \n" + "scaled test data - path to a file that will store scaled test data for a \n" + "classifier \n" + "svm output - path to a file that will store output (result) of the \n" + "classification process \n" + "Creating data file: \n" + "vsl-module-steganalysis-bsm <stego directory> <clear directory> <fileout>\n" + "stego directory - path to directory that contains images with hidden \n" + "messages; If it is wrong or represents directory without handled images then \n" + "training samples for stego images will not be created \n" + "clear directory - path to directory that contains images without hidden \n" + "messages; If it is wrong or represents directory without handled images then \n" + "training samples for clear images will not be created");
    }

    public LinkedHashMap<String, String> analyse(StegoImage img, LinkedHashMap<String, String> options) throws SteganalysisException {
        String modelPath = options.get("model");
        String scalingParams = options.get("scaling params");
        int predictProbability;
        try {
            predictProbability = Integer.valueOf(options.get("predict probability"));
        } catch (NumberFormatException e) {
            throw new SteganalysisException("Could not parse " + "predict probability", e);
        }
        svm_model model = null;
        try {
            model = svm.svm_load_model(modelPath);
        } catch (Exception e) {
            throw new SteganalysisException("Could not load SVM model file", e);
        }
        if (svm.svm_check_probability_model(model) != 0) {
            logger.info("Model supports probability estimates, " + "but disabled in prediction.\n");
        }
        double[] measures = new double[18];
        measures = getImageMeasures(img);
        logger.info("Bit Similarity Measures: " + Arrays.toString(measures));
        String testDataFile = FileUtility.getUniqueFilename(options.get("test data"));
        try {
            saveImageNodes(testDataFile, createSvmNodes(measures), 1);
        } catch (Exception e) {
            logger.error("Error occured during writing to the output " + "train file", e);
            System.exit(1);
        }
        svm_scale scale = new svm_scale();
        String scaledTestData = FileUtility.getUniqueFilename(options.get("scaled test data"));
        try {
            scale.run(new String[] { "-r", scalingParams, testDataFile, scaledTestData });
        } catch (IOException e) {
            logger.error("Error occured while scaling train file", e);
        }
        String svmOutputFile = FileUtility.getUniqueFilename(options.get("svm output"));
        BufferedReader input = null;
        DataOutputStream output = null;
        try {
            input = new BufferedReader(new FileReader(scaledTestData));
        } catch (FileNotFoundException e) {
            throw new SteganalysisException("Could not create input reader " + "for predict procedure", e);
        }
        try {
            output = new DataOutputStream(new FileOutputStream(svmOutputFile));
        } catch (FileNotFoundException e) {
            throw new SteganalysisException("Could not create output writer " + "for predict procedure", e);
        }
        double v;
        try {
            v = predict(input, output, model, predictProbability);
        } catch (IOException e) {
            throw new SteganalysisException("Exception occured during " + "predict procedure", e);
        }
        try {
            input.close();
            output.close();
        } catch (Exception e) {
            logger.error("Error occured while closing stream(s)", e);
        }
        logger.info("Predicted value by SVM classifier: " + v);
        LinkedHashMap<String, String> response = new LinkedHashMap<String, String>();
        response.put("Predicted value", String.valueOf(v));
        response.put("result", "Predicted value: " + v);
        return response;
    }

    public double predict(BufferedReader input, DataOutputStream output, svm_model model, int predict_probability) throws IOException {
        int correct = 0;
        int total = 0;
        double v = 0;
        int svm_type = svm.svm_get_svm_type(model);
        int nr_class = svm.svm_get_nr_class(model);
        double[] prob_estimates = null;
        if (predict_probability == 1) {
            if (svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR) {
                logger.info("Prob. model for test data: target value = " + "predicted value + z,\nz: Laplace distribution " + "e^(-|z|/sigma)/(2sigma),sigma=" + svm.svm_get_svr_probability(model) + "\n");
            } else {
                int[] labels = new int[nr_class];
                svm.svm_get_labels(model, labels);
                prob_estimates = new double[nr_class];
                output.writeBytes("labels");
                for (int j = 0; j < nr_class; j++) {
                    output.writeBytes(" " + labels[j]);
                }
                output.writeBytes("\n");
            }
        }
        while (true) {
            String line = input.readLine();
            if (line == null) {
                break;
            }
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            double target = atof(st.nextToken());
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }
            if (predict_probability == 1 && (svm_type == svm_parameter.C_SVC || svm_type == svm_parameter.NU_SVC)) {
                v = svm.svm_predict_probability(model, x, prob_estimates);
                output.writeBytes(v + " ");
                for (int j = 0; j < nr_class; j++) {
                    output.writeBytes(prob_estimates[j] + " ");
                }
                output.writeBytes("\n");
            } else {
                v = svm.svm_predict(model, x);
                output.writeBytes(v + "\n");
            }
            if (v == target) {
                ++correct;
            }
            ++total;
        }
        return v;
    }

    private String createDataFile(String stegoDir, String clearDir, String dataFile) {
        BufferedWriter fp = null;
        try {
            dataFile = FileUtility.getUniqueFilename(dataFile);
            fp = new BufferedWriter(new FileWriter(dataFile));
        } catch (Exception e) {
            logger.error("Could not create output file: " + dataFile, e);
            System.exit(1);
        }
        ArrayList<svm_node[]> stegoNodes = new ArrayList<svm_node[]>();
        ArrayList<svm_node[]> clearNodes = new ArrayList<svm_node[]>();
        File sDir = new File(stegoDir);
        File cDir = new File(clearDir);
        File[] sFiles = sDir.listFiles();
        File[] cFiles = cDir.listFiles();
        logger.trace("Creating svm_nodes for stego images");
        if (sFiles != null && sFiles.length > 0) {
            for (int i = 0; i < sFiles.length; i++) {
                StegoImage img = null;
                try {
                    BufferedImage bi = ImageUtility.readImage(sFiles[i].getAbsolutePath());
                    img = new StegoImage(bi, sFiles[i].getAbsolutePath());
                } catch (Exception e) {
                    logger.warn("Could not create image from file: " + sFiles[i].getAbsolutePath(), e);
                    continue;
                }
                double[] measures;
                try {
                    measures = getImageMeasures(img);
                } catch (Exception e) {
                    logger.warn("Could not create measures for file: " + sFiles[i].getAbsolutePath(), e);
                    continue;
                }
                svm_node[] nodes = createSvmNodes(measures);
                stegoNodes.add(nodes);
                logger.trace("Processed files: " + (i + 1) + "/" + sFiles.length);
            }
        }
        logger.trace("Creating svm_nodes for clear images");
        if (cFiles != null && cFiles.length > 0) {
            for (int i = 0; i < cFiles.length; i++) {
                StegoImage img = null;
                try {
                    BufferedImage bi = ImageUtility.readImage(cFiles[i].getAbsolutePath());
                    img = new StegoImage(bi, cFiles[i].getAbsolutePath());
                } catch (Exception e) {
                    logger.warn("Could not create image from file: " + cFiles[i].getAbsolutePath(), e);
                    continue;
                }
                double[] measures;
                try {
                    measures = getImageMeasures(img);
                } catch (Exception e) {
                    logger.warn("Could not create measures for file: " + cFiles[i].getAbsolutePath(), e);
                    continue;
                }
                svm_node[] nodes = createSvmNodes(measures);
                clearNodes.add(nodes);
                logger.trace("Processed files: " + (i + 1) + "/" + cFiles.length);
            }
        }
        if (stegoNodes.isEmpty() && clearNodes.isEmpty()) {
            logger.error("No result for stego directory or clear directory. " + "Please check paths and contained images.");
            System.exit(1);
        }
        logger.trace("Writing clear svm_nodes to file");
        for (int i = 0; i < stegoNodes.size(); i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("+");
            sb.append(String.valueOf(1));
            sb.append(" ");
            svm_node[] n = stegoNodes.get(i);
            for (int j = 0; j < n.length; j++) {
                sb.append(String.valueOf(n[j].index));
                sb.append(":");
                sb.append(n[j].value);
                sb.append(" ");
            }
            sb.append("\n");
            try {
                fp.append(sb);
            } catch (Exception e) {
                logger.error("Error occured during writing to the output " + "train file", e);
                System.exit(1);
            }
        }
        logger.trace("Writing clear svm_nodes to file");
        for (int i = 0; i < clearNodes.size(); i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("-");
            sb.append(String.valueOf(1));
            sb.append(" ");
            svm_node[] n = clearNodes.get(i);
            for (int j = 0; j < n.length; j++) {
                sb.append(String.valueOf(n[j].index));
                sb.append(":");
                sb.append(n[j].value);
                sb.append(" ");
            }
            sb.append("\n");
            try {
                fp.append(sb);
            } catch (Exception e) {
                logger.error("Error occured during writing to the output " + "train file", e);
                System.exit(1);
            }
        }
        try {
            fp.close();
        } catch (IOException e) {
            logger.error("Error occured while closing file writer", e);
        }
        return dataFile;
    }

    private void saveImageNodes(String name, svm_node[] n, int val) throws IOException {
        BufferedWriter fp = new BufferedWriter(new FileWriter(name));
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(val));
        sb.append(" ");
        for (int j = 0; j < n.length; j++) {
            sb.append(String.valueOf(n[j].index));
            sb.append(":");
            sb.append(n[j].value);
            sb.append(" ");
        }
        sb.append("\n");
        fp.append(sb);
        fp.close();
    }

    private svm_node[] createSvmNodes(double[] measures) {
        ArrayList<svm_node> nodes = new ArrayList<svm_node>();
        for (int i = 0; i < measures.length; i++) {
            if (measures[i] != 0 && !Double.isInfinite(measures[i]) && !Double.isNaN(measures[i])) {
                svm_node n = new svm_node();
                n.index = i + 1;
                n.value = measures[i];
                nodes.add(n);
            }
        }
        return nodes.toArray(new svm_node[0]);
    }

    private double[] getImageMeasures(StegoImage img) {
        double[] measures = new double[18];
        if (img.getLayerCount() == 1) {
            boolean[][] seventhPlane = img.getBitPlane(0, 1);
            boolean[][] eightPlane = img.getBitPlane(0, 0);
            measures = getMeasures(seventhPlane, eightPlane);
        } else if (img.getLayerCount() == 3) {
            double[][] tmp = new double[3][18];
            for (int i = 0; i < 3; i++) {
                boolean[][] seventhPlane = img.getBitPlane(i, 1);
                boolean[][] eightPlane = img.getBitPlane(i, 0);
                tmp[i] = getMeasures(seventhPlane, eightPlane);
            }
            for (int i = 0; i < measures.length; i++) {
                measures[i] = (tmp[0][i] + tmp[1][i] + tmp[2][i]) / 3;
            }
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }
        return measures;
    }

    /**
     * Returns binary similarity measures between two bit planes.
     * @param seventhPlane 7th bit plane of an image layer
     * @param eightPlane 8th bit plane of an image layer
     */
    private double[] getMeasures(boolean[][] plane7, boolean[][] plane8) {
        double[] measures = new double[18];
        int rows = plane7.length;
        int columns = plane7[0].length;
        double[] p7 = new double[4];
        double[] p8 = new double[4];
        double[] Sn7 = new double[512];
        double[] Sn8 = new double[512];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int tmp7 = 0;
                int tmp8 = 0;
                boolean rowUp = false;
                boolean rowDown = false;
                boolean colLeft = false;
                boolean colRight = false;
                if (i - 1 >= 0) {
                    p7[stencil(plane7[i][j], plane7[i - 1][j])]++;
                    p8[stencil(plane8[i][j], plane8[i - 1][j])]++;
                    rowUp = true;
                }
                if (j + 1 < columns) {
                    p7[stencil(plane7[i][j], plane7[i][j + 1])]++;
                    p8[stencil(plane8[i][j], plane8[i][j + 1])]++;
                    colRight = true;
                }
                if (i + 1 < rows) {
                    p7[stencil(plane7[i][j], plane7[i + 1][j])]++;
                    p8[stencil(plane8[i][j], plane8[i + 1][j])]++;
                    rowDown = true;
                }
                if (j - 1 >= 0) {
                    p7[stencil(plane7[i][j], plane7[i][j - 1])]++;
                    p8[stencil(plane8[i][j], plane8[i][j - 1])]++;
                    colLeft = true;
                }
                tmp7 += 1 * ((rowUp && colLeft) ? toInt(plane7[i - 1][j - 1]) : 0) + 2 * (rowUp ? toInt(plane7[i - 1][j]) : 0) + 4 * ((rowUp && colRight) ? toInt(plane7[i - 1][j + 1]) : 0) + 8 * (colRight ? toInt(plane7[i][j + 1]) : 0) + 16 * ((colRight && rowDown) ? toInt(plane7[i + 1][j + 1]) : 0) + 32 * (rowDown ? toInt(plane7[i + 1][j]) : 0) + 64 * ((colLeft && rowDown) ? toInt(plane7[i + 1][j - 1]) : 0) + 128 * (colLeft ? toInt(plane7[i][j - 1]) : 0) + 256 * toInt(plane7[i][j]);
                if (tmp7 > 0) {
                    Sn7[tmp7 - 1]++;
                }
                tmp8 += 1 * ((rowUp && colLeft) ? toInt(plane8[i - 1][j - 1]) : 0) + 2 * (rowUp ? toInt(plane8[i - 1][j]) : 0) + 4 * ((rowUp && colRight) ? toInt(plane8[i - 1][j + 1]) : 0) + 8 * (colRight ? toInt(plane8[i][j + 1]) : 0) + 16 * ((colRight && rowDown) ? toInt(plane8[i + 1][j + 1]) : 0) + 32 * (rowDown ? toInt(plane8[i + 1][j]) : 0) + 64 * ((colLeft && rowDown) ? toInt(plane8[i + 1][j - 1]) : 0) + 128 * (colLeft ? toInt(plane8[i][j - 1]) : 0) + 256 * toInt(plane8[i][j]);
                if (tmp8 > 0) {
                    Sn8[tmp8 - 1]++;
                }
            }
        }
        double size = rows * columns;
        double sum7 = p7[0] + p7[1] + p7[2] + p7[3];
        double sum8 = p8[0] + p8[1] + p8[2] + p8[3];
        double a7 = p7[0] / size;
        double b7 = p7[1] / size;
        double c7 = p7[2] / size;
        double d7 = p7[3] / size;
        double a8 = p8[0] / size;
        double b8 = p8[1] / size;
        double c8 = p8[2] / size;
        double d8 = p8[3] / size;
        double[] pn7 = new double[p7.length];
        double[] pn8 = new double[p8.length];
        for (int i = 0; i < p7.length; i++) {
            pn7[i] = p7[i] / sum7;
            pn8[i] = p8[i] / sum8;
        }
        double m1_7 = 2 * (a7 + d7) / (2.0 * (a7 + d7) + b7 + c7);
        double m1_8 = 2 * (a8 + d8) / (2.0 * (a8 + d8) + b8 + c8);
        measures[0] = m1_7 - m1_8;
        double m2_7 = a7 / (a7 + 2 * (b7 + c7));
        double m2_8 = a8 / (a8 + 2 * (b8 + c8));
        measures[1] = m2_7 - m2_8;
        double m3_7 = a7 / (b7 + c7);
        double m3_8 = a8 / (b8 + c8);
        measures[2] = m3_7 = m3_8;
        double m4_7 = (a7 + d7) / (b7 + c7);
        double m4_8 = (a8 + d8) / (b8 + c8);
        measures[3] = m4_7 - m4_8;
        double m5_7 = (a7 / (a7 + b7) + a7 / (a7 + c7) + d7 / (b7 + d7) + d7 / (c7 + d7)) / 4.0;
        double m5_8 = (a8 / (a8 + b8) + a8 / (a8 + c8) + d8 / (b8 + d8) + d8 / (c8 + d8)) / 4.0;
        measures[4] = m5_7 - m5_8;
        double m6_7 = a7 * d7 / Math.sqrt((a7 + b7) * (a7 + c7) * (b7 + d7) * (c7 + d7));
        double m6_8 = a8 * d8 / Math.sqrt((a8 + b8) * (a8 + c8) * (b8 + d8) * (c8 + d8));
        measures[5] = m6_7 - m6_8;
        double m7_7 = Math.sqrt((a7 / (a7 + b7)) * (a7 / (a7 + c7)));
        double m7_8 = Math.sqrt((a8 / (a8 + b8)) * (a8 / (a8 + c8)));
        measures[6] = m7_7 - m7_8;
        double m8_7 = (b7 + c7) / (2 * a7 + b7 + c7);
        double m8_8 = (b8 + c8) / (2 * a8 + b8 + c8);
        measures[7] = m8_7 - m8_8;
        double m9_7 = b7 * c7 / Math.pow(a7 + b7 + c7 + d7, 2.0);
        double m9_8 = b8 * c8 / Math.pow(a8 + b8 + c8 + d8, 2.0);
        measures[8] = m9_7 - m9_8;
        double m10_7 = (b7 + c7) / (4.0 * (a7 + b7 + c7 + d7));
        double m10_8 = (b8 + c8) / (4.0 * (a8 + b8 + c8 + d8));
        measures[9] = m10_7 - m10_8;
        double dm11 = 0;
        double dm12 = 0;
        double dm13 = 0;
        double dm14 = 0;
        for (int i = 0; i < pn7.length; i++) {
            dm11 += Math.min(pn7[i], pn8[i]);
            dm12 += Math.abs(pn7[i] - pn8[i]);
            dm13 -= pn7[i] * Math.log(pn8[i]);
            dm14 -= pn7[i] * Math.log(pn7[i] / pn8[i]);
        }
        measures[10] = dm11;
        measures[11] = dm12;
        measures[12] = dm13;
        measures[13] = dm14;
        double dm15 = 0;
        double dm16 = 0;
        double dm17 = 0;
        double dm18 = 0;
        double tmp;
        for (int i = 0; i < Sn7.length; i++) {
            dm15 += Math.min(Sn7[i], Sn8[i]);
            dm16 += Math.abs(Sn7[i] - Sn8[i]);
            tmp = Sn7[i] * Math.log(Sn8[i]);
            dm17 -= Double.isNaN(tmp) || Double.isInfinite(tmp) ? 0 : tmp;
            tmp = Sn7[i] * Math.log(Sn7[i] / Sn8[i]);
            dm18 -= Double.isNaN(tmp) || Double.isInfinite(tmp) ? 0 : tmp;
        }
        measures[14] = dm15;
        measures[15] = dm16;
        measures[16] = dm17;
        measures[17] = dm18;
        return measures;
    }

    private int stencil(boolean xr, boolean xs) {
        return xr ? xs ? 3 : 2 : xs ? 1 : 0;
    }

    private int toInt(boolean b) {
        return b ? 1 : 0;
    }

    private double atof(String s) {
        return Double.valueOf(s).doubleValue();
    }

    private int atoi(String s) {
        return Integer.parseInt(s);
    }
}
