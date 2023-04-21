package net.sourceforge.osgi.deployment.maven.manifest;

/**
 * An {@link ManifestException} is thrown if any error occures while processing the input data from the {@link IDeploymentPluginContext} to generate the
 * manifest file.
 * 
 * @author Siamak Haschemi, haschemi@informatik.hu-berlin.de
 * 
 */
public final class ManifestException extends RuntimeException {

    private static final long serialVersionUID = -5665247031595519354L;

    /**
   * Constructor which creates an Exception chain with a message and a {@link Throwable} as the cause of this exception.
   * 
   * @param p_message
   *          the message of this exception
   * @param p_cause
   *          the cause of this exception
   */
    public ManifestException(final String p_message, final Throwable p_cause) {
        super(p_message, p_cause);
    }

    /**
   * Constructor which creates an Exception chain with a {@link Throwable} as the cause of this exception. No message is provided.
   * 
   * @param p_cause
   *          p_cause the cause of this exception
   */
    public ManifestException(final Throwable p_cause) {
        super(p_cause);
    }

    /**
   * Constructor which creates an Exception with no original cause ({@link Throwable}) . Only a message is provided.
   * 
   * @param p_message
   *          the message of this exception
   */
    public ManifestException(final String p_message) {
        super(p_message);
    }
}
