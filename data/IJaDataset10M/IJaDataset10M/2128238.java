package net.tileeditor.integerimporter;

import net.tileeditor.general.AcceptCancelDialog;
import net.tileeditor.general.TileEditor;
import net.tileeditor.general.AcceptCancelPanel;
import net.tileeditor.Source;
import net.tileeditor.SimpleObject;
import net.tileeditor.TheHold;
import net.tileeditor.Held;
import javax.swing.*;
import java.awt.*;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NONE;

public class IntegerRangeDialog extends AcceptCancelDialog {

    public JTextField lowerBoundText;

    public JTextField upperBoundText;

    public IntegerRangeDialog(TileEditor tileEditor) {
        super(tileEditor, "Import Integer Range", true);
        setLayout(new GridBagLayout());
        int width = 220;
        int height = 120;
        setSize(width, height);
        Dimension dimension = tileEditor.getSize();
        Point point = tileEditor.getLocation();
        setLocation(dimension.width / 2 - width / 2 + point.x, dimension.height / 2 - height / 2 + point.y);
        String lower = "0";
        String upper = "100";
        add(label("Lower Bound:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, CENTER, NONE, new Insets(2, 2, 2, 2), 0, 0));
        lowerBoundText = textField(lower);
        add(lowerBoundText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, CENTER, NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(label("Upper Bound:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, CENTER, NONE, new Insets(2, 2, 2, 2), 0, 0));
        upperBoundText = textField(upper);
        add(upperBoundText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, CENTER, NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(new AcceptCancelPanel(this), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, CENTER, NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    public void accepted() {
        try {
            Integer lower = Integer.valueOf(lowerBoundText.getText());
            Integer upper = Integer.valueOf(upperBoundText.getText());
            if ((lower > upper) || (lower < 0) || (upper < 0)) {
                return;
            }
            Source source = Source.getInstance();
            for (int i = lower; i <= upper; i++) {
                SimpleObject simpleObject = new SimpleObject(i);
                simpleObject.setName("Integer " + Integer.toString(i));
                source.addSimpleObject(simpleObject);
            }
            TheHold.getInstance().hold(Held.BRUSH_IMPORTED);
            super.accepted();
        } catch (NumberFormatException e) {
        }
    }
}
