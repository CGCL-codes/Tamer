package org.kalypso.nofdpidss.variant.shape;

import java.lang.reflect.InvocationTargetException;
import javax.xml.namespace.QName;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.kalypso.commons.xml.XmlTypes;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress;
import org.kalypso.gmlschema.GMLSchemaFactory;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.gmlschema.property.IPropertyType;
import org.kalypso.gmlschema.property.IValuePropertyType;
import org.kalypso.gmlschema.property.relation.IRelationType;
import org.kalypso.gmlschema.types.IMarshallingTypeHandler;
import org.kalypso.gmlschema.types.ITypeRegistry;
import org.kalypso.gmlschema.types.MarshallingTypeRegistrySingleton;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.measures.MeasureTypeCsvReader;
import org.kalypso.ogc.gml.serialize.ShapeSerializer;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.GMLWorkspace;
import org.kalypsodeegree_impl.io.shpapi.ShapeConst;
import org.kalypsodeegree_impl.tools.GeometryUtilities;

public class VariantMeasures2ShapeExporter implements ICoreRunnableWithProgress {

    protected class ShapeSettings {

        public Feature rootFeature;

        public GMLWorkspace workspace;

        public IRelationType shapeParentRelation;

        public Double m_id = 0.0;

        public IFeatureType shapeFT;
    }

    private final IMeasure[] m_measures;

    public VariantMeasures2ShapeExporter(final IMeasure[] measures) {
        m_measures = measures;
    }

    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        try {
            final ShapeSettings shape = createShapeWorkspace();
            final MeasureTypeCsvReader reader = new MeasureTypeCsvReader();
            for (final IMeasure measure : m_measures) {
                MeasureShapeExporter.export(measure, shape, reader);
            }
            final IProject project = NofdpCorePlugin.getProjectManager().getActiveProject();
            final IFolder tmpFolder = project.getFolder(".tmp");
            if (!tmpFolder.exists()) tmpFolder.create(true, true, monitor);
            final String shapeBase = tmpFolder.getLocation() + "/measuresOfVariant";
            ShapeSerializer.serialize(shape.workspace, shapeBase, null);
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return StatusUtilities.createErrorStatus(e.getMessage());
        }
        return Status.OK_STATUS;
    }

    private ShapeSettings createShapeWorkspace() {
        final ShapeSettings shape = new ShapeSettings();
        final ITypeRegistry<IMarshallingTypeHandler> typeRegistry = MarshallingTypeRegistrySingleton.getTypeRegistry();
        final IMarshallingTypeHandler doubleTypeHandler = typeRegistry.getTypeHandlerForTypeName(XmlTypes.XS_DOUBLE);
        final IMarshallingTypeHandler stringTypeHandler = typeRegistry.getTypeHandlerForTypeName(XmlTypes.XS_STRING);
        final IMarshallingTypeHandler polygonTypeHandler = typeRegistry.getTypeHandlerForTypeName(GeometryUtilities.QN_POLYGON);
        final QName shapeTypeQName = new QName("anyNS", "shapeType");
        final IValuePropertyType polygonType = GMLSchemaFactory.createValuePropertyType(new QName("anyNS", "geometry"), polygonTypeHandler, 1, 1, false);
        final IValuePropertyType doubleId = GMLSchemaFactory.createValuePropertyType(new QName("anyNS", "id"), doubleTypeHandler, 1, 1, false);
        final IValuePropertyType stringTypeId = GMLSchemaFactory.createValuePropertyType(new QName("anyNS", "measure"), stringTypeHandler, 1, 1, false);
        final IValuePropertyType stringTypeName = GMLSchemaFactory.createValuePropertyType(new QName("anyNS", "type"), stringTypeHandler, 1, 1, false);
        final IValuePropertyType stringName = GMLSchemaFactory.createValuePropertyType(new QName("anyNS", "name"), stringTypeHandler, 1, 1, false);
        final IValuePropertyType stringDescription = GMLSchemaFactory.createValuePropertyType(new QName("anyNS", "desc"), stringTypeHandler, 1, 1, false);
        final IPropertyType[] properties = new IPropertyType[] { polygonType, doubleId, stringTypeId, stringTypeName, stringName, stringDescription };
        shape.shapeFT = GMLSchemaFactory.createFeatureType(shapeTypeQName, properties);
        shape.rootFeature = ShapeSerializer.createWorkspaceRootFeature(shape.shapeFT, ShapeConst.SHAPE_TYPE_POLYGONZ);
        shape.workspace = shape.rootFeature.getWorkspace();
        shape.shapeParentRelation = (IRelationType) shape.rootFeature.getFeatureType().getProperty(ShapeSerializer.PROPERTY_FEATURE_MEMBER);
        return shape;
    }
}
