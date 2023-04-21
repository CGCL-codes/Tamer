package ucalgary.ebe.ci.mice.cursor;

import icculus.manymouse.jni.ManyMouse;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is responsible for detecting the mice and creating a list of mice cursor
 * @author herbiga
 *
 */
public class DetectMice {

    private int numberOfMice;

    private List<MouseCursor> cursors;

    /**
	 * Standard constructor
	 */
    public DetectMice() {
        numberOfMice = 0;
        cursors = new Vector<MouseCursor>();
    }

    /**
	 * Returns a list of all detected mice
	 * @param shell
	 * @param display
	 * @param cursorTexts
	 * @param cursorColors
	 * @param rotateDirections
	 * @return List of MouseCursor
	 * @see ebe.manymouse.cursor.MouseCursor
	 */
    public List<MouseCursor> getMice(Shell shell, Display display, List<String> cursorTexts, List<RGB> cursorColors, List<Integer> rotateDirections) {
        String cursorText;
        RGB cursorColor;
        int rotateDirection;
        numberOfMice = ManyMouse.Init();
        for (int i = 0; i < numberOfMice; i++) {
            if (cursorTexts == null) {
                cursorText = null;
            } else {
                if (i < cursorTexts.size()) {
                    cursorText = cursorTexts.get(i);
                } else {
                    cursorText = null;
                }
            }
            if (cursorColors == null) {
                cursorColor = null;
            } else {
                if (i < cursorColors.size()) {
                    cursorColor = cursorColors.get(i);
                } else {
                    cursorColor = null;
                }
            }
            if (rotateDirections == null) {
                rotateDirection = 0;
            } else {
                if (i < rotateDirections.size()) {
                    rotateDirection = rotateDirections.get(i).intValue();
                } else {
                    rotateDirection = 0;
                }
            }
            cursors.add(i, new MouseCursor(shell, display, i, cursorText, cursorColor, rotateDirection));
        }
        return cursors;
    }
}
