package tudresden.ocl20.pivot.metamodels.java.internal.model;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractParameter;

/**
 * <p>
 * An implementation of the Pivot Model {@link Parameter} concept for Java.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaParameter extends AbstractParameter implements Parameter {

    /** The {@link Logger} for this class. */
    private static final Logger LOGGER = JavaMetaModelPlugin.getLogger(JavaParameter.class);

    /** The adapted {@link Class} of this {@link JavaParameter}. */
    private Class<?> myClass;

    /**
	 * The adapted {@link java.lang.reflect.Type} of this {@link JavaParameter}.
	 */
    private java.lang.reflect.Type myGenericType;

    /** The {@link Operation} of this {@link JavaParameter}. */
    private Method myMethod;

    /** The {@link ParameterDirectionKind} of this {@link JavaParameter}. */
    private ParameterDirectionKind myKind;

    /**
	 * The {@link JavaParameter} has a number used to create a unique parameter
	 * name.
	 */
    private int myParameterNumber;

    /** The {@link JavaAdapterFactory} the {@link JavaParameter} belongs to. */
    private JavaAdapterFactory myFactory;

    /**
	 * <p>
	 * Creates a new {@link JavaParameter} instance.
	 * </p>
	 * 
	 * @param dslParameter
	 *          The {@link Class} that is adopted by this class.
	 * @param genericParameter
	 *          The generic {@link java.lang.reflect.Type} of this
	 *          {@link JavaParameter} or <code>null</code>.
	 * @param parameterKind
	 *          The {@link ParameterDirectionKind} of this {@link JavaParameter}.
	 * @param method
	 *          The {@link Method} of this {@link JavaParameter}.
	 * @param parameterNumber
	 *          A number used to create a unique parameter name.
	 * @param aFactory
	 *          The {@link JavaAdapterFactory}, the new created
	 *          {@link JavaParameter} shall belong to.
	 */
    public JavaParameter(Class<?> dslParameter, java.lang.reflect.Type genericParameter, ParameterDirectionKind parameterKind, Method method, int parameterNumber, JavaAdapterFactory aFactory) {
        if (LOGGER.isDebugEnabled()) {
            String msg;
            msg = "JavaParameter(";
            msg += ", dslParameter = " + dslParameter;
            msg += ", genericParameter = " + genericParameter;
            msg += ", parameterKind = " + parameterKind;
            msg += ", method = " + method;
            msg += ", parameterNumber = " + parameterNumber;
            msg += ", aFactory = " + aFactory;
            msg += ") - enter";
            LOGGER.debug(msg);
        }
        this.myClass = dslParameter;
        this.myGenericType = genericParameter;
        this.myKind = parameterKind;
        this.myMethod = method;
        this.myParameterNumber = parameterNumber;
        this.myFactory = aFactory;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("JavaParameter() - exit");
        }
    }

    public ParameterDirectionKind getKind() {
        return this.myKind;
    }

    @Override
    public String getName() {
        return this.getKind().toString() + this.myParameterNumber;
    }

    @Override
    public Operation getOperation() {
        return this.myFactory.createOperation(this.myMethod);
    }

    @Override
    public Type getType() {
        return this.myFactory.adaptJavaType(this.myClass, this.myGenericType);
    }
}
