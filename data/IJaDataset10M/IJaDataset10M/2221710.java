package org.one.stone.soup.wiki.applet;

public class TestApplet extends WikiApplet {

    public TestApplet() {
        super();
        System.out.println("Created");
    }

    public void init() {
        System.out.println("init");
        super.init();
    }

    public void start() {
        System.out.println("start");
        super.start();
    }

    public void stop() {
        System.out.println("stop");
        super.stop();
    }

    public void destroy() {
        System.out.println("destroy");
        super.destroy();
    }

    public String getVersion() {
        return "build 005";
    }
}
