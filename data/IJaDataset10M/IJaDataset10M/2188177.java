package View.JTree;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import View.CoolSwing.CoolButton;

/**
 * 自定义绘制树的节点
 * @author zmm
 *
 */
public class MyTreeCellRenderer extends CoolButton implements TreeCellRenderer {

    private String stringValue = null;

    protected boolean selected = false;

    protected boolean hasFocus = false;

    /**
     * 构造方法
     */
    public MyTreeCellRenderer() {
        super();
    }

    /**
	 * TreeCellRenderer的方法被重写，
	 * 被JAnimatorTree调用获得CoolButton来绘制树的单元
	 */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        this.selected = selected;
        this.hasFocus = hasFocus;
        setSelected(selected);
        setFocusable(hasFocus);
        setText(stringValue);
        return this;
    }

    /**
	 * 重写CoolButton的绘制
	 */
    @Override
    public void paintComponent(Graphics g) {
        if (selected || hasFocus) {
            GradientPaint p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, getHeight() - 1, new Color(0, 0, 0));
            GradientPaint p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, getHeight() - 3, new Color(0, 0, 0, 50));
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(p1);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2d.setPaint(p2);
            g2d.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, 16, 16);
            g2d.dispose();
        }
        super.paintComponent(g);
    }
}
