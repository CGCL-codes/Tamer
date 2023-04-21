package org.kalypso.nofdpidss.hydraulic.computation.processing.worker.workspace.model.helper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.model.wspm.core.IWspmConstants;
import org.kalypso.model.wspm.core.profil.IProfil;
import org.kalypso.model.wspm.core.profil.util.ProfilObsHelper;
import org.kalypso.model.wspm.core.util.WspmProfileHelper;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IHydraulModel;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IRoughnessClass;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IDoppelTrapezProfilMeasure;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolHydraulicModel;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.hydraulic.computation.i18n.Messages;
import org.kalypso.nofdpidss.hydraulic.computation.utils.profiles.HCProfileWrapper;
import org.kalypso.nofdpidss.profiles.INofdpWspmConstants;
import org.kalypso.observation.result.IComponent;
import org.kalypso.observation.result.IRecord;
import org.kalypsodeegree.model.geometry.GM_Point;

public class RoughnessHelper {

    public static void applyRoughnessOnProfile(final HCProfileWrapper profile, final GM_Point gmStart, final GM_Point gmEnd, final IRoughnessClass roughness) throws Exception {
        final IProfil iProfile = profile.getIProfile();
        final IComponent compBreite = ProfilObsHelper.getPropertyFromId(iProfile, IWspmConstants.POINT_PROPERTY_BREITE);
        final IComponent compHoehe = ProfilObsHelper.getPropertyFromId(iProfile, IWspmConstants.POINT_PROPERTY_HOEHE);
        final IComponent compRechtswert = ProfilObsHelper.getPropertyFromId(iProfile, IWspmConstants.POINT_PROPERTY_RECHTSWERT);
        final IComponent compHochwert = ProfilObsHelper.getPropertyFromId(iProfile, IWspmConstants.POINT_PROPERTY_HOCHWERT);
        final IComponent compRoughness = ProfilObsHelper.getPropertyFromId(iProfile, INofdpWspmConstants.POINT_PROPERTY_ROUGHNESS);
        final Double wStart = WspmProfileHelper.getWidthPosition(gmStart, iProfile, profile.getSrsName());
        final Double wEnd = WspmProfileHelper.getWidthPosition(gmEnd, iProfile, profile.getSrsName());
        final GM_Point start = WspmProfileHelper.getGeoPosition(wStart, iProfile);
        final GM_Point end = WspmProfileHelper.getGeoPosition(wEnd, iProfile);
        final IRecord rStart = iProfile.createProfilPoint();
        rStart.setValue(compBreite, wStart);
        rStart.setValue(compHoehe, start.getZ());
        rStart.setValue(compRechtswert, start.getX());
        rStart.setValue(compHochwert, start.getY());
        rStart.setValue(compRoughness, "#" + roughness.getId());
        WspmProfileHelper.addRecordByWidth(iProfile, rStart);
        final IRecord rEnd = iProfile.createProfilPoint();
        rEnd.setValue(compBreite, wEnd);
        rEnd.setValue(compHoehe, end.getZ());
        rEnd.setValue(compRechtswert, end.getX());
        rEnd.setValue(compHochwert, end.getY());
        rEnd.setValue(compRoughness, "#" + roughness.getId());
        WspmProfileHelper.addRecordByWidth(iProfile, rEnd);
        final IRecord[] points = iProfile.getPoints();
        for (final IRecord point : points) {
            final Double width = (Double) point.getValue(compBreite);
            if (width < wStart || width > wEnd) continue;
            point.setValue(compRoughness, "#" + roughness.getId());
        }
    }

    public static void updateRoughnessesOnDoppelTrapezMember(final IDoppelTrapezProfilMeasure measure, final IProfil profil) throws CoreException {
        final IRecord[] points = profil.getPoints();
        if (points.length != 9) throw ExceptionHelper.getCoreException(IStatus.ERROR, RoughnessHelper.class, Messages.RoughnessHelper_3);
        final IComponent roughness = ProfilObsHelper.getPropertyFromId(profil, INofdpWspmConstants.POINT_PROPERTY_ROUGHNESS);
        final PoolHydraulicModel pool = (PoolHydraulicModel) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eHydraulicModel);
        final IHydraulModel model = pool.getModel();
        final IRoughnessClass rLeftFloodPlain = measure.getRoughnessOnLeftFloodPlain(model);
        final IRoughnessClass rChannel = measure.getRoughnessInChannel(model);
        final IRoughnessClass rRightFloodPlain = measure.getRoughnessOnRightFloodPlain(model);
        for (int i = 0; i < points.length; i++) {
            final IRecord point = points[i];
            if (i < 2) {
                if (rLeftFloodPlain != null) point.setValue(roughness, "#" + rLeftFloodPlain.getId());
            } else if (i < 6) {
                if (rChannel != null) point.setValue(roughness, "#" + rChannel.getId());
            } else {
                if (rRightFloodPlain != null) point.setValue(roughness, "#" + rRightFloodPlain.getId());
            }
        }
    }
}
