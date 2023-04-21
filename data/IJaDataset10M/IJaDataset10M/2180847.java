package playground.scnadine.gpsProcessing.activityAlgorithms;

import java.util.Iterator;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessing.GPSActivities;
import playground.scnadine.gpsProcessing.GPSActivity;

public class GPSActivityCalcActivityCategory extends GPSActivityAlgorithm {

    private int shortActivityDuration;

    private int longActivityDuration;

    private int smallActivityMovement;

    private int largeActivityMovement;

    public GPSActivityCalcActivityCategory(Config config, String CONFIG_MODULE) {
        super();
        this.shortActivityDuration = Integer.parseInt(config.findParam(CONFIG_MODULE, "shortActivityDuration"));
        this.longActivityDuration = Integer.parseInt(config.findParam(CONFIG_MODULE, "longActivityDuration"));
        this.smallActivityMovement = Integer.parseInt(config.findParam(CONFIG_MODULE, "smallActivityMovement"));
        this.largeActivityMovement = Integer.parseInt(config.findParam(CONFIG_MODULE, "largeActivityMovement"));
    }

    @Override
    public void run(GPSActivities activities) {
        Iterator<GPSActivity> it = activities.getActivities().iterator();
        while (it.hasNext()) {
            GPSActivity currentActivity = it.next();
            int activityCategory = -1;
            if (currentActivity.getActivityCoords().length > 2) activityCategory = 1; else if (currentActivity.getActivityDuration() > this.shortActivityDuration && currentActivity.getActivityDuration() <= this.longActivityDuration && currentActivity.getActivityDistance() <= this.smallActivityMovement) activityCategory = 2; else if (currentActivity.getActivityDuration() > this.longActivityDuration) activityCategory = 3; else if (currentActivity.getActivityDuration() > this.shortActivityDuration && currentActivity.getActivityDuration() > this.largeActivityMovement) activityCategory = 4; else if (currentActivity.getActivityDuration() > this.shortActivityDuration && currentActivity.getActivityDuration() <= this.longActivityDuration && currentActivity.getActivityDistance() > this.smallActivityMovement && currentActivity.getActivityDistance() <= this.largeActivityMovement) activityCategory = 5;
            currentActivity.setActivityCategory(activityCategory);
        }
    }
}
