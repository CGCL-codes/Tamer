package trashcan;

import sourceforge.shinigami.music.Mp3;
import sourceforge.shinigami.music.Midi;
import sourceforge.shinigami.music.Wave;

public class MusicTest {

    public static void main(String[] args) throws InterruptedException {
        MusicTest.testIntervals();
        System.exit(0);
    }

    static void testTypes() throws InterruptedException {
        Wave bg1 = new Wave("trashcan/ocean.wav");
        bg1.play();
        Thread.sleep(10000);
        bg1.stop();
        Midi bg2 = new Midi("trashcan/zelda.mid");
        bg2.play();
        Thread.sleep(10000);
        bg2.stop();
        Mp3 bg3 = new Mp3("trashcan/26 - Let it be.mp3");
        bg3.play();
        Thread.sleep(10000);
        bg3.stop();
        Wave bgu = new Wave("trashcan/zelda-06.wav");
        bgu.play();
        Thread.sleep(10000);
        bgu.stop();
    }

    static void testIntervals() throws InterruptedException {
        Midi m = new Midi("trashcan/zelda.mid");
        m.play();
        Thread.sleep(4000);
        m.stop();
        Thread.sleep(1000);
        m.play();
        Thread.sleep(4000);
    }
}
