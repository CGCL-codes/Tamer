package net.sf.amemailchecker.mail.impl.extension;

public class SMTPExtensionBean extends ExtensionBean {

    public boolean isAuthTypeSupported(String authType) {
        return isExtensionSupported("AUTH") && getExtensionValue("AUTH").indexOf(authType) != -1;
    }
}
