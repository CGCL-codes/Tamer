package testing.jira;

import java.util.List;
import com.atlassian.core.ofbiz.osuser.CoreOFBizCredentialsProvider;

public class TestCredentialsProvider extends CoreOFBizCredentialsProvider {

    /**
	 * Auto-generated serial version UID.
	 */
    private static final long serialVersionUID = 1L;

    public TestCredentialsProvider() {
        System.out.println("\t" + this + ": Constructor");
    }

    public boolean authenticate(String name, String password) {
        System.out.println("\t" + this + ": authenticate(" + name + "," + password + ")");
        boolean result = super.authenticate(name, password);
        return result;
    }

    public boolean changePassword(String name, String password) {
        System.out.println("\t" + this + ": changePassword(" + name + "," + password + ")");
        boolean result = super.changePassword(name, password);
        return result;
    }

    public boolean create(String name) {
        System.out.println("\t" + this + ": create(" + name + ")");
        boolean result = super.create(name);
        return result;
    }

    public boolean handles(String name) {
        System.out.println("\t" + this + ": handles(" + name + ")");
        boolean result = super.handles(name);
        return result;
    }

    public List<?> list() {
        System.out.println("\t" + this + ": list()");
        List<?> result = super.list();
        return result;
    }

    public boolean load(String name, com.opensymphony.user.Entity.Accessor accessor) {
        System.out.println("\t" + this + ": load(" + name + "," + accessor + ")");
        boolean result = super.load(name, accessor);
        System.out.println(accessor.getEntity().getProfileProvider().getPropertySet("default"));
        return result;
    }

    public boolean remove(String name) {
        System.out.println("\t" + this + ": remove(" + name + ")");
        boolean result = super.remove(name);
        return result;
    }
}
