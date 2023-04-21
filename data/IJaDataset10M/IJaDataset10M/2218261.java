package lt.ktu.scheduler;

import javax.swing.JFileChooser;
import lt.ktu.scheduler.base.settings.ScheduleSettings;
import lt.ktu.scheduler.data.Data;
import lt.ktu.scheduler.data.DataParser;
import lt.ktu.scheduler.gui.OutputFrame;
import lt.ktu.scheduler.model.Schedule;
import lt.ktu.scheduler.utils.DeepCopyUtil;
import lt.ktu.scheduler.utils.Logger;

public class Main {

    private static int iterations = 30;

    public static void main(String[] args) {
        JFileChooser fc = new JFileChooser();
        fc.showDialog(null, "Open");
        Data d = Data.getInstance();
        DataParser dp = new DataParser(d, fc.getSelectedFile());
        Schedule best = null;
        int bestPoints = Integer.MAX_VALUE;
        for (int i = 0; i < iterations; i++) {
            Schedule s = new Schedule((Data) DeepCopyUtil.deepCopy(d), new ScheduleSettings() {

                public int getFramesPerPeriode() {
                    return 24;
                }

                public int getPeriodeCount() {
                    return 5;
                }
            });
            Logger.log("Value: " + s.calculatePenaltyPoints(), true);
            if (s.calculatePenaltyPoints() < bestPoints) {
                best = (Schedule) DeepCopyUtil.deepCopy(s);
                bestPoints = best.calculatePenaltyPoints();
                Logger.log("New Best Value: " + bestPoints, true);
            }
        }
        new OutputFrame(best);
    }
}
