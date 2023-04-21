package ise.dbconsole.command;

import ise.dbconsole.*;

/**
 * @author Dale Anson, Sep 2004
 * @version   $Revision: 7 $
 */
public class CopyrightCommand extends Command {

    private String LS = System.getProperty("line.separator");

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
        sb.append(Bundle.BUNDLE.getString("appTitle")).append(" ");
        sb.append(Bundle.BUNDLE.getString("appVersion"));
        sb.append(LS).append(LS);
        sb.append(Bundle.BUNDLE.getString("appCopyright"));
        sb.append(LS).append(LS);
        sb.append(Bundle.BUNDLE.getString("apacheBlurb"));
        return sb.toString();
    }

    public boolean showSpinner() {
        return false;
    }
}
