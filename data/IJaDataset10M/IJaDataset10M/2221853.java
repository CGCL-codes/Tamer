package saga.view;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.Icon;
import saga.event.MapInventoryEvent;
import saga.event.MapInventoryListener;
import saga.model.Item;
import saga.model.map.Map;
import saga.util.IconFactory;
import saga.util.IconGrid;

/**
 * This is the map layer where dropped items are displayed.
 * @author  Klaus Rennecke
 * @version $Revision: 1.1 $
 */
public class DropLayer extends MapLayer implements MapInventoryListener, VisualAppearance {

    /** Icon cache for item icons. */
    protected HashMap iconCache;

    /** Font for item count. */
    protected Font font;

    /** Font metrics for item count font. */
    protected FontMetrics metrics;

    /** Creates new DropLayer */
    public DropLayer() {
    }

    /** Paints this layer.  */
    protected void paintLayer(Graphics g) {
        if (map == null) {
            return;
        }
        if (metrics == null) {
            if (font == null) {
                font = COUNT_SUBSCRIPT_FONT;
            }
            metrics = g.getFontMetrics(font);
        }
        g.setFont(font);
        Shape clip = g.getClip();
        int maxX = map.getWidth() * DEFAULT_TILE_SIZE;
        int maxY = map.getHeight() * DEFAULT_TILE_SIZE;
        for (int x = 0, mapX = 0; x < maxX; x += DEFAULT_TILE_SIZE, mapX++) {
            if (clip.intersects((double) x, 0, DEFAULT_TILE_SIZE, maxY)) {
                for (int y = 0, mapY = 0; y < maxY; y += DEFAULT_TILE_SIZE, mapY++) {
                    if (clip.intersects((double) x, (double) y, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE)) {
                        paintInventory(g, map.getItemEntriesAt(mapX, mapY).iterator(), x, y);
                    }
                }
            }
        }
    }

    protected void paintInventory(Graphics g, Iterator i, int x, int y) {
        while (i.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
            Icon icon = getIcon((Item) entry.getKey());
            icon.paintIcon(null, g, x, y);
            Integer value = (Integer) entry.getValue();
            if (value.intValue() == 1) continue;
            String count = value.toString();
            Rectangle2D bounds = metrics.getStringBounds(count, g);
            int xx = x + DEFAULT_TILE_SIZE - (int) bounds.getWidth();
            int yy = y + DEFAULT_TILE_SIZE;
            g.setColor(EFFECT_SHADOW_COLOR);
            g.drawString(count, xx, yy);
            g.setColor(COUNT_SUBSCRIPT_COLOR);
            g.drawString(count, --xx, --yy);
        }
    }

    protected Icon getIcon(Item item) {
        String name = item.getIconName();
        Icon icon;
        if (iconCache != null) {
            icon = (Icon) iconCache.get(name);
        } else {
            iconCache = new HashMap();
            icon = null;
        }
        if (icon == null) {
            try {
                icon = IconFactory.getInstance().getIcon(name);
            } catch (Exception ex) {
                icon = IconGrid.Defect.INSTANCE;
            }
            iconCache.put(name, icon);
        }
        return icon;
    }

    /** Sets the size from the given map.  */
    protected void setMap(Map map) {
        if (this.map == map) return;
        if (this.map != null) {
            this.map.removeMapInventoryListener(this);
        }
        super.setMap(map);
        if (this.map != null) {
            this.map.addMapInventoryListener(this);
        }
    }

    /** The map inventory changed.   */
    public void mapInventoryChange(MapInventoryEvent ev) {
        int x = ev.getX() * DEFAULT_TILE_SIZE;
        int y = ev.getY() * DEFAULT_TILE_SIZE;
        repaint(x, y, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
    }
}
