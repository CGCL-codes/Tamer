package engine.distribution.master;

import java.util.UUID;

/**
 * Generator for slave IDs.
 * @author Karol Stosiek (karol.stosiek@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class SlaveIdGenerator {

    /**
   * Generates slave ID.
   * @param slaveName Name of the slave to generate ID for.
   * @return ID for the slave.
   */
    public static String generateId(String slaveName) {
        return slaveName + "#" + UUID.randomUUID();
    }
}
