package ie.blackoutscout.common.beans.implementations;

import ie.blackoutscout.common.beans.interfaces.IMatchEvent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 */
@Entity
public class MatchEvent implements IMatchEvent {

    @Id
    @GeneratedValue(generator = "id_Gen")
    @SequenceGenerator(name = "id_Gen", sequenceName = "id_Seq")
    private long id;

    @Column(name = "EVENT_ID")
    private Long eventId;

    private Long fixtureId;

    @Column(length = 1000)
    private String commentary;

    private Integer possession;

    private Integer actualTime;

    private Integer lostTime;

    private Integer gameTime;

    private Integer homeScore;

    private Integer guestScore;

    private Integer index;

    private Integer half;

    private Long brTimeStamp;

    public Long getBrTimeStamp() {
        return brTimeStamp;
    }

    public void setBrTimeStamp(Long brTimeStamp) {
        this.brTimeStamp = brTimeStamp;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(Long fixtureId) {
        this.fixtureId = fixtureId;
    }

    public Integer getGameTime() {
        return gameTime;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public Integer getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(Integer guestScore) {
        this.guestScore = guestScore;
    }

    public Integer getHalf() {
        return half;
    }

    public void setHalf(Integer half) {
        this.half = half;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLostTime() {
        return lostTime;
    }

    public void setLostTime(Integer lostTime) {
        this.lostTime = lostTime;
    }

    public Integer getPossession() {
        return possession;
    }

    public void setPossession(Integer possession) {
        this.possession = possession;
    }

    public String getIdString() {
        return "EVENT_ID";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatchEvent other = (MatchEvent) obj;
        if (this.eventId != other.eventId && (this.eventId == null || !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (this.eventId != null ? this.eventId.hashCode() : 0);
        return hash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
