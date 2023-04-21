package lcm.painters;

import java.awt.Graphics2D;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.textarea.Gutter;

public interface DirtyMarkPainter {

    void paint(Graphics2D gfx, Gutter gutter, int y, int height, Buffer buffer, int physicalLine);
}
