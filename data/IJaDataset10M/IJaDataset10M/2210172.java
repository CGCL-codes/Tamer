package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec;

import java.util.Set;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.TwixDecoder;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.TwixEncoder;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.Provider;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.ProviderDecoder;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.ProviderEncoder;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.ProviderException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.TransformerSpi;

/**
 * The Twix specific BER provider for LDAP.
 * 
 * @author <a href="mailto:dev@directory.apache.org"> Apache Directory Project</a>
 *         $Rev: 437007 $
 */
public class TwixProvider extends Provider {

    /** The Transformer for this provider */
    private final TwixTransformer transformer;

    /**
     * Creates an instance of a Twix based LDAP BER Provider.
     */
    private TwixProvider() {
        super("Twix LDAP BER Provider", "Apache Directory Project");
        transformer = new TwixTransformer(this);
    }

    /** the singleton TwixProvider instance */
    private static TwixProvider singleton;

    /**
     * Gets a handle on the singleton TwixProvider. Only one instance should
     * have to be instantiated for the entire jvm.
     * 
     * @return the singleton SnaccProvider instance
     */
    public static synchronized Provider getProvider() {
        if (singleton == null) {
            singleton = new TwixProvider();
        }
        return singleton;
    }

    /**
     * Gets the encoder associated with this provider.
     * 
     * @return the provider's encoder.
     * @throws net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.ProviderException
     *             if the provider or its encoder cannot be found
     */
    public ProviderEncoder getEncoder() throws ProviderException {
        return new TwixEncoder(this);
    }

    /**
     * Gets the decoder associated with this provider.
     * 
     * @return the provider's decoder.
     * @throws net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.ProviderException
     *             if the provider or its decoder cannot be found
     */
    public ProviderDecoder getDecoder(Set binaries) throws ProviderException {
        return new TwixDecoder(this, binaries);
    }

    /**
     * Gets the transformer associated with this provider.
     * 
     * @return the provider's transformer.
     * @throws net.sourceforge.myvd.protocol.ldap.mina.ldap.message.spi.ProviderException
     *             if the provider or its transformer cannot be found
     */
    public TransformerSpi getTransformer() throws ProviderException {
        return transformer;
    }
}
