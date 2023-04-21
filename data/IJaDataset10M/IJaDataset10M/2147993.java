package game;

import network.client.INetCode;
import org.lwjgl.opengl.GL11;
import network.messages.DataObject;

/**
 *
 * @author swift
 */
public class HomingRoller {

    private float timediff;

    private Texture tex;

    private XPhase game;

    private float x;

    private float y;

    private float z;

    private float vx;

    private float vy;

    private float vz;

    private float v1;

    private float v2;

    private boolean process = true;

    private float fx, fy, fz;

    DataObject[] pp;

    private INetCode nc;

    private int SIZEX;

    private int SIZEY;

    private ShootBuffer sb;

    private long explodeTime;

    private boolean ground = false;

    private int targetID = 0;

    float targetX;

    float targetY;

    float dist = 1000;

    private int speed = 100;

    /** Creates a new instance of EnergyBall */
    public HomingRoller(int SIZEX, int SIZEY, float playerPosX, float playerPosY, float playerHeight, float vx, float vy, float vz, Texture tex, XPhase game, INetCode nc, ShootBuffer sb) {
        this.tex = tex;
        this.game = game;
        this.x = playerPosX;
        this.y = playerPosY;
        this.z = playerHeight + 1.5f;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.nc = nc;
        this.SIZEX = SIZEX;
        this.SIZEY = SIZEY;
        this.sb = sb;
        sb.add(this);
    }

    public void draw(long time) {
        if (process) {
            pp = game.getPP();
            float m = (float) time / speed;
            GL11.glColor4f(.0f, 1.0f, .0f, .0f);
            float posx = x + m * vx;
            float posy = -(y - m * vy);
            if ((posx < SIZEX - 1 && posy < SIZEY - 1) && (posx >= 0 && posy >= 0)) {
                if (z > game.getHeight(posx, posy) + 1 && !ground) z -= (m * 0.1f); else {
                    ground = true;
                    if (targetID == 0) searchEnemy(posx, posy);
                    getTargetCoord();
                    v1 = (targetX - posx);
                    v2 = (targetY - posy);
                    float f = (float) Math.sqrt(v1 * v1 + v2 * v2);
                    dist = f;
                    v1 /= f;
                    v2 /= f;
                    float a = (0.2f / dist);
                    v1 *= a;
                    v2 *= a;
                    vx += v1;
                    vy += v2;
                    float l = (float) Math.sqrt(vx * vx + vy * vy);
                    vx /= l;
                    vy /= l;
                    z = game.getHeight(posx, posy) + 1;
                }
                float posz = z;
                this.fx = posx;
                this.fy = posy;
                this.fz = posz;
                newTestIfHit(posx, posy, posz);
                if (m > 1) new Particle(SIZEX, SIZEY, fx, -fy, fz, 0, 0, -0.2f, tex, game, false, false);
            } else {
                explodeTime = System.currentTimeMillis();
                newExplode();
            }
        } else {
            newExplode();
        }
    }

    private void newExplode() {
        process = false;
        timediff = (System.currentTimeMillis() - explodeTime);
        if (timediff > 1000) timediff = 1000;
        timediff /= 1000;
        GL11.glColor4f(1.0f, .0f, .0f, 1.0f - timediff);
        game.drawSprite(fx, fy, fz, 1, tex);
    }

    private void getTargetCoord() {
        for (int i = 0; i < pp.length; i++) {
            if (pp[i] != null) {
                if (pp[i].getId() == targetID) {
                    targetX = pp[i].getPos_x();
                    targetY = -pp[i].getPos_y();
                }
            }
        }
    }

    private void searchEnemy(float posx, float posy) {
        pp = game.getPP();
        float vlen;
        float nvx;
        float nvy;
        float x, y;
        for (int i = 0; i < pp.length; i++) {
            if (pp[i] != null) {
                x = pp[i].getPos_x();
                y = -pp[i].getPos_y();
                nvx = x - posx;
                nvy = y - posy;
                vlen = (float) Math.sqrt(nvx * nvx + nvy * nvy);
                if (vlen < dist) {
                    targetID = pp[i].getId();
                    dist = vlen;
                }
            }
        }
    }

    private void newTestIfHit(float posx, float posy, float posz) {
        pp = game.getPP();
        float x, y, z;
        float vx, vy, vz;
        float vlen;
        for (int i = 0; i < pp.length; i++) {
            if (pp[i] != null) {
                x = pp[i].getPos_x();
                y = -pp[i].getPos_y();
                z = pp[i].getPos_z() + 0.85f;
                vx = x - posx;
                vy = y - posy;
                vz = z - posz;
                vlen = (float) Math.sqrt(vx * vx + vy * vy + vz * vz);
                if (vlen < 1.5) {
                    System.out.println("player hit!, id: " + pp[i].getId());
                    nc.sendHit(pp[i].getId(), WEAPONS.pulsgun.healthMinus());
                    process = false;
                    explodeTime = System.currentTimeMillis();
                    newExplode();
                    game.setServerMessage("You hit " + nc.getPlayersName(pp[i].getId()));
                }
            }
        }
    }
}
