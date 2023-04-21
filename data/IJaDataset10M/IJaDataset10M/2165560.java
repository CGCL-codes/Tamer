package org.zeroexchange.model.user;

import javax.persistence.Entity;
import org.zeroexchange.model.StringPKPersistent;

/**
 * @author black
 *
 */
@Entity
public class Role extends StringPKPersistent {

    public static final String ACEGITOKEN_USER = "ROLE_USER";

    public static final String ACEGITOKEN_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ROLE_USER = "USER";

    public static String[] acegiToken(String roleUser) {
        return null;
    }
}
