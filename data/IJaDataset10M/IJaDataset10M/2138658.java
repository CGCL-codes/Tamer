package ui.tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import ui.menuet.Menuet;

public abstract class ToolViewPart extends ToolView {

    /**
	 * This is applied only after the MenuetElement
	 * has been created to add on functionality
	 * common to all tools in the gorup.
	 */
    public void applyToolGroupSettings() {
        if (mElement != null) {
            mElement.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent e) {
                }

                public void mouseDown(MouseEvent e) {
                    toolSelected();
                }

                public void mouseUp(MouseEvent e) {
                }
            });
        }
    }

    public int getToolMode() {
        return Menuet.MENUET_MODE_PART;
    }
}
