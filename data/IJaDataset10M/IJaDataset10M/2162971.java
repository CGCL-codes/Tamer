package sequence;

import java.io.IOException;
import java.io.ObjectOutputStream;
import types.Alphabet;
import types.SparseVector;
import types.StaticUtils;

public class TwoYwithXFeatureFunction implements SequenceFeatureFunction {

    private static final long serialVersionUID = 1L;

    public int xAsize, yAsize;

    public int numInputs;

    public TwoYwithXFeatureFunction(Alphabet xAlphabet, Alphabet yAlphabet) {
        this.xAsize = xAlphabet.size();
        this.yAsize = yAlphabet.size();
        this.numInputs = xAlphabet.size() + 1;
    }

    public SparseVector apply(SparseVector[] x, int[] y) {
        SparseVector result = this.apply(x, 0, y[0], 0);
        for (int t = 1; t < x.length; t++) {
            SparseVector fsubt = this.apply(x, y[t - 1], y[t], t);
            StaticUtils.plusEquals(result, fsubt);
        }
        return result;
    }

    private int initialIndex(int y, int x) {
        return y * this.numInputs + x;
    }

    private int finalIndex(int y, int x) {
        return this.yAsize * this.numInputs + y * this.numInputs + x;
    }

    private int labelXlabel(int y1, int y2, int x) {
        return 2 * this.yAsize * this.numInputs + y1 * this.yAsize * this.numInputs + y2 * this.numInputs + x;
    }

    private int labelXinput(int y, int x) {
        return 2 * this.yAsize * this.numInputs + this.yAsize * this.yAsize * this.numInputs + y * this.xAsize + x;
    }

    public int wSize() {
        return 2 * this.yAsize * this.numInputs + this.yAsize * this.yAsize * this.numInputs + this.yAsize * this.numInputs;
    }

    public SparseVector apply(SparseVector[] xseq, int ytm1, int yt, int t) {
        SparseVector result = new SparseVector();
        for (int i = 0; i < xseq[t].numEntries(); i++) {
            int x = xseq[t].getIndexAt(i);
            if (t == 0) {
                result.add(this.initialIndex(yt, x), 1);
            } else {
                result.add(this.labelXlabel(ytm1, yt, x), 1);
            }
            if (t == xseq.length - 1) {
                result.add(this.finalIndex(yt, x), 1);
            }
            result.add(this.labelXinput(yt, x), 1);
        }
        int x = this.xAsize;
        if (t == 0) {
            result.add(this.initialIndex(yt, x), 1);
        } else {
            result.add(this.labelXlabel(ytm1, yt, x), 1);
        }
        if (t == xseq.length - 1) {
            result.add(this.finalIndex(yt, x), 1);
        }
        result.add(this.labelXinput(yt, x), 1);
        return result;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeLong(serialVersionUID);
        out.writeInt(xAsize);
        out.writeInt(yAsize);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        long inid = in.readLong();
        if (inid != serialVersionUID) throw new IOException("Serial version mismatch: expected " + serialVersionUID + " got " + inid);
        xAsize = in.readInt();
        numInputs = xAsize + 1;
        yAsize = in.readInt();
    }
}
