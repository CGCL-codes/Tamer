package jpatch.boundary.mouse;

import java.awt.*;
import java.awt.event.*;
import jpatch.boundary.*;

public class PopupMouseListener extends MouseAdapter {

    private int iButton;

    private JPatchPopupMenu popupMenu;

    public PopupMouseListener(int button) {
        iButton = button;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == iButton) {
            if (popupMenu != null && popupMenu.isShowing()) {
                popupMenu.setVisible(false);
            } else {
                ViewDefinition viewDef = MainFrame.getInstance().getJPatchScreen().getViewDefinition((Component) mouseEvent.getSource());
                Component source = (Component) mouseEvent.getSource();
                popupMenu = new JPatchPopupMenu(viewDef);
                popupMenu.show(source, mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }
}
