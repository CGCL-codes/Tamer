package marker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import org.gjt.sp.jedit.EditPane;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaExtension;

public class MarkerExtension extends TextAreaExtension {

    private EditPane editPane;

    public MarkerExtension(EditPane editPane) {
        this.editPane = editPane;
    }

    public void paintValidLine(Graphics2D gfx, int screenLine, int physicalLine, int start, int end, int y) {
        String path = editPane.getBuffer().getPath();
        JEditTextArea ta = editPane.getTextArea();
        int width = ta.getGutter().getWidth();
        int lineHeight = ta.getPainter().getFontMetrics().getHeight();
        Collection<MarkerSet> markerSets = MarkerSetsPlugin.getMarkerSets();
        Color c = gfx.getColor();
        for (MarkerSet ms : markerSets) {
            gfx.setColor(ms.getColor());
            FileMarker marker = ms.getMarkerFor(path, physicalLine);
            if (marker == null) continue;
            gfx.fillRect(0, y, width, lineHeight);
        }
        gfx.setColor(c);
    }
}
