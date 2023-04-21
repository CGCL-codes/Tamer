package issrg.pba;

import java.util.Vector;
import issrg.pba.rbac.*;

/**
 * This is the default Parsed Token implementation. It simply contains the
 * Holder, the Issuer, the Credentials of the Holder and the URL to the revoked certificate.
 */
public class DefaultParsedToken implements ParsedToken {

    issrg.utils.repository.Entry holder;

    issrg.utils.repository.TokenLocator issuer;

    Credentials creds;

    boolean noRevAvail;

    protected DefaultParsedToken() {
    }

    /**
   * This is the constructor that builds the Default Parsed Token given the 
   * Holder, the Issuer and the Credentials of the Holder issued to him by the Issuer.
   *
   * @param holder - the Holder entry
   * @param issuer - the Issuer entry 
   * @param creds - the Credentials of the Holder
   * @param revLoc - the location of the revoked certificate
   */
    public DefaultParsedToken(issrg.utils.repository.Entry holder, issrg.utils.repository.TokenLocator issuer, Credentials creds) {
        this(holder, issuer, creds, true);
    }

    public DefaultParsedToken(issrg.utils.repository.Entry holder, issrg.utils.repository.TokenLocator issuer, Credentials creds, boolean revocable) {
        this.holder = holder;
        this.issuer = issuer;
        this.creds = creds;
        noRevAvail = revocable;
    }

    public Credentials getCredentials() {
        return creds;
    }

    public issrg.utils.repository.Entry getHolder() {
        return holder;
    }

    public issrg.utils.repository.TokenLocator getIssuerTokenLocator() {
        return issuer;
    }

    public String toString() {
        return "Authorisation Token issued by " + getIssuerTokenLocator().getEntry().getEntryName().getName() + " to " + getHolder().getEntryName().getName();
    }

    public boolean isRevocable() {
        return noRevAvail;
    }
}
