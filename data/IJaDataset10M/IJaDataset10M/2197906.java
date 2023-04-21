package com.ail.core.addressbook;

import java.util.ArrayList;
import java.util.List;
import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;

@TypeDefinition
public class AddressBook extends Type {

    private List<Party> party = new ArrayList<Party>();

    private Party me;

    public List<Party> getParty() {
        return party;
    }

    public void setParty(List<Party> party) {
        this.party = party;
    }

    public Party getMe() {
        return me;
    }

    public void setMe(Party me) {
        this.me = me;
    }
}
