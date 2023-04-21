package net.sf.isolation.sql;

import java.sql.SQLException;
import java.sql.Statement;
import net.sf.isolation.core.IsoInformation;

/**
 * 
 * <dl>
 * <dt><b>Thread Safe:</b></dt>
 * <dd>See implementation</dd>
 * </dl>
 * 
 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
 * @since 1.0
 * @see Statement
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 247 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Branches/dev_20100401/Isolation/src/main/java/net/sf/isolation/sql/IsoSQLException.java $", lastChangedDate = "$LastChangedDate: 2010-08-24 17:52:11 -0400 (Tue, 24 Aug 2010) $")
public class IsoSQLException extends RuntimeException {

    private static final long serialVersionUID = 2016220837348983765L;

    public IsoSQLException(SQLException cause) {
        super(cause);
    }
}
