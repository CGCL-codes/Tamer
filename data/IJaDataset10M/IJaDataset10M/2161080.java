package de.meylirh.pwkit.component.charManager.impl;

import java.util.Date;
import java.util.Set;
import de.meylirh.pwkit.component.charManager.NWNCDKey;
import de.meylirh.pwkit.component.charManager.NWNCharacter;
import de.meylirh.pwkit.component.charManager.NWNNick;
import de.meylirh.pwkit.component.charManager.domain.DBChar;
import de.meylirh.pwkit.domain.CharName;

public class NWNCharacterImpl implements NWNCharacter {

    private CharManagerService service;

    private CharManagerImpl manager;

    private Date created;

    private int id;

    private CharName name;

    private NWNNick nick;

    public NWNCharacterImpl(DBChar c, CharManagerService service, CharManagerImpl manager) {
        this.service = service;
        this.manager = manager;
        created = c.getCreateTimeStamp();
        id = c.getId();
        name = new CharName(c.getFullName());
        nick = new NWNNickImpl(c.getNick(), service, manager);
    }

    public Set<NWNCDKey> getCDKeys() {
        return service.retrieveCDKeysForNick(nick.getID());
    }

    public Date getCreated() {
        return created;
    }

    public int getCurrentNWNCharID() {
        return manager.getCurrentNWNCharID(id);
    }

    public int getID() {
        return id;
    }

    public CharName getName() {
        return name;
    }

    public NWNNick getNick() {
        return nick;
    }

    public boolean isOnline() {
        return getCurrentNWNCharID() != -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NWNCharacter)) {
            return false;
        }
        return getName().equals(((NWNCharacter) obj).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "<NWNCharacter id=" + getID() + " name=" + getName() + " nick=" + getNick().getNick() + " craeted=" + getCreated() + ">";
    }
}
