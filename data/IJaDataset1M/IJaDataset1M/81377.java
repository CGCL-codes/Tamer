package com.sshtools.j2ssh.transport.publickey;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.16 $
 */
public class SECSHPublicKeyFormat extends Base64EncodedFileFormat implements SshPublicKeyFormat {

    private static String BEGIN = "---- BEGIN SSH2 PUBLIC KEY ----";

    private static String END = "---- END SSH2 PUBLIC KEY ----";

    /**
     * Creates a new SECSHPublicKeyFormat object.
     *
     * @param subject
     * @param comment
     */
    public SECSHPublicKeyFormat(String subject, String comment) {
        super(BEGIN, END);
        setHeaderValue("Subject", subject);
        setComment(comment);
    }

    /**
     * Creates a new SECSHPublicKeyFormat object.
     */
    public SECSHPublicKeyFormat() {
        super(BEGIN, END);
    }

    /**
     *
     *
     * @param comment
     */
    public void setComment(String comment) {
        setHeaderValue("Comment", (comment.trim().startsWith("\"") ? "" : "\"") + comment.trim() + (comment.trim().endsWith("\"") ? "" : "\""));
    }

    /**
     *
     *
     * @return
     */
    public String getComment() {
        return getHeaderValue("Comment");
    }

    /**
     *
     *
     * @return
     */
    public String getFormatType() {
        return "SECSH-PublicKey-" + super.getFormatType();
    }

    /**
     *
     *
     * @param algorithm
     *
     * @return
     */
    public boolean supportsAlgorithm(String algorithm) {
        return SshKeyPairFactory.supportsKey(algorithm);
    }
}
