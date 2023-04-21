package net.nexttext.renderer.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.core.PVector;
import net.nexttext.FastMath;
import net.nexttext.GeometricException;
import net.nexttext.renderer.util.Triangulator.YMonotonePolygon.Triangle;

/**
 * Triangulator.
 */
public class Triangulator extends DoublyConnectedEdgeList<TriangulationVertex, TriangulationEdge> {

    private IntBuffer complete_triangulation;

    private Vector<YMonotonePolygon> monotone_polygons = new Vector<YMonotonePolygon>();

    int polyids = 0;

    public static final PVector UNIT_X = new PVector(1, 0, 0);

    /**
	 * Get X coord of an edge at a given Y coord.
	 * @param edge
	 * @param d y
	 * @return x
	 */
    double getXAtY(TriangulationEdge edge, double d) {
        double dx = edge.getDX();
        double dy = edge.getDY();
        if (dy == 0) {
            if (edge.getOrigin().point.y == d) return edge.getOrigin().point.x;
            return edge.getOrigin().point.x;
        }
        double t = (d - edge.getOrigin().point.y) / dy;
        return edge.getOrigin().point.x + dx * t;
    }

    /**
	 * Triangulate from a fresh start if cleanrun is true.
	 * @param cleanrun
	 * @return index buffer of triangles
	 */
    public IntBuffer triangulate(boolean cleanrun) {
        if (cleanrun) complete_triangulation = null;
        return triangulate();
    }

    /**
	 * Triangulate.
	 * @return index buffer of triangles.
	 */
    public IntBuffer triangulate() {
        if (complete_triangulation == null) {
            checkTriangulation();
            generateMonotonePolygons();
            int tricount = triangulateMonotonePolygons();
            complete_triangulation = createIntBuffer(tricount * 3);
            complete_triangulation.rewind();
            for (YMonotonePolygon poly : monotone_polygons) {
                for (Triangle t : poly.poly_tris) {
                    complete_triangulation.put(t.p1);
                    complete_triangulation.put(t.p2);
                    complete_triangulation.put(t.p3);
                }
            }
        }
        return complete_triangulation;
    }

    /**
     * Create a new IntBuffer of the specified size.
     *
     * @param size
     *            required number of ints to store.
     * @return the new IntBuffer
     */
    public static IntBuffer createIntBuffer(int size) {
        IntBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
        buf.clear();
        return buf;
    }

    /**
	 * This is the sweep-line algorithm outlined in section 3.2 of "Computational Geometry", ISBN: 3-540-65620-0.
	 */
    private void generateMonotonePolygons() {
        class SweepLineStatus extends TreeSet<TriangulationEdge> {

            private static final long serialVersionUID = 1L;

            SweepLineComparer sweep_comparer;

            public SweepLineStatus() {
                super(new SweepLineComparer());
                sweep_comparer = (SweepLineComparer) comparator();
            }

            @Override
            public boolean add(TriangulationEdge edge) {
                boolean result = super.add(edge);
                if (!result) {
                }
                return result;
            }

            void printElements() {
                for (TriangulationEdge e : this) {
                }
            }

            public boolean remove(TriangulationEdge edge) {
                boolean result = super.remove(edge);
                if (!result) {
                }
                return result;
            }

            public TriangulationEdge getLeftOf(TriangulationEdge edge) {
                SortedSet<TriangulationEdge> hset = headSet(edge);
                if (hset.size() == 0) {
                    printElements();
                }
                return hset.last();
            }

            public void setCurrentVertex(TriangulationVertex v) {
                sweep_comparer.currentvertex = v;
            }
        }
        for (TriangulationVertex v : getVertices()) {
            v.initializeType();
        }
        SweepLineStatus sweep_line = new SweepLineStatus();
        PriorityQueue<TriangulationVertex> sweep_queue = new PriorityQueue<TriangulationVertex>(getVertices().size(), new SweepQueueComparator());
        sweep_queue.addAll(getVertices());
        class DiagnalEdge {

            int src, dst;

            public DiagnalEdge(int src, int dst) {
                this.src = src;
                this.dst = dst;
            }

            @Override
            public String toString() {
                return "(" + src + "->" + dst + ")";
            }
        }
        Vector<DiagnalEdge> postponed_diagonals = new Vector<DiagnalEdge>();
        TriangulationVertex v_i;
        while (!sweep_queue.isEmpty()) {
            v_i = sweep_queue.poll();
            sweep_line.setCurrentVertex(v_i);
            switch(v_i.getType()) {
                case START:
                    sweep_line.add(v_i.getOutGoingEdge());
                    v_i.getOutGoingEdge().helper = v_i;
                    break;
                case END:
                    if (v_i.getInGoingEdge().isHelperMergeVertex()) {
                        postponed_diagonals.add(new DiagnalEdge(v_i.getIndex(), v_i.getInGoingEdge().helper.getIndex()));
                    }
                    sweep_line.remove(v_i.getInGoingEdge());
                    break;
                case SPLIT:
                    {
                        TriangulationEdge e_j = sweep_line.getLeftOf(v_i.getOutGoingEdge());
                        {
                            postponed_diagonals.add(new DiagnalEdge(v_i.getIndex(), e_j.helper.getIndex()));
                        }
                        e_j.helper = v_i;
                        sweep_line.add(v_i.getOutGoingEdge());
                        v_i.getOutGoingEdge().helper = v_i;
                    }
                    break;
                case MERGE:
                    {
                        if (v_i.getInGoingEdge().isHelperMergeVertex()) {
                            postponed_diagonals.add(new DiagnalEdge(v_i.getIndex(), v_i.getInGoingEdge().helper.getIndex()));
                        }
                        sweep_line.remove(v_i.getInGoingEdge());
                        TriangulationEdge left_of = sweep_line.getLeftOf(v_i.getInGoingEdge());
                        if (left_of.isHelperMergeVertex()) {
                            postponed_diagonals.add(new DiagnalEdge(v_i.getIndex(), left_of.helper.getIndex()));
                        }
                        left_of.helper = v_i;
                    }
                    break;
                case REGULAR_RIGHT:
                    {
                        TriangulationEdge left_of = sweep_line.getLeftOf(v_i.getOutGoingEdge());
                        if (left_of.isHelperMergeVertex()) {
                            postponed_diagonals.add(new DiagnalEdge(v_i.getIndex(), left_of.helper.getIndex()));
                        }
                        left_of.helper = v_i;
                    }
                    break;
                case REGULAR_LEFT:
                    {
                        if (v_i.getInGoingEdge().isHelperMergeVertex()) {
                            postponed_diagonals.add(new DiagnalEdge(v_i.getIndex(), v_i.getInGoingEdge().helper.getIndex()));
                        }
                        sweep_line.remove(v_i.getInGoingEdge());
                        sweep_line.add(v_i.getOutGoingEdge());
                        v_i.getOutGoingEdge().helper = v_i;
                    }
                    break;
                case UNSET:
                    break;
            }
        }
        for (DiagnalEdge de : postponed_diagonals) {
            addDiagonal(de.src, de.dst);
        }
        checkTriangulation();
        monotone_polygons.clear();
        for (TriangulationEdge e : getEdges()) {
            e.marked = false;
        }
        for (TriangulationEdge e : getEdges()) {
            if (!e.marked && e.isRealEdge()) {
                monotone_polygons.add(new YMonotonePolygon(e));
            }
        }
        checkTriangulation();
    }

    /**
	 * Add a diagonal.
	 * @param src source
	 * @param dst destination
	 */
    void addDiagonal(int src, int dst) {
        TriangulationEdge edge = addEdge(src, dst);
        edge.realedge = true;
        edge.getTwin().realedge = true;
    }

    /**
	 * Check if triangulation succeeded.
	 * @return true if succeeded
	 */
    private boolean checkTriangulation() {
        for (TriangulationVertex v : getVertices()) {
            if (v.getFirstEdge() == null) {
                throw new GeometricException("We have a vertex with no edges: " + v);
            }
            if (!v.checkAllEdges()) return false;
        }
        return true;
    }

    /**
	 * Triangulate monotone polygons.
	 * @return triangle count
	 */
    private int triangulateMonotonePolygons() {
        int tricount = 0;
        for (YMonotonePolygon poly : monotone_polygons) {
            tricount += poly.triangulate();
            checkTriangulation();
        }
        return tricount;
    }

    @Override
    public TriangulationEdge createEdge(TriangulationVertex origin, boolean real) {
        return new TriangulationEdge(origin, real);
    }

    @Override
    public TriangulationVertex createVertex(int index, PVector p) {
        return new TriangulationVertex(index, p);
    }

    /**
	 * This class represents a monoton polygon with respect to the y-coordinate.
	 * 
	 * @author emanuel
	 */
    class YMonotonePolygon {

        class Triangle {

            int p1, p2, p3;

            Triangle(int p1, int p2, int p3, boolean clockwise) {
                this.p1 = clockwise ? p1 : p2;
                this.p2 = clockwise ? p2 : p1;
                this.p3 = p3;
            }
        }

        ArrayList<TriangulationEdge> poly_edges = new ArrayList<TriangulationEdge>();

        ArrayList<Triangle> poly_tris = new ArrayList<Triangle>();

        private int polyid;

        public YMonotonePolygon(TriangulationEdge e) {
            polyid = polyids++;
            TriangulationEdge start_edge = e;
            TriangulationEdge next_edge = start_edge;
            do {
                next_edge.marked = true;
                poly_edges.add(next_edge);
                next_edge = (TriangulationEdge) next_edge.getNext();
                if (!next_edge.isRealEdge()) {
                    throw new GeometricException("We cannot add a non-real edge to a polygon.");
                }
            } while (start_edge != next_edge);
        }

        /**
		 * This is the linear-time algorithm outlined in section 3.2 of "Computational Geometry", ISBN: 3-540-65620-0.
		 * @return triangle index count
		 */
        public int triangulate() {
            int trianglecount = (poly_edges.size() - 2);
            int triangle_index_count = trianglecount * 3;
            if (trianglecount == 1) {
                poly_tris.add(new Triangle(poly_edges.get(0).getOrigin().getIndex(), poly_edges.get(1).getOrigin().getIndex(), poly_edges.get(2).getOrigin().getIndex(), false));
                return triangle_index_count;
            }
            ArrayList<TriangulationVertex> queue = createSortedVertexList();
            Stack<TriangulationVertex> stack = new Stack<TriangulationVertex>();
            stack.push(queue.get(0));
            stack.push(queue.get(1));
            for (int i = 2; i < queue.size() - 1; i++) {
                TriangulationVertex u_j = queue.get(i);
                if (u_j.is_left_chain != stack.peek().is_left_chain) {
                    while (stack.size() > 1) {
                        TriangulationVertex popped = stack.pop();
                        poly_tris.add(new Triangle(u_j.getIndex(), popped.getIndex(), stack.peek().getIndex(), !u_j.is_left_chain));
                    }
                    stack.pop();
                    stack.push(queue.get(i - 1));
                    stack.push(u_j);
                } else {
                    TriangulationVertex lastpopped = stack.pop();
                    while (!stack.isEmpty()) {
                        boolean is_left_of = isLeftOf(u_j.getPoint(), lastpopped.getPoint(), stack.peek().getPoint());
                        if (u_j.is_left_chain == is_left_of) {
                            poly_tris.add(new Triangle(u_j.getIndex(), lastpopped.getIndex(), stack.peek().getIndex(), u_j.is_left_chain));
                            lastpopped = stack.pop();
                        } else {
                            break;
                        }
                    }
                    stack.push(lastpopped);
                    stack.push(u_j);
                }
            }
            TriangulationVertex lastpopped = null;
            if (stack.size() > 1) lastpopped = stack.pop();
            TriangulationVertex last = queue.get(queue.size() - 1);
            while (stack.size() > 0) {
                poly_tris.add(new Triangle(last.getIndex(), lastpopped.getIndex(), stack.peek().getIndex(), lastpopped.is_left_chain));
                lastpopped = stack.pop();
            }
            if (trianglecount != poly_tris.size()) {
                throw new GeometricException("Subdivision of monoton polygon: " + polyid + " did not give as many triangles as planned:(" + trianglecount + " != " + poly_tris.size() + ")");
            }
            return triangle_index_count;
        }

        private ArrayList<TriangulationVertex> createSortedVertexList() {
            TriangulationEdge top = poly_edges.get(0);
            TriangulationEdge bottom = top;
            for (TriangulationEdge edge : poly_edges) {
                TriangulationVertex vert = ((TriangulationVertex) edge.getOrigin());
                if (((TriangulationVertex) top.getOrigin()).yLessThan(vert)) top = edge;
                if (vert.yLessThan(bottom.getOrigin())) bottom = edge;
            }
            ArrayList<TriangulationVertex> arr = new ArrayList<TriangulationVertex>();
            int sanity = poly_edges.size() * 2;
            arr.add((TriangulationVertex) top.getOrigin());
            TriangulationEdge tmp_left = (TriangulationEdge) top.getNext();
            TriangulationEdge tmp_right = (TriangulationEdge) top.getPrev();
            while (tmp_left != bottom || tmp_right != bottom) {
                TriangulationVertex left = (TriangulationVertex) tmp_left.getOrigin();
                TriangulationVertex right = (TriangulationVertex) tmp_right.getOrigin();
                left.is_left_chain = true;
                right.is_left_chain = false;
                if (left.yLessThan(right)) {
                    arr.add(right);
                    tmp_right = (TriangulationEdge) tmp_right.getPrev();
                } else {
                    arr.add(left);
                    tmp_left = (TriangulationEdge) tmp_left.getNext();
                }
                if (sanity-- < 0) throw new RuntimeException("We could not get from top to bottom of the poly:" + this);
            }
            arr.add((TriangulationVertex) bottom.getOrigin());
            if (arr.size() != poly_edges.size()) {
                throw new RuntimeException("The number of vertices does not match the number of edges: " + arr.size() + " != " + poly_edges.size());
            }
            return arr;
        }

        private boolean isLeftOf(PVector A, PVector B, PVector P) {
            return 0 > (B.x - A.x) * (P.y - A.y) - (P.x - A.x) * (B.y - A.y);
        }
    }

    /**
	 * Sort the edges according to their X-coordinate from the y coordinate of the sweepline.
	 * 
	 * @author emanuel
	 */
    class SweepLineComparer implements Comparator<TriangulationEdge> {

        TriangulationVertex currentvertex = null;

        public int compare(TriangulationEdge edge1, TriangulationEdge edge2) {
            if (edge1 == edge2) return 0;
            double x1 = getXAtY(edge1, currentvertex.point.y);
            double x2 = getXAtY(edge2, currentvertex.point.y);
            if (x1 == x2) {
                PlanarVertex x1_v = edge1.getOtherEnd(currentvertex);
                PlanarVertex x2_v = edge2.getOtherEnd(currentvertex);
                PVector x1_v_v = x1_v.getPoint().get();
                x1_v_v.sub(currentvertex.getPoint());
                x1_v_v.normalize();
                PVector x2_v_v = x2_v.getPoint().get();
                x2_v_v.sub(currentvertex.getPoint());
                x2_v_v.normalize();
                x1 = x1_v_v.dot(UNIT_X);
                x2 = x2_v_v.dot(UNIT_X);
                if (Math.abs(x1 - x2) < FastMath.FLT_EPSILON) {
                    x1 = x1_v.getPoint().y;
                    x2 = x2_v.getPoint().y;
                }
            }
            return (x1 < x2) ? -1 : 1;
        }
    }

    /**
	 * Simple y-sorting
	 * 
	 * @author emanuel
	 */
    class SweepQueueComparator implements Comparator<TriangulationVertex> {

        public int compare(TriangulationVertex v0, TriangulationVertex v1) {
            if (v0 == v1) return 0;
            return v0.yLessThan(v1) ? 1 : -1;
        }
    }

    public ArrayList<TriangulationEdge> getEdges() {
        return edges;
    }
}
