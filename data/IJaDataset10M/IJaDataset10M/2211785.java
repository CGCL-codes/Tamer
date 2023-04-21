package pos.utils;

import java.security.SecureRandom;
import java.sql.Date;

public class UIDGenerator {

    private static UIDGenerator guidgen;

    private SecureRandom random;

    private UIDGenerator() {
        this.random = new SecureRandom();
    }

    public static synchronized UIDGenerator getInstance() {
        if (guidgen == null) {
            guidgen = new UIDGenerator();
        }
        return guidgen;
    }

    public String getKey() {
        String key = "" + System.currentTimeMillis() + Long.toHexString(random.nextInt());
        return key;
    }
}
