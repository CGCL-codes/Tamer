package org.mili.ejb;

/**
 * This class defines an interface to delegate methods to a protable remote object.
 *
 * @author Michael Lieshoff
 */
public interface IPortableRemoteObject {

    /**
     * Narrow.
     *
     * @param narrowFrom the narrow from
     * @param narrowTo the narrow to
     * @return the object
     * @throws ClassCastException the class cast exception
     */
    Object narrow(Object narrowFrom, Class<?> narrowTo) throws ClassCastException;
}
