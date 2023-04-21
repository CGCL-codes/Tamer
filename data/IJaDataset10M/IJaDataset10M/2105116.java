package playground.gregor.otf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.matsim.core.utils.collections.QuadTree;
import org.matsim.evacuation.flooding.FloodingInfo;
import org.matsim.evacuation.flooding.FloodingReader;
import playground.gregor.collections.gnuclasspath.TreeMap;
import playground.gregor.otf.InundationData.InundationGeometry;
import playground.gregor.otf.InundationData.Polygon;
import playground.gregor.otf.InundationData.Quad;
import playground.gregor.otf.InundationData.Triangle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

public class InundationDataFromNetcdfReader {

    private final String file = "../../inputs/flooding/flooding_old.sww";

    public static final float[] cvdeep = new float[] { 0.f, 0.f, 1.f };

    public static final float[] cdeep = new float[] { 0.f, 0.1f, 0.9f };

    public static final float[] cumid = new float[] { 0.f, 0.2f, 0.8f };

    public static final float[] clmid = new float[] { 0.f, 0.3f, 0.7f };

    public static final float[] clow = new float[] { 0.f, 0.4f, 0.6f };

    public static final float[] cvlow = new float[] { 0.f, 0.9f, 0.7f };

    private static final String BASE = "/home/laemmel/devel/inputs/flooding/flooding0";

    private double offsetEast = 0;

    private double offsetNorth = 0;

    private InundationData inundationData;

    private TreeMap<Double, float[]> colorTree;

    private int seriesLength;

    private QuadTree<Integer> coordinateQuadTree;

    public InundationDataFromNetcdfReader(double on, double oe) {
        this.offsetNorth = on;
        this.offsetEast = oe;
    }

    public InundationData createData() {
        this.inundationData = new InundationData();
        init();
        this.inundationData.colorMapping = this.colorTree;
        this.inundationData.seriesLength = this.seriesLength;
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.inundationData;
    }

    private void writeToFile() throws IOException {
        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("test.dat"));
        o.writeObject(this.inundationData);
        o.close();
    }

    private void init() {
        this.inundationData.powerLookUp = new double[4];
        double z = 1;
        for (int i = 0; i < this.inundationData.powerLookUp.length; i++) {
            this.inundationData.powerLookUp[i] = (z *= 4);
        }
        createColorTabel();
        HashMap<Integer, ArrayList<InundationGeometry>> mapping = new HashMap<Integer, ArrayList<InundationGeometry>>();
        QuadTree<InundationGeometry> triangles = getTriangles(mapping);
        triangles = scaleTriangles(triangles, mapping);
        this.inundationData.floodingData.put(this.inundationData.powerLookUp[0], triangles);
        for (int i = 1; i < this.inundationData.powerLookUp.length; i++) {
            QuadTree<InundationGeometry> tmp = scaleGeometries(triangles, mapping);
            double zoom = this.inundationData.powerLookUp[i];
            this.inundationData.floodingData.put(zoom, tmp);
            triangles = tmp;
        }
        cleanup();
    }

    private void cleanup() {
        System.out.println("Coords before cleanup:" + this.inundationData.xcoords.length);
        this.coordinateQuadTree.clear();
        ArrayList<Float> xcoords = new ArrayList<Float>();
        ArrayList<Float> ycoords = new ArrayList<Float>();
        ArrayList<float[]> walshs = new ArrayList<float[]>();
        int newIdx = 0;
        for (QuadTree<InundationGeometry> tmp : this.inundationData.floodingData.values()) {
            for (InundationGeometry g : tmp.values()) {
                int maxCount = g.getCoords().length;
                for (int i = 0; i < maxCount; i++) {
                    float x = this.inundationData.xcoords[g.getCoords()[i]];
                    float y = this.inundationData.ycoords[g.getCoords()[i]];
                    Collection<Integer> coord;
                    int cIdx;
                    if ((coord = this.coordinateQuadTree.get(x, y, 0.01)).size() == 0) {
                        cIdx = newIdx++;
                        xcoords.add(x);
                        ycoords.add(y);
                        float[] coeffs = this.inundationData.walshs[g.getCoords()[i]];
                        walshs.add(coeffs);
                        this.coordinateQuadTree.put(x, y, cIdx);
                    } else {
                        cIdx = coord.iterator().next();
                    }
                    g.getCoords()[i] = cIdx;
                }
            }
        }
        this.inundationData.walshs = new float[walshs.size()][InundationData.RES + 1];
        for (int i = 0; i < walshs.size(); i++) {
            this.inundationData.walshs[i] = walshs.get(i);
        }
        walshs = null;
        this.inundationData.xcoords = new float[xcoords.size()];
        for (int i = 0; i < xcoords.size(); i++) {
            this.inundationData.xcoords[i] = xcoords.get(i);
        }
        xcoords = null;
        this.inundationData.ycoords = new float[ycoords.size()];
        for (int i = 0; i < ycoords.size(); i++) {
            this.inundationData.ycoords[i] = ycoords.get(i);
        }
        ycoords = null;
        System.out.println("Coords after cleanup:" + this.inundationData.xcoords.length);
    }

    private QuadTree<InundationGeometry> scaleTriangles(QuadTree<InundationGeometry> triangles, HashMap<Integer, ArrayList<InundationGeometry>> mapping) {
        QuadTree<InundationGeometry> ret = new QuadTree<InundationGeometry>(triangles.getMinEasting(), triangles.getMinNorthing(), triangles.getMaxEasting(), triangles.getMaxNorthing());
        HashSet<InundationGeometry> removed = new HashSet<InundationGeometry>();
        HashMap<Integer, ArrayList<InundationGeometry>> newMapping = new HashMap<Integer, ArrayList<InundationGeometry>>();
        int toGo = triangles.values().size();
        ConcurrentLinkedQueue<InundationGeometry> geos = new ConcurrentLinkedQueue<InundationGeometry>();
        for (InundationGeometry p : triangles.values()) {
            geos.add(p);
        }
        GeometryFactory geofac = new GeometryFactory();
        int minSize = 10;
        while (geos.size() > 0) {
            InundationGeometry p = geos.poll();
            if (toGo-- % 1000 == 0) {
                System.out.println("toGo" + toGo);
            }
            if (toGo <= 0) {
                toGo = geos.size();
                minSize--;
                if (minSize < 3) {
                    break;
                }
                System.out.println("minSize: " + minSize + " toGo: " + toGo);
            }
            if (removed.contains(p)) {
                continue;
            }
            HashSet<InundationGeometry> neighbors = new HashSet<InundationGeometry>();
            for (int i = 0; i < p.getCoords().length; i++) {
                neighbors.addAll(mapping.get(p.getCoords()[i]));
            }
            HashSet<Coordinate> l = new HashSet<Coordinate>();
            Coordinate first = null;
            for (InundationGeometry tmp : neighbors) {
                if (removed.contains(tmp)) {
                    continue;
                }
                for (int i = 0; i < tmp.getCoords().length; i++) {
                    double x = this.inundationData.xcoords[tmp.getCoords()[i]];
                    double y = this.inundationData.ycoords[tmp.getCoords()[i]];
                    Coordinate ttt = new Coordinate(x, y);
                    if (first == null) {
                        first = ttt;
                    }
                    l.add(ttt);
                }
            }
            if (l.size() < minSize) {
                geos.add(p);
                continue;
            }
            Coordinate[] coordinates = new Coordinate[l.size() + 1];
            int pos = 0;
            for (Coordinate c : l) {
                coordinates[pos++] = c;
            }
            coordinates[pos] = coordinates[0];
            LinearRing lr = geofac.createLinearRing(coordinates);
            Geometry geo = geofac.createPolygon(lr, null);
            coordinates = geo.convexHull().getCoordinates();
            if (coordinates.length < 4) {
                continue;
            }
            removed.addAll(neighbors);
            int[] coords = new int[coordinates.length - 1];
            pos = 0;
            double x = 0.;
            double y = 0.;
            for (int i = 0; i < coordinates.length - 1; i++) {
                coords[i] = this.coordinateQuadTree.get(coordinates[i].x, coordinates[i].y);
                x += this.inundationData.xcoords[coords[i]];
                y += this.inundationData.ycoords[coords[i]];
            }
            Polygon pol = new Polygon(this.inundationData);
            pol.coordsIdx = coords;
            x /= coords.length;
            y /= coords.length;
            ret.put(x, y, pol);
            for (int i = 0; i < coords.length; i++) {
                ArrayList<InundationGeometry> tmp = newMapping.get(pol.coordsIdx[i]);
                if (tmp == null) {
                    tmp = new ArrayList<InundationGeometry>();
                    newMapping.put(pol.coordsIdx[i], tmp);
                }
                tmp.add(pol);
            }
        }
        mapping.clear();
        mapping.putAll(newMapping);
        this.coordinateQuadTree.clear();
        for (InundationGeometry ig : ret.values()) {
            for (int i = 0; i < ig.getCoords().length; i++) {
                this.coordinateQuadTree.put(this.inundationData.xcoords[ig.getCoords()[i]], this.inundationData.ycoords[ig.getCoords()[i]], ig.getCoords()[i]);
            }
        }
        return ret;
    }

    private QuadTree<InundationGeometry> scaleGeometries(QuadTree<InundationGeometry> triangles, HashMap<Integer, ArrayList<InundationGeometry>> mapping) {
        QuadTree<InundationGeometry> ret = new QuadTree<InundationGeometry>(triangles.getMinEasting(), triangles.getMinNorthing(), triangles.getMaxEasting(), triangles.getMaxNorthing());
        HashSet<InundationGeometry> removed = new HashSet<InundationGeometry>();
        HashMap<Integer, ArrayList<InundationGeometry>> newMapping = new HashMap<Integer, ArrayList<InundationGeometry>>();
        int toGo = triangles.values().size();
        for (InundationGeometry p : triangles.values()) {
            if (removed.contains(p)) {
                continue;
            }
            int maxCount = p.getCoords().length - 1;
            HashSet<InundationGeometry> neighbors = new HashSet<InundationGeometry>();
            for (int i = 0; i <= maxCount; i++) {
                neighbors.addAll(mapping.get(p.getCoords()[i]));
            }
            HashSet<InundationGeometry> newNeighbors = new HashSet<InundationGeometry>();
            for (InundationGeometry ig : neighbors) {
                if (!removed.contains(ig)) {
                    newNeighbors.add(ig);
                }
            }
            if (newNeighbors.size() == 0) {
                continue;
            }
            double maxX = 0;
            double maxY = 0;
            double minX = Double.POSITIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            for (InundationGeometry n : newNeighbors) {
                removed.add(n);
                maxCount = n.getCoords().length - 1;
                for (int i = 0; i <= maxCount; i++) {
                    float x = this.inundationData.xcoords[n.getCoords()[i]];
                    float y = this.inundationData.ycoords[n.getCoords()[i]];
                    maxX = maxX > x ? maxX : x;
                    minX = minX < x ? minX : x;
                    maxY = maxY > y ? maxY : y;
                    minY = minY < y ? minY : y;
                }
            }
            try {
                int c1 = this.coordinateQuadTree.get(maxX, maxY);
                int c2 = this.coordinateQuadTree.get(maxX, minY);
                int c3 = this.coordinateQuadTree.get(minX, minY);
                int c4 = this.coordinateQuadTree.get(minX, maxY);
                Quad q = new Quad(this.inundationData);
                q.coordsIdx[0] = c1;
                q.coordsIdx[1] = c2;
                q.coordsIdx[2] = c3;
                q.coordsIdx[3] = c4;
                double x = (maxX + minX) / 2;
                double y = (maxY + minY) / 2;
                ret.put(x, y, q);
                for (int i = 0; i <= 3; i++) {
                    ArrayList<InundationGeometry> tmp = newMapping.get(q.coordsIdx[i]);
                    if (tmp == null) {
                        tmp = new ArrayList<InundationGeometry>();
                        newMapping.put(q.coordsIdx[i], tmp);
                    }
                    tmp.add(q);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mapping.clear();
        mapping.putAll(newMapping);
        this.coordinateQuadTree.clear();
        for (InundationGeometry ig : ret.values()) {
            for (int i = 0; i < ig.getCoords().length; i++) {
                this.coordinateQuadTree.put(this.inundationData.xcoords[ig.getCoords()[i]], this.inundationData.ycoords[ig.getCoords()[i]], ig.getCoords()[i]);
            }
        }
        return ret;
    }

    private int getNumOfCommonVert(InundationGeometry g1, InundationGeometry g2) {
        int hit = 0;
        for (int i = 0; i < g1.getCoords().length; i++) {
            for (int j = 0; j < g2.getCoords().length; j++) {
                if (g1.getCoords()[i] == g2.getCoords()[j]) {
                    hit++;
                }
            }
        }
        return hit;
    }

    private int getUncommonVert(Triangle candidate, Triangle tri) {
        int hit = 0;
        int uc = 0 + 1 + 2;
        for (int i = 0; i < candidate.coordsIdx.length; i++) {
            for (int j = 0; j < tri.coordsIdx.length; j++) {
                if (candidate.coordsIdx[i] == tri.coordsIdx[j]) {
                    hit++;
                    uc -= i;
                }
            }
        }
        if (hit != 2) {
            return -1;
        }
        return uc;
    }

    private float[] getTriangleCenter(Triangle t) {
        float x = 0.f;
        float y = 0.f;
        for (int j = 0; j < 3; j++) {
            x += this.inundationData.xcoords[t.coordsIdx[j]];
            y += this.inundationData.ycoords[t.coordsIdx[j]];
        }
        return new float[] { x / 3.f, y / 3.f };
    }

    private QuadTree<InundationGeometry> getTriangles(HashMap<Integer, ArrayList<InundationGeometry>> coordTriMapping) {
        int coordIdx = 0;
        double maxX = 10000000;
        double maxY = 10000000;
        double minX = -10000000;
        double minY = -10000000;
        QuadTree<InundationGeometry> ret = new QuadTree<InundationGeometry>(minX, minY, maxX, maxY);
        this.coordinateQuadTree = new QuadTree<Integer>(minX, minY, maxX, maxY);
        ArrayList<Float> xcoords = new ArrayList<Float>();
        ArrayList<Float> ycoords = new ArrayList<Float>();
        ArrayList<float[]> walshs = new ArrayList<float[]>();
        for (int i = 0; i < 1; i++) {
            String file = this.file;
            FloodingReader r = new FloodingReader(file);
            List<int[]> triangles = r.getTriangles();
            Map<Integer, Integer> mapping = r.getIdxMapping();
            List<FloodingInfo> infos = r.getFloodingInfos();
            r = null;
            int count = triangles.size();
            for (int[] tri : triangles) {
                Triangle t = new Triangle(this.inundationData);
                for (int j = 0; j < 3; j++) {
                    Integer idx = mapping.get(tri[j]);
                    if (idx == null) {
                        t = null;
                        break;
                    }
                    FloodingInfo fi = infos.get(idx);
                    int cIdx;
                    Collection<Integer> coord;
                    if ((coord = this.coordinateQuadTree.get(fi.getCoordinate().x - this.offsetEast, fi.getCoordinate().y - this.offsetNorth, 0.01)).size() == 0) {
                        cIdx = coordIdx++;
                        xcoords.add((float) (fi.getCoordinate().x - this.offsetEast));
                        ycoords.add((float) (fi.getCoordinate().y - this.offsetNorth));
                        float[] coeffs = getWalshCoefs(fi.getFloodingSeries());
                        walshs.add(coeffs);
                        this.coordinateQuadTree.put(fi.getCoordinate().x - this.offsetEast, fi.getCoordinate().y - this.offsetNorth, cIdx);
                    } else {
                        cIdx = coord.iterator().next();
                    }
                    t.coordsIdx[j] = cIdx;
                }
                if (t != null) {
                    double x = 0.;
                    double y = 0.;
                    for (int j = 0; j < 3; j++) {
                        x += xcoords.get(t.coordsIdx[j]);
                        y += ycoords.get(t.coordsIdx[j]);
                        ArrayList<InundationGeometry> m = coordTriMapping.get(t.coordsIdx[j]);
                        if (m == null) {
                            m = new ArrayList<InundationGeometry>();
                            coordTriMapping.put(t.coordsIdx[j], m);
                        }
                        m.add(t);
                    }
                    ret.put(x / 3, y / 3, t);
                }
            }
        }
        this.inundationData.walshs = new float[walshs.size()][InundationData.RES + 1];
        for (int i = 0; i < walshs.size(); i++) {
            this.inundationData.walshs[i] = walshs.get(i);
        }
        walshs = null;
        this.inundationData.xcoords = new float[xcoords.size()];
        for (int i = 0; i < xcoords.size(); i++) {
            this.inundationData.xcoords[i] = xcoords.get(i);
        }
        xcoords = null;
        this.inundationData.ycoords = new float[ycoords.size()];
        for (int i = 0; i < ycoords.size(); i++) {
            this.inundationData.ycoords[i] = ycoords.get(i);
        }
        ycoords = null;
        return ret;
    }

    private float[] getWalshCoefs(List<Double> floodingSeries) {
        float[] coeffs = new float[InundationData.RES + 1];
        this.seriesLength = Math.max(this.seriesLength, floodingSeries.size());
        coeffs[InundationData.RES] = floodingSeries.size();
        for (int idx = 0; idx < floodingSeries.size(); idx++) {
            double d = floodingSeries.get(idx);
            if (d > 0.05) {
                coeffs[InundationData.RES] = idx;
                break;
            }
        }
        for (int k = 0; k < InundationData.RES; k++) {
            double tmp1 = 0;
            double tmp2 = 0;
            for (int i = (int) coeffs[InundationData.RES]; i < floodingSeries.size(); i++) {
                int idx = (int) Math.round(((double) (i - (int) coeffs[InundationData.RES]) / (floodingSeries.size() - (int) coeffs[InundationData.RES])) * (InundationData.RES - 1));
                tmp1 += floodingSeries.get(i) * InundationData.walsh8[k][idx];
                tmp2 += InundationData.walsh8[k][idx] * InundationData.walsh8[k][idx];
            }
            coeffs[k] = (float) (tmp1 / tmp2);
        }
        return coeffs;
    }

    private void createColorTabel() {
        TreeMap<Double, float[]> colorTree = new TreeMap<Double, float[]>();
        byte idx = 0;
        idx++;
        double d = 0.05;
        for (d = 0.05; d < 0.5; d += 0.01) {
            double w1 = 1 - (0.5 - d) / 0.45;
            double w2 = 1 - w1;
            colorTree.put(d, new float[] { (float) (clow[0] * w1 + cvlow[0] * w2), (float) (clow[1] * w1 + cvlow[1] * w2), (float) (clow[2] * w1 + cvlow[2] * w2), (float) w1 });
            idx++;
        }
        System.out.println("IDX:" + idx);
        for (d = 0.5; d < 4; d += .05) {
            double w1 = (d - 0.2) / 3.8;
            double w2 = 1 - w1;
            colorTree.put(d, new float[] { (float) (cvdeep[0] * w1 + clow[0] * w2), (float) (cvdeep[1] * w1 + clow[1] * w2), (float) (cvdeep[2] * w1 + clow[2] * w2), 1 });
            idx++;
        }
        System.out.println("IDX:" + idx);
        System.out.println("IDX:" + idx);
        colorTree.put(3., new float[] { (cvdeep[0]), (cvdeep[1]), (cvdeep[2]), 1 });
        this.colorTree = colorTree;
    }
}
