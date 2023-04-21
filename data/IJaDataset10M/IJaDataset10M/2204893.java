package org.objectwiz.plugin.uibuilder.model.value;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.objectwiz.core.Application;
import org.objectwiz.core.facet.persistence.PersistenceFacetHelper;
import org.objectwiz.core.model.ObjectClass;
import org.objectwiz.core.model.SimpleDataTypeFactory;
import org.objectwiz.core.representation.Representation;
import org.objectwiz.plugin.uibuilder.EvaluationContext;
import org.objectwiz.plugin.uibuilder.UIBuilderMetadataException;
import org.objectwiz.plugin.uibuilder.model.reference.ClassRef;

/**
 * A static reference to an entity (i.e. an instance of a {@link MappedClass}).
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
@Table(name = "value_static_object")
public class StaticObjectValue extends Value {

    private ClassRef classRef;

    private String serializedId;

    /**
     * Public no-args constructor.
     */
    public StaticObjectValue() {
    }

    public StaticObjectValue(ClassRef classRef, String serializedId) {
        this.classRef = classRef;
        this.serializedId = serializedId;
    }

    @NotNull
    @Embedded
    public ClassRef getClassRef() {
        return classRef;
    }

    public void setClassRef(ClassRef classRef) {
        this.classRef = classRef;
    }

    @NotNull
    public String getSerializedId() {
        return serializedId;
    }

    public void setSerializedId(String serializedId) {
        this.serializedId = serializedId;
    }

    @Override
    public Object resolve(EvaluationContext ctx) throws Exception {
        Representation representation = ctx.getRepresentation();
        PersistenceFacetHelper persistence = PersistenceFacetHelper.require(ctx.getApplication());
        ObjectClass cl = classRef.resolve(ctx.getApplication());
        if (cl.isEnumeration()) {
            return representation.createEnumReference(cl, serializedId);
        } else if (cl.getIdProperty() == null) {
            throw new UIBuilderMetadataException("Classes without ID property cannot be referenced statically: " + cl.getName());
        }
        SimpleDataTypeFactory factory = ctx.getApplication().getPlatform().getSimpleDataTypeFactory();
        Object id = factory.parse(cl.getIdProperty().getSimpleDefinition().getTypeName(), serializedId);
        return persistence.find(cl.getName(), id);
    }

    @Override
    public boolean isValidInstance(Application app, Object instance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
