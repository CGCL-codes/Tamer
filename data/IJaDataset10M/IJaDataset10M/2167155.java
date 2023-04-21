package org.eyrene.jplayer.core;

/**
 * <p>Title: JPlayerSimpleBuilder.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class JPlayerSimpleBuilder implements JPlayerBuilder {

    private JPlayer jPlayer;

    private JPlaylist jPlaylist;

    /**
     * Default constructor
     */
    public JPlayerSimpleBuilder() {
        this.jPlayer = new JPlayer();
    }

    public JPlayer getJPlayer() {
        return jPlayer;
    }

    public JPlayerBuilder buildJPlaylist(JMedia firstJMedia) {
        this.jPlaylist = new JPlaylist();
        this.jPlayer.setJPlaylist(jPlaylist);
        return this;
    }

    public JPlaylist getJPlaylist() {
        return this.jPlaylist;
    }
}
