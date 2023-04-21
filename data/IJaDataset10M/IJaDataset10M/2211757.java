package org.opennms.netmgt.archive;

/**
 * <P>
 * The ArchiverException is thrown by the database archvier. The message in the
 * exception should provide the reason
 * </P>
 * 
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya Nataraj </A>
 * @author <A HREF="http://www.opennms.org">OpenNMS.org </A>
 */
public class ArchiverException extends Exception {

    private static final long serialVersionUID = 1L;

    public ArchiverException() {
        super();
    }

    public ArchiverException(String s) {
        super(s);
    }
}
