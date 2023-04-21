package org.starobjects.tested.fitnesse.testapp.dom.claim;

import java.util.List;
import org.nakedobjects.applib.annotation.Named;

@Named("Claims")
public interface ClaimRepository {

    public List<Claim> allClaims();

    public List<Claim> findClaims(@Named("Description") String description);

    public List<Claim> claimsFor(Claimant claimant);

    public Claim newClaim(Claimant claimant);

    @Named("New Claim")
    public Claim newClaimWithDescription(Claimant claimant, String description);
}
