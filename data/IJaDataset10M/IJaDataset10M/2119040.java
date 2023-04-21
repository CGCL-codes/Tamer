package L1;

import L1.BehavioralFeature;
import L1.Class;
import java.util.List;
import L1.Constraint;
import L1.Operation;
import L1.DataType;
import L1.Type;
import L1.Parameter;
import L1.Interface;

/**
 * An operation may invoke both the execution of method behaviors as well as other behavioral responses.
 */
public interface Operation extends BehavioralFeature {

    /**
    * Specifies whether an execution of the BehavioralFeature leaves the state of the system unchanged (isQuery=true) or whether side effects may occur (isQuery=false).
    */
    public abstract Boolean getIsQuery();

    /**
    * Specifies whether an execution of the BehavioralFeature leaves the state of the system unchanged (isQuery=true) or whether side effects may occur (isQuery=false).
    */
    public abstract void setIsQuery(Boolean isquery);

    /**
    * Specifies whether the return parameter is ordered or not, if present.
    */
    public abstract Boolean getIsOrdered();

    /**
    * Specifies whether the return parameter is ordered or not, if present.
    */
    public abstract void setIsOrdered(Boolean isordered);

    /**
    * Specifies whether the return parameter is unique or not, if present.
    */
    public abstract Boolean getIsUnique();

    /**
    * Specifies whether the return parameter is unique or not, if present.
    */
    public abstract void setIsUnique(Boolean isunique);

    /**
    * Specifies the lower multiplicity of the return parameter, if present.
    */
    public abstract Integer getLower();

    /**
    * Specifies the lower multiplicity of the return parameter, if present.
    */
    public abstract void setLower(Integer lower);

    /**
    * Specifies the upper multiplicity of the return parameter, if present.
    */
    public abstract Long getUpper();

    /**
    * Specifies the upper multiplicity of the return parameter, if present.
    */
    public abstract void setUpper(Long upper);

    /**
    * The class that owns the operation.
    */
    public abstract Class get_class();

    /**
    * The class that owns the operation.
    */
    public abstract void set_class(Class _class);

    /**
    * An optional set of Constraints on the state of the system when the Operation is invoked.
    */
    public abstract List<Constraint> getPrecondition();

    /**
    * An optional set of Constraints on the state of the system when the Operation is invoked.
    */
    public abstract void setPrecondition(List<Constraint> precondition);

    /**
    * An optional set of Constraints specifying the state of the system when the Operation is completed.
    */
    public abstract List<Constraint> getPostcondition();

    /**
    * An optional set of Constraints specifying the state of the system when the Operation is completed.
    */
    public abstract void setPostcondition(List<Constraint> postcondition);

    /**
    * References the Operations that are redefined by this Operation.
    */
    public abstract List<Operation> getRedefinedOperation();

    /**
    * References the Operations that are redefined by this Operation.
    */
    public abstract void setRedefinedOperation(List<Operation> redefinedoperation);

    /**
    * The DataType that owns this Operation.
    */
    public abstract DataType getDatatype();

    /**
    * The DataType that owns this Operation.
    */
    public abstract void setDatatype(DataType datatype);

    /**
    * An optional Constraint on the result values of an invocation of this Operation.
    */
    public abstract Constraint getBodyCondition();

    /**
    * An optional Constraint on the result values of an invocation of this Operation.
    */
    public abstract void setBodyCondition(Constraint bodycondition);

    /**
    * Specifies the return result of the operation, if present.
    */
    public abstract Type getType();

    /**
    * Specifies the return result of the operation, if present.
    */
    public abstract void setType(Type type);

    /**
    * Specifies the parameters owned by this Operation.
    */
    public abstract List<Parameter> getOwnedParameter();

    /**
    * Specifies the parameters owned by this Operation.
    */
    public abstract void setOwnedParameter(List<Parameter> ownedparameter);

    /**
    * References the Types representing exceptions that may be raised during an invocation of this operation.
    */
    public abstract List<Type> getRaisedException();

    /**
    * References the Types representing exceptions that may be raised during an invocation of this operation.
    */
    public abstract void setRaisedException(List<Type> raisedexception);

    /**
    * The Interface that owns this Operation.
    */
    public abstract Interface get_interface();

    /**
    * The Interface that owns this Operation.
    */
    public abstract void set_interface(Interface _interface);
}
