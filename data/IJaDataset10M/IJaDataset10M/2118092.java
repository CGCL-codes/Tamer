package org.chernovia.games.f8.macebook;

import java.awt.Button;

public class ButtFace extends Button {

    private static final long serialVersionUID = 5745587737002386956L;

    private String imgUrl;

    private String uid;

    private String name;

    private int hitpoints;

    private boolean redButt = false;

    public ButtFace(String i, String n, String url) {
        super(n);
        uid = i;
        name = n;
        imgUrl = url;
        hitpoints = -1;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public String getImg() {
        return imgUrl;
    }

    public void setImg(String img) {
        imgUrl = img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public boolean isRedButt() {
        return redButt;
    }

    public void setRedButt(boolean redButt) {
        this.redButt = redButt;
    }
}
