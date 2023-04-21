package protein3dprint.polygons;

import java.io.PrintStream;
import java.util.Vector;
import protein3dprint.message.MessageHandler;
import protein3dprint.vec.Vec;

public class Polygons {

    PrintStream log_stream;

    MessageHandler msg_handler;

    double coalesce_cutoff;

    boolean output_ready = false;

    float vert_coords[][];

    int tri_vertices[][];

    int tri_colors[][];

    int poly_nverts = -1;

    Vec poly_coords[] = new Vec[2];

    int poly_pris[] = new int[2];

    int poly_secs[] = new int[2];

    int poly_color[] = new int[3];

    private class Triangle {

        int indices[][] = new int[3][];

        int color[];

        Triangle(int indices1[], int indices2[], int indices3[], int col[]) {
            indices[0] = indices1;
            indices[1] = indices2;
            indices[2] = indices3;
            color = col;
        }
    }

    Vector triangles = new Vector(1024, 1024);

    public Polygons(double cutoff, PrintStream log, MessageHandler handler) {
        log_stream = log;
        msg_handler = handler;
        coalesce_cutoff = cutoff;
    }

    public float[][] vertex_coords() {
        assert_output_ready();
        return vert_coords;
    }

    public int[][] triangle_vertices() {
        assert_output_ready();
        return tri_vertices;
    }

    public int[][] triangle_colors() {
        assert_output_ready();
        return tri_colors;
    }

    public void triangle(Vec coord1, int pri1, int sec1, Vec coord2, int pri2, int sec2, Vec coord3, int pri3, int sec3, int color[]) {
        triangles.add(new Triangle(coordIndex(coord1, pri1, sec1), coordIndex(coord2, pri2, sec2), coordIndex(coord3, pri3, sec3), color));
    }

    public void polyBegin(int color[]) {
        if (poly_nverts == -1) {
            poly_nverts = 0;
            poly_color = color;
        } else {
            msg_handler.display("polyBegin(): polygon already started");
        }
    }

    public void polyVertex(Vec coord, int pri, int sec) {
        if (poly_nverts == -1) {
            msg_handler.display("polyVertex(): must call polyBegin first");
        } else {
            if (poly_nverts < 2) {
                poly_coords[poly_nverts] = coord;
                poly_pris[poly_nverts] = pri;
                poly_secs[poly_nverts] = sec;
                poly_nverts++;
            } else {
                triangle(poly_coords[0], poly_pris[0], poly_secs[0], poly_coords[1], poly_pris[1], poly_secs[1], coord, pri, sec, poly_color);
                poly_coords[1] = coord;
                poly_pris[1] = pri;
                poly_secs[1] = sec;
                poly_nverts++;
            }
        }
    }

    public void polyEnd() {
        if (poly_nverts == -1) {
            msg_handler.display("polyEnd(): polygon never started");
        } else {
            poly_nverts = -1;
        }
    }

    public void test() {
        int blue[] = { 0, 0, 255 };
        int green[] = { 0, 255, 0 };
        int red[] = { 255, 0, 0 };
        int magenta[] = { 255, 0, 255 };
        int white[] = { 255, 255, 255 };
        triangle(new Vec(0.0, 0.0, 0.0), 0, -1, new Vec(1.0, 0.0, 1.0), 0, 0, new Vec(1.0, 1.0, 0.0), 0, 1, blue);
        triangle(new Vec(0.0, 0.0, 0.0), 0, -1, new Vec(1.0, 1.0, 0.0), 0, 1, new Vec(1.0, 0.0, -1.0), 1, 0, green);
        triangle(new Vec(0.0, 0.0, 0.0), 0, -1, new Vec(1.0, 0.0, -1.0), 1, 0, new Vec(1.0, -1.0, 0.0), 1, 1, red);
        triangle(new Vec(0.0, 0.0, 0.0), 0, -1, new Vec(1.0, -1.0, 0.0), 1, 1, new Vec(1.0, 0.0, 1.0), 0, 0, magenta);
        polyBegin(white);
        polyVertex(new Vec(1.0, 0.0, 1.0), 0, 0);
        polyVertex(new Vec(1.0, 1.0, 0.0), 0, 1);
        polyVertex(new Vec(1.0, 0.0, -1.0), 1, 0);
        polyVertex(new Vec(1.0, -1.0, 0.0), 1, 1);
        polyEnd();
    }

    int n_coords = 0;

    private class Coords {

        int pri;

        int sec;

        Vector coords = new Vector(16, 16);

        int indices[];
    }

    Vector vertices = new Vector(1024, 1024);

    void assert_output_ready() {
        int coord = 0;
        if (output_ready) return;
        msg_handler.display("Starting Polygons assert_output_ready", 0);
        vert_coords = new float[n_coords][];
        for (int p = 0; p < vertices.size(); p++) {
            Vector sec_verts = (Vector) vertices.get(p);
            if (sec_verts != null) {
                for (int s = 0; s < sec_verts.size(); s++) {
                    Coords coords = (Coords) sec_verts.get(s);
                    coords.indices = new int[coords.coords.size()];
                    for (int c = 0; c < coords.coords.size(); c++) {
                        vert_coords[coord] = ((Vec) coords.coords.get(c)).toFloats();
                        coords.indices[c] = coord;
                        coord++;
                    }
                }
            }
        }
        tri_vertices = new int[triangles.size()][3];
        tri_colors = new int[triangles.size()][];
        Triangle triangle;
        for (int t = 0; t < triangles.size(); t++) {
            triangle = (Triangle) triangles.get(t);
            for (int v = 0; v < 3; v++) {
                int p_s_i[] = triangle.indices[v];
                Vector sec_verts = (Vector) vertices.get(p_s_i[0]);
                Coords coords = null;
                int c;
                for (c = 0; c < sec_verts.size(); c++) {
                    coords = (Coords) sec_verts.get(c);
                    if ((coords.pri == p_s_i[0]) && (coords.sec == p_s_i[1])) break;
                }
                if (c == sec_verts.size()) {
                    msg_handler.display("vertex indexing error");
                    tri_vertices[t][v] = 0;
                } else {
                    tri_vertices[t][v] = coords.indices[p_s_i[2]];
                }
            }
            tri_colors[t] = triangle.color;
        }
        output_ready = true;
        msg_handler.display("Finished Polygons assert_output_ready", 0);
        log_stream.println("Polygons: " + tri_vertices.length + " triangles, " + n_coords + " vertices");
    }

    int[] coordIndex(Vec coord, int pri, int sec) {
        double coalesce_cutoff_sq = coalesce_cutoff * coalesce_cutoff;
        int result[] = new int[3];
        if ((sec != -1) && (sec < pri)) {
            return coordIndex(coord, sec, pri);
        } else {
            if (pri >= vertices.size()) {
                vertices.ensureCapacity(pri + 1);
                vertices.setSize(pri + 1);
            }
            Vector sec_verts = (Vector) vertices.get(pri);
            if (sec_verts == null) {
                sec_verts = new Vector(4, 4);
                vertices.set(pri, sec_verts);
            }
            Coords coords = null;
            int v;
            for (v = 0; v < sec_verts.size(); v++) {
                coords = (Coords) sec_verts.get(v);
                if ((coords.pri == pri) && (coords.sec == sec)) break;
            }
            if (v == sec_verts.size()) {
                coords = new Coords();
                coords.pri = pri;
                coords.sec = sec;
                sec_verts.add(coords);
            }
            int index;
            for (index = 0; index < coords.coords.size(); index++) {
                if (Vec.sub((Vec) coords.coords.get(index), coord).lengthsq() < coalesce_cutoff_sq) break;
            }
            if (index == coords.coords.size()) {
                index = coords.coords.size();
                coords.coords.add(coord);
                n_coords++;
            }
            result[0] = pri;
            result[1] = sec;
            result[2] = index;
        }
        return result;
    }
}
