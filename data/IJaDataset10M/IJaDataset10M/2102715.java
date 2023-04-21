package org.ldaptive.ssl;

import java.security.GeneralSecurityException;

/**
 * Provides a base interface for all credential configurations. Since credential
 * configs are invoked via reflection by the PropertyInvoker their method
 * signatures are not important. They only need to be able to create an SSL
 * context initializer once their properties have been set.
 *
 * @author  Middleware Services
 * @version  $Revision: 2198 $ $Date: 2012-01-04 16:02:09 -0500 (Wed, 04 Jan 2012) $
 */
public interface CredentialConfig {

    /**
   * Creates an SSL context initializer using the configured trust and
   * authentication material in this config.
   *
   * @return  SSL context initializer
   *
   * @throws  GeneralSecurityException  if the ssl context initializer cannot be
   * created
   */
    SSLContextInitializer createSSLContextInitializer() throws GeneralSecurityException;
}
