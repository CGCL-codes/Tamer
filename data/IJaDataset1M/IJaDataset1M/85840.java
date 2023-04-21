package com.nullfish.lib.ui.grid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.JPanel;

/**
 * @author shunji
 *
 */
public abstract class LineGrid extends JPanel {

    protected static Map gridGroupMap = new WeakHashMap();

    protected boolean doubleLine = true;

    /**
	 * デフォルトコンストラクタ
	 *
	 */
    public LineGrid() {
        this(null);
        setOpaque(false);
    }

    /**
	 * コンストラクタ
	 * @param groupKey	グリッドのグループのキー
	 */
    public LineGrid(Object groupKey) {
        Set group = (Set) gridGroupMap.get(groupKey);
        if (group == null) {
            group = new HashSet();
            gridGroupMap.put(groupKey, group);
        }
        group.add(this);
        setOpaque(false);
        setForeground(Color.WHITE);
    }

    public static void setGroupDoubleLine(boolean bool, Object groupKey) {
        Set group = (Set) gridGroupMap.get(groupKey);
        if (group == null) {
            return;
        }
        Iterator ite = group.iterator();
        while (ite.hasNext()) {
            LineGrid grid = (LineGrid) ite.next();
            grid.setDoubleLine(bool);
        }
    }

    public void setDoubleLine(boolean bool) {
        this.doubleLine = bool;
        repaint();
    }

    public static void setGroupColor(Color color, Object groupKey) {
        Set group = (Set) gridGroupMap.get(groupKey);
        if (group == null) {
            return;
        }
        Iterator ite = group.iterator();
        while (ite.hasNext()) {
            LineGrid grid = (LineGrid) ite.next();
            grid.setForeground(color);
            grid.repaint();
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        FontMetrics fontMetrics = getFontMetrics(font);
        Rectangle2D rect = fontMetrics.getStringBounds("m", getGraphics());
        this.setPreferredSize(new Dimension((int) rect.getWidth(), (int) rect.getHeight()));
        this.setMinimumSize(new Dimension((int) rect.getWidth(), (int) rect.getHeight()));
    }

    public static void removeGroup(Object groupKey) {
        gridGroupMap.remove(groupKey);
    }

    public static void setGroupFont(Font font, Object groupKey) {
        Set group = (Set) gridGroupMap.get(groupKey);
        if (group == null) {
            return;
        }
        Iterator ite = group.iterator();
        while (ite.hasNext()) {
            LineGrid grid = (LineGrid) ite.next();
            grid.setFont(font);
        }
    }
}
