package gov.nasa.jpf.jvm;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPFException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.BitSet;

/**
 * a scheduler that exlicitly asks the user for the next thread to pick
 */
public class InteractiveScheduler extends Scheduler {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static boolean quit = false;

    private int thread;

    private int random;

    private boolean lastRandom;

    public InteractiveScheduler(Config config) {
        initialize();
    }

    public int getRandom() {
        return random;
    }

    public int getThread() {
        return thread;
    }

    public void initialize() {
        thread = 0;
        random = -1;
        lastRandom = true;
    }

    public ThreadInfo locateThread(SystemState ss) {
        if (quit) {
            return null;
        }
        int nthreads = ss.getThreadCount();
        int nrunnable = 0;
        BitSet runnable = new BitSet();
        for (int i = 0; i < nthreads; i++) {
            ThreadInfo th = ss.getThreadInfo(i);
            if (th.isRunnable()) {
                runnable.set(i);
                nrunnable++;
            }
        }
        if (nrunnable == 0) {
            System.out.println("Deadlock: backing up");
            return null;
        }
        System.out.print("Runnable threads [" + nrunnable + "]: ");
        for (int i = 0; i < nthreads; i++) {
            if (runnable.get(i)) {
                if (i == thread) {
                    System.out.print("[" + i + "] ");
                } else {
                    System.out.print(i + " ");
                }
            }
        }
        System.out.println();
        while (true) {
            try {
                System.out.print("> ");
                String s = br.readLine();
                if (s.equals("quit") || s.equals("q")) {
                    quit = true;
                    return null;
                } else if (s.equals("backtrack") || s.equals("back") || s.equals("b")) {
                    return null;
                } else if (s.equals("show") || s.equals("s")) {
                    while (true) {
                        System.out.print("show> ");
                        s = br.readLine();
                        if (s.equals("")) {
                            break;
                        }
                        try {
                            int l = Integer.parseInt(s);
                            if (runnable.get(l)) {
                                ThreadInfo th = (ThreadInfo) ss.getThreadInfo(l);
                                System.out.println(th.getMethod().getCompleteName() + ":" + th.getPC().getPosition() + "  " + th.getPC());
                                break;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (s.equals("random") || s.equals("r")) {
                    while (true) {
                        System.out.println("Current random: " + random);
                        System.out.print("random> ");
                        s = br.readLine();
                        if (s.equals("")) {
                            break;
                        }
                        try {
                            random = Integer.parseInt(s);
                            break;
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (s.equals("help") || s.equals("h") || s.equals("?")) {
                    System.out.println("command:");
                    System.out.println("  quit");
                    System.out.println("  random");
                    System.out.println("  backtrack");
                    System.out.println("  help");
                } else {
                    if (runnable.get(thread) && s.equals("")) {
                        break;
                    }
                    try {
                        int i = Integer.parseInt(s);
                        if (runnable.get(i)) {
                            thread = i;
                            break;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
                throw new JPFException(e.getMessage());
            }
        }
        System.out.println("Running thread " + thread);
        return ss.getThreadInfo(thread);
    }

    public void next() {
        if (lastRandom) {
            random = -1;
            thread++;
        } else {
            random++;
        }
    }

    public int random(int max) {
        if (random == -1) {
            random = 0;
        }
        lastRandom = (random == max - 1);
        return random;
    }
}
