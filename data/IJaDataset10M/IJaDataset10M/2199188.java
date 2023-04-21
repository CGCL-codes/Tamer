package tudresden.ocl20.pivot.modelbus.modelinstance.types.base;

import java.util.HashSet;
import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelbus.internal.ModelBusMessages;
import tudresden.ocl20.pivot.modelbus.modelinstance.exception.AsTypeCastException;
import tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceInteger;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveType;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * Represents an adaptation for {@link Integer}s of a {@link JavaModelInstance}.
 * </p>
 * 
 * <p>
 * This type is located in the ModelBus plug-in because the standard library and
 * the Java model instance type plug-in both require such an implementation but
 * are not allowed to know each other.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaModelInstanceInteger extends AbstractModelInstanceInteger implements IModelInstanceInteger {

    /** The {@link Logger} for this class. */
    private static final Logger LOGGER = ModelBusPlugin.getLogger(JavaModelInstanceInteger.class);

    /** The adapted {@link Long} of this {@link JavaModelInstanceInteger}. */
    private Long myLong;

    /**
	 * <p>
	 * Creates a new {@link JavaModelInstanceInteger}.
	 * </p>
	 * 
	 * @param aLong
	 *          The {@link Long} that shall be adapted by this
	 *          {@link JavaModelInstanceInteger}.
	 */
    protected JavaModelInstanceInteger(Long aLong) {
        if (LOGGER.isDebugEnabled()) {
            String msg;
            msg = "JavaModelInstanceInteger(";
            msg += "aLong = " + aLong;
            msg += ")";
            LOGGER.debug(msg);
        }
        this.myLong = aLong;
        this.myTypes = new HashSet<Type>();
        this.myTypes.add(PrimitiveAndCollectionTypeConstants.MODEL_TYPE_INTEGER);
        if (LOGGER.isDebugEnabled()) {
            String msg;
            msg = "JavaModelInstanceInteger(Long) - exit";
            LOGGER.debug(msg);
        }
    }

    public IModelInstanceElement asType(Type type) throws AsTypeCastException {
        IModelInstanceElement result;
        result = null;
        if (type instanceof PrimitiveType) {
            PrimitiveType primitiveType;
            primitiveType = (PrimitiveType) type;
            if (primitiveType.getKind().equals(PrimitiveTypeKind.INTEGER)) {
                result = new JavaModelInstanceInteger(this.myLong);
            } else if (primitiveType.getKind().equals(PrimitiveTypeKind.REAL)) {
                result = new JavaModelInstanceReal(this.myLong);
            } else if (primitiveType.getKind().equals(PrimitiveTypeKind.STRING)) {
                if (this.myLong == null) {
                    result = new JavaModelInstanceString(null);
                } else {
                    result = new JavaModelInstanceString(this.myLong.toString());
                }
            }
        }
        if (result == null) {
            String msg;
            msg = ModelBusMessages.IModelInstanceElement_CannotCast;
            msg = NLS.bind(msg, this.getName(), type.getName());
            throw new AsTypeCastException(msg);
        }
        return result;
    }

    public IModelInstanceElement copyForAtPre() {
        return new JavaModelInstanceInteger(this.myLong);
    }

    public Long getLong() {
        return this.myLong;
    }

    public Double getDouble() {
        return this.myLong.doubleValue();
    }
}
