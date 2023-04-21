package ch.intertec.storybook.toolkit.swing.panel;

import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import ch.intertec.storybook.toolkit.swing.ColorUtil;
import ch.intertec.storybook.toolkit.swing.IPastelComponent;

public class PastelPanel extends JPanel implements IPastelComponent {

    private static final long serialVersionUID = -7308148217729488897L;

    private Color color;

    public PastelPanel(LayoutManager layout, Color color) {
        super(layout);
        init(color);
    }

    public PastelPanel(Color color) {
        super();
        init(color);
    }

    private void init(Color color) {
        this.color = color;
        if (color != null) {
            setOpaque(true);
            setBackground(ColorUtil.getPastel(color));
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        init(color);
    }
}
