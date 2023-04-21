package com.hadeslee.yoyoplayer.lyric;

import com.hadeslee.yoyoplayer.util.Config;
import com.hadeslee.yoyoplayer.util.MultiImageBorder;
import com.hadeslee.yoyoplayer.util.Playerable;
import com.hadeslee.yoyoplayer.util.Util;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 *
 * @author hadeslee
 */
public class LyricUI extends JPanel {

    private static final long serialVersionUID = 20071214L;

    private Config config;

    private LyricPanel lp;

    private Playerable player;

    private MultiImageBorder border;

    public LyricUI() {
        super(new BorderLayout());
        this.setPreferredSize(new Dimension(285, 465));
        this.setMinimumSize(new Dimension(285, 50));
    }

    public void setPlayer(Playerable player) {
        this.player = player;
    }

    public void setParent(Component parent) {
        border.setParent(parent);
    }

    public void setBorderEnabled(boolean b) {
        if (b) {
            this.setBorder(border);
        } else {
            this.setBorder(null);
        }
    }

    public void loadUI(Component parent, Config config) {
        this.config = config;
        border = new MultiImageBorder(parent, config);
        border.setCorner1(Util.getImage("lyric/corner1.png"));
        border.setCorner2(Util.getImage("playlist/corner2.png"));
        border.setCorner3(Util.getImage("playlist/corner3.png"));
        border.setCorner4(Util.getImage("playlist/corner4.png"));
        border.setTop(Util.getImage("playlist/top.png"));
        border.setBottom(Util.getImage("playlist/bottom.png"));
        border.setLeft(Util.getImage("playlist/left.png"));
        border.setRight(Util.getImage("playlist/right.png"));
        this.setBorder(border);
        this.addMouseListener(border);
        this.addMouseMotionListener(border);
        lp = new LyricPanel(player);
        lp.setConfig(config);
        this.add(lp, BorderLayout.CENTER);
    }

    public void setShowLogo(boolean b) {
        lp.setShowLogo(b);
    }

    /**
     * 设置播放列表
     * @param pl 播放列表
     */
    public void setPlayList(Playerable pl) {
        lp.setPlayList(player);
    }

    public LyricPanel getLyricPanel() {
        return lp;
    }

    /**
     * 设置一个新的歌词对象,此方法可能会被
     * PlayList调用
     * @param ly 歌词
     */
    public void setLyric(Lyric ly) {
        lp.setLyric(ly);
    }

    public void pause() {
        lp.pause();
    }

    public void start() {
        lp.start();
    }
}
