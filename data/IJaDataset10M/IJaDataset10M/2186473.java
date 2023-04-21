package javab.bling.BOptions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javab.bling.BImageRadioButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BOptionsTabPanel extends JPanel {

    private String buttonLayoutPosition;

    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);

    private static final int MINIMUM_COLOR_DIFFERENCE = 50;

    protected BOptionsTabPanel(BOptions o, List<BOptionItem> cards, String buttonLayoutPosition, boolean showLabels, int start) {
        this.buttonLayoutPosition = buttonLayoutPosition;
        setBackground(figureBackground(getBackground()));
        setBorder(new BOptionsTabPanelBorder(this));
        setLayout(new GridBagLayout());
        makeGUI(o, cards, showLabels, start);
    }

    private void makeGUI(BOptions o, List<BOptionItem> cards, boolean showLabels, int start) {
        ButtonGroup bg = new ButtonGroup();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        for (int i = 0; i < cards.size(); i++) {
            BOptionItem p = cards.get(i);
            BImageRadioButton b = new BImageRadioButton(p.getIcon(), p.getDisplayText(), p.getDisplayText(), o);
            b.setName(p.getDisplayText());
            b.setActionCommand("panelButton");
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setVerticalTextPosition(SwingConstants.BOTTOM);
            if (showLabels) b.setText(p.getDisplayText());
            bg.add(b);
            if (i == start) bg.setSelected(b.getModel(), true);
            if (buttonLayoutPosition == BorderLayout.EAST || buttonLayoutPosition == BorderLayout.WEST) {
                gbc.gridy = i;
            } else {
                gbc.gridx = i;
            }
            add(b, gbc);
        }
        gbc.weighty = 1;
        gbc.weightx = 1;
        if (buttonLayoutPosition == BorderLayout.EAST || buttonLayoutPosition == BorderLayout.WEST) {
            gbc.gridy = cards.size();
        } else {
            gbc.gridx = cards.size();
        }
        add(Box.createGlue(), gbc);
    }

    public String getButtonLayoutPosition() {
        return buttonLayoutPosition;
    }

    public static Color figureBackground(Color currentBackground) {
        int current = currentBackground.getRed() + currentBackground.getGreen() + currentBackground.getBlue();
        int defaultBackground = BACKGROUND_COLOR.getRed() + BACKGROUND_COLOR.getGreen() + BACKGROUND_COLOR.getBlue();
        if (defaultBackground - current < MINIMUM_COLOR_DIFFERENCE) {
            return Color.WHITE;
        } else {
            return BACKGROUND_COLOR;
        }
    }
}
