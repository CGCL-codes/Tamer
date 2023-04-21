package game.report.visualizations;

import game.gui.ErrorSurfaceFrame;
import game.trainers.Trainer;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Class to render and animate training error surface graph
 */
public class TrainingErrorSurfaceImage {

    public static File generateTrainingErrorSurfaceAnimation(Trainer t, File s) {
        if (t.getBest() == null) return null;
        ErrorSurfaceFrame f;
        final int resolution = 30;
        final double scale = 30;
        int idx = 0;
        int idy = 1;
        double[][] data = t.scanErrorSurfaceHistory(idx, idy, resolution, scale, 0);
        f = new ErrorSurfaceFrame("Error surface graph", data, resolution, scale);
        f.addIterationHistory(t.getIterationHistory(idx, idy));
        boolean stop = false;
        int num = t.getIterationHistory().size();
        BufferedImage[] frames = new BufferedImage[num];
        String[] delayTimes = new String[num];
        for (int i = 0; i < num - 1; i++) {
            frames[i] = f.getChartImage();
            delayTimes[i] = "10";
            data = t.scanErrorSurfaceHistory(idx, idy, resolution, scale, i);
            double[] iter = t.getIterationHistory().get(i);
            f.refreshSurface(data, i, iter[idx], iter[idy]);
            f.repaint();
            if (stop) i = num;
        }
        double[] best = new double[2];
        best[0] = t.getBest()[idx];
        best[1] = t.getBest()[idy];
        f.addBestSolution(best);
        frames[num - 1] = f.getChartImage();
        delayTimes[num - 1] = "10";
        try {
            game.utils.WriteAnimatedGif.saveAnimate(s, frames, delayTimes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }
}
