package org.powermock.core.transformers;

import javassist.CtClass;

/**
 * Interface that all mock transformers must implement. The purpose of a mock
 * transformer is to create a modified version of a <code>Class</code> so that
 * it is mock enabled.
 * 
 * @author Johan Haleby
 */
public interface MockTransformer {

    /**
	 * Transforms the <code>Class</code> with name
	 * <code>fullyQualifiedName</code>.
	 * 
	 * @param fullyQualifiedName
	 *            The fully qualified name of the <code>Class</code> to
	 *            transform into a mock enabled class.
	 * @return A <code>CtClass</code> representation of the mocked class.
	 */
    public CtClass transform(CtClass clazz) throws Exception;
}
