package ai.gui;

import ai.io.DataSource;
import ai.io.TrainingFileReader;
import ai.*;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pjl
 */
public class ImageLabelDataSource implements DataSource {

    private TrainingFileReader trainingReader;

    private Dimension dimension;

    private TrainingFileReader trainingLabelReader;

    public ImageLabelDataSource(String name) throws FileNotFoundException, IOException {
        String root = "/home/pjl/TrainingData/";
        File file = new File(root + name + "-images-idx3-ubyte");
        trainingReader = new TrainingFileReader(file);
        int d[] = trainingReader.getSizes();
        dimension = new Dimension(d[1], d[2]);
        file = new File(root + name + "-labels-idx1-ubyte");
        trainingLabelReader = new TrainingFileReader(file);
    }

    public int getSize() {
        return trainingReader.getSizes()[0];
    }

    static float labs[][];

    static int nLabels = 10;

    static {
        labs = new float[nLabels][];
        for (int i = 0; i < 10; i++) {
            labs[i] = new float[10];
            labs[i][i] = 1.0f;
        }
    }

    public Data getData(int index) {
        Data data = new Data();
        try {
            byte[][] b = (byte[][]) trainingReader.getData(index);
            int w = b.length;
            int h = b[0].length;
            data.image = new float[w * h];
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    data.image[i + j * w] = ((float) (b[j][i] & 0xff)) / 256.0f;
                }
            }
            int lab = trainingLabelReader.getLabel(index);
            data.label = labs[lab];
        } catch (Exception ex) {
            Logger.getLogger(ImageLabelDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public Dimension getInDimension() {
        int siz[] = trainingReader.getSizes();
        Dimension dim = new Dimension(siz[1], siz[2]);
        return dim;
    }

    static Dimension dout = new Dimension(10, 1);

    public Dimension getOutDimension() {
        return dout;
    }
}
