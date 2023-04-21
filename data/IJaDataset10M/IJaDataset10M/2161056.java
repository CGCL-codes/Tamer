package net.sf.opendf.profiler.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import net.sf.opendf.profiler.data.Step;
import net.sf.opendf.profiler.data.Trace;
import net.sf.opendf.profiler.io.TraceParser;
import net.sf.opendf.profiler.schedule.BasicResourceScheduler;
import net.sf.opendf.profiler.schedule.Duration;
import net.sf.opendf.profiler.schedule.ResourceScheduler;
import net.sf.opendf.profiler.schedule.ScheduleOutput;
import net.sf.opendf.profiler.schedule.Scheduler;
import net.sf.opendf.profiler.schedule.SchedulerWithResourceConstraints;
import net.sf.opendf.profiler.schedule.SimpleResourceScheduler;
import net.sf.opendf.profiler.schedule.Time;
import net.sf.opendf.profiler.schedule.data.ResourceConfiguration;
import net.sf.opendf.profiler.util.Lib;

public class AnnotateLatency {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        System.out.println("Reading trace '" + args[0] + "'...");
        TraceParser tp = new TraceParser(new FileInputStream(args[0]), TraceParser.DEPS_DF);
        Trace t = tp.parse();
        System.out.println("Reading resource config '" + args[1] + "'...");
        ResourceConfiguration rc = ResourceConfiguration.createConfiguration(new FileInputStream(args[1]));
        System.out.println("Scheduling...");
        annotateWithLatency(t, rc);
        FileOutputStream os = new FileOutputStream(args[2]);
        PrintWriter pw = new PrintWriter(os);
        System.out.println("\nWriting trace '" + args[2] + "'...");
        Lib.writeTrace(t, pw);
        pw.close();
        os.close();
        System.out.println("Done.");
    }

    public static void annotateWithLatency(Trace t, ResourceConfiguration rc) {
        Trace t1 = (Trace) t.clone();
        Scheduler s = new SchedulerWithResourceConstraints(new BasicResourceScheduler(rc));
        s.schedule(t1, new TraceAnnotationScheduleOutput(t));
    }

    static class TraceAnnotationScheduleOutput implements ScheduleOutput {

        public void attribute(Object key, Object value) {
        }

        public void beginStep(Object stepID, Time start, Duration duration) {
            Step s = trace.getStep((Integer) stepID);
            s.setAttribute("startTime", new Double(start.value()));
            s.setAttribute("endTime", new Double(start.add(duration).value()));
        }

        public void endStep() {
        }

        public void executeStep(Object stepID, Time start, Duration duration) {
            beginStep(stepID, start, duration);
            endStep();
        }

        public void finish(Time tm) {
        }

        public void start() {
        }

        public TraceAnnotationScheduleOutput(Trace t) {
            this.trace = t;
        }

        private Trace trace;
    }
}
