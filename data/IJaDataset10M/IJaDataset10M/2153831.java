package net.sourceforge.tile3d.view.writeFile.main.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.sourceforge.tile3d.view.writeFile.core.model.X3dTile;

public class TotalTiles implements Serializable {

    HashMap<Long, TileCalculator> totaltiles;

    public TotalTiles() {
        super();
        totaltiles = new HashMap<Long, TileCalculator>();
    }

    public void clear() {
        totaltiles.clear();
    }

    public Object clone() {
        return totaltiles.clone();
    }

    public boolean containsKey(Object p_key) {
        return totaltiles.containsKey(p_key);
    }

    public boolean containsValue(Object p_value) {
        return totaltiles.containsValue(p_value);
    }

    public Set<Entry<Long, TileCalculator>> entrySet() {
        return totaltiles.entrySet();
    }

    public boolean equals(Object p_o) {
        return totaltiles.equals(p_o);
    }

    public TileCalculator get(Object p_key) {
        return totaltiles.get(p_key);
    }

    public int hashCode() {
        return totaltiles.hashCode();
    }

    public boolean isEmpty() {
        return totaltiles.isEmpty();
    }

    public Set<Long> keySet() {
        return totaltiles.keySet();
    }

    public TileCalculator put(Long p_key, TileCalculator p_value) {
        return totaltiles.put(p_key, p_value);
    }

    public void putAll(Map<? extends Long, ? extends TileCalculator> p_m) {
        totaltiles.putAll(p_m);
    }

    public TileCalculator remove(Object p_key) {
        return totaltiles.remove(p_key);
    }

    public int size() {
        return totaltiles.size();
    }

    public String toString() {
        return totaltiles.toString();
    }

    public Collection<TileCalculator> values() {
        return totaltiles.values();
    }

    public void calculatorTile(BoxTileBase p_boxTile, X3dTile p_tile) {
        TileCalculator tileCalculator = get(p_tile.getTileId());
        p_boxTile.updateFullTileTotal(tileCalculator);
        p_boxTile.updateHalfTileTotal(tileCalculator);
    }

    public void reset() {
        Set<Entry<Long, TileCalculator>> tileCalculatorEntry = totaltiles.entrySet();
        for (Iterator iter = tileCalculatorEntry.iterator(); iter.hasNext(); ) {
            Entry<Long, TileCalculator> element = (Entry<Long, TileCalculator>) iter.next();
            TileCalculator value = element.getValue();
            value.reset();
        }
    }
}
