package EXCLUDE.fisce.game;

import com.cirnoworks.fisce.game.Scene;

/**
 * @author cloudee
 * 
 */
public class MainLoopTest extends Scene {

    /**
	 * @param fps
	 */
    public MainLoopTest() {
        super(30);
    }

    protected void oneLoop() {
    }

    protected void setFps(int fps) {
        System.out.println(fps / 4096f);
    }

    public static void main(String[] args) {
        MainLoopTest tester = new MainLoopTest();
        tester.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tester.shutdown();
    }

    public void script() {
    }

    protected void load() {
    }
}
