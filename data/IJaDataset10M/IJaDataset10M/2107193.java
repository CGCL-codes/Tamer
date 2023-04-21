package org.fenggui.util;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface ILogSystem {

    public void error(String error, Object... vars);

    public void error(String error, Throwable ex, Object... vars);

    public void warn(String warn, Object... vars);

    public void debug(String debug, Object... vars);
}
