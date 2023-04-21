package tests.jfun.yan.benchmark;

import java.text.NumberFormat;

/**
 * 
 * <p>
 * Benchmark Helper class
 * </p>
 * 
 * <p>Copyright: Copyright (c) 2003-2005 NonSoft.com</p>
 * 
 * @author Shawn Chain
 * @version 2.0, $Revision: 1.1 $, $Date: 2006/01/06 16:59:22 $
 */
public abstract class Benchmark {

    private String title;

    private long loops;

    private double average;

    private double speed;

    private long elapsed;

    public Benchmark(String title, long loops) {
        this.title = title;
        this.loops = loops;
    }

    public long start() {
        return start(false);
    }

    public long start(boolean forceRun) {
        long loop = loops;
        if (!forceRun) {
            if (System.getProperty("bench") == null) {
                System.out.println("Benchmark is disabled, to enable it, append \"-Dbench=true\" to jvm startup args");
                return 0;
            }
        }
        try {
            Thread.sleep(100);
            System.gc();
        } catch (Exception e) {
        }
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < loop; i++) {
                run();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        long end = System.currentTimeMillis();
        elapsed = end - start;
        average = ((double) elapsed) / ((double) loop);
        speed = (double) loop / (double) elapsed * (double) 1000;
        NumberFormat nf = NumberFormat.getNumberInstance();
        StringBuffer sb = new StringBuffer();
        sb.append("Benchmark of [").append(title).append("]\n");
        sb.append("  - Loops:\t").append(nf.format(loops)).append("\n");
        sb.append("  - Elapsed:\t").append(elapsed).append(" ms\n");
        sb.append("  - Average:\t").append(average).append(" ms/call\n");
        sb.append("  - Speed:\t").append(nf.format(speed)).append(" calls/scond");
        System.out.println(sb.toString());
        return elapsed;
    }

    public abstract void run() throws Exception;

    public double getAverage() {
        return average;
    }

    public long getElapsed() {
        return elapsed;
    }

    public long getLoops() {
        return loops;
    }

    public double getSpeed() {
        return speed;
    }

    public String getTitle() {
        return title;
    }
}
