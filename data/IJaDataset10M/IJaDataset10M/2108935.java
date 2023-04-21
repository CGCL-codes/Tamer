package dinamica;

import java.util.Locale;
import dinamica.GenericTransaction;
import dinamica.Recordset;

/**
 * Receives a request parameter that MUST be named "locale"
 * and set a session attribute named "dinamica.user.locale"
 * containing a Locale object initialized with the language
 * code (en, es, it, etc). This can be used to change the language in interactive mode.
 * <br><br>
 * Creation date: 10/05/2005
 * (c) 2005 Martin Cordova<br>
 * This code is released under the LGPL license<br>
 * Dinamica Framework - http://www.martincordova.com<br>
 * @author Martin Cordova (dinamica@martincordova.com)
 */
public class SetLanguage extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable {
        Locale l = new Locale(inputParams.getString("locale"));
        getSession().setAttribute("dinamica.user.locale", l);
        super.service(inputParams);
        return 0;
    }
}
