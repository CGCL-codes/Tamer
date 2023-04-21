package sumoTween.timer;

public class freeTimer implements TimerIF {

    public static freeTimer timer = new freeTimer();

    long pauseStartTime = 0;

    long pauseTime = 0;

    public long starttime = -1;

    public float maxTime = 0.0f;

    public float diff = 0.0f;

    public freeTimer() {
        reset();
    }

    public long getSysTime() {
        return System.currentTimeMillis();
    }

    public void reset() {
        starttime = getSysTime();
    }

    public float getTime() {
        if (starttime == -1) {
            reset();
        }
        float t = (getSysTime() - starttime - pauseTime) / 1000.0f;
        if (maxTime != 0.0f && t > maxTime) {
            starttime = getSysTime();
            t = 0;
        }
        return t;
    }

    public long timeSincePause() {
        return getSysTime() - pauseStartTime;
    }

    public void pause(boolean t) {
        if (t) {
            pauseStartTime = getSysTime();
        } else {
            pauseTime = getSysTime() - pauseStartTime;
        }
    }

    public String getTitle() {
        return null;
    }

    public boolean isPlaying() {
        return true;
    }

    public void pause() {
    }

    public void play() {
    }

    public void setPlayRange(float start, float end) {
    }

    public void setTime(float f) {
        starttime = System.currentTimeMillis() - (long) (f * 1000f);
    }
}
