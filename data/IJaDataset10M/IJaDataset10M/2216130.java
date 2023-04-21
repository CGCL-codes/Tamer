package org.nakedobject.app.cart.services;

import java.util.List;
import org.nakedobject.app.cart.Claim;
import org.nakedobject.app.cart.Employee;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Named;

public class ClaimRepository extends AbstractFactoryAndRepository {

    public String getId() {
        return "claims";
    }

    public List<Claim> allClaims() {
        return allInstances(Claim.class);
    }

    public List<Claim> findClaims(@Named("Description") String description) {
        return allMatches(Claim.class, description);
    }

    public List<Claim> claimsFor(@Named("Claimant") Employee claimant) {
        Claim pattern = newTransientInstance(Claim.class);
        pattern.setStatus(null);
        pattern.setClaimant(claimant);
        return allMatches(Claim.class, pattern);
    }
}
