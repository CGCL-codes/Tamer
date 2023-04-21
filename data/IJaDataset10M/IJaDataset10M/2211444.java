package imp.roadmap;

import java.awt.Color;

/**
 * Analyzer is a Thread that runs the analysis while the screen displays
 * the raw roadmap.
 * @author keller
 */
public class Analyzer extends Thread {

    private int Xoffset = 200;

    private int Yoffset = 50;

    public static Color cautionColor = new Color(255, 243, 116);

    RoadMapFrame frame;

    boolean showJoinsOnCompletion;

    public Analyzer(RoadMapFrame frame, boolean showJoinsOnCompletion) {
        this.frame = frame;
        this.showJoinsOnCompletion = showJoinsOnCompletion;
    }

    @Override
    public void run() {
        frame.setStatus(" Analyzing: Please wait for OK!");
        frame.setStatusColor(cautionColor);
        frame.analyze(showJoinsOnCompletion);
        frame.setStatus(" OK to Edit");
        frame.setStatusColor(Color.WHITE);
    }

    public void cancel() {
    }
}
