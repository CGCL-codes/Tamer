package pl.tyszka.rolemanager.security;

import java.security.BasicPermission;

/**
 * 
 * @author  <a href="mailto:daras@users.sourceforge.net">Dariusz Tyszka</a>
 * @version $Revision: 1.4 $
 * 
 * @since 1.0
 */
public class AccessPermission extends BasicPermission {

    public AccessPermission(String name) {
        super(name);
    }

    public AccessPermission(String name, String actions) {
        super(name, actions);
    }
}
