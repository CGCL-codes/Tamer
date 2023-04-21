package jdbm;

import java.io.IOException;

/**
 * Enumeration interface for persistent object traversal.
 *
 * Same concept as <code>java.util.Enumeration</code>, but throws
 * <code>java.io.IOException</code>.
 *
 * @author <a href="mailto:boisvert@intalio.com">Alex Boisvert</a>
 * @version $Id: JDBMEnumeration.java,v 1.2 2001/06/16 07:19:09 arimot Exp $
 */
public interface JDBMEnumeration {

    public Object nextElement() throws IOException;

    public boolean hasMoreElements() throws IOException;
}
