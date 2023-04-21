package gumbo.app.awt.widget.bones;

import gumbo.core.util.AssertUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A panel consisting of rows, with each row having a label and widget, and a
 * label and widget.
 * @author jonb
 * @deprecated Use JPanelLWLWTool
 */
public class JPanelLWLW extends JPanel {

    /**
	 * Creates an instance.
	 * @param title Panel title. None if null.
	 */
    public JPanelLWLW(String title) {
        if (title != null) {
            setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        }
        setLayout(new GridBagLayout());
    }

    public void addRow(JLabel labelA, JComponent widgetA, JLabel labelB, JComponent widgetB) {
        AssertUtils.assertNonNullArg(widgetA);
        AssertUtils.assertNonNullArg(widgetB);
        int widgetGridxA = 1;
        int widgetGridWidthA = 1;
        if (labelA == null) {
            widgetGridxA = 0;
            widgetGridWidthA = 2;
        }
        int widgetGridxB = 3;
        int widgetGridWidthB = 1;
        if (labelB == null) {
            widgetGridxB = 2;
            widgetGridWidthB = 2;
        }
        if (labelA != null) {
            GridBagConstraints labelConA = new GridBagConstraints();
            labelConA.gridx = 0;
            labelConA.gridy = _rowCount;
            labelConA.weightx = 0.1;
            labelConA.weighty = 0.1;
            labelConA.anchor = GridBagConstraints.WEST;
            labelConA.insets = new Insets(2, 2, 2, 2);
            add(labelA, labelConA);
        }
        if (widgetA != null) {
            GridBagConstraints widgetConA = new GridBagConstraints();
            widgetConA.gridwidth = widgetGridWidthA;
            widgetConA.gridx = widgetGridxA;
            widgetConA.gridy = _rowCount;
            widgetConA.weightx = 0.4;
            widgetConA.weighty = 0.1;
            widgetConA.anchor = GridBagConstraints.EAST;
            widgetConA.fill = GridBagConstraints.HORIZONTAL;
            widgetConA.insets = new Insets(2, 2, 2, 2);
            add(widgetA, widgetConA);
        }
        if (labelB != null) {
            GridBagConstraints labelConB = new GridBagConstraints();
            labelConB.gridx = 2;
            labelConB.gridy = _rowCount;
            labelConB.weightx = 0.1;
            labelConB.weighty = 0.1;
            labelConB.anchor = GridBagConstraints.WEST;
            labelConB.insets = new Insets(2, 2, 2, 2);
            add(labelB, labelConB);
        }
        if (widgetB != null) {
            GridBagConstraints widgetConB = new GridBagConstraints();
            widgetConB.gridwidth = widgetGridWidthB;
            widgetConB.gridx = widgetGridxB;
            widgetConB.gridy = _rowCount;
            widgetConB.weightx = 0.4;
            widgetConB.weighty = 0.1;
            widgetConB.anchor = GridBagConstraints.EAST;
            widgetConB.fill = GridBagConstraints.HORIZONTAL;
            widgetConB.insets = new Insets(2, 2, 2, 2);
            add(widgetB, widgetConB);
        }
        _rowCount++;
    }

    private int _rowCount = 0;

    private static final long serialVersionUID = 1L;
}
