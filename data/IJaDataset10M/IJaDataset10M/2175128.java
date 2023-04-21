package org.broadleafcommerce.profile.email.domain;

import java.io.Serializable;

/**
 * The EmailTarget interface is used to specify the recipients of the email.
 *
 * @see EmailTargetImpl
 * @author bpolster
 */
public interface EmailTarget extends Serializable {

    public String getEmailAddress();

    public void setEmailAddress(String emailAddress);

    public String[] getCCAddresses();

    public void setCCAddresses(String[] ccAddresses);

    public String[] getBCCAddresses();

    public void setBCCAddresses(String[] BCCAddresses);
}
