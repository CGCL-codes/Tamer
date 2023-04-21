package crafty.metamodel.type;

import crafty.metamodel.MetaModelVisitor;

/**
 * An element that represents an external type. An external type is something
 * that is unknown in the entire metamodel context. This may be temporary until
 * the type is resolved by subsequent processing of the metamodel, or the
 * type is only symbolic as it may be declared outside the scope of the metamodel.
 *
 * @author Alex Radeski
 * @version $Revision: 1.2 $
 */
public class MetaExternalType extends MetaDataType {

    public MetaExternalType() {
    }

    public MetaExternalType(String name) {
        super(name);
    }

    public void accept(MetaModelVisitor v) {
        v.visitExternalType(this);
    }
}
