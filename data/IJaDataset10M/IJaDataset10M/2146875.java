package trb.trials4k;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Event;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 16604 - removed wheel class
 * 15446 - remove Body class
 * 14834 - remove Vertex class
 * 13807 - remove Rider class
 * 12586 - removed Bike
 * 13951 - inlined collision detection in Level and added Intersection class
 * 13207 - read levels from file
 * 13898 - inlined level parsing code
 * 13729 - inlined methods
 * 7640 - removed Intersection class
 * 6802 - removed Tuple class
 * 10791 (3811) - removed Edge class
 * 10660 (3782) - inlined code and removed test code
 * ? (3685) - removed System.outs
 * 2808 - commented out create bike
 * 3766 - convertes vs to array of floats
 *
 * vertices: 5*54 = 270
 * edges: 173 * 6 = 1038
 * 
 * @author admin
 */
public class B extends Applet implements Runnable {

    public static final float WHEEL_TORQUE = 0.12f;

    public static final float BIKE_TORQUE = 0.005f;

    public static final int X1 = 0;

    public static final int Y1 = 1;

    public static final int X2 = 2;

    public static final int Y2 = 3;

    public static final int CIRCLE_RADIUS = 2;

    public static final int CHECKPOINT_PASSED = 2;

    public static final int LEVEL_STRIDE = 0x1000;

    public static final int OBJECT_STRIDE = 0x10;

    public static final int LINE_OFF = OBJECT_STRIDE * 1024;

    public static final int CIRCLE_OFF = OBJECT_STRIDE * 1024 * 2;

    public static final int CHECKPOINT_OFF = OBJECT_STRIDE * 1024 * 3;

    public static final int LINE_CNT_OFF = 0;

    public static final int CIRCLE_CNT_OFF = 1;

    public static final int CHECKPOINT_CNT_OFF = 2;

    public static final int LEVEL_CNT_OFF = LEVEL_STRIDE * 16;

    public static final int STROKE_WIDTH = 16;

    public static final int VTX_STRIDE = 10;

    public static final int POS_X = 0;

    public static final int POS_Y = 1;

    public static final int OLD_X = 2;

    public static final int OLD_Y = 3;

    public static final int ACC_X = 4;

    public static final int ACC_Y = 5;

    public static final int RADIUS = 6;

    public static final int MASS = 7;

    public static final int COLLIDABLE = 8;

    public static final int TYPE = 9;

    public static final float UNKNOWN = 0;

    public static final float RIDER = 1;

    static final int STATE_DEAD = 0;

    static final int STATE_PLAYING = 1;

    static final int STATE_FINISHED = 2;

    static final int BACKGROUND_COLOR = 0xff2f174f;

    static final int WIRE_COUNT = 18;

    static final float WHEEL_RADIUS = 35f;

    static final float TIRE_RADIUS = 10f;

    static final float WHEEL_DIAMETER = WHEEL_RADIUS * 2 + TIRE_RADIUS * 2;

    static final float WHEELBASE = WHEEL_DIAMETER * 1.9f;

    static final float[] f = new float[1000000];

    int currentLevelIdx = 1;

    public int currentCheckpoint = 0;

    public int tries = 0;

    public long startTime = System.currentTimeMillis();

    boolean[] changed = new boolean[0x10000];

    boolean[] k = new boolean[0x10000];

    public float[] vs = new float[1000];

    public int vertexCnt = 0;

    public final List<float[]> constraints = new ArrayList();

    public boolean collidedWithRider = false;

    int shoulders;

    int foot;

    int hands;

    List<float[]> riderEdges;

    List<float[]> riderEdges1;

    List<float[]> riderEdges2;

    int[] riderVertices;

    float riderT = 0;

    int backWheel;

    int frontWheel;

    int stearing;

    int chain;

    int engine;

    static final int EDGE_LENGTH = 0;

    static final int EDGE_STIFFNESS = 1;

    static final int EDGE_DAMPING = 2;

    static final int EDGE_MIN_LENGTH = 3;

    static final int EDGE_MAX_LENGTH = 4;

    static final int EDGE_OO_TOTAL_MASS = 5;

    static final int EDGE_VISIBLE = 6;

    static final int EDGE_V1 = 7;

    static final int EDGE_V2 = 8;

    @Override
    public void start() {
        new Thread(this).start();
    }

    public void run() {
        setSize(800, 600);
        BufferedImage screen = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) screen.getGraphics();
        Graphics appletGraphics = getGraphics();
        int tick = 0, fps = 0, acc = 0;
        long lastTime = System.nanoTime();
        loadLevel();
        int state = STATE_PLAYING;
        requestFocusInWindow();
        int yieldCnt = 0;
        do {
            int levelOff = currentLevelIdx * LEVEL_STRIDE;
            if (k['1'] && changed['1']) {
                System.out.println("1");
                currentLevelIdx = 0;
                loadLevel();
                state = STATE_PLAYING;
                changed['1'] = false;
            }
            if (k['2'] && changed['2']) {
                System.out.println("2");
                currentLevelIdx = 1;
                loadLevel();
                state = STATE_PLAYING;
                changed['2'] = false;
            }
            if (state != STATE_FINISHED && k[Event.BACK_SPACE] && changed[Event.BACK_SPACE]) {
                respawn();
                state = STATE_PLAYING;
                changed[Event.BACK_SPACE] = false;
            }
            for (int i = 0; i < vertexCnt; i++) {
                vs[ACC_X + i * VTX_STRIDE] = 0;
                vs[ACC_Y + i * VTX_STRIDE] = 0.2f;
            }
            if (state == STATE_PLAYING) {
                float wheelTorque = 0f;
                if (k[Event.UP] || k['w']) {
                    wheelTorque += WHEEL_TORQUE;
                }
                if (k[Event.DOWN] || k['s']) {
                    wheelTorque -= WHEEL_TORQUE;
                }
                for (int i = backWheel; i < backWheel + WIRE_COUNT; i++) {
                    vs[ACC_X + i * VTX_STRIDE] -= (vs[POS_Y + i * VTX_STRIDE] - vs[POS_Y + (backWheel - 1) * VTX_STRIDE]) * wheelTorque;
                    vs[ACC_Y + i * VTX_STRIDE] += (vs[POS_X + i * VTX_STRIDE] - vs[POS_X + (backWheel - 1) * VTX_STRIDE]) * wheelTorque;
                }
                float bikeTorque = 0;
                if (k[Event.LEFT] || k['a']) {
                    bikeTorque -= BIKE_TORQUE;
                    riderT = Math.max(0, riderT - 0.2f);
                }
                if (k[Event.RIGHT] || k['d']) {
                    bikeTorque += BIKE_TORQUE;
                    riderT = Math.min(1, riderT + 0.2f);
                }
                for (int i = 0; i < riderEdges.size(); i++) {
                    riderEdges.get(i)[EDGE_LENGTH] = riderEdges1.get(i)[EDGE_LENGTH] * (1 - riderT) + riderEdges2.get(i)[EDGE_LENGTH] * riderT;
                }
                float nx = -(vs[POS_Y + (frontWheel - 1) * VTX_STRIDE] - vs[POS_Y + (backWheel - 1) * VTX_STRIDE]) * bikeTorque;
                float ny = (vs[POS_X + (frontWheel - 1) * VTX_STRIDE] - vs[POS_X + (backWheel - 1) * VTX_STRIDE]) * bikeTorque;
                vs[POS_X + (frontWheel - 1) * VTX_STRIDE] += nx;
                vs[POS_Y + (frontWheel - 1) * VTX_STRIDE] += ny;
                vs[POS_X + (backWheel - 1) * VTX_STRIDE] -= nx;
                vs[POS_Y + (backWheel - 1) * VTX_STRIDE] -= ny;
            }
            float checkpointCnt = f[levelOff + CHECKPOINT_CNT_OFF];
            for (int checkpointIdx = 0; checkpointIdx < checkpointCnt; checkpointIdx++) {
                int x = (int) f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * checkpointIdx + X1];
                if (x < vs[POS_X]) {
                    f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * checkpointIdx + CHECKPOINT_PASSED] = 1;
                    currentCheckpoint = Math.max(currentCheckpoint, checkpointIdx);
                    if (currentCheckpoint == checkpointCnt - 1) {
                        state = STATE_FINISHED;
                    }
                }
            }
            for (int idx = 0; idx < vertexCnt; idx++) {
                int off = idx * VTX_STRIDE;
                float tempX = vs[POS_X + off];
                float tempY = vs[POS_Y + off];
                vs[POS_X + off] += vs[POS_X + off] - vs[OLD_X + off] + vs[ACC_X + off];
                vs[POS_Y + off] += vs[POS_Y + off] - vs[OLD_Y + off] + vs[ACC_Y + off];
                vs[OLD_X + off] = tempX;
                vs[OLD_Y + off] = tempY;
            }
            collidedWithRider = false;
            for (int i = 0; i < 10; i++) {
                for (float[] ed : constraints) {
                    int v1 = ((int) ed[EDGE_V1]) * VTX_STRIDE;
                    int v2 = ((int) ed[EDGE_V2]) * VTX_STRIDE;
                    float stiff = ed[EDGE_STIFFNESS];
                    float v1v2x = vs[POS_X + v2] - vs[POS_X + v1];
                    float v1v2y = vs[POS_Y + v2] - vs[POS_Y + v1];
                    float x = v1v2x * v1v2x + v1v2y * v1v2y;
                    float guess = ed[EDGE_LENGTH];
                    float v1v2Length = (guess + x / guess) * 0.5f;
                    float diff = v1v2Length - ed[EDGE_LENGTH];
                    float adjustment = diff * stiff;
                    float damp = ed[EDGE_DAMPING];
                    if (v1v2Length - adjustment < ed[EDGE_MIN_LENGTH]) {
                        adjustment = v1v2Length - ed[EDGE_MIN_LENGTH];
                        damp = 0;
                    }
                    if (v1v2Length - adjustment > ed[EDGE_MAX_LENGTH]) {
                        adjustment = v1v2Length - ed[EDGE_MAX_LENGTH];
                        damp = 0;
                    }
                    float ooLength = 1f / v1v2Length;
                    v1v2x *= ooLength;
                    v1v2y *= ooLength;
                    float mf1 = vs[MASS + v2] * ed[EDGE_OO_TOTAL_MASS];
                    float mf2 = vs[MASS + v1] * ed[EDGE_OO_TOTAL_MASS];
                    vs[POS_X + v1] += v1v2x * adjustment * mf1 * 0.5f;
                    vs[POS_Y + v1] += v1v2y * adjustment * mf1 * 0.5f;
                    vs[POS_X + v2] -= v1v2x * adjustment * mf2 * 0.5f;
                    vs[POS_Y + v2] -= v1v2y * adjustment * mf2 * 0.5f;
                    if (damp != 0) {
                        float velDiffx = (vs[POS_X + v2] - vs[OLD_X + v2]) - (vs[POS_X + v1] - vs[OLD_X + v1]);
                        float velDiffy = (vs[POS_Y + v2] - vs[OLD_Y + v2]) - (vs[POS_Y + v1] - vs[OLD_Y + v1]);
                        velDiffx *= (damp * 0.5f);
                        velDiffy *= (damp * 0.5f);
                        vs[POS_X + v1] += velDiffx;
                        vs[POS_Y + v1] += velDiffy;
                        vs[POS_X + v2] -= velDiffx;
                        vs[POS_Y + v2] -= velDiffy;
                    }
                }
                for (int vIdx = 0; vIdx < vertexCnt; vIdx++) {
                    int v = vIdx * VTX_STRIDE;
                    boolean foundCollision = false;
                    float normalx = 0;
                    float normaly = 1;
                    float closestx = 0;
                    float closesty = 0;
                    float closestDistance = 0;
                    float ballx = vs[POS_X + v];
                    float bally = vs[POS_Y + v];
                    float vradius = vs[RADIUS + v] + STROKE_WIDTH / 2;
                    if (vs[COLLIDABLE + v] == 0) {
                        break;
                    }
                    for (int circleIdx = 0; circleIdx < f[levelOff + CIRCLE_CNT_OFF]; circleIdx++) {
                        float circlex = f[levelOff + CIRCLE_OFF + OBJECT_STRIDE * circleIdx + X1];
                        float circley = f[levelOff + CIRCLE_OFF + OBJECT_STRIDE * circleIdx + Y1];
                        float radius = f[levelOff + CIRCLE_OFF + OBJECT_STRIDE * circleIdx + CIRCLE_RADIUS];
                        float dx = ballx - circlex;
                        float dy = bally - circley;
                        float length = length(dx, dy);
                        float dist = length - radius;
                        boolean intersected = dist < vradius;
                        if (!foundCollision || dist < closestDistance) {
                            closestDistance = dist;
                            foundCollision = intersected;
                            closestx = circlex + (dx / length * radius);
                            closesty = circley + (dy / length * radius);
                            normalx = ballx - closestx;
                            normaly = bally - closesty;
                            float normalLength = length(normalx, normaly);
                            normalx /= normalLength;
                            normaly /= normalLength;
                        }
                    }
                    for (int lineIdx = 0; lineIdx < f[levelOff + LINE_CNT_OFF]; lineIdx++) {
                        float linex1 = f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + X1];
                        float liney1 = f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + Y1];
                        float linex2 = f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + X2];
                        float liney2 = f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + Y2];
                        float tempProjectedx = 0;
                        float tempProjectedy = 0;
                        float dist = 0;
                        boolean intersected = false;
                        float rrr = (ballx - linex1) * (linex2 - linex1) + (bally - liney1) * (liney2 - liney1);
                        float len = length(linex2 - linex1, liney2 - liney1);
                        float t = rrr / len / len;
                        if (t >= 0 && t <= 1) {
                            tempProjectedx = linex1 + (t * (linex2 - linex1));
                            tempProjectedy = liney1 + (t * (liney2 - liney1));
                            dist = length(ballx - tempProjectedx, bally - tempProjectedy);
                            intersected = (dist <= vradius);
                        } else {
                            dist = length(ballx - linex1, bally - liney1);
                            float distance2 = length(ballx - linex2, bally - liney2);
                            if (dist < vradius) {
                                intersected = true;
                                tempProjectedx = linex1;
                                tempProjectedy = liney1;
                            }
                            if (distance2 < vradius && distance2 < dist) {
                                intersected = true;
                                tempProjectedx = linex2;
                                tempProjectedy = liney2;
                                dist = distance2;
                            }
                        }
                        if (!foundCollision || dist < closestDistance) {
                            closestDistance = dist;
                            foundCollision = intersected;
                            closestx = tempProjectedx;
                            closesty = tempProjectedy;
                            normalx = ballx - closestx;
                            normaly = bally - closesty;
                            float normalLength = length(normalx, normaly);
                            normalx /= normalLength;
                            normaly /= normalLength;
                        }
                    }
                    if (foundCollision) {
                        final float t = 0.5f;
                        vs[POS_X + v] = (normalx * vradius + closestx) * (1 - t);
                        vs[POS_Y + v] = (normaly * vradius + closesty) * (1 - t);
                        vs[POS_X + v] += vs[OLD_X + v] * t;
                        vs[POS_Y + v] += vs[OLD_Y + v] * t;
                        if (vs[TYPE + v] == RIDER) {
                            collidedWithRider = true;
                        }
                    }
                }
            }
            if (state == STATE_PLAYING && collidedWithRider) {
                state = STATE_DEAD;
                if (false) {
                    for (float[] e : new ArrayList<float[]>(constraints)) {
                        int v1 = (int) e[EDGE_V1];
                        int v2 = (int) e[EDGE_V2];
                        if (v1 == stearing || v2 == stearing) {
                            constraints.remove(e);
                        }
                        if (v1 == chain || v2 == chain) {
                            constraints.remove(e);
                        }
                    }
                    for (int v : riderVertices) {
                        vs[OLD_X + v * VTX_STRIDE] += (float) Math.random() * 10 - 5;
                        vs[OLD_Y + v * VTX_STRIDE] += 10;
                    }
                } else {
                    constraints.clear();
                    for (int v = 0; v < vertexCnt; v++) {
                        vs[OLD_X + v * VTX_STRIDE] += (float) Math.random() * 10 - 5;
                        vs[OLD_Y + v * VTX_STRIDE] += 10;
                    }
                }
            }
            long now = System.nanoTime();
            acc += now - lastTime;
            tick++;
            if (acc >= 1000000000L) {
                acc -= 1000000000L;
                fps = tick;
                tick = 0;
            }
            lastTime = now;
            int scrollx = 200;
            int scrolly = 400;
            {
                scrollx -= vs[POS_X + chain * VTX_STRIDE];
                scrolly -= vs[POS_Y + chain * VTX_STRIDE];
            }
            g.setColor(new Color(BACKGROUND_COLOR));
            g.fillRect(0, 0, 1024, 1024);
            g.translate(scrollx, scrolly);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int lineIdx = 0; lineIdx < f[levelOff + LINE_CNT_OFF]; lineIdx++) {
                int linex1 = (int) f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + X1];
                int liney1 = (int) f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + Y1];
                int linex2 = (int) f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + X2];
                int liney2 = (int) f[levelOff + LINE_OFF + OBJECT_STRIDE * lineIdx + Y2];
                g.drawLine(linex1, liney1, linex2, liney2);
            }
            for (int circleIdx = 0; circleIdx < f[levelOff + CIRCLE_CNT_OFF]; circleIdx++) {
                int x = (int) f[levelOff + CIRCLE_OFF + OBJECT_STRIDE * circleIdx + X1];
                int y = (int) f[levelOff + CIRCLE_OFF + OBJECT_STRIDE * circleIdx + Y1];
                int r = (int) f[levelOff + CIRCLE_OFF + OBJECT_STRIDE * circleIdx + CIRCLE_RADIUS];
                g.drawOval(x - r, y - r, r * 2, r * 2);
            }
            g.setStroke(new BasicStroke(1));
            for (int checkpointIdx = 0; checkpointIdx < f[levelOff + CHECKPOINT_CNT_OFF]; checkpointIdx++) {
                int x = (int) f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * checkpointIdx + X1];
                int y = (int) f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * checkpointIdx + Y1];
                g.setColor(f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * checkpointIdx + CHECKPOINT_PASSED] > 0 ? Color.GREEN : Color.RED);
                g.drawLine(x, y, x, y - 100);
            }
            g.setColor(Color.WHITE);
            for (int off = 0; off < vertexCnt * VTX_STRIDE; off += VTX_STRIDE) {
                float r = vs[RADIUS + off];
                g.fillOval(Math.round(vs[POS_X + off] - r), Math.round(vs[POS_Y + off] - r), (int) r * 2, (int) r * 2);
            }
            for (float[] edge : constraints) {
                int v1Off = ((int) edge[EDGE_V1]) * VTX_STRIDE;
                int v2Off = ((int) edge[EDGE_V2]) * VTX_STRIDE;
                if (edge[EDGE_VISIBLE] != 0) {
                    g.drawLine(Math.round(vs[POS_X + v1Off]), Math.round(vs[POS_Y + v1Off]), Math.round(vs[POS_X + v2Off]), Math.round(vs[POS_Y + v2Off]));
                }
            }
            g.translate(-scrollx, -scrolly);
            g.setColor(Color.white);
            g.drawString("FPS " + String.valueOf(fps), 20, 30);
            if (state == STATE_DEAD || state == STATE_FINISHED) {
                g.drawString("1-9 to restart level", 20, 70);
            }
            if (state == STATE_PLAYING) {
                g.drawString("BACKSPACE to continue from last checkpoint", 20, 50);
            }
            if (state == STATE_FINISHED) {
                g.drawString("FINISHED", 20, 130);
            }
            g.drawString("Tries " + tries, 20, 90);
            g.drawString("time " + ((System.currentTimeMillis() - startTime) / 1000) + " " + yieldCnt, 20, 110);
            appletGraphics.drawImage(screen, 0, 0, null);
            yieldCnt = 0;
            do {
                Thread.yield();
                yieldCnt++;
            } while (System.nanoTime() - lastTime < 16000000);
        } while (isActive());
    }

    private void loadLevel() {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream("data.bin"));
            int levelCnt = in.readByte();
            f[LEVEL_CNT_OFF] = levelCnt;
            for (int levelIdx = 0; levelIdx < levelCnt; levelIdx++) {
                int levelOff = levelIdx * LEVEL_STRIDE;
                int lineArrayCnt = in.readByte();
                for (int lineArrayIdx = 0; lineArrayIdx < lineArrayCnt; lineArrayIdx++) {
                    int pointCnt = in.readByte();
                    f[levelOff + LINE_CNT_OFF] = pointCnt - 1;
                    float lastx = 0;
                    float lasty = 0;
                    int dstIdx = levelOff + LINE_OFF;
                    for (int pointIdx = 0; pointIdx < pointCnt; pointIdx++) {
                        float x = in.readUnsignedShort();
                        float y = in.readUnsignedShort();
                        if (pointIdx > 0) {
                            f[dstIdx + X1] = lastx;
                            f[dstIdx + Y1] = lasty;
                            f[dstIdx + X2] = x;
                            f[dstIdx + Y2] = y;
                            dstIdx += OBJECT_STRIDE;
                        }
                        lastx = x;
                        lasty = y;
                    }
                }
                int circleCnt = in.readByte();
                f[levelOff + CIRCLE_CNT_OFF] = circleCnt;
                for (int i = 0; i < circleCnt; i++) {
                    f[levelOff + CIRCLE_OFF + i * OBJECT_STRIDE + X1] = in.readUnsignedShort();
                    f[levelOff + CIRCLE_OFF + i * OBJECT_STRIDE + Y1] = in.readUnsignedShort();
                    f[levelOff + CIRCLE_OFF + i * OBJECT_STRIDE + CIRCLE_RADIUS] = in.readUnsignedByte();
                }
                int checkpointCnt = in.readByte();
                f[levelOff + CHECKPOINT_CNT_OFF] = checkpointCnt;
                for (int i = 0; i < checkpointCnt; i++) {
                    f[levelOff + CHECKPOINT_OFF + i * OBJECT_STRIDE + X1] = in.readUnsignedShort();
                    f[levelOff + CHECKPOINT_OFF + i * OBJECT_STRIDE + Y1] = in.readUnsignedShort();
                }
            }
            currentCheckpoint = 0;
            tries = 0;
            startTime = System.currentTimeMillis();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        respawn();
    }

    private void respawn() {
        vertexCnt = 0;
        vs = new float[1000];
        constraints.clear();
        float hard = 1f;
        backWheel = createWheel(48, 125, 38, 9);
        printMass();
        frontWheel = createWheel(223, 125, 38, 9);
        printMass();
        int frontWheelCenter = (frontWheel - 1);
        int backWheelCenter = (backWheel - 1);
        stearing = createVertex(176, 19, 10);
        chain = createVertex(125, 113, 13);
        int skjerm = createVertex(70, 59, 12);
        int[] bike = { stearing, chain, skjerm };
        addRandom(createEdges(bike, hard));
        float[] frontSpring = createEdge(this.vs, frontWheelCenter, stearing, 0.08f, 0);
        frontSpring[EDGE_DAMPING] = 0.02f;
        frontSpring[EDGE_MAX_LENGTH] = 116;
        frontSpring[EDGE_MIN_LENGTH] = 90;
        frontSpring[EDGE_LENGTH] = 120;
        addRandom(frontSpring);
        addRandom(frontSpring);
        addRandom(frontSpring);
        addRandom(createEdge(this.vs, frontWheelCenter, chain, hard, 0));
        addRandom(createEdge(this.vs, frontWheelCenter, chain, hard, 0));
        addRandom(createEdge(this.vs, frontWheelCenter, chain, hard, 0));
        addRandom(createEdge(this.vs, backWheelCenter, chain, hard, 0));
        addRandom(createEdge(this.vs, backWheelCenter, chain, hard, 0));
        addRandom(createEdge(this.vs, backWheelCenter, chain, hard, 0));
        float[] e = createEdge(this.vs, backWheelCenter, skjerm, .08f, 0);
        e[EDGE_MAX_LENGTH] = 69;
        e[EDGE_MIN_LENGTH] = 50;
        e[EDGE_DAMPING] = 0.02f;
        addRandom(e);
        addRandom(e);
        addRandom(e);
        shoulders = createVertex(107, 15, 22);
        foot = createVertex(125, 102, 10);
        vs[COLLIDABLE + foot * VTX_STRIDE] = 0;
        hands = createVertex(175, 19, 10);
        riderVertices = new int[] { shoulders, foot, hands, stearing, chain };
        for (int riderIdx = 0; riderIdx < 1; riderIdx++) {
            vs[TYPE + riderVertices[riderIdx] * VTX_STRIDE] = RIDER;
        }
        int tempVertexCnt = vertexCnt;
        int[] vs2 = { createVertex(155, 0, 15), createVertex(125, 102, 10), createVertex(175, 19, 10), stearing, chain };
        riderEdges1 = createEdges(riderVertices, 0.9f);
        riderEdges2 = createEdges(vs2, 0.9f);
        riderEdges = createEdges(riderVertices, 0.9f);
        addRandom(riderEdges);
        vertexCnt = tempVertexCnt;
        int levelOff = currentLevelIdx * LEVEL_STRIDE;
        int x = (int) f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * currentCheckpoint + X1];
        int y = (int) f[levelOff + CHECKPOINT_OFF + OBJECT_STRIDE * currentCheckpoint + Y1];
        for (int v = 0; v < vertexCnt * VTX_STRIDE; v += VTX_STRIDE) {
            vs[POS_X + v] += (x - 125);
            vs[POS_Y + v] += (y - 225);
            vs[OLD_X + v] = vs[POS_X + v];
            vs[OLD_Y + v] = vs[POS_Y + v];
            vs[ACC_X + v] = 0;
            vs[ACC_Y + v] = 0;
        }
        tries++;
        if (currentCheckpoint == 0) {
            tries = 0;
            startTime = System.currentTimeMillis();
        }
        System.out.println("Edge count: " + constraints.size() + " " + vertexCnt);
    }

    private void writeBikeAndRider() {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            int off = 0;
            for (int vIdx = 0; vIdx < vertexCnt; vIdx++) {
                float x = vs[POS_X + off];
                float y = vs[POS_Y + off];
                float radius = vs[RADIUS + off];
                float mass = vs[MASS + off];
                float collidable = vs[COLLIDABLE + off];
                System.out.println("(" + (int) x + "," + (int) y + ") " + radius + " " + mass + " " + collidable);
                off += VTX_STRIDE;
                dataOut.writeByte((byte) x);
                dataOut.writeByte((byte) y);
                dataOut.writeByte((byte) radius);
                dataOut.writeShort((short) mass);
                dataOut.writeBoolean(collidable == 1);
            }
            for (float[] ed : constraints) {
                System.out.println("" + " " + (int) ed[EDGE_V1] + " " + (int) ed[EDGE_V2] + " " + ed[EDGE_DAMPING] + " " + ed[EDGE_MAX_LENGTH] + " " + ed[EDGE_VISIBLE]);
                dataOut.writeByte((byte) ed[EDGE_V1]);
                dataOut.writeByte((byte) ed[EDGE_V2]);
                dataOut.writeByte((byte) ed[EDGE_LENGTH]);
                dataOut.writeByte((byte) ed[EDGE_DAMPING]);
                dataOut.writeByte((byte) ed[EDGE_MAX_LENGTH]);
                dataOut.writeBoolean(ed[EDGE_VISIBLE] == 1);
            }
            System.out.println("Vertices: " + vertexCnt);
            System.out.println("Edges: " + constraints.size());
            System.out.println("bytes: " + byteOut.toByteArray().length);
            FileOutputStream fileOut = new FileOutputStream("rider.bin");
            fileOut.write(byteOut.toByteArray());
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addRandom(List<float[]> newConstraints) {
        for (float[] c : newConstraints) {
            addRandom(c);
        }
    }

    public void addRandom(float[] constraint) {
        constraints.add((int) (Math.random() * constraints.size()), constraint);
    }

    public int createVertex(float x, float y, float radius) {
        int idx = vertexCnt;
        vertexCnt++;
        int off = idx * VTX_STRIDE;
        vs[POS_X + off] = x;
        vs[POS_Y + off] = y;
        vs[OLD_X + off] = x;
        vs[OLD_Y + off] = y;
        vs[RADIUS + off] = radius;
        vs[MASS + off] = (float) Math.PI * radius * radius;
        vs[COLLIDABLE + off] = 1;
        return idx;
    }

    @Override
    public boolean handleEvent(Event e) {
        switch(e.id) {
            case Event.KEY_ACTION:
            case Event.KEY_PRESS:
                changed[e.key] = k[e.key] == false;
                k[e.key] = true;
                break;
            case Event.KEY_ACTION_RELEASE:
            case Event.KEY_RELEASE:
                changed[e.key] = k[e.key] == true;
                k[e.key] = false;
                break;
        }
        return false;
    }

    public List<float[]> createEdges(int[] verts, float stiffness) {
        List<float[]> edges = new ArrayList();
        for (int i = 0; i < verts.length; i++) {
            for (int j = i + 1; j < verts.length; j++) {
                float[] edge = createEdge(vs, verts[i], verts[j], stiffness, 0);
                edges.add(edge);
            }
        }
        return edges;
    }

    public int createWheel(float wheelx, float wheely, float wheelRadius, float tireRadius) {
        createVertex(wheelx, wheely, 10);
        int vsIdx = vertexCnt;
        for (float angle = 0; angle < 2 * Math.PI - 0.01f; angle += 2 * Math.PI / WIRE_COUNT) {
            float x = wheelx + (float) Math.cos(angle) * wheelRadius;
            float y = wheely + (float) Math.sin(angle) * wheelRadius;
            int v = createVertex(x, y, tireRadius);
            int vOff = v * VTX_STRIDE;
            vs[MASS + vOff] = 175f;
            vs[OLD_X + vOff] = vs[POS_X + vOff] - 3;
        }
        float lowStiff = 1f;
        float middleStiff = 1f;
        float highStiff = 1f;
        float damping = 0f;
        for (int i = 0; i < WIRE_COUNT; i++) {
            int i1 = vsIdx + i;
            int i2 = vsIdx + (i + 1) % WIRE_COUNT;
            int i3 = vsIdx + (i + (WIRE_COUNT * 1 / 3)) % WIRE_COUNT;
            addRandom(createEdge(vs, vsIdx - 1, i1, middleStiff, damping));
            addRandom(createEdge(vs, i1, i2, highStiff, damping));
            addRandom(createEdge(vs, i1, i3, lowStiff, damping));
        }
        return vsIdx;
    }

    /**
	 * Calculates the length of the (x, y) vector.
	 */
    public static final float length(float x, float y) {
        return (float) Math.sqrt(y * y + x * x);
    }

    public float[] createEdge(float[] edgeVertices, int v1, int v2, float stiffness, float damping) {
        float[] ed = new float[9];
        ed[EDGE_STIFFNESS] = stiffness;
        float dx = vs[POS_X + v1 * VTX_STRIDE] - vs[POS_X + v2 * VTX_STRIDE];
        float dy = vs[POS_Y + v1 * VTX_STRIDE] - vs[POS_Y + v2 * VTX_STRIDE];
        ed[EDGE_LENGTH] = length(dx, dy);
        ed[EDGE_OO_TOTAL_MASS] = 1f / (edgeVertices[MASS + v1 * VTX_STRIDE] + edgeVertices[MASS + v1 * VTX_STRIDE]);
        ed[EDGE_DAMPING] = damping;
        ed[EDGE_MAX_LENGTH] = 255f;
        ed[EDGE_VISIBLE] = 1;
        ed[EDGE_V1] = v1;
        ed[EDGE_V2] = v2;
        return ed;
    }

    private void drawCenterOfMass(Graphics2D g) {
        float xSum = 0;
        float ySum = 0;
        float massSum = 0;
        for (int i = 0; i < vertexCnt; i++) {
            float mass = vs[MASS + i * VTX_STRIDE];
            xSum += vs[POS_X + i * VTX_STRIDE] * mass;
            ySum += vs[POS_Y + i * VTX_STRIDE] * mass;
            massSum += mass;
        }
        int x = Math.round(xSum / massSum);
        int y = Math.round(ySum / massSum);
        g.setColor(Color.RED);
        g.drawRect(x - 5, y - 5, 10, 10);
    }

    private void printMass() {
        float massSum = 0;
        for (int i = 0; i < vertexCnt; i++) {
            massSum += vs[MASS + i * VTX_STRIDE];
        }
        System.out.println("MassSum " + massSum);
    }

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 1000, 1000);
        g.drawString("Hey hey hey", 20, 20);
        g.drawString("Hellooow World", 20, 40);
    }
}
