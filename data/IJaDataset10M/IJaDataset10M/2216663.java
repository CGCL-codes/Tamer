package org.uxd.domain;

/** */
public class TelephoneRole {

    /** Role descriptor for this party telephone
	 */
    private DescriptorLeaf role = null;

    /** Party (organization or person)for this telephone
	 */
    private PartyComponent party = null;

    /** Telephone object for this association
	 */
    private Telephone telephone = null;

    /** Empty constructor for this object
	 */
    public TelephoneRole() {
    }

    public DescriptorLeaf getRole() {
        return role;
    }

    public void setRole(DescriptorLeaf role) {
        this.role = role;
    }

    public PartyComponent getParty() {
        return party;
    }

    public void setParty(PartyComponent party) {
        this.party = party;
    }

    public Telephone getTelephone() {
        return telephone;
    }

    public void setTelephone(Telephone telephone) {
        this.telephone = telephone;
    }
}
