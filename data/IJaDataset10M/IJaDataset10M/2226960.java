package sg.edu.nus.iss.se07.ui.widget;

import java.awt.*;

;

public class CenteredDialog extends Dialog {

    public CenteredDialog(Frame parent, String title, Dimension dialogDimension) {
        super(parent, title);
        this.setSize(dialogDimension);
        CenterToFrame(parent, dialogDimension);
    }

    private void CenterToFrame(Frame parent, Dimension dialogDimension) {
        Point parentLocation = parent.getLocation();
        Dimension d = parent.getSize();
        int x = parentLocation.x + (d.width / 2) - dialogDimension.width / 2;
        int y = parentLocation.y + (d.height / 2) - dialogDimension.height / 2;
        ;
        this.setLocation(x, y);
    }
}
