package org.kalypso.nofdpidss.flow.network.utils.storage;

import java.awt.Graphics;
import java.awt.Point;
import org.eclipse.core.runtime.CoreException;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.jts.SnapUtilities.SNAP_TYPE;
import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.IModelMember;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.flow.network.base.sperrzone.SperrzonenUtils;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.map.utilities.MapUtilities;
import org.kalypsodeegree.model.geometry.GM_Exception;
import org.kalypsodeegree.model.geometry.GM_Point;

/**
 * @author Dirk Kuch
 */
public class FNSnapPainterAbstractStorageNode {

    public static final int RADIUS = 10;

    private IBranch m_lastSnappedBranch;

    private final IModelMember m_model;

    private final IBranch[] m_branches;

    public FNSnapPainterAbstractStorageNode(final IModelMember model) {
        m_model = model;
        m_branches = model.getBranchMembers();
    }

    public IBranch getLastSnappedBranch() {
        return m_lastSnappedBranch;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.application.flow.network.ISnapPainter#isSnapPoaint(org.kalypso.ogc.gml.map.MapPanel,
   *      java.awt.Point)
   */
    public GM_Point getSnapPoint(final IMapPanel panel, final GM_Point point) throws CoreException {
        try {
            if (point == null) return null;
            for (final IBranch branch : m_branches) {
                final GM_Point pSnap = MapUtilities.snap(panel, branch.getCurve(), point, FNSnapPainterAbstractStorageNode.RADIUS, SNAP_TYPE.SNAP_TO_LINE);
                if (pSnap == null) continue;
                if (!SperrzonenUtils.pointIsOnSperrzone(branch, pSnap)) {
                    m_lastSnappedBranch = branch;
                    return pSnap;
                }
            }
        } catch (final GM_Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
        return null;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.application.flow.network.ISnapPainter#paint(java.awt.Graphics,
   *      org.kalypsodeegree.graphics.transformation.GeoTransform, java.awt.Point)
   */
    public Point paint(final Graphics g, final IMapPanel panel, final Point currentPoint) {
        if (currentPoint == null) return null;
        try {
            final GM_Point gmPoint = MapUtilities.transform(panel, currentPoint);
            final GM_Point snapPoint = getSnapPoint(panel, gmPoint);
            if (snapPoint != null) {
                final Point point = MapUtilities.retransform(panel, snapPoint);
                g.drawRect((int) point.getX() - 10, (int) point.getY() - 10, 20, 20);
                return point;
            }
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
        return null;
    }
}
