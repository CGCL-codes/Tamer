package org.eclipse.jdt.core.util;

/**
 * The class represents an entry in the exception table of a ICodeAttribute as 
 * specified in the JVM specifications.
 * 
 * This interface may be implemented by clients. 
 * 
 * @since 2.0
 */
public interface IExceptionTableEntry {

    /**
	 * Answer back the start pc of this entry.
	 * 
	 * @return the start pc of this entry
	 */
    int getStartPC();

    /**
	 * Answer back the end pc of this entry.
	 * 
	 * @return the end pc of this entry
	 */
    int getEndPC();

    /**
	 * Answer back the handler pc of this entry.
	 * 
	 * @return the handler pc of this entry
	 */
    int getHandlerPC();

    /**
	 * Answer back the catch type index in the constant pool.
	 * 
	 * @return the catch type index in the constant pool
	 */
    int getCatchTypeIndex();

    /**
	 * Answer back the catch type name, null if getCatchTypeIndex() returns 0.
	 * This is the case for any exception handler.
	 * 
	 * @return the catch type name, null if getCatchTypeIndex() returns 0.
	 * This is the case for any exception handler
	 */
    char[] getCatchType();
}
