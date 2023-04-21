package awilkins.objectmodel.properties.impl.objectmodel;

import awilkins.objectmodel.properties.ObjectPropertyMetadata;
import awilkins.objectmodel.properties.ObjectPropertyMetadataFactory;

/**
 * DefaultObjectPropertyMetadataFactory
 *
 * @author Antony
 */
public class DefaultObjectPropertyMetadataFactory extends ObjectPropertyMetadataFactory {

    public interface ObjectPropertyMetadataBuilder {

        ObjectPropertyMetadata createMetadata(Object o, ObjectPropertyMetadataFactory factory);

        ObjectPropertyMetadata createPrimitiveMetadata(Object o, ObjectPropertyMetadataFactory factory);
    }

    private static final ObjectPropertyMetadataBuilder DEFAULT_METADATA_FACTORY = new FieldObjectPropertyMetadataFactory();

    private ObjectPropertyMetadataBuilder metadataFactory;

    /**
   *
   */
    public DefaultObjectPropertyMetadataFactory() {
        super();
    }

    public final ObjectPropertyMetadataBuilder getMetadataFactory() {
        if (metadataFactory == null) {
            return DEFAULT_METADATA_FACTORY;
        }
        return metadataFactory;
    }

    public final void setMetadataFactory(ObjectPropertyMetadataBuilder factory) {
        metadataFactory = factory;
    }

    /**
   * @param o
   * @return
   */
    protected ObjectPropertyMetadata createMetadata(Object o) {
        return getMetadataFactory().createMetadata(o, this);
    }

    /**
   * @param o
   * @return
   */
    protected ObjectPropertyMetadata createPrimitiveMetadata(Object o) {
        return getMetadataFactory().createPrimitiveMetadata(o, this);
    }
}
