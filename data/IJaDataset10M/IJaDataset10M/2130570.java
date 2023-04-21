package problem7;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * 画图面板。
 * 
 * JDK中提供了各种图形的封装类。所有类都实现了Shape接口，
 * 在面板中画这些图形，只需要g2d.draw(s)即可。
 * @author hcy
 *
 */
public class DrawPanel extends JPanel {

    public DrawPanel() {
        shapes = new ArrayList<MyShape>();
        cll = new CreateLineListener(this);
        crl = new CreateRectListener(this);
        cel = new CreateElliListener(this);
        cpl = new CreatePolygonLintener(this);
        currl = null;
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int w = -1, h = -1;
                if (e.getX() > dp.getWidth()) {
                    w = e.getX() + 10;
                }
                if (e.getY() > dp.getHeight()) {
                    h = e.getY() + 10;
                }
                w = (w == -1) ? dp.getWidth() : w;
                h = (h == -1) ? dp.getHeight() : h;
                dp.setPreferredSize(new Dimension(w, h));
                dp.setSize(w, h);
            }
        });
    }

    /**
	 * 根据所需要画的图形的类型，设置监听器。
	 * @param st
	 */
    public void setShapeListener(ShapeType st, boolean fill, Color c) {
        if (currl != null) {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.removeMouseListener(currl);
            this.removeMouseMotionListener(currl);
        }
        switch(st) {
            case RECT:
                this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                crl.setColor(c);
                crl.setFill(fill);
                this.addMouseListener(crl);
                this.addMouseMotionListener(crl);
                currl = crl;
                break;
            case LINE:
                this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                cll.setColor(c);
                cll.setFill(fill);
                this.addMouseListener(cll);
                this.addMouseMotionListener(cll);
                currl = cll;
                break;
            case ELLI:
                this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                cel.setColor(c);
                cel.setFill(fill);
                this.addMouseListener(cel);
                this.addMouseMotionListener(cel);
                currl = cel;
                break;
            case POLYGON:
                this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                cpl.setColor(c);
                cpl.setFill(fill);
                this.addMouseListener(cpl);
                this.addMouseMotionListener(cpl);
                currl = cpl;
                break;
            case NONE:
                break;
            default:
                break;
        }
    }

    /**
	 * 擦除所有形状。
	 */
    public void clear() {
        shapes.clear();
        this.repaint();
    }

    public void setColor(Color c) {
        if (currl != null) {
            currl.setColor(c);
        }
    }

    public void setFill(boolean fill) {
        if (currl != null) {
            currl.setFill(fill);
        }
    }

    public void addShape(MyShape s) {
        shapes.add(s);
    }

    public MyShape deleteShape(MyShape s) {
        shapes.remove(s);
        return s;
    }

    public MyShape lastShape() {
        return shapes.get(shapes.size() - 1);
    }

    public void writeToFile(File f) {
        if (f == null) {
            return;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(f, false);
            bw = new BufferedWriter(fw);
            for (MyShape s : shapes) {
                bw.write(s.toString());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                fw.close();
                bw.close();
            } catch (IOException e1) {
            }
        }
    }

    public void readFromFile(File f) {
        if (f == null) {
            return;
        }
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String s = br.readLine();
            while (s != null) {
                s = br.readLine();
                System.out.println(s);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                br.close();
                fr.close();
            } catch (IOException e1) {
            }
        }
        this.repaint();
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth() + 1, this.getHeight() + 1);
        g2d.setColor(Color.BLACK);
        Color c = null, oldc = null;
        for (MyShape s : shapes) {
            oldc = g2d.getColor();
            c = s.getColor();
            g2d.setColor(c);
            if (s.isFill()) {
                g2d.fill(s.getShape());
            } else {
                g2d.draw(s.getShape());
            }
            g2d.setColor(oldc);
        }
    }

    private ArrayList<MyShape> shapes;

    private MyMouseAdapter cll;

    private MyMouseAdapter crl;

    private MyMouseAdapter cel;

    private MyMouseAdapter cpl;

    private MyMouseAdapter currl;

    private DrawPanel dp = this;
}
