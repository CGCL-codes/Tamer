package org.heartstorming.bada;

import junit.framework.*;
import org.heartstorming.bada.player.*;
import org.heartstorming.bada.playlist.*;

public class BadaTest extends TestCase {

    Bada bada;

    PlayList playList;

    public BadaTest(String s) {
        super(s);
    }

    public void setUp() {
        bada = new Bada();
        playList = bada.getPlayList();
        playList.add("./temp/Desperado.mp3");
    }

    public void tearDown() {
        bada = null;
        playList = null;
    }

    public void __testNormal() throws InterruptedException {
        bada.play();
        sleep(5);
        bada.pause();
        sleep(2);
        bada.play();
        sleep(5);
        bada.pause();
        sleep(2);
        bada.play();
        sleep(5);
        bada.stop();
    }

    public void __testMark() throws InterruptedException {
        bada.start();
        sleep(4);
        bada.mark();
        sleep(20);
        bada.next();
        sleep(5);
        bada.mark();
        sleep(30);
        bada.prev();
        bada.prev();
        bada.prev();
        bada.prev();
        sleep(20);
    }

    public void __testUnmark() throws InterruptedException {
        bada.start();
        sleep(2);
        bada.mark();
        sleep(2);
        bada.mark();
        sleep(1);
        bada.mark();
        bada.mark();
        bada.mark();
        bada.mark();
        bada.mark();
        bada.mark();
        bada.mark();
        sleep(2);
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        bada.unmark();
        sleep(20);
    }

    public void __testSetPhrase() throws InterruptedException {
        bada.start();
        sleep(2);
        bada.mark();
        long target = bada.getTimeBar().getCurrent().lastMS();
        sleep(2);
        bada.next();
        sleep(2);
        bada.mark();
        sleep(2);
        long target2 = bada.getTimeBar().getCurrent().lastMS();
        bada.next();
        sleep(2);
        bada.setPhrase(0);
        sleep(2);
        assertEquals(target, bada.getTimeBar().getCurrent().lastMS());
        bada.setPhrase(1);
        sleep(2);
        assertEquals(target2, bada.getTimeBar().getCurrent().lastMS());
    }

    public void testFoo() {
    }

    public void __testMark2() throws InterruptedException {
        bada = new Bada();
        playList = bada.getPlayList();
        playList.add("./temp/se-parkinsons.mp3");
        bada.start();
        sleep(10);
        bada.mark();
        sleep(5);
        assertTrue(0 != bada.getTimeBar().getCurrent().lastMS());
    }

    void sleep(int minutes) throws InterruptedException {
        Thread.sleep(1000 * minutes);
    }
}
