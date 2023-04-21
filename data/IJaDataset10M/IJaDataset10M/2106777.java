package org.columba.mail.config;

import java.io.File;
import java.util.Observer;
import org.columba.core.xml.XmlElement;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.parser.ParserException;

/**
 * Encapsulates an identity used with an account.
 */
public class Identity {

    private static final String SIGNATURE_FILE = "signature_file";

    private static final String ORGANISATION = "organisation";

    private static final String REPLY_ADDRESS = "reply_address";

    private static final String NAME = "name";

    private static final String ADDRESS = "address";

    protected XmlElement e;

    protected Address address;

    protected Address replyToAddress;

    public Identity(XmlElement e) throws ParserException {
        this.e = e;
        String value = e.getAttribute(Identity.ADDRESS);
        if (value != null && value.length() > 0) {
            address = Address.parse(e.getAttribute(Identity.ADDRESS));
            address.setDisplayName(e.getAttribute(Identity.NAME));
        }
        value = e.getAttribute(Identity.REPLY_ADDRESS);
        if (value != null && value.length() > 0) {
            replyToAddress = Address.parse(value);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        e.addAttribute(Identity.NAME, address.getDisplayName());
        e.addAttribute(Identity.ADDRESS, address.getMailAddress());
    }

    public Address getReplyToAddress() {
        return replyToAddress;
    }

    public void setReplyToAddress(Address address) {
        replyToAddress = address;
        if (address != null) {
            e.addAttribute(Identity.REPLY_ADDRESS, address.getMailAddress());
        } else {
            e.getAttributes().remove(Identity.REPLY_ADDRESS);
        }
    }

    public String getOrganisation() {
        return e.getAttribute(Identity.ORGANISATION);
    }

    public void setOrganisation(String organisation) {
        if (organisation != null) {
            e.addAttribute(Identity.ORGANISATION, organisation);
        } else {
            e.getAttributes().remove(Identity.ORGANISATION);
        }
    }

    /**
     * Returns the signature that should be attached to outgoing mails.
     */
    public File getSignature() {
        String path = e.getAttribute(Identity.SIGNATURE_FILE);
        if (path != null) {
            File signature = new File(path);
            if (signature.exists() && signature.isFile()) {
                return signature;
            }
        }
        return null;
    }

    /**
     * Sets the signature to be attached to outgoing mails.
     */
    public void setSignature(File signature) {
        if (signature != null && signature.exists() && signature.isFile()) {
            e.addAttribute(Identity.SIGNATURE_FILE, signature.getPath());
        } else {
            e.getAttributes().remove(Identity.SIGNATURE_FILE);
        }
        e.notifyObservers();
    }

    public void addObserver(Observer observer) {
        e.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        e.deleteObserver(observer);
    }
}
