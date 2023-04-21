package lamao.soh.core.collisionhandlers;

import lamao.soh.core.entities.SHBall;
import lamao.soh.core.entities.SHPaddle;
import lamao.soh.utils.events.ISHEventHandler;
import lamao.soh.utils.events.SHEvent;
import lamao.soh.core.SHGamePack;

/**
 * @author lamao
 *
 */
public class SHBallPaddleCollisionHandler implements ISHEventHandler {

    @Override
    public void processEvent(SHEvent event) {
        SHBall ball = (SHBall) event.params.get("src");
        SHPaddle paddle = (SHPaddle) event.params.get("dst");
        SHGamePack.dispatcher.addEvent("level-paddle-hit", this, null);
        paddle.onHit(ball);
    }
}
