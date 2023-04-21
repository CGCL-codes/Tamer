package issrg.utils.repository;

import issrg.pba.PbaException;
import issrg.pba.rbac.CustomisePERMIS;
import issrg.utils.webdav.WebDAVSocketHTTPS;
import issrg.utils.ToRawCredential;
import issrg.saml.SAMLSecurity;
import java.net.URL;
import java.security.Principal;
import javax.naming.directory.*;
import org.apache.log4j.*;

public class WebDAVRepository extends DefaultRepository {

    private static Logger logger = Logger.getLogger(WebDAVRepository.class);

    private final String host;

    private final int port;

    private final String p12filename, p12passphrase;

    private final boolean useSSL;

    public WebDAVRepository(String host, int port) {
        this.host = issrg.utils.WebdavUtil.getHostFromURL(host);
        this.port = port;
        this.p12filename = "";
        this.p12passphrase = "";
        this.useSSL = false;
    }

    public WebDAVRepository(String host, int port, String p12filename, String p12passphrase) {
        this.host = issrg.utils.WebdavUtil.getHostFromURL(host);
        this.port = port;
        this.p12filename = p12filename;
        this.p12passphrase = p12passphrase;
        this.useSSL = true;
    }

    public WebDAVRepository(String host, int port, SAMLSecurity security) {
        this(host, port, security.callbackInfo(security.KEYSTORE), security.callbackInfo(security.PASSWORD));
    }

    public Attributes getAttributes(Principal DN, String[] attrNames) throws RepositoryException {
        Attributes attrs = getAllAttributes(DN);
        Attributes newAttrs = new BasicAttributes();
        if (attrs.size() != 0) {
            for (int i = 0; i < attrNames.length; i++) {
                newAttrs.put(attrs.get(attrNames[i]));
            }
        }
        return newAttrs;
    }

    public Attributes getAllAttributes(Principal DN) {
        Attributes attributes;
        if (useSSL == false) attributes = issrg.utils.WebdavUtil.getACs(DN, host, port); else attributes = issrg.utils.WebdavUtil.getACs(DN, host, port, p12filename, p12passphrase);
        ToRawCredential toRaw = new ToRawCredential(attributes, this.getClass().getName());
        try {
            attributes = toRaw.convert();
        } catch (RepositoryException re) {
            return null;
        }
        return (Attributes) attributes.clone();
    }

    public Throwable getDiagnosis() {
        return null;
    }

    public int getStatus() {
        return 0;
    }
}
