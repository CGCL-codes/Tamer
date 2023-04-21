package com.dukesoftware.viewlon3.gui.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.vecmath.Point3d;
import com.dukesoftware.utils.swing.others.JPanelWithListener;
import com.dukesoftware.utils.thread.ILoopTaskAdv;
import com.dukesoftware.viewlon3.data.common.DataControl;
import com.dukesoftware.viewlon3.data.node.LogicalObjecSpring2D;
import com.dukesoftware.viewlon3.data.relation.interfacetool.RelationManager;
import com.dukesoftware.viewlon3.gui.infopanel.InfoPanel;
import com.dukesoftware.viewlon3.utils.viewlon.Fonts;

/**
 * キャンバスの抽象クラスです。
 * 
 * 
 *
 *
 */
public abstract class CommonCanvas extends JPanelWithListener implements ILoopTaskAdv {

    private static final long serialVersionUID = -1141618135318849715L;

    public static final Color PASSED_TIME_COLOR = Color.RED.brighter().brighter();

    public static final Color CURRENT_TIME_COLOR = Color.GREEN.brighter().brighter();

    private static final Color BACK_COLOR = Color.BLACK;

    public static final int INTERVAL = 50;

    protected final DataControl d_con;

    protected final RelationManager r_con;

    protected final int panelx, panely;

    protected final Point3d centerp;

    protected Point p;

    protected boolean labelFlag = true;

    protected int dist = LogicalObjecSpring2D.DEFAULT_DIST;

    protected final InfoPanel info_panel;

    public CommonCanvas(RelationManager r_con, DataControl d_con, int mapx, int mapy, InfoPanel info_panel) {
        super();
        panelx = mapx;
        panely = mapy;
        centerp = new Point3d(panelx / 2.0, panely / 2.0, 0.0);
        this.d_con = d_con;
        this.r_con = r_con;
        this.info_panel = info_panel;
        setBackground(BACK_COLOR);
        setOpaque(true);
        setPreferredSize(new Dimension(panelx, panely));
    }

    protected abstract void paintBody(Graphics g);

    protected abstract void setFocus();

    public void initOutOfLoop() {
    }

    ;

    public void endOutOfLoop() {
    }

    /**
	 * 共通ペイントメソッド
	 */
    public final void paint(Graphics g) {
        super.paint(g);
        paintBody(g);
    }

    /**
	 * Object間の論理ばね距離定数を得る
	 * @return
	 */
    public final int getDist() {
        return dist;
    }

    /**
	 *  Object間の論理ばね距離定数を設定 
	 */
    protected void setDist(int dist) {
        this.dist = dist;
    }

    protected void saveImage() {
        String filename = new Date(d_con.getPointerData().getCurrentDateLong()).toString();
        filename = filename.replace(':', '-');
        filename = filename.replace(' ', '_');
        filename = filename.concat(".jpg");
        File dest = new File(filename);
        BufferedImage dest_image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D off = dest_image.createGraphics();
        paintBody(off);
        try {
            ImageIO.write(dest_image, "jpg", dest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected final void switchLabelFlag() {
        labelFlag = !labelFlag;
    }

    protected void paintDate(Graphics g) {
        g.setFont(Fonts.FONT_DATE);
        if (d_con.isSyncronizedMode()) g.setColor(CURRENT_TIME_COLOR); else g.setColor(PASSED_TIME_COLOR);
        g.drawString(new Date(d_con.getPointerData().getCurrentDateLong()).toString(), 10, 30);
    }

    public void mousePressed(MouseEvent e) {
        p = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        Point prev = e.getPoint();
        centerp.x = centerp.x + prev.x - p.x;
        centerp.y = centerp.y + prev.y - p.y;
        p = prev;
    }
}
