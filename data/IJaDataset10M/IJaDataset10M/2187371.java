package xesloganalyzer;

/**
 *
 * @author XY
 */
public class LoopChecker {

    private XESLog log;

    private boolean isloop = false;

    public LoopChecker(XESLog log) {
        this.log = log;
    }

    public boolean isLoop() {
        for (XESTrace trace : this.log.getTraces()) {
            for (int i = 0; i < trace.getEvents().size() - 1; i++) {
                if (trace.getEvents().get(i).getName().equals(trace.getEvents().get(i + 1).getName())) {
                    return true;
                }
            }
            for (int i = 0; i < (trace.getEvents().size() - 2); i++) {
                String t1 = trace.getEvents().get(i).getName();
                String t2 = trace.getEvents().get(i + 1).getName();
                String t3 = trace.getEvents().get(i + 2).getName();
            }
        }
        return false;
    }
}
