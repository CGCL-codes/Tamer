package gui.adjustments;

import gui.FrameTab;
import java.util.Vector;

/**
 * @author kordikp
 */
public class MainAdjuster {

    public static final String PREPROCESSING_FRAME = "Preprocessing panel";

    public MainAdjuster(Vector<FrameTab> tabs) {
        for (FrameTab tab : tabs) {
            if (tab.getName().compareTo(PREPROCESSING_FRAME) == 0) {
                new PreprocessingAdjuster(tab);
            }
        }
    }
}
