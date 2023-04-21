package nl.ru.rd.facedetection.nnbfd;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import nl.ru.rd.facedetection.nnbfd.neuralnetwork.NeuralNetwork;

/**
 * Teaches a Neural Network using a Learnset. Specific class for a test in a paper.
 * 
 * @author Wouter Geraedts (s0814857 - wgeraedts) PGP 66AA5935
 */
public class Learning {

    private static NeuralNetwork initializeNN() {
        File networkFile = new File("network");
        NeuralNetwork network = null;
        if (!networkFile.exists()) network = new FacedetectionNetwork(); else network = NeuralNetwork.readFromFile("network");
        return network;
    }

    private static Matrix createRandomMatrix() {
        Matrix matrix = new Matrix(20, 20);
        for (int x = 0; x < 20; x++) for (int y = 0; y < 20; y++) matrix.setValue(x, y, (short) Math.round(Math.random() * 255));
        return matrix;
    }

    private static void addDirectoryToList(ArrayList<LearnSet> list, String directory, double result) {
        File faalDirectory = new File(directory);
        for (String file : faalDirectory.list()) {
            String filePath = directory + file;
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                System.out.println(filePath);
                e.printStackTrace();
            }
            Matrix matrix = Preprocessor.toMatrix(image);
            list.add(new LearnSet(matrix, new double[] { result }));
        }
    }

    public static void main(String[] args) {
        NeuralNetwork network = initializeNN();
        if (network == null) return;
        for (int a = 0; a < 1000; a++) {
            System.out.println(a);
            ArrayList<LearnSet> list = new ArrayList<LearnSet>(3000);
            for (int i = 0; i < 10000; i++) {
                Matrix matrix = createRandomMatrix();
                Preprocessor.equalize(matrix);
                list.add(new LearnSet(matrix, new double[] { 0.0 }));
            }
            Learning.addDirectoryToList(list, "/media/data/web/web/test/", 1.0);
            Learning.addDirectoryToList(list, "faal/", 0.0);
            Learning.addDirectoryToList(list, "test/", 0.0);
            System.out.println(list.size());
            while (list.size() > 0) {
                int i = (int) Math.floor(Math.random() * ((double) list.size()));
                LearnSet set = list.get(i);
                list.remove(i);
                network.learn(set.matrix, set.expectedResult, 0.01);
            }
            try {
                network.toFile("networks/network" + a);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
