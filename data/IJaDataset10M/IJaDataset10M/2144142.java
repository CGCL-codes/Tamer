package org.kalypso.nofdpidss.variant.shape;

import org.apache.commons.lang.NotImplementedException;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureAdaptedAgriculture;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureAdaptedForestManagement;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBankRecessionAndFillUp;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBottomLevelChange;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBufferStrip;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDikeConstruction;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDikeRelocation;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDiversion;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureEarthWall;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureEcologicalFlooding;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureExcavationWorks;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureFloodplainLowering;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureForestDevelopment;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureMeandering;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureMobileWall;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureObstacleRemoval;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasurePolder;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureRetardingBasin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureUrbanLanduse;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureWeir;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureZoningPlanModification;
import org.kalypso.nofdpidss.core.base.measures.MeasureTypeCsvReader;
import org.kalypso.nofdpidss.variant.shape.VariantMeasures2ShapeExporter.ShapeSettings;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.geometry.GM_Curve;
import org.kalypsodeegree.model.geometry.GM_Point;
import org.kalypsodeegree.model.geometry.GM_Surface;
import org.kalypsodeegree_impl.model.feature.FeatureFactory;
import org.kalypsodeegree_impl.model.geometry.JTSAdapter;
import com.vividsolutions.jts.geom.Geometry;

public class MeasureShapeExporter {

    public static void export(final IMeasure measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        if (measure instanceof IMeasureDiversion) {
            exportDiversion((IMeasureDiversion) measure, shape, reader);
        } else if (measure instanceof IMeasureMeandering) {
            exportMeandering((IMeasureMeandering) measure, shape, reader);
        } else if (measure instanceof IMeasureBankRecessionAndFillUp) {
            exportBankRecessionAndFillUp((IMeasureBankRecessionAndFillUp) measure, shape, reader);
        } else if (measure instanceof IMeasureBottomLevelChange) {
            exportBottomLevelChange((IMeasureBottomLevelChange) measure, shape, reader);
        } else if (measure instanceof IMeasureDikeRelocation) {
            exportDykeRelocation((IMeasureDikeRelocation) measure, shape, reader);
        } else if (measure instanceof IMeasureDikeConstruction) {
            exportDykeConstruction((IMeasureDikeConstruction) measure, shape, reader);
        } else if (measure instanceof IMeasureEarthWall) {
            exportEarthWall((IMeasureEarthWall) measure, shape, reader);
        } else if (measure instanceof IMeasureEcologicalFlooding) {
            exportEcologicalFlooding((IMeasureEcologicalFlooding) measure, shape, reader);
        } else if (measure instanceof IMeasureExcavationWorks) {
            exportExcavationWorks((IMeasureExcavationWorks) measure, shape, reader);
        } else if (measure instanceof IMeasureFloodplainLowering) {
            exportFloodplainLowering((IMeasureFloodplainLowering) measure, shape, reader);
        } else if (measure instanceof IMeasureMobileWall) {
            exportMobileWall((IMeasureMobileWall) measure, shape, reader);
        } else if (measure instanceof IMeasureObstacleRemoval) {
            exportObstacleRemoval((IMeasureObstacleRemoval) measure, shape, reader);
        } else if (measure instanceof IMeasurePolder) {
            exportPolder((IMeasurePolder) measure, shape, reader);
        } else if (measure instanceof IMeasureRetardingBasin) {
            exportRetardingBasin((IMeasureRetardingBasin) measure, shape, reader);
        } else if (measure instanceof IMeasureAdaptedAgriculture) {
            exportAdaptedAgriculture((IMeasureAdaptedAgriculture) measure, shape, reader);
        } else if (measure instanceof IMeasureAdaptedForestManagement) {
            exportAdaptedForestManagement((IMeasureAdaptedForestManagement) measure, shape, reader);
        } else if (measure instanceof IMeasureBufferStrip) {
            exportBufferStrip((IMeasureBufferStrip) measure, shape, reader);
        } else if (measure instanceof IMeasureForestDevelopment) {
            exportForestDevelopment((IMeasureForestDevelopment) measure, shape, reader);
        } else if (measure instanceof IMeasureUrbanLanduse) {
            exportUrbanLanduse((IMeasureUrbanLanduse) measure, shape, reader);
        } else if (measure instanceof IMeasureWeir) {
            exportWeir((IMeasureWeir) measure, shape, reader);
        } else if (measure instanceof IMeasureZoningPlanModification) {
            exportZoningPlanModification((IMeasureZoningPlanModification) measure, shape, reader);
        } else throw new NotImplementedException();
    }

    private static void exportZoningPlanModification(final IMeasureZoningPlanModification measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "ZPM", reader.getString("ZPM"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportWeir(final IMeasureWeir measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Point point = measure.getBranchConnectionPoint();
        final Double buffer = measure.getCrestWidth() / 2 + 10;
        final Geometry jtsPoint = JTSAdapter.export(point);
        final Geometry bufferedPoint = jtsPoint.buffer(buffer);
        final GM_Surface<?> surface = (GM_Surface<?>) JTSAdapter.wrap(bufferedPoint);
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "WEI", reader.getString("WEI"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportUrbanLanduse(final IMeasureUrbanLanduse measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "AUL", reader.getString("AUL"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportForestDevelopment(final IMeasureForestDevelopment measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "FFO", reader.getString("FFO"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportBufferStrip(final IMeasureBufferStrip measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "BUS", reader.getString("BUS"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportAdaptedForestManagement(final IMeasureAdaptedForestManagement measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "AFO", reader.getString("AFO"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportAdaptedAgriculture(final IMeasureAdaptedAgriculture measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "ADA", reader.getString("ADA"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportRetardingBasin(final IMeasureRetardingBasin measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "RBA", reader.getString("RBA"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportPolder(final IMeasurePolder measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "POL", reader.getString("POL"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportObstacleRemoval(final IMeasureObstacleRemoval measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "OBR", reader.getString("OBR"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportMobileWall(final IMeasureMobileWall measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Curve geometry = measure.getGeometry();
        final Geometry jtsLine = JTSAdapter.export(geometry);
        final Geometry buffer = jtsLine.buffer(1);
        final GM_Surface<?> surface = (GM_Surface<?>) JTSAdapter.wrap(buffer);
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "MOW", reader.getString("MOW"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportFloodplainLowering(final IMeasureFloodplainLowering measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getSurface();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "FPL", reader.getString("FPL"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportExcavationWorks(final IMeasureExcavationWorks measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "FEX", reader.getString("FEX"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportEcologicalFlooding(final IMeasureEcologicalFlooding measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "ECO", reader.getString("ECO"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportEarthWall(final IMeasureEarthWall measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getGeometry();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "FEW", reader.getString("FEW"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportDykeConstruction(final IMeasureDikeConstruction measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Curve dikeGeometry = measure.getDikeGeometry();
        final Double width = measure.getTopWidth() * 2;
        final Geometry jtsCurve = JTSAdapter.export(dikeGeometry);
        final Geometry jtsSurface = jtsCurve.buffer(width);
        final GM_Surface<?> surface = (GM_Surface<?>) JTSAdapter.wrap(jtsSurface);
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "DIK", reader.getString("DIK"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportDykeRelocation(final IMeasureDikeRelocation measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> oldDikeSurface = measure.getOldDikeFootBoundary();
        Object[] shapeData = new Object[] { oldDikeSurface, shape.m_id++, "DRO", reader.getString("DRO"), measure.getName(), measure.getDescription() };
        Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
        final GM_Curve dikeGeometry = measure.getDikeGeometry();
        final Double width = measure.getTopWidth() * 2;
        final Geometry jtsCurve = JTSAdapter.export(dikeGeometry);
        final Geometry jtsSurface = jtsCurve.buffer(width);
        final GM_Surface<?> surface = (GM_Surface<?>) JTSAdapter.wrap(jtsSurface);
        shapeData = new Object[] { surface, shape.m_id++, "DRN", reader.getString("DRN"), measure.getName(), measure.getDescription() };
        feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportBottomLevelChange(final IMeasureBottomLevelChange measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Curve curve = measure.getCalcConnectionLine();
        final Geometry jtsCurve = JTSAdapter.export(curve);
        final Geometry buffer = jtsCurve.buffer(10);
        final GM_Surface<?> surface = (GM_Surface<?>) JTSAdapter.wrap(buffer);
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "REC", reader.getString("REC"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportBankRecessionAndFillUp(final IMeasureBankRecessionAndFillUp measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = measure.getSurface();
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "RRL", reader.getString("RRL"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportMeandering(final IMeasureMeandering measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = (GM_Surface<?>) measure.getLowerChannel().union(measure.getUpperChannel());
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "MEA", reader.getString("MEA"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }

    private static void exportDiversion(final IMeasureDiversion measure, final ShapeSettings shape, final MeasureTypeCsvReader reader) throws Exception {
        final GM_Surface<?> surface = (GM_Surface<?>) measure.getLowerChannel().union(measure.getUpperChannel());
        final Object[] shapeData = new Object[] { surface, shape.m_id++, "DIV", reader.getString("DIV"), measure.getName(), measure.getDescription() };
        final Feature feature = FeatureFactory.createFeature(shape.rootFeature, shape.shapeParentRelation, "FeatureID" + shape.m_id, shape.shapeFT, shapeData);
        shape.workspace.addFeatureAsComposition(shape.rootFeature, shape.shapeParentRelation, -1, feature);
    }
}
