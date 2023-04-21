package sdljava.event;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sdljava.SDLException;
import sdljava.SDLMain;
import sdljava.SDLTest;
import sdljava.SDLTimer;
import sdljava.video.SDLSurface;
import sdljava.video.SDLVideo;

public class SDLEventManagerTest extends SDLTest {

    SDLSurface framebuffer;

    SDLEventManager manager;

    boolean isStopped = false;

    ByteBuffer pixelData = null;

    int pitch = 0;

    public void init() throws SDLException {
        SDLMain.init(SDLMain.SDL_INIT_EVERYTHING | SDLMain.SDL_INIT_NOPARACHUTE);
        framebuffer = SDLVideo.setVideoMode(800, 600, 32, (long) SDLVideo.SDL_SWSURFACE | SDLVideo.SDL_DOUBLEBUF);
        pixelData = framebuffer.getPixelData();
        pixelData.order(ByteOrder.nativeOrder());
        pitch = (framebuffer.getPitch() / 4);
        manager = SDLEventManager.getInstance();
        SDLEventListenerTest testListener = new SDLEventListenerTest(this);
        manager.register(testListener, SDLKeyboardEvent.class);
    }

    public void destroy() {
        if (manager != null) manager.stop();
        SDLMain.quit();
    }

    public void stopIt() {
        isStopped = true;
    }

    public void work() throws SDLException, InterruptedException {
        manager.startAndWait();
        while (!isStopped) {
            if (framebuffer.lockSurface() == false) return;
            int yofs = 0, tick = (int) SDLTimer.getTicks();
            for (int i = 0; i < framebuffer.getHeight(); i++) {
                for (int j = 0, ofs = yofs; j < framebuffer.getWidth(); j++, ofs++) {
                    pixelData.putInt(ofs * 4, i * i + j * j + tick);
                }
                yofs += pitch;
            }
            if (framebuffer.mustLock()) framebuffer.unlockSurface();
            framebuffer.updateRect();
            SDLTimer.delay(10);
            System.out.println("Press any key ... 'ESC' key to quit.");
        }
    }

    public static void main(String[] args) {
        SDLEventManagerTest t = null;
        try {
            t = new SDLEventManagerTest();
            t.init();
            t.work();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (t != null) t.destroy();
        }
    }
}
