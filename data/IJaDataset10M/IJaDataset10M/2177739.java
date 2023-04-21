package anveshitha.timer;

import static anveshitha.config.AnveshithaConstants.JAVA;
import static anveshitha.config.AnveshithaConstants.TIMER_DELAYS;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import anveshitha.indexers.JavaSourceCodeIndexer;

/**
 * @author varun
 */
public class AnveshithaScheduler {

    private static Timer timer;

    private static TimerTask javaIndexerTask;

    public static void schedule() {
        javaIndexerTask = new JavaSourceCodeIndexer(true);
        timer = new Timer(true);
        timer.scheduleAtFixedRate(javaIndexerTask, new Date(), TIMER_DELAYS.get(JAVA));
    }
}
