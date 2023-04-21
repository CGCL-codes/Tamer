package org.logview4j.ui.panel;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import org.logview4j.ui.border.RaisedBorder;
import org.logview4j.ui.border.ShadowBorder;
import org.logview4j.ui.color.ColorFactory;
import org.logview4j.ui.toolbar.MinimalJToolBar;

/**
 * A header panel has a shadow border, a gradient panel at the top
 * and a toolbar for buttons etc in the gradient panel, like eclipse
 */
public class HeaderPanel extends JPanel {

    protected Border shadowBorder = new ShadowBorder();

    protected Border raisedBorder = new RaisedBorder();

    protected GradientPanel gradientPanel = new GradientPanel(new BorderLayout());

    protected JPanel mainPanel = new JPanel(new BorderLayout());

    protected JLabel title = new JLabel();

    protected JToolBar toolbar = null;

    protected JComponent rootComponent = null;

    public HeaderPanel(JComponent rootComponent) {
        super(new BorderLayout());
        this.rootComponent = rootComponent;
        toolbar = new MinimalJToolBar();
        init();
    }

    public HeaderPanel(JComponent rootComponent, JToolBar newToolbar) {
        super(new BorderLayout());
        this.rootComponent = rootComponent;
        this.toolbar = newToolbar;
        init();
    }

    protected void init() {
        setBorder(shadowBorder);
        gradientPanel.setBorder(raisedBorder);
        title.setForeground(ColorFactory.getGradientTextForeground());
        title.setOpaque(false);
        mainPanel.add(rootComponent, BorderLayout.CENTER);
        gradientPanel.add(title, BorderLayout.CENTER);
        gradientPanel.add(toolbar, BorderLayout.EAST);
        add(gradientPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setText(String text) {
        title.setText(text);
    }

    public void setIcon(ImageIcon icon) {
        title.setIcon(icon);
    }
}
