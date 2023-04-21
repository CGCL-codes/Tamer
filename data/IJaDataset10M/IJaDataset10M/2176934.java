package org.opencv.imgproc;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.utils.Converters;

public class Subdiv2D {

    protected final long nativeObj;

    protected Subdiv2D(long addr) {
        nativeObj = addr;
    }

    public static final int PTLOC_ERROR = -2, PTLOC_OUTSIDE_RECT = -1, PTLOC_INSIDE = 0, PTLOC_VERTEX = 1, PTLOC_ON_EDGE = 2, NEXT_AROUND_ORG = 0x00, NEXT_AROUND_DST = 0x22, PREV_AROUND_ORG = 0x11, PREV_AROUND_DST = 0x33, NEXT_AROUND_LEFT = 0x13, NEXT_AROUND_RIGHT = 0x31, PREV_AROUND_LEFT = 0x20, PREV_AROUND_RIGHT = 0x02;

    public Subdiv2D() {
        nativeObj = Subdiv2D_0();
        return;
    }

    public Subdiv2D(Rect rect) {
        nativeObj = Subdiv2D_1(rect.x, rect.y, rect.width, rect.height);
        return;
    }

    public int edgeDst(int edge, Point dstpt) {
        double[] dstpt_out = new double[2];
        int retVal = edgeDst_0(nativeObj, edge, dstpt_out);
        if (dstpt != null) {
            dstpt.x = dstpt_out[0];
            dstpt.y = dstpt_out[1];
        }
        return retVal;
    }

    public int edgeDst(int edge) {
        int retVal = edgeDst_1(nativeObj, edge);
        return retVal;
    }

    public int edgeOrg(int edge, Point orgpt) {
        double[] orgpt_out = new double[2];
        int retVal = edgeOrg_0(nativeObj, edge, orgpt_out);
        if (orgpt != null) {
            orgpt.x = orgpt_out[0];
            orgpt.y = orgpt_out[1];
        }
        return retVal;
    }

    public int edgeOrg(int edge) {
        int retVal = edgeOrg_1(nativeObj, edge);
        return retVal;
    }

    public int findNearest(Point pt, Point nearestPt) {
        double[] nearestPt_out = new double[2];
        int retVal = findNearest_0(nativeObj, pt.x, pt.y, nearestPt_out);
        if (nearestPt != null) {
            nearestPt.x = nearestPt_out[0];
            nearestPt.y = nearestPt_out[1];
        }
        return retVal;
    }

    public int findNearest(Point pt) {
        int retVal = findNearest_1(nativeObj, pt.x, pt.y);
        return retVal;
    }

    public int getEdge(int edge, int nextEdgeType) {
        int retVal = getEdge_0(nativeObj, edge, nextEdgeType);
        return retVal;
    }

    public void getEdgeList(Mat edgeList) {
        Mat edgeList_mat = new Mat();
        getEdgeList_0(nativeObj, edgeList_mat.nativeObj);
        return;
    }

    public void getTriangleList(Mat triangleList) {
        Mat triangleList_mat = new Mat();
        getTriangleList_0(nativeObj, triangleList_mat.nativeObj);
        return;
    }

    public Point getVertex(int vertex, int[] firstEdge) {
        double[] firstEdge_out = new double[1];
        Point retVal = new Point(getVertex_0(nativeObj, vertex, firstEdge_out));
        if (firstEdge != null) firstEdge[0] = (int) firstEdge_out[0];
        return retVal;
    }

    public Point getVertex(int vertex) {
        Point retVal = new Point(getVertex_1(nativeObj, vertex));
        return retVal;
    }

    public void getVoronoiFacetList(List<Integer> idx, List<List<Point>> facetList, List<Point> facetCenters) {
        Mat idx_mat = Converters.vector_int_to_Mat(idx);
        Mat facetList_mat = new Mat();
        Mat facetCenters_mat = new Mat();
        getVoronoiFacetList_0(nativeObj, idx_mat.nativeObj, facetList_mat.nativeObj, facetCenters_mat.nativeObj);
        Converters.Mat_to_vector_vector_Point2f(facetList_mat, facetList);
        Converters.Mat_to_vector_Point2f(facetCenters_mat, facetCenters);
        return;
    }

    public void initDelaunay(Rect rect) {
        initDelaunay_0(nativeObj, rect.x, rect.y, rect.width, rect.height);
        return;
    }

    public int insert(Point pt) {
        int retVal = insert_0(nativeObj, pt.x, pt.y);
        return retVal;
    }

    public void insert(List<Point> ptvec) {
        Mat ptvec_mat = Converters.vector_Point2f_to_Mat(ptvec);
        insert_1(nativeObj, ptvec_mat.nativeObj);
        return;
    }

    public int locate(Point pt, int[] edge, int[] vertex) {
        double[] edge_out = new double[1];
        double[] vertex_out = new double[1];
        int retVal = locate_0(nativeObj, pt.x, pt.y, edge_out, vertex_out);
        if (edge != null) edge[0] = (int) edge_out[0];
        if (vertex != null) vertex[0] = (int) vertex_out[0];
        return retVal;
    }

    public int nextEdge(int edge) {
        int retVal = nextEdge_0(nativeObj, edge);
        return retVal;
    }

    public int rotateEdge(int edge, int rotate) {
        int retVal = rotateEdge_0(nativeObj, edge, rotate);
        return retVal;
    }

    public int symEdge(int edge) {
        int retVal = symEdge_0(nativeObj, edge);
        return retVal;
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
        super.finalize();
    }

    static {
        System.loadLibrary("opencv_java");
    }

    private static native long Subdiv2D_0();

    private static native long Subdiv2D_1(int rect_x, int rect_y, int rect_width, int rect_height);

    private static native int edgeDst_0(long nativeObj, int edge, double[] dstpt_out);

    private static native int edgeDst_1(long nativeObj, int edge);

    private static native int edgeOrg_0(long nativeObj, int edge, double[] orgpt_out);

    private static native int edgeOrg_1(long nativeObj, int edge);

    private static native int findNearest_0(long nativeObj, double pt_x, double pt_y, double[] nearestPt_out);

    private static native int findNearest_1(long nativeObj, double pt_x, double pt_y);

    private static native int getEdge_0(long nativeObj, int edge, int nextEdgeType);

    private static native void getEdgeList_0(long nativeObj, long edgeList_mat_nativeObj);

    private static native void getTriangleList_0(long nativeObj, long triangleList_mat_nativeObj);

    private static native double[] getVertex_0(long nativeObj, int vertex, double[] firstEdge_out);

    private static native double[] getVertex_1(long nativeObj, int vertex);

    private static native void getVoronoiFacetList_0(long nativeObj, long idx_mat_nativeObj, long facetList_mat_nativeObj, long facetCenters_mat_nativeObj);

    private static native void initDelaunay_0(long nativeObj, int rect_x, int rect_y, int rect_width, int rect_height);

    private static native int insert_0(long nativeObj, double pt_x, double pt_y);

    private static native void insert_1(long nativeObj, long ptvec_mat_nativeObj);

    private static native int locate_0(long nativeObj, double pt_x, double pt_y, double[] edge_out, double[] vertex_out);

    private static native int nextEdge_0(long nativeObj, int edge);

    private static native int rotateEdge_0(long nativeObj, int edge, int rotate);

    private static native int symEdge_0(long nativeObj, int edge);

    private static native void delete(long nativeObj);
}
