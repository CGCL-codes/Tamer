package com.dm.applet;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

public class Applet1 extends Applet {

    private int i;

    Image image;

    AudioClip loopClip;

    URL auurl;

    public void paint(Graphics g) {
        g.drawImage(image, 10, 10, this);
        loopClip.loop();
    }

    public void destroy() {
        System.out.println("destroy my applet!");
    }

    public void init() {
        System.out.println("init applet");
        i = 1;
        try {
            image = getImage(new URL("http://www.yesky.com/TLimages/img/head/logo.gif"));
            auurl = new URL("http://www.shu.edu.cn/~xyx/java/Animator/audio/");
            loopClip = getAudioClip(auurl, "bark.au");
        } catch (MalformedURLException e) {
        }
    }

    public void start() {
        System.out.println("applet start");
        if (i == 1) System.out.println("i is changed by init method!");
    }
}
