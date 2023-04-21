package org.kalypso.nofdpidss.core.common.utils.gml;

import java.util.List;
import javax.xml.namespace.QName;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.gmlschema.property.IPropertyType;
import org.kalypso.gmlschema.property.IValuePropertyType;
import org.kalypso.gmlschema.property.relation.IRelationType;
import org.kalypso.gmlschema.property.restriction.EnumerationRestriction;
import org.kalypso.gmlschema.property.restriction.IRestriction;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.i18n.Messages;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.FeatureList;
import org.kalypsodeegree.model.feature.GMLWorkspace;

/**
 * @author Dirk Kuch
 */
public class McGmlUtils {

    public enum DRAWING_TYPE {

        polygone, lineWithPolygone, bufferedLine, eChoose, point, lineWithBufferAndPolygone, lineWithBufferAndSecondLineWithBufferAndPolygone;

        public static DRAWING_TYPE getDrawingType(final String string) {
            final DRAWING_TYPE type = DRAWING_TYPE.valueOf(string);
            if (type == null) throw new IllegalStateException(Messages.McGmlUtils_0 + string);
            return type;
        }
    }

    public static void deleteMeasure(final MyBasePool pool, final Feature feature) throws Exception {
        final QName name = feature.getFeatureType().getQName();
        if (GmlConstants.NS_MEASURES.equals(name.getNamespaceURI()) && name.getLocalPart().startsWith("meas")) FeatureUtils.deleteFeature(pool.getWorkspace(), feature);
    }

    public static FeatureList findParentFeature(final PoolProject pool, final IFeatureType substitutionGroupFT) {
        final Feature rootFeature = pool.getWorkspace().getRootFeature();
        final Feature meaMembers = (Feature) rootFeature.getProperty(GmlConstants.QN_MEASURE_MEASURE_MEMBERS);
        final List<?> meaConstainers = (List<?>) meaMembers.getProperty(GmlConstants.QN_MEASURE_MEASURE_CONTAINER_MEMBER);
        for (final Object obj : meaConstainers) {
            if (obj == null || !(obj instanceof Feature)) continue;
            final Feature fContainer = (Feature) obj;
            final Object[] properties = fContainer.getProperties();
            for (final Object object : properties) {
                if (!(object instanceof FeatureList)) continue;
                final FeatureList fMembers = (FeatureList) object;
                final IRelationType rt = fMembers.getParentFeatureTypeProperty();
                final IFeatureType targetFeatureType = rt.getTargetFeatureType();
                if (substitutionGroupFT.getQName().equals(targetFeatureType.getQName())) return fMembers;
            }
        }
        throw new IllegalStateException(Messages.McGmlUtils_2);
    }

    public static Feature getAllowedMembers(final GMLWorkspace workspace) {
        final Feature fRoot = workspace.getRootFeature();
        final Feature fMeasureMember = (Feature) fRoot.getProperty(GmlConstants.QN_MEASURE_MEASURE_MEMBERS);
        final Feature fAllowedMembers = (Feature) fMeasureMember.getProperty(new QName(GmlConstants.NS_MEASURES, "suitabilityAllowedContentsMember"));
        return fAllowedMembers;
    }

    public static List<?> getMeasureContainer(final PoolProject pool) {
        return pool.getModel().getMeasureMembers().getContainer();
    }

    public static DRAWING_TYPE selectGeometry(final Feature feature) {
        final IFeatureType featureType = feature.getFeatureType();
        final IPropertyType property = featureType.getProperty(GmlConstants.QN_MEASURE_MEASURE_DIGI_TOOL);
        if (!(property instanceof IValuePropertyType)) throw new IllegalStateException(Messages.McGmlUtils_4);
        final IValuePropertyType vt = (IValuePropertyType) property;
        final IRestriction[] restrictions = vt.getRestriction();
        for (final IRestriction restriction : restrictions) if (restriction instanceof EnumerationRestriction) {
            final EnumerationRestriction r = (EnumerationRestriction) restriction;
            final Object[] enumeration = r.getEnumeration();
            if (enumeration.length != 1) return DRAWING_TYPE.eChoose;
            final String name = enumeration[0].toString();
            if ("polygone".equals(name)) return DRAWING_TYPE.polygone; else if ("bufferedLine".equals(name)) return DRAWING_TYPE.bufferedLine; else if ("lineWithPolygone".equals(name)) return DRAWING_TYPE.lineWithPolygone; else if ("point".equals(name)) return DRAWING_TYPE.point;
        }
        return DRAWING_TYPE.eChoose;
    }
}
