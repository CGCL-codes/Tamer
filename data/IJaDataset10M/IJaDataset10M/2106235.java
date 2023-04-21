package org.helianto.core.test;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * A base class for service layer integration tests.
 * 
 * @author Mauricio Fernandes de Castro
 */
public abstract class AbstractIntegrationTest extends AbstractTransactionalSpringContextTests {

    /**
     * Generate a not repeatable key.
     */
    public static String generateKey() {
        return String.valueOf(new Date().getTime());
    }

    /**
     * Generate a not repeatable key of a given size.
     */
    public static String generateKey(int size) {
        String localKey = generateKey();
        while (localKey.length() != size) {
            if (localKey.length() > size) {
                localKey = localKey.substring(localKey.length() - size, localKey.length());
            } else if (localKey.length() < size) {
                localKey = localKey.concat(localKey);
            }
        }
        return localKey;
    }

    /**
     * Generate a not repeatable key of a given size.
     */
    public static String generateKey(int size, int index) {
        String localKey = index + "-" + generateKey();
        while (localKey.length() != size) {
            if (localKey.length() > size) {
                localKey = localKey.substring(localKey.length() - size, localKey.length());
            } else if (localKey.length() < size) {
                localKey = localKey.concat(localKey);
            }
        }
        return localKey;
    }

    protected static Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);
}
