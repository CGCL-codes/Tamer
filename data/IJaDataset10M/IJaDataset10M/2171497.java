package edu.ucsd.ncmir.jinx.segmentation.surfacers;

import edu.ucsd.ncmir.spl.graphics.ThreadedObject;
import edu.ucsd.ncmir.jinx.core.JxPoint;
import edu.ucsd.ncmir.jinx.core.JxView;
import edu.ucsd.ncmir.jinx.events.JxStatusEvent;
import edu.ucsd.ncmir.jinx.gui.workspace.JxCapper;
import edu.ucsd.ncmir.jinx.objects.JxObject;
import edu.ucsd.ncmir.jinx.objects.JxObjectTreeNode;
import edu.ucsd.ncmir.jinx.objects.JxPlaneTraceList;
import edu.ucsd.ncmir.jinx.objects.trace.JxOrientation;
import edu.ucsd.ncmir.jinx.objects.trace.JxTrace;
import edu.ucsd.ncmir.jinx.segmentation.JxSegmentation;
import edu.ucsd.ncmir.spl.smoothers.SurfaceSmoother;
import edu.ucsd.ncmir.spl.graphics.meshables.TriangleMesh;
import edu.ucsd.ncmir.spl.graphics.Triplet;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author spl
 */
public class JxCreateFilamentObjectsThread extends JxAbstractCreateSurfacesThread {

    public JxCreateFilamentObjectsThread(JxSegmentation segmentation, SurfaceSmoother interpolator, JxCapper capper, JxObjectTreeNode node) {
        super("Threads", segmentation, interpolator, capper, node);
    }

    protected TriangleMesh processClosedObject(JxView view, JxObject object, JxCapper capper, SurfaceSmoother interpolator) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected TriangleMesh processOpenObject(JxView view, JxObject object, SurfaceSmoother interpolator) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected TriangleMesh processPointThread(JxView view, JxObject object, SurfaceSmoother interpolator) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected TriangleMesh processThread(JxView view, JxObject object, SurfaceSmoother interpolator) throws Exception {
        new JxStatusEvent().send("Preprocessing " + object.getName());
        ArrayList<LinkedList<JxPoint>> thread_list = this.createThreadList(object, view);
        TriangleMesh triangle_mesh = new TriangleMesh();
        double radius = object.getDiameter() / 2;
        new JxStatusEvent().send("Tiling " + object.getName());
        JxOrientation orientation = super.getOrientation(view);
        for (LinkedList<JxPoint> list : thread_list) {
            JxPoint[] points = list.toArray(new JxPoint[0]);
            if (points.length > 0) {
                Triplet[] triplets = new Triplet[points.length];
                for (int i = 0; i < points.length; i++) triplets[i] = orientation.transform(points[i]);
                ThreadedObject threaded_object = new ThreadedObject(triplets, radius);
                triangle_mesh.addTriangleMesh(threaded_object);
            }
        }
        return triangle_mesh;
    }

    private ArrayList<LinkedList<JxPoint>> createThreadList(JxObject object, JxView view) {
        JxPlaneTraceList[] trace_lists = object.getPlaneTraceList(view);
        ArrayList<LinkedList<JxPoint>> thread_list = new ArrayList<LinkedList<JxPoint>>();
        int maxpoints = object.getMaxPoints();
        for (JxPlaneTraceList trace_list : trace_lists) for (JxTrace trace : trace_list) thread_list.add(this.createLinkedList(trace, maxpoints));
        return thread_list;
    }

    private LinkedList<JxPoint> createLinkedList(JxTrace thread, int maxpoints) {
        LinkedList<JxPoint> points = new LinkedList<JxPoint>();
        if (thread.size() > maxpoints) for (JxPoint p : thread.getSplinedTraceArray(maxpoints)) points.add(p); else for (JxPoint p : thread) points.add(p);
        return points;
    }
}
