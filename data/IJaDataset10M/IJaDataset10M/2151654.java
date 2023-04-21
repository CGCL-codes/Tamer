package bbj.graphicsobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.awt.Polygon;
import javax.swing.JTextArea;
import java.awt.*;
import bbj.*;

/**
 *
 * @author Alexander
 */
public class UIFreeComment extends SceneItem {

    /** Номер экзампляра данного класса на сцене */
    public static int m_localNumber = 0;

    /**
     * Основной конструктор по умолчанию
     * @param x Координата по оси Х
     * @param y Координата по оси У
     * @param text Текст комментария
     */
    public UIFreeComment(int x, int y) {
        setDefaultName();
        this.defX = x;
        this.defY = y;
        defW = 120;
        defH = 80;
        applyScale(getGraphics());
        this.setBounds(this.x, this.y, w, h);
        this.m_isSelected = false;
        SceneItemListener listener = new SceneItemListener(this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.setToolTipText("Свободный комментарий: " + this.m_text);
    }

    /**
     * Метод отрисовки комментария
     * @param g Компонент, на котором рисуем
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        applyScale(g);
        Graphics2D g2 = (Graphics2D) g;
        int i, rem;
        Polygon dark = new Polygon();
        this.setBounds(this.x, this.y, w + 10, h + 10);
        x += 10;
        y += 10;
        dark.addPoint(x, y);
        dark.addPoint(x + w - 20, y);
        dark.addPoint(x + w, y + 20);
        dark.addPoint(x + w, y + h);
        dark.addPoint(x, y + h);
        x -= 10;
        y -= 10;
        Color c = new Color(0, 0, 0, 125);
        Stroke stroke = new BasicStroke(1);
        g2.setStroke(stroke);
        g.setColor(c);
        g.fillPolygon(dark);
        Polygon p = new Polygon();
        p.addPoint(x, y);
        p.addPoint(x + w - 20, y);
        p.addPoint(x + w, y + 20);
        p.addPoint(x + w, y + h);
        p.addPoint(x, y + h);
        if (m_isSelected) g.setColor(Color.getHSBColor(105, 215, 245)); else g.setColor(new Color(255, 210, 210));
        g.fillPolygon(p);
        if (m_isSelected) g.setColor(Color.red); else g.setColor(Color.black);
        g.drawPolygon(p);
        g.setColor(Color.black);
        char[] drawedText = m_text.toCharArray();
        for (i = 0; i < drawedText.length / 14 && i < 4; i++) {
            g.drawChars(drawedText, 0 + i * 14, 14, x + 5, 25 + i * (int) (12 * m_SR) + y);
        }
        char[] dots = { '.', '.', '.' };
        if (i >= 4) g.drawChars(dots, 0, 3, x + 5, 18 + i * (int) (12 * m_SR) + y);
        i++;
        rem = drawedText.length % 14;
        if (rem != 0) g.drawChars(drawedText, drawedText.length - rem, rem, x + 5, 18 + i * (int) (12 * m_SR) + y);
        if (m_isEdit) f.repaint();
    }

    @Override
    protected void setDefaultName() {
        m_text = "Комментарий " + Integer.toString(m_localNumber++);
    }
}
