package play.go.controller;

import java.io.IOException;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;
import play.go.meta.GameMeta;
import play.go.model.Game;
import play.go.model.Player;
import com.google.appengine.api.datastore.Key;

public abstract class AbstructGoController extends Controller {

    protected Navigation text(String text) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().print(text);
        response.flushBuffer();
        return null;
    }

    protected Player getPlayer() {
        Player player = Memcache.get(asString("token"));
        if (player == null) {
            throw new IllegalStateException("player is null.");
        }
        return player;
    }

    protected Game getGame() {
        Key key = Datastore.createKey(GameMeta.get(), asInteger("gameId"));
        System.out.println("key: " + key);
        Game game = Datastore.getOrNull(GameMeta.get(), key);
        if (game == null) {
            throw new IllegalStateException("game is null.");
        }
        return game;
    }
}
