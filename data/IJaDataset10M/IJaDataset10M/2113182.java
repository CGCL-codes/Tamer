package FroG.view.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.border.Border;
import FroG.view.XControlUtil;

/**
 * 
 * @Description: 菜单根条目
 * @Author: zhangzuoqiang
 * @Date: Aug 25, 2011
 */
public class XRootMenu extends JMenu {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Color foregroundColor;

    private TexturePaint paint;

    private Border border;

    public XRootMenu() {
        foregroundColor = XControlUtil.DEFAULT_TEXT_COLOR;
        paint = XControlUtil.createTexturePaint("FroG/view/images/menubar_background_selected.png");
        border = BorderFactory.createEmptyBorder(0, 5, 0, 4);
        init();
    }

    public XRootMenu(String text) {
        super(text);
        foregroundColor = XControlUtil.DEFAULT_TEXT_COLOR;
        paint = XControlUtil.createTexturePaint("FroG/view/images/menubar_background_selected.png");
        border = BorderFactory.createEmptyBorder(0, 5, 0, 4);
        init();
    }

    private void init() {
        setFont(XControlUtil.FONT_14_BOLD);
        setBorder(border);
        setForeground(foregroundColor);
    }

    protected void paintComponent(Graphics g) {
        if (isSelected()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(paint);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        } else {
            super.paintComponent(g);
        }
    }
}
