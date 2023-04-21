package jaxlib.security.action;

import java.io.Serializable;
import java.security.PrivilegedAction;

/**
 * Privilged action which delivers a system property.
 *
 * @see System#getProperty(String)
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: GetPropertyAction.java,v 1.1.1.1 2004/08/02 20:56:17 joerg_wassmer Exp $
 */
public class GetPropertyAction extends Object implements PrivilegedAction<String>, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    private String name;

    public GetPropertyAction(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String run() {
        return System.getProperty(this.name);
    }
}
