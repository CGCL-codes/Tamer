package test;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.RelativeTime;
import javax.realtime.Timer;
import jtools.jargo.ArgParser;
import jtools.jargo.ArgSpec;
import jtools.jargo.ArgValue;
import jtools.jargo.CommandLineSpec;
import jtools.time.HighResTimer;

public class ClockTest {

    public static void main(String[] args) throws Exception {
        CommandLineSpec cmd_line_spec = new CommandLineSpec();
        ArgSpec num_it = new ArgSpec("iteration", new String[] { "jtools.jargo.NaturalValidator" });
        ArgSpec type = new ArgSpec("rtsj");
        ArgSpec lin = new ArgSpec("linearity");
        cmd_line_spec.addRequiredArg(num_it);
        cmd_line_spec.addArg(type);
        cmd_line_spec.addArg(lin);
        ArgParser arg_parser = new ArgParser(cmd_line_spec);
        arg_parser.parse(args);
        ArgValue value = arg_parser.getArg("iteration");
        int limit = ((Integer) value.getValue()).intValue();
        if (arg_parser.getArg("rtsj") != null) {
            Clock rtsj_clock = Clock.getRealtimeClock();
            AbsoluteTime before = rtsj_clock.getTime();
            for (int i = 0; i < limit; i++) {
            }
            RelativeTime delta = rtsj_clock.getTime().subtract(before);
            System.out.println("[" + limit + " iteration] rtsj_timer = " + delta);
        }
        HighResTimer hrc_timer = new HighResTimer();
        hrc_timer.start();
        for (int i = 0; i < limit; i++) {
        }
        hrc_timer.stop();
        System.out.println("[" + limit + " iteration] jtools_timer = " + hrc_timer.getElapsedTime() + " ms");
        long java_clock = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
        }
        java_clock = System.currentTimeMillis() - java_clock;
        System.out.println("[" + limit + " iteration] java_timer = " + java_clock + " ms");
        int k = 1;
        if (arg_parser.getArg("linearity") != null) {
            while (true) {
                hrc_timer.start();
                for (int i = 0; i < k; i++) {
                }
                hrc_timer.stop();
                System.out.println("time for " + k + " iterations = " + hrc_timer.getElapsedTime());
                if (hrc_timer.getElapsedTime().getMilliSec() >= 1) break;
                k++;
            }
        }
    }
}
