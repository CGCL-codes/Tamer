package org.tacticalTroopers.jme.server.object;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3f;
import org.tacticalTroopers.jme.common.message.GameMap;
import org.tacticalTroopers.jme.server.team.Team;
import com.jme3.math.FastMath;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

@Serializable()
public class ServerGameMap {

    GameMap gameMap = new GameMap();

    List<ServerElement> walls = new ArrayList<ServerElement>();

    public ServerGameMap() {
        ElementFactory ef = ElementFactory.getInstance();
        Vector3f topLeft = new Vector3f(-50, 0, 25);
        Vector3f bottomRight = new Vector3f(50, 0, -25);
        Float width = 30f;
        Float depth = 15f;
        walls.add(ef.createWall().setPosition(0f, 0f, 0f).setSize(width, 0.1f, depth));
        walls.add(ef.createWall().setPosition(25f, 2.5f, 0f).setSize(.5f, 2.5f, 5f));
        walls.add(ef.createWall().setPosition(-25f, 2.5f, 0f).setSize(.5f, 2.5f, 5f));
        ServerElement a = ef.createWall().setPosition(width, 2.5f, 0f).setSize(.1f, 5f, depth);
        ServerElement b = ef.createWall().setPosition(0f, 2.5f, -depth).setSize(width, 5f, .1f);
        ServerElement c = ef.createWall().setPosition(0f, 2.5f, depth).setSize(width, 5f, .1f);
        ServerElement d = ef.createWall().setPosition(-width, 2.5f, 0f).setSize(.1f, 5f, depth);
        a.getElementMessage().setAttribute("hidden", true);
        b.getElementMessage().setAttribute("hidden", true);
        c.getElementMessage().setAttribute("hidden", true);
        d.getElementMessage().setAttribute("hidden", true);
        walls.add(a);
        walls.add(b);
        walls.add(c);
        walls.add(d);
        walls.add(ef.createWall().setPosition(0f, -1.9f, 0f).setSize(4f, 2f, 4f).setRotation(0f, 0f, 0.1f));
        for (ServerElement w : walls) {
            gameMap.addWall(w.getElementMessage());
        }
    }

    public List<ServerElement> getStaticElements() {
        return walls;
    }

    @Override
    public String toString() {
        return "ServerPlayGroundMap{" + walls + "}";
    }

    public Message getGameMapMessage() {
        return gameMap;
    }

    public Vector3f getRespownPosition(Team team) {
        return new Vector3f(0f, 5f, 0f);
    }
}
