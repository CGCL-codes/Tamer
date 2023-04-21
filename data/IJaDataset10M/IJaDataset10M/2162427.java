package org.atricore.idbus.kernel.main.provisioning.spi.response;

import org.atricore.idbus.kernel.main.provisioning.domain.User;

/**
 * User: eugenia
 * Date: 05-nov-2009
 * Time: 10:22:02
 * email: erocha@atricore.org
 */
public class UpdateUserPasswordResponse extends AbstractProvisioningResponse {

    private User user;

    private Boolean originalPasswordInvalid;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getOriginalPasswordInvalid() {
        return originalPasswordInvalid;
    }

    public void setOriginalPasswordInvalid(Boolean originalPasswordInvalid) {
        this.originalPasswordInvalid = originalPasswordInvalid;
    }
}
