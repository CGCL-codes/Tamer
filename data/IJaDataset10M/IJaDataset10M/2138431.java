package bitWave.utilities;

import bitWave.geometry.GeometryFactory;
import bitWave.geometry.Line;
import bitWave.geometry.Mesh;
import bitWave.geometry.Vertex;
import bitWave.linAlg.LinAlgFactory;
import bitWave.linAlg.Vec4;

public class Tracer {

    LinAlgFactory m_laf;

    GeometryFactory m_geof;

    int m_w = 0;

    int m_h = 0;

    int m_x = 0;

    int m_y = 0;

    short m_key = 0;

    byte[] m_edges;

    Tracer(LinAlgFactory laf, GeometryFactory geof) {
        this.m_laf = laf;
        this.m_geof = geof;
    }

    /**
     * Scans the data line by line until the key is found.
     * if the value at the current position contains the key, the position is not
     * advanced at all.
     */
    boolean find() {
        int index = this.m_y * this.m_w + this.m_x;
        while (index < this.m_w * this.m_h) {
            if (this.m_edges[index] == 1) {
                this.m_x = index % this.m_w;
                this.m_y = index / this.m_w;
                return true;
            }
            index++;
        }
        this.m_x = index % this.m_w;
        this.m_y = index / this.m_w;
        return false;
    }

    /**
     * Hops from one pixel with the key value to the next as often as possible, adding
     * vertices to the current line. If the end of line has been reached, the old scan
     * position is restored.
     */
    void follow(Line l) {
        int ox = this.m_x;
        int oy = this.m_y;
        int count = 0;
        while (draw(l, 1, 0) || draw(l, 1, 1) || draw(l, 0, 1) || draw(l, -1, 1) || draw(l, -1, 0) || draw(l, -1, -1) || draw(l, 0, -1) || draw(l, 1, -1)) {
            count++;
        }
        this.m_x = ox;
        this.m_y = oy;
    }

    boolean rate(short current, short next) {
        return ((current <= 0) && (next > 0));
    }

    /**
     * Checks whether the value at the given x and y offset to the current scan position
     * equals the key value. If the effective position falls out of the map, the result is
     * always false.
     * @param dx
     * @param dy
     * @return True, if the value at the given offset equals the key.
     */
    boolean draw(Line l, int dx, int dy) {
        int x = this.m_x + dx;
        if (x < 0 || x >= this.m_w) return false;
        int y = this.m_y + dy;
        if (y < 0 || y >= this.m_h) return false;
        int index = y * this.m_w + x;
        if (this.m_edges[index] == 1) {
            this.m_x += dx;
            this.m_y += dy;
            l.addPoint(eat(l.getMesh()));
            return true;
        }
        return false;
    }

    /**
     * Returns a vertex for the given scan position and changes the pixel value to key+1 so
     * it will not be found again.
     * @return A vertex for the given scan position.
     */
    Vertex eat(Mesh m) {
        Vec4 pos = this.m_laf.createVector((double) this.m_x / (double) this.m_w, (double) this.m_y / (double) this.m_h, 0);
        this.m_edges[this.m_y * this.m_w + this.m_x] = 0;
        int index = m.addVertex(pos);
        return m.getVertexByIndex(index);
    }

    public void init(int width, int height) {
        this.m_w = width;
        this.m_h = height;
    }

    public void detectEdges(short[] data, byte[] edges, short key) {
        this.m_key = key;
        short current, next;
        for (int y = 0; y < this.m_h; y++) {
            int index = y * this.m_w;
            int nindex = index;
            current = data[index];
            for (int x = 0; x < this.m_w - 1; x++) {
                nindex = index + 1;
                next = data[nindex];
                if (rate(current, next)) edges[index] = 1;
                current = next;
                index = nindex;
            }
        }
        for (int y = 0; y < this.m_h; y++) {
            int index = y * this.m_w + this.m_w - 1;
            int nindex = index;
            current = data[index];
            for (int x = 0; x < this.m_w - 1; x++) {
                nindex = index - 1;
                next = data[nindex];
                if (rate(current, next)) edges[index] = 1;
                current = next;
                index = nindex;
            }
        }
        for (int x = 0; x < this.m_w; x++) {
            int index = x;
            int nindex = index;
            current = data[index];
            for (int y = 0; y < this.m_h - 1; y++) {
                nindex += this.m_w;
                next = data[nindex];
                if (rate(current, next)) edges[index] = 1;
                current = next;
                index = nindex;
            }
        }
        for (int x = 0; x < this.m_w; x++) {
            int index = (this.m_h - 1) * this.m_w + x;
            int nindex = index;
            current = data[index];
            for (int y = 0; y < this.m_h - 1; y++) {
                nindex = index - this.m_w;
                next = data[nindex];
                if (rate(current, next)) edges[index] = 1;
                current = next;
                index = nindex;
            }
        }
    }

    /**
     * Traces the pixel map given by data, width and height and returns a Mesh containing vertices
     * in the range of (0, 0, 0) to (1, 1, 0). The result is the raw trace, returning a vertex
     * per pixel of the given value, with lines connecting adjacent vertices.
     * @param edges
     * @return A mesh containing the trace.
     */
    public Mesh trace(byte[] edges) {
        this.m_edges = edges;
        this.m_x = 0;
        this.m_y = 0;
        Mesh m = this.m_geof.createMesh();
        int curY = -1;
        while (find()) {
            Line l = m.addLine();
            l.addPoint(eat(m));
            follow(l);
            if (this.m_y != curY) {
                System.out.println(this.m_y);
                curY = this.m_y;
            }
        }
        return m;
    }

    public void mergeLines(Line l1, Line l2, double tolerance) {
        double eps = tolerance * tolerance;
        if ((l1.getVertexCount() > 0) && (l2.getVertexCount() > 0)) {
            Vec4 end1 = l1.getVertexPositionByIndex(l1.getVertexCount() - 1);
            Vec4 start2 = l2.getVertexPositionByIndex(0);
            Vec4 end2 = l2.getVertexPositionByIndex(l2.getVertexCount() - 1);
            Vec4 diff;
            double len;
            diff = this.m_laf.subtractVectors(start2, end1);
            len = diff.getLengthSquared();
            if (len < eps) {
                l1.setCapacity(l1.getVertexCount() + l2.getVertexCount() * 2);
                for (int i = 0; i < l2.getVertexCount(); i++) l1.addPoint(l2.getVertexIndexByIndex(i));
                l2.clear();
            } else {
                diff = this.m_laf.subtractVectors(end2, end1);
                len = diff.getLengthSquared();
                if (len < eps) {
                    l1.setCapacity(l1.getVertexCount() + l2.getVertexCount() * 2);
                    for (int i = l2.getVertexCount() - 1; i >= 0; i--) l1.addPoint(l2.getVertexIndexByIndex(i));
                    l2.clear();
                } else {
                    Vec4 start1 = l1.getVertexPositionByIndex(l1.getVertexCount() - 1);
                    diff = this.m_laf.subtractVectors(end2, start1);
                    len = diff.getLengthSquared();
                    if (len < eps) {
                        l2.setCapacity(l1.getVertexCount() + l2.getVertexCount() * 2);
                        for (int i = 0; i < l1.getVertexCount(); i++) l2.addPoint(l1.getVertexIndexByIndex(i));
                        l1.clear();
                    } else {
                        diff = this.m_laf.subtractVectors(start2, start1);
                        len = diff.getLengthSquared();
                        if (len < eps) {
                            l2.setCapacity(l1.getVertexCount() + l2.getVertexCount() * 2);
                            for (int i = l1.getVertexCount() - 1; i >= 0; i--) l2.addPoint(l1.getVertexIndexByIndex(i));
                            l1.clear();
                        }
                    }
                }
            }
        }
    }

    public void mergeLines(Mesh m, double tolerance) {
        System.out.println("Merging with tolerance " + tolerance + "...");
        int n = m.getLineCount();
        for (int i = 0; i < n; i++) {
            Line l1 = m.getLineByIndex(i);
            for (int p = i + 1; p < n; p++) {
                Line l2 = m.getLineByIndex(p);
                mergeLines(l1, l2, tolerance);
            }
        }
    }

    public Mesh cullEmptyLines(Mesh m) {
        System.out.println("Culling empty lines...");
        Mesh d = this.m_geof.createMesh();
        int n = m.getLineCount();
        for (int i = 0; i < n; i++) {
            Line l1 = m.getLineByIndex(i);
            int lc = l1.getVertexCount();
            if (lc > 2) {
                Line l2 = d.addLine();
                l2.setCapacity(lc);
                for (int p = 0; p < lc; p++) l2.addPoint(l1.getVertexByIndex(p));
            }
        }
        return d;
    }

    /**
     * Clean up the given mesh and return the result. 
     * @param m The mesh to clean up.
     * @return A cleaned-up version of the same mesh.
     */
    public Mesh clean(Mesh m) throws CloneNotSupportedException {
        Mesh c = (Mesh) m.clone();
        mergeLines(c, 0.0);
        mergeLines(c, 2 / (double) this.m_h);
        mergeLines(c, 6 / (double) this.m_h);
        return cullEmptyLines(c);
    }

    /**
     * Smoothes line l1 with the given angular threshold and adds the new vertices to l2. 
     * @param l1 The line to smooth.
     * @param l2 The line to receive the new vertices.
     * @param thresh The angular threshold in radians.
     */
    void smoothLine(Line l1, Line l2, double thresh, double minStride, double maxStride) {
        int n = l1.getVertexCount();
        if (n > 1) {
            Vec4 v1, v2, d1, d2;
            int i = 0;
            Mesh m = l2.getMesh();
            while (i < n - 1) {
                v1 = l1.getVertexPositionByIndex(i);
                v2 = l1.getVertexPositionByIndex(i + 1);
                l2.addPoint(m.addVertex(v1));
                d1 = this.m_laf.subtractVectors(v2, v1);
                d1.normalize();
                int p;
                double stride = 0;
                for (p = i + 1; p < n; p++) {
                    v2 = l1.getVertexPositionByIndex(p);
                    d2 = this.m_laf.subtractVectors(v2, v1);
                    double l = d2.normalize();
                    stride += l;
                    double dev = Math.PI - Math.abs(this.m_laf.calculateDotProduct(d1, d2)) * Math.PI;
                    if ((stride > minStride) && (dev > thresh || stride > maxStride)) {
                        p--;
                        break;
                    }
                }
                i = p;
            }
            l2.addPoint(m.addVertex(l1.getVertexPositionByIndex(n - 1)));
        }
    }

    public Mesh smooth(Mesh m, double thresh, double minStride, double maxStride) throws CloneNotSupportedException {
        System.out.println("Smoothing lines...");
        Mesh s = this.m_geof.createMesh();
        for (int i = 0; i < m.getLineCount(); i++) {
            Line l1 = m.getLineByIndex(i);
            Line l2 = s.addLine();
            smoothLine(l1, l2, thresh, minStride, maxStride);
        }
        return cullEmptyLines(s);
    }

    /**
    * Projects a planar mesh in 0..1x, 0..1y onto a sphere of radius r with the 0 meridian in +X and north at +Y. 
    * @param m The source mesh.
    * @param r The radius.
    * @return The spherical mesh.
    */
    public Mesh toSpherical(Mesh m, double r) throws CloneNotSupportedException {
        Mesh s = (Mesh) m.clone();
        for (int i = 0; i < m.getVertexCount(); i++) {
            Vec4 p = s.getVertexPositionByIndex(i);
            double lat = (Math.PI / 2.0) - (p.y() * Math.PI);
            double lon = p.x() * 2.0 * Math.PI;
            double y = r * Math.sin(lat);
            double R = r * Math.cos(lat);
            double x = R * Math.cos(lon);
            double z = R * Math.sin(lon);
            Vec4 t = this.m_laf.createVector(x, y, z);
            s.setVertexPositionByIndex(i, t);
        }
        return s;
    }
}
