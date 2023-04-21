package atrip.view.searchpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.Border;
import atrip.view.XContorlUtil;
import atrip.view.XHeader;
import atrip.view.XListSplitListener;

/**
 * 
 * @Description: 查询条件设置面板
 * @Author: zhangzuoqiang
 * @Date: Aug 25, 2011
 */
public class XSearchPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JPanel split;

    private XHeader header;

    private XListSplitListener splitListener;

    public XSearchPanel() {
        split = new JPanel(new BorderLayout());
        header = new XHeader() {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1L;

            public void setShrink(boolean shrinked) {
                super.setShrink(shrinked);
                if (shrinked) {
                    split.setCursor(Cursor.getDefaultCursor());
                } else {
                    split.setCursor(Cursor.getPredefinedCursor(10));
                }
            }
        };
        splitListener = new XListSplitListener(header);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(XContorlUtil.LIST_BACKGROUND);
        JPanel rightInsetPane = new JPanel();
        rightInsetPane.setPreferredSize(new Dimension(2, 0));
        rightInsetPane.setBackground(XContorlUtil.LIST_BACKGROUND);
        add(rightInsetPane, "East");
        add(header, "North");
        split.setBorder(new Border() {

            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(XContorlUtil.LIST_SPLIT_COLOR);
                g.drawLine(x, y, x, y + height);
            }

            public Insets getBorderInsets(Component c) {
                return new Insets(0, 1, 0, 0);
            }

            public boolean isBorderOpaque() {
                return true;
            }
        });
        split.setOpaque(true);
        split.setPreferredSize(new Dimension(4, 0));
        split.setBackground(XContorlUtil.LIST_BACKGROUND);
        split.setCursor(Cursor.getPredefinedCursor(10));
        split.addMouseListener(splitListener);
        split.addMouseMotionListener(splitListener);
        add(split, "West");
    }

    public void setTitle(String title) {
        header.setTitle(title);
    }

    public String getTitle() {
        return header.getTitle();
    }

    public void setShrink(boolean shrinked) {
        header.setShrink(shrinked);
    }

    public boolean isShrinked() {
        return header.isShrinked();
    }
}
