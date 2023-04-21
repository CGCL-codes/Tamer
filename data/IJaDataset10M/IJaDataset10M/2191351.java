package org.parallelj.common.jdt.checkers;

import static org.parallelj.common.jdt.helpers.JavaCodeHelper.areTypesEquals;
import static org.parallelj.common.jdt.helpers.JavaCodeHelper.getFieldInitializer;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * Defines several methods used to perform Java class field checking.
 * 
 * @author Atos Worldline
 */
public class FieldChecker extends ElementChecker {

    private FieldDeclaration fd = null;

    /**
	 * Construct a FieldChecker with a FieldDeclaration as single parameter.
	 * 
	 * @param fd
	 *            A JDT FieldDeclaration object
	 * @throws IllegalArgumentException
	 *             If {@code fd} is null
	 */
    FieldChecker(FieldDeclaration fd) {
        super(fd);
        if (fd == null) {
            throw new IllegalArgumentException("Cannot create a FieldChecker with a null FieldDeclaration");
        }
        this.fd = fd;
    }

    /**
	 * Return true if this field contains an initialization expression.
	 * 
	 * @return true if this field contains an initialization expression, false
	 *         otherwise
	 */
    public boolean isInitialized() {
        return getFieldInitializer(this.fd) != null;
    }

    /**
	 * Return true if this field type is equal to {@code expectedType} fully
	 * qualified type name.
	 * 
	 * @param expectedType
	 *            Expected fully qualified type name
	 * @return true if the field {@code fieldName} type is {@code fieldType},
	 *         false otherwise
	 */
    public boolean isTyped(String expectedType) {
        return areTypesEquals(this.fd.getType(), expectedType);
    }

    /**
	 * Return true if this field is transient.
	 * 
	 * @return true if this field is transient, false otherwise
	 */
    public boolean isTransient() {
        return Modifier.isTransient(this.fd.getModifiers());
    }
}
