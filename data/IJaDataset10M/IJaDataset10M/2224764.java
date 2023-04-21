package sobchak.tools;

import sobchak.ITajik;
import sobchak.event.EventProducer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 04.03.2010
 * Time: 16:19:29
 * To change this template use File | Settings | File Templates.
 */
public final class Wall extends EventProducer {

    private AtomicInteger brick_put = new AtomicInteger();

    protected static Wall buildWall() {
        return new Wall();
    }

    protected Wall() {
    }

    public boolean putBrickOnWall(Brick brick, CementPortion cement, ITajik tajik) {
        if (cement == null || brick == null) {
            listener.onEvent(tajik, Events.BAD_CEMENT_BRICK);
            return false;
        }
        if (!cement.already_in_wall.compareAndSet(false, true)) {
            listener.onEvent(tajik, Events.BRICK_CEMENT_USED);
            return false;
        }
        if (!brick.already_in_wall.compareAndSet(false, true)) {
            listener.onEvent(tajik, Events.BRICK_CEMENT_USED);
            return false;
        }
        brick_put.incrementAndGet();
        listener.onEvent(tajik, Events.BRICK_PUT);
        return true;
    }

    public int getBricksInWall() {
        return brick_put.get();
    }
}
