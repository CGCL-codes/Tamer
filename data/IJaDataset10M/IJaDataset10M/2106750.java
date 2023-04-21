package radius.server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public abstract class AbstractRadiusServer implements RadiusServer {

    protected Log log = LogFactory.getLog(getClass());

    protected volatile boolean started;

    protected volatile boolean paused;

    protected volatile boolean stopped;

    protected RadiusContextFactory contextFactory;

    protected RadiusDataConsumer consumer;

    protected SocketAddress authAddress;

    protected SocketAddress accountAddress;

    public boolean pause() {
        log.info("pause server");
        paused = true;
        return true;
    }

    public boolean goOn() {
        log.info("server goon");
        paused = false;
        return true;
    }

    public boolean restart() {
        log.info("restart server");
        if (stop()) {
            return start();
        } else {
            return false;
        }
    }

    public boolean stop() {
        log.info("stop server");
        stopped = true;
        return true;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void enableAuth(SocketAddress address) {
        this.authAddress = address;
    }

    public void enableAccount(SocketAddress address) {
        this.accountAddress = address;
    }

    public void setConsumer(RadiusDataConsumer consumer) {
        this.consumer = consumer;
    }

    public void consume(RadiusContext context) {
        SocketAddress targetAddress = context.getTargetAddress();
        ByteBuffer resultBuffer = context.getTargetBuffer();
        SocketAddress localAddress = context.getLocalAddress();
        doConsume(targetAddress, resultBuffer, localAddress);
    }

    protected abstract void doConsume(SocketAddress targetAddress, ByteBuffer resultBuffer, SocketAddress localAddress);

    public void setRadiusContextFactory(RadiusContextFactory factory) {
        this.contextFactory = factory;
    }
}
