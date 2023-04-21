package tudresden.ocl20.pivot.standardlibrary.java.internal.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import tudresden.ocl20.pivot.essentialocl.EssentialOclPlugin;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclAny;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBoolean;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclModelInstanceObject;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclSet;
import tudresden.ocl20.pivot.modelinstancetype.exception.OperationAccessException;
import tudresden.ocl20.pivot.modelinstancetype.exception.OperationNotFoundException;
import tudresden.ocl20.pivot.modelinstancetype.exception.PropertyAccessException;
import tudresden.ocl20.pivot.modelinstancetype.exception.PropertyNotFoundException;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.standardlibrary.java.factory.JavaStandardLibraryFactory;

/**
 * <p>
 * This class implements the OCL type {@link OclModelInstanceObject} in Java.
 * </p>
 * 
 * @author Ronny Brandt
 * @author Michael Thiele
 */
public class JavaOclModelInstanceObject extends JavaOclAny implements OclModelInstanceObject, IAddableElement {

    protected Type metaType;

    /**
	 * <p>
	 * Instantiates a new {@link JavaOclObject}.
	 * </p>
	 * 
	 * @param adaptee
	 *            The adapted model instance object.
	 */
    public JavaOclModelInstanceObject(IModelInstanceObject imiObject, Type metaType) {
        super(imiObject);
        this.metaType = metaType;
    }

    public JavaOclModelInstanceObject(String undefinedReason, Type metaType) {
        super(undefinedReason);
        this.metaType = metaType;
    }

    public JavaOclModelInstanceObject(Throwable invalidReason, Type metaType) {
        super(invalidReason);
        this.metaType = metaType;
    }

    public OclAny add(OclAny that) {
        OclAny result;
        result = checkInvalid(metaType, this, that);
        if (result == null) result = checkUndefined("+", metaType, this, that);
        if (result == null) {
            if (that.getModelInstanceElement().isKindOf(metaType)) {
                result = findMethod("+", that);
                if (result == null) result = findMethod("add", that);
                if (result == null) result = findMethod("plus", that);
                if (result == null) result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(metaType, new NoSuchMethodException("Cannot find operation +, add or plus on " + this));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public OclSet<OclModelInstanceObject> asSet() {
        OclSet<OclModelInstanceObject> result;
        result = checkInvalid(EssentialOclPlugin.getOclLibraryProvider().getOclLibrary().getSetType(metaType), this);
        if (result == null) result = checkAsSet(metaType);
        if (result == null) {
            Set<IModelInstanceElement> resultSet = new HashSet<IModelInstanceElement>();
            resultSet.add(this.getModelInstanceObject());
            result = JavaStandardLibraryFactory.INSTANCE.createOclSet(resultSet, EssentialOclPlugin.getOclLibraryProvider().getOclLibrary().getSetType(metaType));
        }
        return result;
    }

    public IModelInstanceObject getModelInstanceObject() {
        return (IModelInstanceObject) this.imiElement;
    }

    public OclAny getNeutralElement() {
        return null;
    }

    /**
	 * <p>
	 * Returns the {@link OclAny} of a given {@link Property} that is defined on
	 * this {@link IModelInstanceObject}.
	 * </p>
	 * 
	 * @param property
	 *            The {@link Property} whose value shall be returned.
	 * @return The result as an {@link OclAny}.
	 */
    public OclAny getProperty(Property property) {
        OclAny result;
        if (this.oclIsInvalid().isTrue()) {
            result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(property.getType(), this.getInvalidReason());
        } else if (this.oclIsUndefined().isTrue()) {
            result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(property.getType(), new NullPointerException(this.getUndefinedReason()));
        } else {
            IModelInstanceElement imiResult;
            try {
                imiResult = getModelInstanceObject().getProperty(property);
                if (imiResult.isUndefined()) {
                    result = JavaStandardLibraryFactory.INSTANCE.createOclUndefined(property.getType(), imiResult.getName() + " is null.");
                } else {
                    result = JavaStandardLibraryFactory.INSTANCE.createOclAny(imiResult);
                }
            } catch (PropertyNotFoundException e) {
                result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(property.getType(), e);
            } catch (PropertyAccessException e) {
                result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(property.getType(), e);
            }
        }
        return result;
    }

    public OclBoolean isEqualTo(OclAny that) {
        OclBoolean result;
        result = checkIsEqualTo(that);
        if (result == null) {
            if (!(that instanceof OclModelInstanceObject)) {
                result = JavaOclBoolean.getInstance(false);
            } else {
                Object thatObject = ((IModelInstanceObject) that.getModelInstanceElement()).getObject();
                if (getModelInstanceObject().getObject().equals(thatObject)) {
                    result = JavaOclBoolean.getInstance(true);
                } else {
                    result = JavaOclBoolean.getInstance(false);
                }
            }
        }
        return result;
    }

    public OclAny invokeOperation(Operation operation, OclAny... args) {
        OclAny result = null;
        final String operationName = operation.getName();
        if ((operationName.equals("oclIsTypeOf") || operationName.equals("oclIsKindOf") || operationName.equals("oclAsType")) && args.length == 1) {
            result = super.invokeOperation(operation, args);
        }
        if (result == null && (operationName.equals("oclIsUndefined") || operationName.equals("oclIsInvalid")) && args.length == 0) result = super.invokeOperation(operation, args);
        if (result == null && (operationName.equals("asSet") && args.length == 0)) result = this.asSet();
        if (result == null) result = checkInvalid(operation, args);
        if (result == null) {
            IModelInstanceElement imiResult;
            List<IModelInstanceElement> imiArguments;
            imiArguments = new LinkedList<IModelInstanceElement>();
            try {
                for (OclAny arg : args) {
                    imiArguments.add(arg.getModelInstanceElement());
                }
                imiResult = getModelInstanceObject().invokeOperation(operation, imiArguments);
                result = JavaStandardLibraryFactory.INSTANCE.createOclAny(imiResult);
            } catch (OperationNotFoundException e) {
                result = super.invokeOperation(operation, args);
            } catch (OperationAccessException e) {
                result = super.invokeOperation(operation, args);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName());
        result.append("[");
        if (!toStringUndefinedOrInvalid(result)) {
            result.append(getModelInstanceObject().toString());
        }
        result.append("]");
        return result.toString();
    }

    /**
	 * Used to determine invalid return values for {@link Operation}s.
	 * <code>this</code> is checked to be <code>undefined</code> or
	 * <code>invalid</code> and the arguments are checked for
	 * <code>invalid</code> .
	 * 
	 * @param operation
	 *            the operation to call
	 * @param args
	 *            the arguments of the operation
	 * @return <code>null</code> if neither the source nor the args are
	 *         undefined or invalid, the undefined or invalid source else
	 */
    protected OclAny checkInvalid(Operation operation, OclAny... args) {
        OclAny result = null;
        if (this.oclIsInvalid().isTrue() && !(operation.getName().equals("oclIsInvalid") && args.length == 0) && !(operation.getName().equals("isEqualTo") && args.length == 1)) {
            result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(operation.getType(), this.getInvalidReason());
        } else if (this.oclIsUndefined().isTrue() && !(operation.getName().equals("oclIsInvalid") && args.length == 0) && !(operation.getName().equals("oclIsUndefined") && args.length == 0) && !(operation.getName().equals("isEqualTo") && args.length == 1)) {
            result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(operation.getType(), new RuntimeException("Tried to invoke operation " + operation.getName() + " on null. Reason: " + this.getUndefinedReason()));
        }
        if (result == null) {
            for (OclAny arg : args) {
                if (arg.oclIsInvalid().isTrue()) {
                    result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(operation.getType(), arg.getInvalidReason());
                    break;
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private OclAny findMethod(String methodName, OclAny that) {
        OclAny result = null;
        try {
            findMethod(methodName, this.getClass(), that.getClass());
            Operation operation = null;
            List<Type> argTypes;
            argTypes = new ArrayList<Type>();
            argTypes.add(that.getModelInstanceElement().getType());
            operation = this.imiElement.getType().lookupOperation(methodName, argTypes);
            if (operation != null) result = super.invokeOperation(operation, that); else result = JavaStandardLibraryFactory.INSTANCE.createOclInvalid(metaType, new NoSuchMethodException("Cannot find " + methodName + " on " + this));
        } catch (NoSuchMethodException e) {
        }
        return result;
    }
}
