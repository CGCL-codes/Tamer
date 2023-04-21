package org.kalypso.nofdpidss.measure.construction.create.builders.geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.jts.SnapUtilities.SNAP_TYPE;
import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.INode;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekModelMember;
import org.kalypso.model.wspm.sobek.core.pub.ISnapPainter.SnappedBranch;
import org.kalypso.model.wspm.sobek.core.sperrzone.ISperrzone;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.flow.network.base.sperrzone.ISperrzoneFilter;
import org.kalypso.nofdpidss.flow.network.base.sperrzone.SperrzonenUtils;
import org.kalypso.nofdpidss.measure.construction.create.builders.IMeasureBuilder;
import org.kalypso.nofdpidss.measure.construction.i18n.Messages;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.map.utilities.MapUtilities;
import org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilderExtensionProvider;
import org.kalypso.transformation.GeoTransformer;
import org.kalypsodeegree.graphics.transformation.GeoTransform;
import org.kalypsodeegree.model.geometry.GM_Exception;
import org.kalypsodeegree.model.geometry.GM_Object;
import org.kalypsodeegree.model.geometry.GM_Point;
import org.kalypsodeegree.model.geometry.GM_Position;
import org.kalypsodeegree_impl.model.geometry.GeometryFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Dirk Kuch
 */
public class SnapBranchLineGeometryBuilder extends MyAbstractGeometryBuilder {

    public static final int RADIUS = 15;

    private static final Color m_colorRed = new Color(255, 0, 0);

    private final Map<IBranch, ISperrzone[]> m_sperrzones = new HashMap<IBranch, ISperrzone[]>();

    /**
   * points of line geometry, first and last point must be a SnappedBranch point
   */
    private final List<Object> m_points = new ArrayList<Object>();

    final ISperrzoneFilter FILTER = new ISperrzoneFilter() {

        public boolean lookForMeasure(IMeasure measure) {
            if (getBuilder().getMeasure().equals(measure)) return false;
            return true;
        }

        public boolean lookForNode(INode node) {
            return true;
        }
    };

    /**
   * @param targetCrs
   *          The target coordinate system.
   */
    public SnapBranchLineGeometryBuilder(final ISobekModelMember model, final IMeasureBuilder builder, final IMapPanel panel, final String targetCrs, final IGeometryBuilderExtensionProvider extender, final Object gmoLineString) {
        super(model, builder, panel, targetCrs, extender);
        if (gmoLineString != null) throw new NotImplementedException();
        hashSperrzones();
    }

    private void hashSperrzones() {
        final List<ISperrzone> sperrzones = new ArrayList<ISperrzone>();
        final IMeasure[] measures = getBuilder().getMeasures();
        for (final IMeasure measure : measures) {
            if (FILTER.lookForMeasure(measure)) {
                sperrzones.add(measure.getSperrzone());
            }
        }
        final INode[] nodes = getNodes();
        for (final INode node : nodes) {
            final ISperrzone sperrzone = node.getSperrzone();
            sperrzones.add(sperrzone);
        }
        final IBranch[] branches = getBranches();
        for (final IBranch branch : branches) {
            final List<ISperrzone> mySperrzones = new ArrayList<ISperrzone>();
            for (final ISperrzone sperrzone : sperrzones) {
                final Geometry[] geometries = sperrzone.getGeometries(branch);
                if (geometries == null || geometries.length == 0) continue;
                mySperrzones.add(sperrzone);
            }
            m_sperrzones.put(branch, mySperrzones.toArray(new ISperrzone[] {}));
        }
    }

    /**
   * @see org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilder#addPoint(org.kalypsodeegree.model.geometry.GM_Point)
   */
    public GM_Object addPoint(final GM_Point p) throws Exception {
        final SnappedBranch[] snapPoint = getSnapPoint(p);
        if (m_points.size() == 0) {
            if (snapPoint.length == 0) return null; else if (snapPoint.length == 1) {
                m_points.add(snapPoint[0]);
                return finish();
            } else throw ExceptionHelper.getCoreException(IStatus.INFO, this.getClass(), Messages.SnapBranchLineGeometryBuilder_2);
        } else if (snapPoint.length == 1) m_points.add(snapPoint[0]); else m_points.add(p);
        return null;
    }

    private void drawHandles(final Graphics g, final int[] x, final int[] y) {
        final int sizeOuter = 6;
        for (int i = 0; i < y.length; i++) g.drawRect(x[i] - sizeOuter / 2, y[i] - sizeOuter / 2, sizeOuter, sizeOuter);
    }

    /**
   * @see org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilder#finish()
   */
    public GM_Object finish() throws Exception {
        if (m_points.size() > 1) {
            setCursor(CURSOR_TYPE.eDefault);
            final GeoTransformer transformer = new GeoTransformer(getTargetCRS());
            final List<GM_Position> positions = new ArrayList<GM_Position>();
            for (final Object obj : m_points) if (obj instanceof GM_Point) {
                final GM_Point point = (GM_Point) obj;
                final GM_Point pTransformed = (GM_Point) transformer.transform(point);
                positions.add(pTransformed.getPosition());
            } else if (obj instanceof SnappedBranch) {
                final SnappedBranch branch = (SnappedBranch) obj;
                final GM_Point pTransformed = (GM_Point) transformer.transform(branch.m_snapPoint);
                positions.add(pTransformed.getPosition());
            } else throw new IllegalStateException(Messages.SnapBranchLineGeometryBuilder_0);
            return GeometryFactory.createGM_Curve(positions.toArray(new GM_Position[] {}), getTargetCRS());
        }
        return null;
    }

    private int[][] getPointArrays(final GeoTransform projection, final Point currentPoint) {
        final List<Integer> xArray = new ArrayList<Integer>();
        final List<Integer> yArray = new ArrayList<Integer>();
        for (final Object obj : m_points) {
            GM_Point point = null;
            if (obj instanceof GM_Point) point = (GM_Point) obj; else if (obj instanceof SnappedBranch) point = ((SnappedBranch) obj).m_snapPoint; else throw new IllegalStateException(Messages.SnapBranchLineGeometryBuilder_1);
            final int x = (int) projection.getDestX(point.getX());
            final int y = (int) projection.getDestY(point.getY());
            xArray.add(new Integer(x));
            yArray.add(new Integer(y));
        }
        if (currentPoint != null) {
            xArray.add(currentPoint.x);
            yArray.add(currentPoint.y);
        }
        final int[] xs = ArrayUtils.toPrimitive(xArray.toArray(new Integer[xArray.size()]));
        final int[] ys = ArrayUtils.toPrimitive(yArray.toArray(new Integer[yArray.size()]));
        return new int[][] { xs, ys };
    }

    public Object[] getPoints() {
        return m_points.toArray();
    }

    /**
   * @throws CoreException
   * @see org.kalypso.nofdpidss.ui.application.flow.network.ISnapPainter#isSnapPoaint(org.kalypso.ogc.gml.map.MapPanel,
   *      java.awt.Point)
   */
    public SnappedBranch[] getSnapPoint(final GM_Point point) throws CoreException {
        final List<SnappedBranch> myList = new ArrayList<SnappedBranch>();
        try {
            GM_Point snapPoint = null;
            for (final IBranch branch : getBranches()) {
                snapPoint = MapUtilities.snap(getPanel(), branch.getCurve(), point, RADIUS, SNAP_TYPE.SNAP_AUTO);
                if (snapPoint != null) {
                    final ISperrzone[] sperrzones = m_sperrzones.get(branch);
                    if (SperrzonenUtils.pointIsOnSperrzone(branch, sperrzones, snapPoint)) continue;
                    myList.add(new SnappedBranch(branch, snapPoint));
                }
            }
        } catch (final GM_Exception e) {
            e.printStackTrace();
        }
        return myList.toArray(new SnappedBranch[] {});
    }

    /**
   * @see org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilder#paint(java.awt.Graphics,
   *      org.kalypsodeegree.graphics.transformation.GeoTransform, java.awt.Point)
   */
    public void paint(final Graphics g, final GeoTransform projection, final Point p) {
        try {
            paintHelp(g);
            final GM_Point point = MapUtilities.transform(getPanel(), p);
            final SnappedBranch[] snapPoint = getSnapPoint(point);
            if (m_points.isEmpty()) {
                if (snapPoint.length == 0) {
                    setCursor(CURSOR_TYPE.eNarf);
                    return;
                }
                setCursor(CURSOR_TYPE.eCrosshair);
                final Point snap = MapUtilities.retransform(getPanel(), snapPoint[0].m_snapPoint);
                g.setColor(SnapBranchLineGeometryBuilder.m_colorRed);
                g.drawRect((int) snap.getX() - 10, (int) snap.getY() - 10, 20, 20);
            } else {
                final int[][] points = getPointArrays(getPanel().getProjection(), p);
                final int[] arrayX = points[0];
                final int[] arrayY = points[1];
                g.setColor(SnapBranchLineGeometryBuilder.m_colorRed);
                g.drawPolyline(arrayX, arrayY, arrayX.length);
                drawHandles(g, arrayX, arrayY);
                if (snapPoint.length > 0) {
                    setCursor(CURSOR_TYPE.eCrosshair);
                    final Point snap = MapUtilities.retransform(getPanel(), snapPoint[0].m_snapPoint);
                    g.setColor(SnapBranchLineGeometryBuilder.m_colorRed);
                    g.drawRect((int) snap.getX() - 10, (int) snap.getY() - 10, 20, 20);
                }
            }
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.geometry.IMyGeometryBuilder#removeLastPoint()
   */
    public void removeLastPoint() {
        if (m_points.size() > 0) m_points.remove(m_points.size() - 1);
    }

    /**
   * @see org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilder#removePoints()
   */
    @Override
    public void reset() {
        m_points.clear();
        super.reset();
    }
}
