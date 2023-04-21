package us.wthr.jdem846.ui.panels;

import java.awt.Color;
import javax.swing.border.Border;
import us.wthr.jdem846.ui.base.Panel;
import us.wthr.jdem846.ui.border.StandardBorder;

@SuppressWarnings("serial")
public class RoundedPanel extends Panel {

    public RoundedPanel() {
        setBorder(new StandardBorder());
    }

    @Override
    public void setBackground(Color background) {
        super.setBackground(background);
        Border border = getBorder();
        if (border != null && border instanceof StandardBorder) {
            ((StandardBorder) border).setBackground(background);
        }
    }
}
