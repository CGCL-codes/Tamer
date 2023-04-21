package org.kalypso.nofdpidss.measure.construction.create.builders.geometry;

import org.eclipse.core.runtime.CoreException;
import org.kalypso.jts.SnapUtilities.SNAP_TYPE;
import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekModelMember;
import org.kalypso.model.wspm.sobek.core.pub.ISnapPainter.SnappedBranch;
import org.kalypso.model.wspm.sobek.core.sperrzone.ISperrzone;
import org.kalypso.nofdpidss.flow.network.base.sperrzone.SperrzonenUtils;
import org.kalypso.nofdpidss.measure.construction.create.builders.IMeasureBuilder;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.map.utilities.MapUtilities;
import org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilderExtensionProvider;
import org.kalypsodeegree.model.geometry.GM_Exception;
import org.kalypsodeegree.model.geometry.GM_Point;

public class BottomLevelChangelSnapBranchPointGeometryBuilder extends SnapBranchPointGeometryBuilder {

    private final IBranch m_branch;

    public BottomLevelChangelSnapBranchPointGeometryBuilder(final ISobekModelMember model, final IMeasureBuilder builder, final IMapPanel panel, final String targetCrs, final IGeometryBuilderExtensionProvider extender, final IBranch branch) {
        super(model, builder, panel, targetCrs, extender);
        m_branch = branch;
    }

    /**
   * @throws CoreException
   * @see org.kalypso.nofdpidss.ui.application.flow.network.ISnapPainter#isSnapPoaint(org.kalypso.ogc.gml.map.MapPanel,
   *      java.awt.Point)
   */
    @Override
    public SnappedBranch[] getSnapPoint(final GM_Point point) throws CoreException {
        try {
            final GM_Point snapPoint = MapUtilities.snap(getPanel(), m_branch.getCurve(), point, RADIUS, SNAP_TYPE.SNAP_TO_LINE);
            if (snapPoint != null) {
                final ISperrzone[] sperrzones = m_sperrzones.get(m_branch);
                if (SperrzonenUtils.pointIsOnSperrzone(m_branch, sperrzones, snapPoint)) return new SnappedBranch[] {};
                return new SnappedBranch[] { new SnappedBranch(m_branch, snapPoint) };
            }
        } catch (final GM_Exception e) {
            e.printStackTrace();
        }
        return new SnappedBranch[] {};
    }
}
