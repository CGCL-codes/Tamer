package goldengate.ftp.core.logging;

/**
 * Based on the Netty InternalLoggerFactory
 * Based on The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 * @author frederic
 * goldengate.ftp.core.logging FtpInternalLoggerFactory
 * 
 */
public abstract class FtpInternalLoggerFactory extends org.jboss.netty.logging.InternalLoggerFactory {

    /**
	 * 
	 * @param clazz
	 * @return the FtpInternalLogger
	 */
    public static FtpInternalLogger getLogger(Class<?> clazz) {
        final FtpInternalLogger logger = (FtpInternalLogger) getDefaultFactory().newInstance(clazz.getName());
        return logger;
    }
}
