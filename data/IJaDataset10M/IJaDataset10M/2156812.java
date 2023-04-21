package net.sf.isolation.sql.spi;

import java.io.Serializable;
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
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 168 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Tags/dev_20100521/Isolation/src/net/sf/isolation/sql/spi/IsoSPExpressionParameterInformation.java $", lastChangedDate = "$LastChangedDate: 2010-03-21 02:23:32 -0400 (Sun, 21 Mar 2010) $")
public class IsoSPExpressionParameterInformation implements Serializable {

    private static final long serialVersionUID = 5827387699275423260L;

    private String name;

    private int index;

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
