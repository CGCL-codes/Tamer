package projects.geoRouting.nodes.timers;

import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

public class MessageAnimationTimer extends Timer {

    @Override
    public void fire() {
        Tools.repaintGUI();
        this.startGlobalTimer(1);
    }
}
