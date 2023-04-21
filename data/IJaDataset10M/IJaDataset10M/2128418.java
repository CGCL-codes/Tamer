package de.aw.ucop.test.system.businessmodel;

import java.util.HashMap;
import java.util.Map;
import de.aw.ucop.metamodel.DataBean;

public class User extends DataBean {

    private String _name;

    /**
	 * @uml.property   name="_roles"
	 * @uml.associationEnd   qualifier="key:java.lang.Object de.aw.ucop.test.system.businessmodel.Role"
	 */
    private Map<String, Role> _roles;

    public User(String name) {
        _name = name;
        _roles = new HashMap<String, Role>();
    }

    public String get_name() {
        return _name;
    }
}
