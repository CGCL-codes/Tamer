package uips.events;

import java.util.Map;

/**
 * Instance of this class is delivered ot event handler, holds map of
 * properties sent in event from client.
 * <br><br>
 * Based on Miroslav Macik's C# version of UIProtocolServer
 *
 * @author Miroslav Macik (macikm1@fel.cvut.cz, CTU Prague, FEE)
 * @author Jindrich Basek, (basekjin@fel.cvut.cz, CTU Prague, FEE)
 */
public interface IEvent {

    /**
     * Returns map of properties sent in event from client
     *
     * @return Map of properties sent in event from client
     */
    public Map<String, String> Properties();

    /**
     * Converts given paramteres to Map<String, String> representing model
     * variant values
     *
     * @param params model variant values in order property name,
     * property value, property name, property value, ...
     * @return Map<String, String> representing model variant values
     */
    public Map<String, String> prepareVariantMap(String... params);
}
