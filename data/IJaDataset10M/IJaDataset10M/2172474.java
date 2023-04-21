package self.amigo.elem.uml;

import java.awt.*;
import self.amigo.elem.*;

public class SequenceDiagramObjectView extends ASequenceDiagramElement {

    /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
    private static final long serialVersionUID = 1;

    protected ARectangularHandledElement constructDelegate() {
        return new EssentialObjectView();
    }

    protected String getDefaultName() {
        return "untitledObject: UnspecifiedClass";
    }

    protected class EssentialObjectView extends AEssential {

        /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
        private static final long serialVersionUID = 1;

        protected void setInitialBounds() {
            fig.setBounds(10, 10, 80, 40);
        }

        protected void paintFrame(Graphics surface) {
            if (!outlineOnly) {
                surface.setColor(Color.white);
                surface.fillRect(fig.x, fig.y, fig.width, fig.height);
            }
            surface.setColor(frameColor);
            surface.drawRect(fig.x, fig.y, fig.width, fig.height);
            textPainter.paint(surface, ctxText, outlineOnly);
        }

        protected boolean isTextUnderlined() {
            return true;
        }

        protected void resizeToMinIfRequired() {
            if (fig.width < 80) fig.width = 80;
            if (fig.height < 40) fig.height = 40;
        }

        protected void resizeMessageHistory(int y) {
            if (y > messageHistoryStart.y + 20) {
                layer.beginUpdate();
                messageHistoryEnd.y = y;
                length = y - messageHistoryStart.y;
                layer.endUpdate();
            }
        }

        protected void resetFrame() {
            messageHistoryStart.setLocation(fig.x + fig.width / 2, fig.y + fig.height);
            messageHistoryEnd.setLocation(fig.x + fig.width / 2, fig.y + fig.height + length);
        }
    }
}
