package info.benjaminhill.clip2png;

/**
 * @author Benjamin Hill
 */
public class Clip2PNGTimer {

    public static void main(final String[] args) throws InterruptedException {
        final Clip2Image c2p = new Clip2Image("png");
        c2p.tryClipboard = false;
        Thread.sleep(5000);
        c2p.start();
    }
}
