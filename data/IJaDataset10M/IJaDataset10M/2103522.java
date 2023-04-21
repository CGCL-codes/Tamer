package org.ldaptive.provider.unboundid;

import java.util.Arrays;
import javax.net.SocketFactory;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import org.ldaptive.ResultCode;
import org.ldaptive.provider.ControlProcessor;
import org.ldaptive.provider.ProviderConfig;

/**
 * Contains configuration data for the UnboundID provider.
 *
 * @author  Middleware Services
 * @version  $Revision: 2357 $ $Date: 2012-04-13 15:38:21 -0400 (Fri, 13 Apr 2012) $
 */
public class UnboundIDProviderConfig extends ProviderConfig {

    /** Connection options. */
    private LDAPConnectionOptions connectionOptions;

    /** socket factory for ldap connections. */
    private SocketFactory socketFactory;

    /** Search result codes to ignore. */
    private ResultCode[] searchIgnoreResultCodes;

    /** Unbound id specific control processor. */
    private ControlProcessor<Control> controlProcessor;

    /** Default constructor. */
    public UnboundIDProviderConfig() {
        setOperationRetryResultCodes(new ResultCode[] { ResultCode.LDAP_TIMEOUT, ResultCode.CONNECT_ERROR });
        searchIgnoreResultCodes = new ResultCode[] { ResultCode.TIME_LIMIT_EXCEEDED, ResultCode.SIZE_LIMIT_EXCEEDED };
        controlProcessor = new ControlProcessor<Control>(new UnboundIDControlHandler());
    }

    /**
   * Returns the connection options.
   *
   * @return ldap connection options
   */
    public LDAPConnectionOptions getConnectionOptions() {
        return connectionOptions;
    }

    /**
   * Sets the connection options.
   *
   * @param  options  ldap connection options
   */
    public void setConnectionOptions(final LDAPConnectionOptions options) {
        connectionOptions = options;
    }

    /**
   * Returns the socket factory to use for LDAP connections.
   *
   * @return  socket factory
   */
    public SocketFactory getSocketFactory() {
        return socketFactory;
    }

    /**
   * Sets the socket factory to use for LDAP connections.
   *
   * @param  sf  socket factory
   */
    public void setSocketFactory(final SocketFactory sf) {
        checkImmutable();
        logger.trace("setting socketFactory: {}", sf);
        socketFactory = sf;
    }

    /**
   * Returns the search ignore result codes.
   *
   * @return  result codes to ignore
   */
    public ResultCode[] getSearchIgnoreResultCodes() {
        return searchIgnoreResultCodes;
    }

    /**
   * Sets the search ignore result codes.
   *
   * @param  codes  to ignore
   */
    public void setSearchIgnoreResultCodes(final ResultCode[] codes) {
        checkImmutable();
        logger.trace("setting searchIgnoreResultCodes: {}", Arrays.toString(codes));
        searchIgnoreResultCodes = codes;
    }

    /**
   * Returns the control processor.
   *
   * @return  control processor
   */
    public ControlProcessor<Control> getControlProcessor() {
        return controlProcessor;
    }

    /**
   * Sets the control processor.
   *
   * @param  processor  control processor
   */
    public void setControlProcessor(final ControlProcessor<Control> processor) {
        checkImmutable();
        logger.trace("setting controlProcessor: {}", processor);
        controlProcessor = processor;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[%s@%d::operationRetryResultCodes=%s, properties=%s, " + "connectionStrategy=%s, connectionOptions=%s, socketFactory=%s, " + "searchIgnoreResultCodes=%s, controlProcessor=%s]", getClass().getName(), hashCode(), Arrays.toString(getOperationRetryResultCodes()), getProperties(), getConnectionStrategy(), connectionOptions, socketFactory, Arrays.toString(searchIgnoreResultCodes), controlProcessor);
    }
}
