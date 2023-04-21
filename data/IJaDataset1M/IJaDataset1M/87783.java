package xpg.server;

/**
 *
 * @author  landgraf
 * @version
 */
interface UserCertificate {

    User getUser();

    KeyCertificate getKeyCertificate();

    KeyCertificate[] getSubKeys();
}
