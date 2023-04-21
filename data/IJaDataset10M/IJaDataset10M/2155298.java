package com.lipstikLF.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalBorders;
import com.lipstikLF.LipstikLookAndFeel;
import com.lipstikLF.delegate.LipstikInternalFrameTitlePane;
import com.lipstikLF.delegate.LipstikTitlePane;
import com.lipstikLF.theme.LipstikColorTheme;

/**
 * The LipstikBorderFactory is a static factory that creates all the borders
 * that are needed by the Look And Feel.
 */
public class LipstikBorderFactory {

    public static final int BORDER_NORMAL = 1;

    public static final int BORDER_SHADOW = 2;

    public static final int BORDER_FLAT = 6;

    public static final int BORDER_BRIGHTER = 16;

    public static final int BORDER_DISABLED = 32;

    public static final int MENUBAR_NONE = 0;

    public static final int MENUBAR_SHARP = 1;

    public static final int MENUBAR_SHADOW = 2;

    public static final int ICON_BAR_WIDTH = 24;

    public static Border getFlatControlBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new ControlFlatBorder(), new BasicBorders.MarginBorder());
    }

    public static Border getFlatComboBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new ComboFlatBorder(), new BasicBorders.MarginBorder());
    }

    public static Border getRoundControlBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new RoundControlBorder(), new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1));
    }

    public static Border getButtonToolBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1), new BorderUIResource.CompoundBorderUIResource(new PushBorder(), new BorderUIResource.EmptyBorderUIResource(1, 2, 1, 2)));
    }

    public static Border getButtonPushBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new PushBorder(), new BasicBorders.MarginBorder());
    }

    public static Border getButtonFileChooserBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1), new PushBorder());
    }

    public static Border getButtonMenuBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new ButtonMenuBorder(), new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1));
    }

    public static Border getMenuItemBorder() {
        return new BorderUIResource(new BasicBorders.MarginBorder());
    }

    public static Border getTableHeaderCellBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new TableHeaderCellBorder(), new BorderUIResource.EmptyBorderUIResource(1, 2, 1, 2));
    }

    public static Border getInternalFrameBorder(boolean resizable) {
        return new InternalFrameBorder(resizable);
    }

    public static Border getFocusBorder() {
        return new BorderUIResource.CompoundBorderUIResource(new FocusBorder(), new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1));
    }

    public static Border getScrollPaneBorder() {
        return new ScrollPaneBorder();
    }

    public static Border getPopupMenuBorder(int style) {
        return new PopupMenuBorder(style);
    }

    public static Border getToolBarBorder() {
        return new ToolBarBorder();
    }

    public static Border getArrowBorder() {
        return new ArrowBorder();
    }

    public static Border getSpinBorder() {
        return new SpinBorder();
    }

    public static OptionalMatteBorder getOptionalMatteBorder() {
        return new OptionalMatteBorder();
    }

    public static Border getToolTipBorder() {
        return new ToolTipBorder();
    }

    public static Border getMenuBarBorder() {
        return new MenuBarBorder();
    }

    private static BasicStroke focusStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 1.0f, 1.0f }, 1.0f);

    public static void paintFocusBorder(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke s = g2d.getStroke();
        g2d.setColor(Color.GRAY);
        g2d.setStroke(focusStroke);
        width--;
        height--;
        g2d.drawLine(x, y, x + width, y);
        g2d.drawLine(x, y, x, y + height);
        g2d.drawLine(x, y + height, x + width, y + height);
        g2d.drawLine(x + width, y, x + width, y + height);
        g2d.setStroke(s);
    }

    public static void paintRoundBorder(Graphics g, int x, int y, int width, int height, LipstikColorTheme theme, Color back, int status) {
        g.translate(x, y);
        width--;
        height--;
        if ((status & BORDER_DISABLED) > 0) g.setColor(theme.getBorderDisabled()); else if ((status & BORDER_BRIGHTER) > 0) g.setColor(theme.getBorderBrighter()); else g.setColor(theme.getBorderNormal());
        g.drawLine(2, 0, width, 0);
        g.drawLine(0, 2, 0, height);
        g.drawLine(2, height, width, height);
        g.drawLine(width, 2, width, height);
        g.setColor(theme.getControlDarkShadow());
        g.drawLine(0, 1, 1, 1);
        g.drawLine(width - 1, 1, width, 1);
        g.drawLine(0, height - 1, 1, height - 1);
        g.drawLine(width - 1, height - 1, width, height - 1);
        g.drawLine(1, 0, 1, 0);
        g.drawLine(width - 1, 0, width - 1, 0);
        g.drawLine(1, height, 1, height);
        g.drawLine(width - 1, height, width - 1, height);
        if (back != null) {
            g.setColor(back);
            g.drawLine(0, 0, 0, 0);
            g.drawLine(width, 0, width, 0);
            g.drawLine(0, height, 0, height);
            g.drawLine(width, height, width, height);
        }
        if ((status & BORDER_SHADOW) > 0) {
            g.setColor(theme.getControlHighlight());
            g.drawLine(2, 1, width - 2, 1);
            g.drawLine(1, 2, 1, height - 2);
            if (status != BORDER_FLAT) g.setColor(theme.getControlShadow());
            g.drawLine(2, height - 1, width - 2, height - 1);
            g.drawLine(width - 1, 2, width - 1, height - 2);
        }
        g.translate(-x, -y);
    }

    public static void paintComboBorder(Graphics g, int x, int y, int width, int height, LipstikColorTheme theme, Color back, int status, boolean isLeftToRight) {
        g.translate(x, y);
        width--;
        height--;
        if ((status & BORDER_DISABLED) > 0) g.setColor(theme.getBorderDisabled()); else g.setColor(theme.getBorderNormal());
        g.drawRect(x, y, width, height);
        g.setColor(theme.getControlDarkShadow());
        if (isLeftToRight) {
            g.drawLine(0, 1, 1, 1);
            g.drawLine(0, height - 1, 1, height - 1);
            g.drawLine(1, 0, 1, 0);
            g.drawLine(1, height, 1, height);
            g.setColor(back);
            g.drawLine(0, 0, 0, 0);
            g.drawLine(0, height, 0, height);
        } else {
            g.drawLine(width - 1, 1, width, 1);
            g.drawLine(width - 1, height - 1, width, height - 1);
            g.drawLine(width - 1, 0, width - 1, 0);
            g.drawLine(width - 1, height, width - 1, height);
            g.setColor(back);
            g.drawLine(width, 0, width, 0);
            g.drawLine(width, height, width, height);
        }
        g.setColor(theme.getControlHighlight());
        g.drawLine(2, 1, width - 2, 1);
        g.drawLine(1, 2, 1, height - 2);
        g.drawLine(2, height - 1, width - 2, height - 1);
        g.drawLine(width - 1, 2, width - 1, height - 2);
        g.translate(-x, -y);
    }

    private static class RoundControlBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -2553446415037707091L;

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            paintRoundBorder(g, x, y, width, height, theme, c.getParent().getBackground(), BORDER_NORMAL);
        }
    }

    private static class ControlFlatBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -264591793602333293L;

        private static Insets insets = new Insets(1, 2, 1, 2);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            g.setColor(c.isEnabled() ? theme.getBorderNormal() : theme.getBorderDisabled());
            paintRoundBorder(g, x, y, width, height, theme, theme.getControl(), BORDER_FLAT);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    private static class ComboFlatBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -264591793602333293L;

        private static Insets insets = new Insets(1, 4, 1, 4);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            g.setColor(c.isEnabled() ? theme.getBorderNormal() : theme.getBorderDisabled());
            paintComboBorder(g, x, y, width, height, theme, theme.getControl(), BORDER_FLAT, c.getComponentOrientation().isLeftToRight());
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    private static class ScrollPaneBorder extends MetalBorders.ScrollPaneBorder implements UIResource {

        private static final long serialVersionUID = 2538719258402390206L;

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            g.translate(x, y);
            g.setColor(theme.getBorderNormal());
            g.drawRect(0, 0, w - 2, h - 2);
            g.setColor(theme.getControlShadow());
            g.drawLine(w - 1, 0, w - 1, h - 1);
            g.drawLine(0, h - 1, w - 1, h - 1);
            g.translate(-x, -y);
        }
    }

    private static class InternalFrameBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = 3365845391871100583L;

        private static Insets insets = new Insets(2, 2, 2, 2);

        private boolean isResizable;

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public InternalFrameBorder(boolean resizable) {
            this.isResizable = resizable;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            Color color;
            if (c instanceof JInternalFrame) {
                JInternalFrame f = (JInternalFrame) c;
                color = f.isSelected() ? theme.getPrimary1() : theme.getSecondary1();
            } else color = theme.getPrimary1();
            g.setColor(color);
            if (isResizable) {
                g.drawRect(x + 1, y + 1, width - 3, height - 3);
                g.drawLine(x, y, x + width - 1, y);
                g.drawLine(x, y, x, y + height - 1);
                g.setColor(color.darker());
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 1);
            } else g.drawRect(x, y, width - 1, height - 1);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    public static class OptionalMatteBorder implements Border {

        private static Insets insets = new Insets(0, 0, 0, 0);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Component parent = c.getParent();
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            Color color;
            boolean isSelected;
            if (parent instanceof LipstikInternalFrameTitlePane) isSelected = ((LipstikInternalFrameTitlePane) parent).getFrame().isSelected(); else isSelected = ((LipstikTitlePane) parent).getWindow().isActive();
            if (isSelected) color = theme.getPrimary3(); else color = theme.getSecondary3();
            g.setColor(color.darker());
            g.drawRect(x, y, width, height);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    private static class TableHeaderCellBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = 5161336172291081537L;

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color oldColor = g.getColor();
            g.setColor(LipstikLookAndFeel.getMyCurrentTheme().getControlShadow());
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
            g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            g.setColor(oldColor);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    private static class PushBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -1738920814136542496L;

        private static Insets insets = new Insets(1, 1, 1, 1);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (!button.isRolloverEnabled() || model.isSelected() || model.isPressed() || (button.isRolloverEnabled() && model.isRollover())) {
                LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
                int status = (model.isArmed() || model.isPressed() || model.isSelected()) ? BORDER_NORMAL : BORDER_SHADOW;
                paintRoundBorder(g, x, y, width, height, theme, c.getBackground(), status);
            }
        }
    }

    /**
     * The rollover border for menu.
     */
    private static class ButtonMenuBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -580056221938775236L;

        private static Insets insets = new Insets(1, 1, 1, 1);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (button.isEnabled() && (model.isRollover() || model.isSelected())) {
                Component parent = c.getParent();
                LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
                Color color = c.isOpaque() ? parent.getBackground() : parent.getParent().getBackground();
                int status = (model.isArmed() || model.isPressed() || model.isSelected()) ? BORDER_NORMAL : BORDER_SHADOW;
                paintRoundBorder(g, x, y, width, height, theme, color, status);
            }
        }
    }

    /**
     * The arrow border for comboboxes.
     */
    private static class ArrowBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = 1989495973214946885L;

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            boolean leftToRight = c.getParent().getComponentOrientation().isLeftToRight();
            int xoffs = (leftToRight) ? 0 : 1;
            width--;
            height--;
            if (!(model.isArmed() || model.isPressed() || model.isSelected())) g.setColor(theme.getControlHighlight()); else g.setColor(theme.getControlShadow());
            g.drawLine(xoffs, 1, width, 1);
            g.drawLine(xoffs, 1, xoffs, height);
            xoffs = width - (xoffs ^ 1);
            g.setColor(theme.getControlShadow());
            g.drawLine(1, height - 1, width, height - 1);
            g.drawLine(xoffs, 2, xoffs, height - 1);
            g.setColor(model.isEnabled() ? theme.getBorderNormal() : theme.getBorderDisabled());
            g.drawLine(0, height, width, height);
            g.drawLine(0, 0, width, 0);
            if (leftToRight) {
                g.drawLine(width, 2, width, height - 2);
                g.setColor(theme.getControlDarkShadow());
                g.drawLine(width - 1, 1, width, 1);
                g.drawLine(width - 1, 0, width - 1, 0);
                g.drawLine(width - 1, height - 1, width, height - 1);
                g.drawLine(width - 1, height, width - 1, height);
                g.setColor(c.getBackground());
                g.drawLine(width, 0, width, 0);
                g.drawLine(width, height, width, height);
            } else {
                g.drawLine(0, 2, 0, height - 2);
                g.setColor(theme.getControlDarkShadow());
                g.drawLine(0, 1, 1, 1);
                g.drawLine(1, 0, 1, 0);
                g.drawLine(0, height - 1, 1, height - 1);
                g.drawLine(1, height, 1, height);
                g.setColor(c.getBackground());
                g.drawLine(0, 0, 0, 0);
                g.drawLine(0, height, 0, height);
            }
        }
    }

    /**
     * The sping border for spin buttons
     */
    private static class SpinBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -8863080288503687794L;

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            boolean leftToRight = c.getParent().getComponentOrientation().isLeftToRight();
            boolean isTop = c.getY() <= 3;
            int xoffs = (leftToRight) ? 0 : 1;
            int yoffs = (isTop) ? 1 : 0;
            width--;
            height--;
            if (!(model.isArmed() || model.isPressed() || model.isSelected())) g.setColor(theme.getControlHighlight()); else g.setColor(theme.getControlShadow());
            g.drawLine(xoffs, yoffs, width, yoffs);
            g.drawLine(xoffs, yoffs, xoffs, height);
            g.setColor(theme.getBorderNormal());
            g.drawLine(0, height, width, height);
            if (isTop) g.drawLine(0, 0, width, 0);
            if (leftToRight) {
                g.drawLine(width, 0, width, height);
                g.setColor(theme.getControlDarkShadow());
                if (isTop) {
                    g.drawLine(width - 1, 1, width, 1);
                    g.drawLine(width - 1, 0, width - 1, 0);
                    g.setColor(c.getParent().getParent().getBackground());
                    g.drawLine(width, 0, width, 0);
                } else {
                    g.drawLine(width - 1, height - 1, width, height - 1);
                    g.drawLine(width - 1, height, width - 1, height);
                    g.setColor(c.getParent().getParent().getBackground());
                    g.drawLine(width, height, width, height);
                }
                g.drawLine(0, yoffs + 1, 0, height - 1);
            } else {
                g.drawLine(0, 0, 0, height);
                g.setColor(theme.getControlDarkShadow());
                if (isTop) {
                    g.drawLine(0, 1, 1, 1);
                    g.drawLine(1, 0, 1, 0);
                    g.setColor(c.getParent().getBackground());
                    g.drawLine(0, 0, 0, 0);
                } else {
                    g.drawLine(0, height - 1, 1, height - 1);
                    g.drawLine(1, height, 1, height);
                    g.setColor(c.getParent().getBackground());
                    g.drawLine(0, height, 0, height);
                }
                g.drawLine(width, yoffs + 1, width, height - 1);
            }
        }
    }

    /**
     * The focus border
     */
    private static class FocusBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -6975763436358838149L;

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY);
            paintFocusBorder(g, x, y, width, height);
        }
    }

    /**
     * Menu bar border.
     */
    private static class MenuBarBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = -7643702634478262312L;

        private static Insets insets = new Insets(2, 1, 2, 1);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(LipstikLookAndFeel.getMyCurrentTheme().getControlShadow());
            g.drawLine(x, height - 1, width, height - 1);
        }
    }

    /**
     * Popup menu border with shadow.
     */
    public static class PopupMenuBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = 4671441181413690888L;

        private static Insets insets = new Insets(2, 1, 2, 1);

        private final int style;

        public PopupMenuBorder(int style) {
            super();
            this.style = style;
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            LipstikColorTheme theme = LipstikLookAndFeel.getMyCurrentTheme();
            g.setColor(theme.getBorderNormal());
            g.drawRect(0, 0, width - 1, height - 1);
            if (style == MENUBAR_SHADOW) LipstikGradients.drawGradient(g, theme.getMenuGradient(), theme.getMenuBackground(), 1, 1, ICON_BAR_WIDTH, height - 2, false); else if (style == MENUBAR_SHARP) g.fillRect(1, 1, ICON_BAR_WIDTH, height - 2);
        }
    }

    private static class ToolBarBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = 7637690504232438744L;

        private static Insets insets = new Insets(0, 0, 0, 0);

        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, insets);
        }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            JToolBar toolBar = (JToolBar) c;
            newInsets.top = 1;
            newInsets.bottom = 1;
            newInsets.left = 1;
            newInsets.right = 1;
            boolean isLeftToRight = c.getComponentOrientation().isLeftToRight();
            if (toolBar.isFloatable() && toolBar.isBorderPainted()) {
                if (toolBar.getOrientation() == JToolBar.HORIZONTAL) {
                    if (isLeftToRight) newInsets.left = 6; else newInsets.right = 6;
                } else newInsets.top = 6;
            }
            Insets margin = toolBar.getMargin();
            if (margin != null) {
                newInsets.left += margin.left;
                newInsets.top += margin.top;
                newInsets.right += margin.right;
                newInsets.bottom += margin.bottom;
            }
            return newInsets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            JToolBar toolBar = (JToolBar) c;
            boolean isLeftToRight = c.getComponentOrientation().isLeftToRight();
            if (toolBar.isFloatable()) {
                if (toolBar.getOrientation() == JToolBar.HORIZONTAL) {
                    int ih = LipstikIconFactory.vicon1.getIconHeight();
                    int ix = isLeftToRight ? 2 : width - LipstikIconFactory.vicon1.getIconWidth() - 2;
                    g.drawImage(LipstikIconFactory.vicon1.getImage(), ix, -1 + (height - ih) >> 1, null);
                } else {
                    int iw = LipstikIconFactory.hicon1.getIconWidth();
                    g.drawImage(LipstikIconFactory.hicon1.getImage(), (width - iw) >> 1, 3, null);
                }
            }
        }
    }

    private static class ToolTipBorder extends AbstractBorder implements UIResource {

        private static final long serialVersionUID = 2211734253687204676L;

        private static Insets insets = new Insets(3, 3, 3, 3);

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY);
            g.drawRect(x, y, width - 1, height - 1);
        }
    }
}
