package org.dizem.sanguosha.model.vo;

import org.dizem.sanguosha.model.player.Player;

/**
 * User: dizem
 * Time: 11-4-24 下午5:30
 */
public class PlayerVO {

    private String ip;

    private int port;

    private int playerId;

    private String name;

    public PlayerVO() {
    }

    public PlayerVO(Player player) {
        this.ip = player.getIp();
        this.port = player.getPort();
        this.playerId = player.getPlayerId();
        this.name = player.getName();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
