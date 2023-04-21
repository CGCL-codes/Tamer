package si.housing.javaadv.reflect;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class TestReflection {

    public static void main(String[] args) throws Exception {
    }

    protected static void enableSecurity() {
        SecurityManager sm = new SecurityManager();
        System.setSecurityManager(sm);
    }

    protected static void disableSecurity() {
        System.setSecurityManager(null);
    }
}
