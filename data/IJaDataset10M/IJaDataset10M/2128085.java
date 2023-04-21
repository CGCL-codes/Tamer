package org.javadelic.burrow;

public class JabFavorite extends Object {

    private String name;

    private String channel;

    private String server;

    private String nickname;

    /** Creates a new JabFavorite */
    public JabFavorite(String name) {
        this.name = name;
    }

    public JabFavorite(String name, String channel, String server, String nickname) {
        this.name = name;
        this.channel = channel;
        this.server = server;
        this.nickname = nickname;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getChannel() {
        return channel;
    }

    public String getServer() {
        return server;
    }

    public String getNickname() {
        return nickname;
    }
}
