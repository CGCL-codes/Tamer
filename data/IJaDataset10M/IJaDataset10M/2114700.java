package org.fao.fenix.domain.security;

import org.fao.fenix.domain.perspective.FenixDomainUser;

public class FenixDoubleUser {

    FenixSecuredUser fenixSecuredUser;

    FenixDomainUser fenixDomainUser;

    public FenixSecuredUser getFenixSecuredUser() {
        return fenixSecuredUser;
    }

    public void setFenixSecuredUser(FenixSecuredUser fenixSecuredUser) {
        this.fenixSecuredUser = fenixSecuredUser;
    }

    public FenixDomainUser getFenixDomainUser() {
        return fenixDomainUser;
    }

    public void setFenixDomainUser(FenixDomainUser fenixDomainUser) {
        this.fenixDomainUser = fenixDomainUser;
    }
}
