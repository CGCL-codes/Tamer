package ise.dbconsole.command;

import ise.dbconsole.*;

/**
 * @author Dale Anson, Sep 2004
 * @version   $Revision: 7 $
 */
public class VersionCommand extends Command implements Constants {

    /** Description of the Method */
    public String execute() {
        return execute(null);
    }

    /**
     * Description of the Method
     *
     * @param data
     */
    public String execute(Object data) {
        StringBuffer sb = new StringBuffer();
        sb.append(LS);
        sb.append(Bundle.BUNDLE.getString("appTitle")).append(" ").append(Bundle.BUNDLE.getString("appVersion")).append(LS);
        return sb.toString();
    }

    public boolean showSpinner() {
        return false;
    }
}
