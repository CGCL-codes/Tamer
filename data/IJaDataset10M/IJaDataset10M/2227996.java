package no.ntnu.idi.glimps;

import no.ntnu.idi.glimps.tweeners.*;

public class Animator {

    private static Tweener tweener = new InverseTweener(new CircularTweener());

    public static Tweener getTweener() {
        return tweener;
    }

    public static synchronized void setTweener(Tweener tweener) {
        Animator.tweener = tweener;
    }

    private static int animationDuration = 5000;

    private static int peakIntensityAt = 1000;

    public static synchronized void setAnimationDuration(int duration) {
        Animator.animationDuration = duration;
        Animator.peakIntensityAt = duration / 5;
    }

    public static int getAnimationDuration() {
        return animationDuration;
    }

    public static int getPeakIntensityAt() {
        return peakIntensityAt;
    }
}
