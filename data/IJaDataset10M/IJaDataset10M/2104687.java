package tudresden.ocl20.pivot.metamodels.java.internal.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.apache.log4j.Logger;
import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractOperation;

/**
 * <p>
 * An implementation of the Pivot Model {@link Operation} concept for UML2.
 * </p>
 */
public class JavaOperation extends AbstractOperation implements Operation {

    /** The {@link Logger} for this class. */
    private static final Logger LOGGER = JavaMetaModelPlugin.getLogger(JavaOperation.class);

    /** The adapted {@link Method}. */
    private Method myMethod;

    /** The {@link JavaAdapterFactory} the {@link JavaOperation} belongs to. */
    private JavaAdapterFactory myFactory;

    /**
	 * <p>
	 * Creates a new {@link JavaOperation} instance.
	 * </p>
	 * 
	 * @param dslOperation
	 *            The {@link Method} that is adopted by this class.
	 * @param aFactory
	 *            The {@link JavaAdapterFactory}, the new created
	 *            {@link JavaOperation} shall belong to.
	 */
    public JavaOperation(Method dslOperation, JavaAdapterFactory aFactory) {
        if (LOGGER.isDebugEnabled()) {
            String msg;
            msg = "JavaOperation(";
            msg += "dslOperation = " + dslOperation;
            msg += ", aFactory = " + aFactory;
            msg += ") - enter";
            LOGGER.debug(msg);
        }
        this.myMethod = dslOperation;
        this.myFactory = aFactory;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("JavaOperation() - exit");
        }
    }

    @Override
    public String getName() {
        return this.myMethod.getName();
    }

    @Override
    public List<Parameter> getOwnedParameter() {
        List<Parameter> result;
        result = this.myFactory.createInputParameters(this.myMethod);
        result.add(this.getReturnParameter());
        return result;
    }

    @Override
    public Type getOwningType() {
        Type owner;
        owner = this.myFactory.createType(this.myMethod.getDeclaringClass());
        return owner;
    }

    @Override
    public Type getType() {
        return this.myFactory.adaptJavaType(this.myMethod.getReturnType(), this.myMethod.getGenericReturnType());
    }

    public Parameter getReturnParameter() {
        return this.myFactory.createParameter(this.myMethod.getReturnType(), this.myMethod.getGenericReturnType(), ParameterDirectionKind.RETURN, this.myMethod, 1);
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.myMethod.getModifiers());
    }
}
