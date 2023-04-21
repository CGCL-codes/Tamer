package nu.lazy8.ledger.messages;

import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;

/**
 *  Description of the Class
 *
 * @author     thomas
 * @created    den 2 juli 2004
 */
public class AccountListChanged extends EBMessage {

    private String params;

    /**
   *Constructor for the AccountListChanged object
   *
   * @param  source  Description of the Parameter
   * @param  params  Description of the Parameter
   */
    public AccountListChanged(EBComponent source, String params) {
        super(source);
        this.params = params;
    }

    /**
   *  Description of the Method
   *
   * @return    Description of the Return Value
   */
    public java.lang.String paramString() {
        return params;
    }
}
