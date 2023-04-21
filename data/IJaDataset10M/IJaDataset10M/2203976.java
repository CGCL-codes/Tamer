package net.taylor.security.encryption;

import java.security.SecureRandom;

/**
 * @author czhao
 *
 */
public interface SecureRandomFactory {

    String COMPONENT_NAME = "net.taylor.security.encryption.SecureRandomFactory";

    public SecureRandom getSecureRandom();
}
