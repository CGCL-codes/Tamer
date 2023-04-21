package L1;

import L1.Classifier;
import java.util.List;
import L1.Property;
import L1.Operation;

/**
 * A data type is a type whose instances are identified only by their value. A data type may contain attributes to support the modeling of structured data types.
 */
public interface DataType extends Classifier {

    /**
    * The Attributes owned by the DataType.
    */
    public abstract List<Property> getOwnedAttribute();

    /**
    * The Attributes owned by the DataType.
    */
    public abstract void setOwnedAttribute(List<Property> ownedattribute);

    /**
    * The Operations owned by the DataType.
    */
    public abstract List<Operation> getOwnedOperation();

    /**
    * The Operations owned by the DataType.
    */
    public abstract void setOwnedOperation(List<Operation> ownedoperation);
}
