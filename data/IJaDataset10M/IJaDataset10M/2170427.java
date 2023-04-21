package skewreduce.lsst;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.Writable;

public class Observ implements Writable {

    double cX;

    double cY;

    double avgDist;

    double pixelSum;

    List<PixVal> pixels = new ArrayList<PixVal>(4);

    List<Coord> polyVertices;

    public Observ() {
    }

    public Observ(List<PixVal> v) {
        pixels = v;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isCore() {
        return true;
    }

    public List<PixVal> getPixels() {
        return pixels;
    }

    public void merge(Observ o) {
        pixels.addAll(o.pixels);
    }

    public void addPixel(PixVal pixVal) {
        pixels.add(pixVal);
    }

    public boolean isIsolated(Image2DPartition bound, List<PixVal> vs) {
        for (PixVal v : pixels) {
            if (bound.atSkin(v, 1)) {
                vs.add(v);
            }
        }
        return vs.isEmpty();
    }

    public void build() {
        double wSumX = 0.0;
        double wSumY = 0.0;
        for (PixVal p : pixels) {
            pixelSum += p.getVal();
            wSumX += (p.getVal() * p.getX());
            wSumY += (p.getVal() * p.getY());
        }
        cX = wSumX / pixelSum;
        cY = wSumY / pixelSum;
        double distSum = 0.0;
        double dist;
        double distX;
        double distY;
        for (PixVal p : pixels) {
            distX = p.getX() - cX;
            distY = p.getY() - cY;
            dist = Math.sqrt(distX * distX + distY * distY);
            distSum += (p.getVal() * dist);
        }
        avgDist = distSum / pixelSum;
        polyVertices = new ArrayList<Coord>(pixels.size());
        new Poligonizer().findPoly(pixels, polyVertices);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        cX = in.readDouble();
        cY = in.readDouble();
        avgDist = in.readDouble();
        pixelSum = in.readDouble();
        int nPx = in.readInt();
        for (int i = 0; i < nPx; ++i) {
            PixVal v = new PixVal();
            v.readFields(in);
            pixels.add(v);
        }
        nPx = in.readInt();
        for (int i = 0; i < nPx; ++i) {
            Coord c = new Coord();
            c.readFields(in);
            polyVertices.add(c);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(cX);
        out.writeDouble(cY);
        out.writeDouble(avgDist);
        out.writeDouble(pixelSum);
        out.writeInt(pixels.size());
        for (PixVal p : pixels) {
            p.write(out);
        }
        if (polyVertices == null) {
            out.writeInt(0);
        } else {
            out.writeInt(polyVertices.size());
            for (Coord c : polyVertices) {
                c.write(out);
            }
        }
    }
}
