package net.cimd.packets.parameters;

/**
 * @author <a href="mailto:dpoleakovski@gmail.com">Dmitri Poleakovski</a>
 * @version $Revision: 1.1 $ $Date: 2007/03/14 14:17:06 $
 */
public class StringParamRestriction extends ParamRestriction {

    protected boolean isAllowedChar(char ch) {
        return ch >= ' ' && ch <= '~';
    }
}
