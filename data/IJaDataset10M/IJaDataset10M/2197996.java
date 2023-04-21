package org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.jts.JTSUtilities;
import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.model.Branch;
import org.kalypso.model.wspm.sobek.core.sperrzone.ISperrzone;
import org.kalypso.model.wspm.sobek.core.sperrzone.ISperrzonenDistances;
import org.kalypso.model.wspm.sobek.core.sperrzone.Sperrzone;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IHydraulModel;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IRoughnessClass;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.implementation.RoughnessClassHandler;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IDoppelTrapezProfilFeatureMember;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureMeandering;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolHydraulicModel;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.base.measures.MCUtils.MEASURE;
import org.kalypso.nofdpidss.core.i18n.Messages;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.geometry.GM_Curve;
import org.kalypsodeegree.model.geometry.GM_Exception;
import org.kalypsodeegree.model.geometry.GM_Point;
import org.kalypsodeegree.model.geometry.GM_Surface;
import org.kalypsodeegree_impl.model.feature.FeatureHelper;
import org.kalypsodeegree_impl.model.geometry.JTSAdapter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * @author Dirk Kuch
 */
public class MeasureMeanderingHandler extends AbstractMeasureHandler implements IMeasureMeandering {

    public MeasureMeanderingHandler(final IProjectModel model, final Feature feature) {
        super(model, feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasurePolder#getBranch()
   */
    public IBranch getBranch() throws CoreException {
        final Object property = getProperty(IMeasureMeandering.QN_LINKS_TO_BRANCH);
        final PoolHydraulicModel pool = (PoolHydraulicModel) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eHydraulicModel);
        final Feature feature = FeatureHelper.resolveLinkedFeature(pool.getWorkspace(), property);
        if (feature == null) return null;
        return new Branch(pool.getModel().getSobekModelMember(), feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasure#getType()
   */
    public MEASURE getType() {
        return MEASURE.eHccMeandering;
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getLowerSnapPoint()
   */
    public GM_Point getLowerSnapPoint() {
        return (GM_Point) getProperty(QN_LOWER_SNAP_POINT);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getUpperSnapPoint()
   */
    public GM_Point getUpperSnapPoint() {
        return (GM_Point) getProperty(QN_UPPER_SNAP_POINT);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getGeometry()
   */
    public GM_Curve getGeometry() {
        return (GM_Curve) getProperty(QN_GEOMETRY);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasure#relaysOnGeometry(com.vividsolutions.jts.geom.Geometry)
   */
    public boolean relaysOnGeometry(final Geometry geometry) throws GM_Exception {
        if (geometry instanceof LineString) {
            final LineString lineString = (LineString) geometry;
            final GM_Point p1 = getUpperSnapPoint();
            final GM_Point p2 = getLowerSnapPoint();
            final GM_Curve curve = getGeometry();
            final Geometry jtsCurve = JTSAdapter.export(curve);
            final Geometry jtsP1 = JTSAdapter.export(p1);
            final Geometry jtsP2 = JTSAdapter.export(p2);
            if (lineString.intersects(jtsP1.buffer(IMeasure.GEOM_BUFFER))) return true; else if (lineString.intersects(jtsP2.buffer(IMeasure.GEOM_BUFFER))) return true; else if (lineString.intersects(jtsCurve)) return true;
            return false;
        }
        throw new NotImplementedException();
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getBottomLevelDownStream()
   */
    public Double getBottomLevelDownStream() {
        return (Double) getProperty(QN_BOTTOM_LEVEL_DOWN_STREAM);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getBottomLevelUpStream()
   */
    public Double getBottomLevelUpStream() {
        return (Double) getProperty(QN_BOTTOM_LEVEL_UP_STREAM);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IDoppelTrapezProfilMeasure#getRoughnessInChannel()
   */
    public IRoughnessClass getRoughnessInChannel(final IHydraulModel model) {
        final Object property = getProperty(QN_ROUGHNESS_CHANNEL);
        final Feature feature = FeatureHelper.resolveLinkedFeature(model.getWorkspace(), property);
        if (feature == null) return null;
        return new RoughnessClassHandler(model, feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IDoppelTrapezProfilMeasure#getRoughnessOnLeftFloodPlain()
   */
    public IRoughnessClass getRoughnessOnLeftFloodPlain(final IHydraulModel model) {
        final Object property = getProperty(QN_ROUGHNESS_LEFT_FLOODPLAIN);
        final Feature feature = FeatureHelper.resolveLinkedFeature(model.getWorkspace(), property);
        if (feature == null) return null;
        return new RoughnessClassHandler(model, feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IDoppelTrapezProfilMeasure#getRoughnessOnRightFloodPlain()
   */
    public IRoughnessClass getRoughnessOnRightFloodPlain(final IHydraulModel model) {
        final Object property = getProperty(QN_ROUGHNESS_RIGHT_FLOODPLAIN);
        final Feature feature = FeatureHelper.resolveLinkedFeature(model.getWorkspace(), property);
        if (feature == null) return null;
        return new RoughnessClassHandler(model, feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureRetardingBasin#isAffectingDEM()
   */
    public Boolean isAffectingDEM() {
        return (Boolean) getProperty(QN_AFFECTING_DEM);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IDoppelTrapezProfilMeasure#getDoppelTrapezProfileMember()
   */
    public IDoppelTrapezProfilFeatureMember getDoppelTrapezProfileMember() {
        final Object property = getProperty(QN_DOPPEL_TRAPEZ_PROFILE_MEMBER);
        if (property instanceof Feature) return new DoppelTrapezProfileMemberHandler(getModel(), (Feature) property);
        return null;
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDiversion#getLowerChannel()
   */
    public GM_Surface<?> getLowerChannel() {
        return (GM_Surface<?>) getProperty(QN_CALC_LOWER_CHANNEL);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDiversion#getUpperChannel()
   */
    public GM_Surface<?> getUpperChannel() {
        return (GM_Surface<?>) getProperty(QN_CALC_UPPER_CHANNEL);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasure#getSperrzone()
   */
    public ISperrzone getSperrzone() {
        final Sperrzone sperrzone = new Sperrzone(this);
        try {
            final IBranch branch = getBranch();
            final GM_Point lowerSnapPoint = getLowerSnapPoint();
            final GM_Point upperSnapPoint = getUpperSnapPoint();
            final GM_Curve curve = branch.getCurve();
            final Point jtsLowerSnapPoint = (Point) JTSAdapter.export(lowerSnapPoint);
            final Point jtsUppernapPoint = (Point) JTSAdapter.export(upperSnapPoint);
            final LineString jtsCuve = (LineString) JTSAdapter.export(curve);
            final LineString segment = JTSUtilities.createLineSegment(jtsCuve, jtsUppernapPoint, jtsLowerSnapPoint);
            final Geometry bufferedSegment = segment.buffer(ISperrzonenDistances.MEASURE_MEANDERING);
            sperrzone.addSperrzone(branch, bufferedSegment);
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
        return sperrzone;
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getCalculatedMeanderCourseLength()
   */
    public Double getCalculatedMeanderCourseLength() {
        return (Double) getProperty(QN_CALC_COURSE_LENGTH);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureMeandering#getCalculatedVolume()
   */
    public Double getCalculatedVolume() {
        return (Double) getProperty(QN_CALC_VOLUME);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure#isValid()
   */
    public IStatus isValid() {
        final List<IStatus> stati = new ArrayList<IStatus>();
        final GM_Curve geometry = getGeometry();
        if (geometry == null) stati.add(StatusUtilities.createErrorStatus(String.format(Messages.MeasureMeanderingHandler_0, getName()))); else {
            final String crs = geometry.getCoordinateSystem();
            if (crs == null) stati.add(StatusUtilities.createWarningStatus(String.format(Messages.MeasureMeanderingHandler_1, getName())));
        }
        if (stati.size() == 0) return StatusUtilities.createOkStatus(String.format(Messages.MeasureMeanderingHandler_2, getName()));
        return ExceptionHelper.getMultiState(this.getClass(), String.format(Messages.MeasureMeanderingHandler_3, getName()), stati.toArray(new IStatus[] {}));
    }
}
