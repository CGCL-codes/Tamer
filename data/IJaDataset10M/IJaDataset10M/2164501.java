package jfuzz.util;

import gov.nasa.jpf.util.Files;
import java.io.File;
import java.io.IOException;
import jfuzz.JFuzz;

/**
 * Contains methods and time files for measuring timing in jFuzz
 * 
 * @author dharv
 */
public class MeasureTiming {

    public static final File TOTAL_TIME = new File(JFuzz.GEN_DIR, "totalTime.txt");

    public static final File FUZZ_TIME = new File(JFuzz.GEN_DIR, "fuzzTime.txt");

    public static final File SOLVE_TIME = new File(JFuzz.GEN_DIR, "solveTime.txt");

    public static final File SIMPLIFY_TIME = new File(JFuzz.GEN_DIR, "simplifyTime.txt");

    public static final File CREATING_INPUTS = new File(JFuzz.GEN_DIR, "createTime.txt");

    public static final File JPF_RUNTIME = new File(JFuzz.GEN_DIR, "jpfTime.txt");

    private static final File jpfStartLog = new File(JFuzz.GEN_DIR, "jpfStart.log");

    private static final File measureTime = new File(JFuzz.GEN_DIR, "measureTime.log");

    static final File TIME_AGGREGATES = new File(JFuzz.GEN_DIR, "timeAgg.csv");

    public static void measureTime(boolean b) throws IOException {
        if (b) Files.writeToFile("", measureTime);
    }

    private static boolean calcTime() {
        return measureTime.exists();
    }

    public static void updateTimeLog(long start, File log) {
        if (calcTime()) {
            long prev = 0l;
            if (log.exists()) prev = Files.readLongFromFile(log);
            long next = (System.currentTimeMillis() - start) + prev;
            try {
                Files.writeToFile(next + "", log);
            } catch (IOException e) {
                System.out.println("Timing failed!");
            }
        }
    }

    /**
     * Because JPF is called in other JVMs we need to write out the start time.
     */
    public static void setStartJPF() {
        if (calcTime()) {
            try {
                Files.writeToFile(System.currentTimeMillis() + "", jpfStartLog);
            } catch (IOException e) {
            }
        }
    }

    /**
     * Records time elapsed from when setStartJPF was last called
     */
    public static void recordTimeJPF() {
        if (calcTime()) updateTimeLog(Files.readLongFromFile(jpfStartLog), JPF_RUNTIME);
    }

    public static void aggregateTimes(int runCount) {
        if (calcTime()) {
            try {
                if (!TIME_AGGREGATES.exists()) {
                    Files.writeToFile("run,total,jpf,fuzz,solve,simplify,inputs\n", TIME_AGGREGATES, false);
                }
                long total = Files.readLongFromFile(TOTAL_TIME);
                long jpf = Files.readLongFromFile(JPF_RUNTIME);
                long fuzz = Files.readLongFromFile(FUZZ_TIME);
                long solve = Files.readLongFromFile(SOLVE_TIME);
                long simplify = Files.readLongFromFile(SIMPLIFY_TIME);
                long inputs = Files.readLongFromFile(CREATING_INPUTS);
                String line = runCount + "," + total + "," + jpf + "," + fuzz + "," + solve + "," + simplify + "," + inputs + "\n";
                Files.writeToFile(line, TIME_AGGREGATES, true);
            } catch (IOException e) {
                System.out.println("Failed to Aggregate Times!");
            }
        }
    }

    public static long readLog(File log) {
        if (calcTime() && log.exists()) return Files.readLongFromFile(log); else return -1;
    }
}
