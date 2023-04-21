package test.javax.management.remote.compliance.serialization;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import test.javax.management.compliance.serialization.support.SerializationVerifier;
import test.javax.management.remote.compliance.OptionalRemoteJMXComplianceTestCase;

/**
 * @version $Revision: 1.5 $
 */
public class OptionalRemoteJMXSerializationTest extends OptionalRemoteJMXComplianceTestCase {

    public OptionalRemoteJMXSerializationTest(String s) {
        super(s);
    }

    protected boolean skipClassName(String className) {
        return true;
    }

    protected boolean skipClass(Class cls) {
        if (cls.isInterface() || !Serializable.class.isAssignableFrom(cls) || Modifier.isAbstract(cls.getModifiers())) return true;
        return false;
    }

    protected void checkCompliance(String className) throws Exception {
        ClassLoader jmxriLoader = createOptionalRemoteJMXRIWithTestsClassLoader();
        ClassLoader mx4jLoader = createRemoteMX4JWithTestsClassLoader();
        SerializationVerifier verifier = new SerializationVerifier("test.javax.management.remote.compliance.serialization.support.OptionalRemoteInstantiator", "test.javax.management.remote.compliance.serialization.support.OptionalRemoteComparator");
        verifier.verifySerialization(className, jmxriLoader, mx4jLoader);
    }
}
