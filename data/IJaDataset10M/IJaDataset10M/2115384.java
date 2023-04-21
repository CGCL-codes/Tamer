package net.kortsoft.gameportlet.model.impl;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Configurable;
import net.kortsoft.gameportlet.model.Player;
import net.kortsoft.gameportlet.model.PlayerId;
import net.kortsoft.gameportlet.model.Room;
import net.kortsoft.gameportlet.model.User;
import net.kortsoft.gameportlet.service.UserFactory;

@Configurable("playerImpl")
public class PlayerImpl implements Player {

    private Long internalId;

    private long externalUserId;

    protected String teamColour;

    protected Room room;

    private UserFactory userFactory;

    public PlayerImpl() {
        super();
    }

    public PlayerImpl(long externalUserId, Room room, String teamColour) {
        super();
        this.externalUserId = externalUserId;
        this.room = room;
        this.teamColour = teamColour;
    }

    @Override
    public PlayerId getId() {
        return internalId != null ? new PlayerIdLong() : null;
    }

    public PlayerId getPlayerId() {
        return getId();
    }

    public void setExternalUserId(long externalUserId) {
        this.externalUserId = externalUserId;
    }

    @Override
    public String getTeamColour() {
        return this.teamColour;
    }

    public void setTeamColour(String teamColour) {
        this.teamColour = teamColour;
    }

    @Override
    public User getUser() {
        return getUserFactory().fromExternalId(externalUserId);
    }

    private UserFactory getUserFactory() {
        return userFactory;
    }

    public void setUserFactory(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlayerImpl) {
            PlayerImpl player = (PlayerImpl) obj;
            return internalId == player.internalId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (internalId != null) return internalId.hashCode(); else return super.hashCode();
    }

    private final class PlayerIdLong implements PlayerId {

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PlayerId) {
                PlayerId other = (PlayerId) obj;
                return internalId.equals(((Long) other.value()).intValue());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return internalId.hashCode();
        }

        @Override
        public String toString() {
            return String.valueOf(internalId);
        }

        @Override
        public Serializable value() {
            return internalId;
        }
    }
}
