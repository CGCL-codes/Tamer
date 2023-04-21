package net.narusas.galaticheroproto.view;

import java.nio.FloatBuffer;
import java.util.List;
import net.narusas.galaticheroproto.model.Army;
import net.narusas.galaticheroproto.model.Config;
import net.narusas.galaticheroproto.model.Fleet;
import net.narusas.galaticheroproto.model.MathUtil;
import net.narusas.galaticheroproto.model.Player;
import net.narusas.galaticheroproto.model.Stage;
import net.narusas.galaticheroproto.model.WarfareGroup;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

public class WarfareGroupLayer implements Layer {

    private final Config config;

    private final Stage stage;

    private int listNo;

    static FloatBuffer buf = BufferUtils.createFloatBuffer(16);

    public WarfareGroupLayer(Config config, Stage stage) {
        this.config = config;
        this.stage = stage;
    }

    public void init() {
        listNo = GL11.glGenLists(1);
        GL11.glNewList(listNo, GL11.GL_COMPILE);
        {
            GL11.glColor4f(0f, 1f, 1f, 0.8f);
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2f(0, 0);
                GL11.glVertex2f(0, 10000);
            }
            GL11.glEnd();
        }
        GL11.glEndList();
    }

    public void render(Board board) {
        loopPlayers();
    }

    private void loopPlayers() {
        List<Player> playerList = stage.getUniverse().getPlayer();
        for (int i = 0; i < playerList.size(); i++) {
            Army army = playerList.get(i).getArmy();
            loopFleets(army.getFleets());
        }
    }

    private void loopFleets(List<Fleet> fleets) {
        for (int i = 0; i < fleets.size(); i++) {
            loopSquadrons(fleets.get(i).getWarfaregroups());
        }
    }

    private void loopSquadrons(List<WarfareGroup> squadrons) {
        for (int i = 0; i < squadrons.size(); i++) {
            loopSquadron(squadrons.get(i));
        }
    }

    private void loopSquadron(WarfareGroup squadron) {
        GL11.glPushMatrix();
        {
            Matrix4f m = MathUtil.getVectorMatrix(squadron.X(), squadron.Y());
            Matrix4f.mul(m, MathUtil.getRotationMatrix(squadron.Bearing()), m);
            m.store(buf);
            buf.flip();
            GL11.glMultMatrix(buf);
            GL11.glColor3f(1, 1, 1);
            GL11.glCallList(listNo);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.4f, 0.2f, 0.2f, 0.4f);
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            FloatBuffer vs = squadron.calcShape();
            GL11.glVertexPointer(2, 0, vs);
            GL11.glDrawArrays(GL11.GL_QUADS, 0, 8);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 1f, 1f, 0.2f);
            GL11.glVertexPointer(2, 0, vs);
            GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        }
        GL11.glPopMatrix();
    }
}
