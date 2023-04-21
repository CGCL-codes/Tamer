package examples.dynimg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Date;
import net.sf.sotacs.dynimg.api.CircleMapArea;
import net.sf.sotacs.dynimg.api.IPainterContext;
import net.sf.sotacs.dynimg.api.MapArea;
import net.sf.sotacs.dynimg.api.PolygonMapArea;
import net.sf.sotacs.dynimg.api.RectangleMapArea;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.html.BasePage;

public abstract class MapDirectLink extends BasePage {

    @InitialValue("literal:Click the shapes to see the action....")
    public abstract void setNotice(String msg);

    public void listenToMapHits(IRequestCycle cycle, String arg) {
        setNotice(arg + " was invoked");
    }

    public void paintme(Graphics2D g2d, IPainterContext context) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, context.getWidth(), context.getHeight());
        g2d.setColor(Color.BLACK);
        g2d.drawString("produced " + new Date(), 0, 180);
        g2d.setColor(Color.GREEN);
        Polygon triangle = new Polygon(new int[] { 130, 150, 170 }, new int[] { 0, 60, 0 }, 3);
        g2d.fill(triangle);
        MapArea area = new PolygonMapArea(triangle);
        area.setTooltip("This is a green triangle");
        area.setDirectLinkArgument("Green Triangle");
        context.addMapArea(area);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(0, 0, 60, 60);
        area = new CircleMapArea(30, 30, 30);
        area.setTooltip("This is a black circle");
        area.setDirectLinkArgument("Black Circle");
        context.addMapArea(area);
        Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.BLUE.darker() };
        Rectangle[] bars = new Rectangle[3];
        for (int i = 0; i < 3; i++) {
            int value = (Integer) context.getParameters()[i];
            g2d.setColor(colors[i]);
            bars[i] = new Rectangle(0, 80 + 30 * i, value, 20);
            g2d.fill(bars[i]);
            area = new RectangleMapArea(bars[i]);
            area.setTooltip("results of 200" + i);
            area.setDirectLinkArgument("Bar Results of 200" + i);
            context.addMapArea(area);
        }
    }
}
