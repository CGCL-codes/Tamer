package slojj.dotsbox.core.entity;

import slojj.dotsbox.parser.Channel;

public class ChannelHolder implements Entity {

    private static final long serialVersionUID = 1L;

    private String pid;

    private String link;

    public ChannelHolder(String link, String pid) {
        this.link = link;
        this.pid = pid;
    }

    public String getId() {
        return link;
    }

    public String getPid() {
        return pid;
    }

    public String getTitle() {
        return null;
    }

    public void copyFromChannel(Channel channel) {
    }
}
