package net.ko.utils;

import java.util.HashMap;

public class KScriptTimer {

    private static HashMap<Object, Long> timer = new HashMap<Object, Long>();

    private static long start = 0;

    public static void start() {
        KScriptTimer.start = System.nanoTime();
    }

    public static void stop() {
        float duree = (System.nanoTime() - KScriptTimer.start) / 1000000f;
        System.out.println(duree);
    }

    public static void stop(String timer) {
        if (KScriptTimer.timer.containsKey(timer)) {
            float duree = (System.nanoTime() - KScriptTimer.timer.get(timer)) / 1000000f;
            System.out.println(duree);
        }
    }

    public static void start(String timer) {
        KScriptTimer.timer.put(timer, System.nanoTime());
    }

    public static float get(String timer) {
        float result = 0;
        if (KScriptTimer.timer.containsKey(timer)) {
            result = (System.nanoTime() - KScriptTimer.timer.get(timer)) / 1000000f;
        }
        return result;
    }

    public static float get() {
        float result = (System.nanoTime() - KScriptTimer.start) / 1000000f;
        ;
        return result;
    }
}
