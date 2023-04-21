package net.sf.isolation.io;

import java.io.IOException;
import net.sf.isolation.core.IsoInformation;

/**
 * <p>
 * </p>
 * 
 * <dl>
 * <dt><b>Thread Safe:</b></dt>
 * <dd>See implementation</dd>
 * </dl>
 * 
 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
 * @since 1.0
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 306 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Branches/dev_20100401/Isolation/src/main/java/net/sf/isolation/io/IsoReaderManager.java $", lastChangedDate = "$LastChangedDate: 2011-02-23 13:47:43 -0500 (Wed, 23 Feb 2011) $")
public interface IsoReaderManager {

    <T> T openReader(Class<T> type, String nombre, Object paramCreation, Object paramHandle) throws IOException;
}
