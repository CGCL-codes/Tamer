package ai.pjbrain;

import ai.AbstractBrain;
import ai.Brain;
import ai.Result;
import ai.WeightVectorImage;
import ai.gui.Image;
import ai.gui.ImageLabel;
import ai.io.DataSource.Data;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import uk.ac.bath.util.Tweakable;

/**
 *
 * @author pjl
 */
public class PJBrain extends AbstractBrain {

    Image image[];

    float w[][];

    int nImagines;

    private ImageLabel reality;

    int nNeuron;

    int nIn;

    int nOut;

    private float[] x;

    private float[] o;

    private float[] out;

    private float beta;

    int n;

    private float[] f;

    private float[] error;

    public PJBrain(Dimension din, Dimension dout) {
        super(din, dout, "PJ Brain");
        nIn = din.width * din.height;
        nOut = dout.width * dout.height;
        n = nIn + nOut;
        nNeuron = 10;
        f = new float[n];
        o = new float[nNeuron + 1];
        error = new float[n];
        o[nNeuron] = 1.0f;
        w = new float[nNeuron + 1][];
        image = new Image[nNeuron + 1];
        out = new float[nOut];
        x = new float[n + 1];
        x[n] = 1.0f;
        beta = .01f;
        for (int i = 0; i < nNeuron + 1; i++) {
            w[i] = new float[n + 1];
            if (i == 0) image[i] = new WeightVectorImage(f, din, "fantasy"); else image[i] = new WeightVectorImage(w[i - 1], din, "Image" + i);
        }
    }

    public Image getImagination(int i) {
        return image[i];
    }

    public int getImaginationCount() {
        return nNeuron;
    }

    public ImageLabel getReality() {
        return reality;
    }

    public Vector<Tweakable> getTweaks() {
        return null;
    }

    static float sigmoid(float in) {
        return (float) (1.0 / (1.0 + Math.exp(-in)));
    }

    public void fire(Data data) {
        createIn(data);
        forwardProject();
        createOut();
    }

    private void createOut() {
        System.arraycopy(f, nIn, out, 0, nOut);
    }

    private void createIn(Data data) {
        System.arraycopy(data.image, 0, x, 0, nIn);
        System.arraycopy(data.label, 0, x, nIn, nOut);
    }

    private void forwardProject() {
        assert (x[x.length - 1] == 1.0f);
        for (int i = 0; i < nNeuron; i++) {
            float sum = 0f;
            for (int j = 0; j < n + 1; j++) {
                sum += w[i][j] * x[j];
            }
            o[i] = sigmoid(sum);
        }
    }

    public void backProject() {
        for (int i = 0; i < n; i++) {
            float sum = 0f;
            for (int j = 0; j < nNeuron + 1; j++) {
                sum += w[j][i] * o[j];
            }
            f[i] = sigmoid(sum);
        }
        for (int i = 0; i < n; i++) {
            error[i] = f[i] - o[i];
        }
    }

    public void setTrain(Data data) {
        createIn(data);
        forwardProject();
        backProject();
    }

    public String outString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ImageLabel getFantasy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getResult(Result result) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getLatestResult(Result result) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dump() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
