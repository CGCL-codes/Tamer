package org.peertrust.security.credentials.gui;

import org.peertrust.security.credentials.Credential;

/**
 * Dummy for the Editor-GUI.
 */
public class DummyCredential extends Credential {

    public String getStringRepresentation() {
        return "-@-";
    }

    public String getSignerName() {
        return "-";
    }

    protected void initCredential(Object arg) {
    }

    public Object getEncoded() {
        return getSignerName();
    }
}
