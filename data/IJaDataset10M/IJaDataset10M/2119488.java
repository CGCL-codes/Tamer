package tudresden.ocl20.pivot.metamodels.java.internal.model;

import org.apache.log4j.Logger;
import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral;

/**
 * <p>
 * An implementation of the Pivot Model {@link EnumerationLiteral} concept for
 * Java.
 * </p>
 * 
 * @author Claas Wilkes
 */
public class JavaEnumerationLiteral extends AbstractEnumerationLiteral implements EnumerationLiteral {

    /** The {@link Logger} for this {@link Class}. */
    private static final Logger LOGGER = JavaMetaModelPlugin.getLogger(JavaEnumerationLiteral.class);

    /**
	 * <p>
	 * The adapted Java {@link Enum} which represents the
	 * {@link EnumerationLiteral}.
	 * </p>
	 * 
	 * @generated
	 */
    private Enum<?> myJavaEnumLiteral;

    /**
	 * The {@link JavaAdapterFactory} the {@link JavaEnumerationLiteral} belongs
	 * to.
	 */
    private JavaAdapterFactory myFactory;

    /**
	 * <p>
	 * Creates a new {@link JavaEnumerationLiteral} instance.
	 * </p>
	 * 
	 * @param adaptedEnum
	 *          The {@link Enum} that is adopted by this class
	 * 
	 * @param aFactory
	 *          The {@link JavaAdapterFactory}, the new created
	 *          {@link JavaEnumerationLiteral} shall belong to.
	 * @generated NOT
	 */
    public JavaEnumerationLiteral(Enum<?> adaptedEnum, JavaAdapterFactory aFactory) {
        if (LOGGER.isDebugEnabled()) {
            String msg;
            msg = "JavaEnumerationLiteral(";
            msg += "dslEnumerationLiteral = " + adaptedEnum;
            msg += ", aFactory = " + aFactory;
            msg += ") - enter";
            LOGGER.debug(msg);
        }
        this.myJavaEnumLiteral = adaptedEnum;
        this.myFactory = aFactory;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("JavaEnumerationLiteral() - exit");
        }
    }

    public Enumeration getEnumeration() {
        return (Enumeration) this.myFactory.createType(this.myJavaEnumLiteral.getDeclaringClass());
    }

    public String getName() {
        return this.myJavaEnumLiteral.name();
    }
}
