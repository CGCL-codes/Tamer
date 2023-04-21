package webuilder.webx;

import java.awt.*;
import javax.swing.*;

public class ToolBarButton extends JButton {

    private static final Insets margins = new Insets(0, 0, 0, 0);

    public ToolBarButton(Icon icon) {
        super(icon);
        setMargin(margins);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
    }

    public ToolBarButton(String imageFile) {
        this(new ImageIcon(imageFile));
    }

    public ToolBarButton(String imageFile, String text) {
        this(new ImageIcon(imageFile));
        setText(text);
    }
}
