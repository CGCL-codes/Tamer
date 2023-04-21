package org.srfc.driftscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.sun.org.apache.bcel.internal.generic.DLOAD;
import net.sf.jinsim.types.CompCar;

/**
 * This is for multiplayrer scoring
 * @author nobel
 *
 */
public class Scorer {

    public static final int FLAG_TOTAL = 0x01;

    public static final int FLAG_SECTION = 0x02;

    public static final int FLAG_ANGLE = 0x03;

    private final Executor executor;

    public static final int ANG_START = 10;

    public static final int ANG_MID = 35;

    public static final int ANG_BIG = 70;

    public static final int ANG_OVER = 85;

    public static final int SPEED_START = 30;

    public static final int INTERVAL_CONTINUOUS = 520;

    private HashSet<Byte> dromometerSet = new HashSet<Byte>();

    public Scorer(Executor e) {
        this.executor = e;
    }

    public Score get(byte plid) {
        synchronized (executor.ID_PLAYER_TABLE) {
            Player p = executor.ID_PLAYER_TABLE.get(plid);
            if (p != null) return p.getScore(); else {
                return null;
            }
        }
    }

    public void put(byte plid, Score s) {
        synchronized (executor.ID_PLAYER_TABLE) {
            Player p = executor.ID_PLAYER_TABLE.get(plid);
            if (p == null) return;
            p.setScore(s);
        }
    }

    /**clear saved date for memory*/
    public void clear() {
        synchronized (executor.ID_PLAYER_TABLE) {
            for (Player p : executor.ID_PLAYER_TABLE.values()) {
                if (p.getScore() != null) p.getScore().resetAll();
            }
        }
    }

    /** clear specific player score */
    public void clear(byte plid) {
        synchronized (executor.ID_PLAYER_TABLE) {
            Player p = executor.ID_PLAYER_TABLE.get(plid);
            if (p != null && p.getScore() != null) p.getScore().resetAll();
        }
    }

    private float speed, angDiff, dir, heading;

    private void initCal(CompCar car) {
        speed = car.getSpeed();
        dir = car.getDirection();
        heading = car.getHeading();
        speed = speed / 32768f * 360f;
        angDiff = (float) Math.abs((dir - heading) * 180 / 32768.0);
        angDiff = angDiff % 360.01f;
        angDiff = angDiff > 180 ? 360 - angDiff : angDiff;
        if (angDiff < 0.1 || speed < 1) angDiff = 0;
    }

    private float calculate(Score score) {
        float delta_t = (System.currentTimeMillis() - score.updateTime) / 1000f;
        float last_t = (System.currentTimeMillis() - score.driStartTime) / 1000f;
        float multi = 3f;
        float appCoe = 0.2f;
        float s_ang = angDiff > ANG_MID ? (multi - (multi - 1) / (ANG_OVER - ANG_MID) * (angDiff - ANG_MID)) : (1 + (multi - 1) / (ANG_MID - ANG_START) * (angDiff - ANG_START));
        float s_time = last_t > 6 ? multi : (1 + ((multi - 1) / 6f) * last_t);
        float result = speed * delta_t * appCoe * s_ang * s_time;
        return result;
    }

    private boolean isDrifting(CompCar car, Score score) {
        boolean result = false;
        if (speed < SPEED_START) result = false; else if (angDiff > ANG_START && angDiff < ANG_OVER) result = true;
        return result;
    }

    /** this is the main update method*/
    public void updateScore(CompCar car) {
        if (!executor.initialized) return;
        initCal(car);
        byte plid = car.getPlayerId();
        Score score = get(plid);
        if (score == null) {
            score = new Score();
            put(plid, score);
        }
        dromometer(plid, score);
        boolean isDrifting_now = isDrifting(car, score);
        score.angleDiff = angDiff;
        score.speed = speed;
        boolean isDrifting_last = score.isDrifting();
        if (!isDrifting_last && !isDrifting_now) {
            long curTime = System.currentTimeMillis();
            if (!score.isScoreRecorded && curTime - score.driftEndTime > INTERVAL_CONTINUOUS) {
                float s_sec = score.getCurrentSecScore();
                if (s_sec >= 1) {
                    score.addSecScore(System.currentTimeMillis(), s_sec);
                    executor.noticeDisplayer(plid, Displayer.DIS_TYPE_SECSCORE);
                }
            } else return;
        } else if (isDrifting_last && !isDrifting_now) {
            score.driftInterval();
        } else {
            if (!isDrifting_last && isDrifting_now && score.isScoreRecorded) {
                score.restartScoring();
            }
            float s_delta = calculate(score);
            score.currentSecScore += s_delta;
            score.updateTime = System.currentTimeMillis();
        }
    }

    public void noticeDromometer(byte plid) {
        synchronized (dromometerSet) {
            dromometerSet.add(plid);
        }
    }

    private void dromometer(byte plid, Score score) {
        if (score == null) return;
        if (!dromometerSet.contains(plid)) return;
        score.setSplitSpeed((int) speed);
        dromometerSet.remove(plid);
        executor.noticeDisplayer(plid, Displayer.DIS_TYPE_SPLITSPEED);
    }

    public class Score {

        private float totalScore = 0;

        private ArrayList<float[]> secScore = new ArrayList<float[]>();

        private float currentSecScore = 0;

        private float angleDiff = 0;

        private float speed = 0;

        private long driStartTime = System.currentTimeMillis();

        private long updateTime = System.currentTimeMillis();

        private long driftEndTime = System.currentTimeMillis();

        private boolean isDrifting = false;

        private boolean isScoreRecorded = true;

        private int splitSpeed = 0;

        public Score() {
        }

        public void addSecScore(long driSecEndTime, float curSecScore) {
            this.secScore.add(new float[] { driSecEndTime - driStartTime, curSecScore });
            totalScore += curSecScore;
            isDrifting = false;
            isScoreRecorded = true;
            currentSecScore = 0;
        }

        public void restartScoring() {
            currentSecScore = 0;
            driStartTime = System.currentTimeMillis();
            driftEndTime = driStartTime;
            updateTime = driStartTime;
            isDrifting = true;
            isScoreRecorded = false;
        }

        public void resetAll() {
            totalScore = 0;
            secScore.clear();
            currentSecScore = 0;
            angleDiff = 0;
            speed = 0;
            driStartTime = System.currentTimeMillis();
            driftEndTime = driStartTime;
            updateTime = driStartTime;
            isDrifting = false;
            isScoreRecorded = true;
        }

        public void driftInterval() {
            isDrifting = false;
            driftEndTime = System.currentTimeMillis();
            isScoreRecorded = false;
        }

        public float getTotalScore() {
            return totalScore;
        }

        public ArrayList<float[]> getSecScore() {
            return new ArrayList<float[]>(secScore);
        }

        public float getCurrentSecScore() {
            return currentSecScore;
        }

        public float getAngleDiff() {
            return angleDiff;
        }

        public float getSpeed() {
            return speed;
        }

        public boolean isDrifting() {
            return isDrifting;
        }

        public int getSplitSpeed() {
            return splitSpeed;
        }

        public void setSplitSpeed(int splitSpeed) {
            this.splitSpeed = splitSpeed;
        }
    }
}
