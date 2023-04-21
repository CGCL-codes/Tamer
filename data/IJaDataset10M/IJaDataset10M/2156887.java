package org.dfdaemon.il2.engine.domain;

import org.dfdaemon.il2.engine.GenericEntity;
import java.util.Date;

/**
 * @author octo
 */
public class Session implements GenericEntity<Long> {

    private Long id;

    private String ip;

    private Date openDate = new Date();

    private Date closeDate;

    private Player player;

    private SessionState state = SessionState.NEW;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public SessionState getState() {
        return state;
    }

    public void setState(final SessionState state) {
        this.state = state;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(final Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(final Date closeDate) {
        this.closeDate = closeDate;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Session");
        sb.append("{");
        sb.append("id=").append(id);
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", openDate=").append(openDate);
        sb.append(", closeDate=").append(closeDate);
        sb.append(", player=").append(player);
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
