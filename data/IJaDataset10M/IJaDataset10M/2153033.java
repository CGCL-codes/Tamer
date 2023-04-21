package tudresden.ocl20.pivot.metamodels.uml2.internal.model;

import org.apache.log4j.Logger;
import tudresden.ocl20.pivot.metamodels.uml2.UML2MetamodelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral;

/**
 * <p>
 * An implementation of the Pivot Model {@link EnumerationLiteral} concept for
 * UML2.
 * </p>
 * 
 * @author Michael Thiele
 * 
 * @generated NOT
 */
public class UML2EnumerationLiteral extends AbstractEnumerationLiteral implements EnumerationLiteral {

    /**
	 * <p>
	 * Logger for this class
	 * </p>
	 * 
	 * @generated NOT
	 */
    private static final Logger LOGGER = UML2MetamodelPlugin.getLogger(UML2EnumerationLiteral.class);

    /**
	 * <p>
	 * The adapted {@link org.eclipse.uml2.uml.EnumerationLiteral} data type.
	 * </p>
	 * 
	 * @generated
	 */
    private org.eclipse.uml2.uml.EnumerationLiteral dslEnumerationLiteral;

    /**
	 * <p>
	 * The {@link UML2AdapterFactory} used to create nested elements.
	 * </p>
	 * 
	 * @generate NOT
	 */
    private UML2AdapterFactory factory;

    /**
	 * <p>
	 * Creates a new <code>UML2EnumerationLiteral</code> instance.
	 * </p>
	 * 
	 * @param dslEnumerationLiteral
	 *            the {@link org.eclipse.uml2.uml.EnumerationLiteral} that is
	 *            adopted by this class
	 * @param factory
	 *            The {@link UML2AdapterFactory} used to create nested elements.
	 * 
	 * @generated NOT
	 */
    public UML2EnumerationLiteral(org.eclipse.uml2.uml.EnumerationLiteral dslEnumerationLiteral, UML2AdapterFactory factory) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("UML2EnumerationLiteral(dslEnumerationLiteral = " + dslEnumerationLiteral + " factory = " + factory + ") - enter");
        }
        this.dslEnumerationLiteral = dslEnumerationLiteral;
        this.factory = factory;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("org.eclipse.uml2.uml.EnumerationLiteral() - exit");
        }
    }

    /**
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral#getName()
	 * 
	 * @generated NOT
	 */
    @Override
    public String getName() {
        return this.dslEnumerationLiteral.getName();
    }

    /**
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral#getEnumeration()
	 * 
	 * @generated NOT
	 */
    @Override
    public Enumeration getEnumeration() {
        return this.factory.createEnumeration(dslEnumerationLiteral.getEnumeration());
    }
}
