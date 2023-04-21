package org.pqt.mr2rib.ribtranslator;

import java.util.Iterator;
import java.util.Vector;
import org.pqt.mr2rib.RendererOptions;
import org.pqt.mr2rib.mrutil.SubdivisionSurface;

/**
 *
 * @author Peter Quint  */
public class RIBSubdivisionSurface extends RIBPrimitive {

    public static final boolean VERBOSE = false;

    /**The number of vertices in each loop*/
    public int nvertices[];

    /** The vertices*/
    public int vertices[];

    /** Creates a new instance of RIBSubdivisionSurface */
    public void writePartPrim(PrettyPrint out) {
        out.print("SubdivisionMesh \"catmull-clark\" ");
        out.printRIB(nvertices);
        out.printRIB(vertices);
        out.print(RendererOptions.emptySubDivParams);
    }

    private class Face extends Object {

        protected Vector vertices = new Vector(4);

        public boolean isDeleted = false;

        public Face(int[] vertids, int start, int length) {
            for (int i = start; i < start + length; i++) {
                vertices.add(new Integer(vertids[i]));
            }
        }

        public int numOverlapVerts(Face face) {
            int res = 0;
            Iterator i = face.vertices.iterator();
            Object o;
            while (i.hasNext()) {
                o = i.next();
                Iterator j = vertices.iterator();
                while (j.hasNext()) if (j.next().equals(o)) res++;
            }
            return res;
        }

        public void checkDupVerts() {
            boolean duplicated;
            int i = 0;
            while (i > vertices.size()) {
                duplicated = false;
                for (int j = 0; ((j < vertices.size()) && (!duplicated)); j++) {
                    if ((j != i) && (vertices.get(i).equals(vertices.get(j)))) duplicated = true;
                }
                if (duplicated) vertices.remove(i); else i++;
            }
        }

        public void addToVectors(Vector nverts, Vector vertids) {
            if ((vertices.size() > 2) && !isDeleted) {
                nverts.add(new Integer(vertices.size()));
                vertids.addAll(vertices);
            }
        }
    }

    /** cleans the polygon = removing any faces which contain errors */
    public void clean() {
        int currentIndex = 0;
        int nv;
        Vector v = new Vector();
        Object o;
        for (int i = 0; i < nvertices.length; i++) {
            nv = nvertices[i];
            v.add(new Face(vertices, currentIndex, nv));
            currentIndex += nv;
        }
        Face f, g;
        boolean duplicated, commonedge;
        int noCE = 0, dup = 0;
        for (int j = 0; j < v.size(); j++) {
            f = (Face) v.get(j);
            f.checkDupVerts();
            duplicated = false;
            commonedge = false;
            for (int k = 0; ((k < v.size()) && (!duplicated)); k++) {
                if (j != k) {
                    g = (Face) v.get(k);
                    if (f.numOverlapVerts(g) > 1) commonedge = true;
                }
            }
            if (duplicated) {
                f.isDeleted = true;
                dup++;
            }
            if (!commonedge) {
                f.isDeleted = true;
                noCE++;
            }
        }
        Vector vnverts = new Vector(nvertices.length);
        Vector vvertid = new Vector(vertices.length);
        Iterator i = v.iterator();
        while (i.hasNext()) ((Face) i.next()).addToVectors(vnverts, vvertid);
        nvertices = Util.extractInt(vnverts);
        vertices = Util.extractInt(vvertid);
        if (VERBOSE) {
            System.out.println("Deleted " + dup + " faces which were duplicates ");
            System.out.println("Deleted " + noCE + " faces that had no common edge ");
        }
    }
}
