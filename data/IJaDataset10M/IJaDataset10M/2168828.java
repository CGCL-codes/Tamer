package myriadempires.core;

import java.awt.Shape;
import myriadempires.core.interfaces.ITerritory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Element;
import myriadempires.core.interfaces.IPlayer;
import svg.SVGPathDataParser;

/**
 *
 * @author Richard
 */
public class Territory extends ITerritory {

    public final String ID;

    public final String name;

    public final Shape GData;

    private ITerritory[] borders;

    private int garrison = 0;

    private IPlayer owner = new IPlayer.referById(0);

    public Territory(Element xml) {
        ID = xml.getAttribute("id");
        name = xml.getAttribute("name");
        GData = SVGPathDataParser.parse(xml.getAttribute("pathdata"));
        String[] b = xml.getAttribute("borders").split(",");
        ArrayList<ITerritory> brdrs = new ArrayList<ITerritory>();
        for (int i = 0; i < b.length; i++) {
            final String bid = b[i];
            brdrs.add(ITerritory.ReferByID(bid));
        }
        borders = brdrs.toArray(new ITerritory[0]);
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    /**
	 * Gets all territories with which this territory has a border.
	 * @return
	 */
    public ITerritory[] getBorders() {
        return borders.clone();
    }

    /**
	 * Gets all border territories. If <code>foreignOnly</code> is true, the list is
	 * filtered to only contain territories owned by a player other than this
	 * territory's owner.
	 *
	 * @param foreignOnly Determines whether or not the list should be filtered to
	 * contain only foreign territories.
	 * @return A list of border territories.
	 */
    public ITerritory[] getBorders(boolean foreignOnly) {
        List<ITerritory> b = new ArrayList<ITerritory>(Arrays.asList(borders));
        if (foreignOnly) {
            b.removeAll(Arrays.asList(Game.Map.getTerritories(owner)));
        }
        return b.toArray(new ITerritory[0]);
    }

    /**
	 * Gets all border territories owned by <code>owner</code>.
	 *
	 * This is a convenience method, identical to
	 * <code>getBorders(owner,true)</code>
	 *
	 * @param owner The player who's territories should be returned.
	 * @return A list of border territories.
	 * @see #getBorders(risk.core.reference.IPlayer, boolean) 
	 */
    public ITerritory[] getBorders(IPlayer owner) {
        return getBorders(owner, true);
    }

    /**
	 * Gets all border territories, if <code>retain</code> is true, that are
	 * owned by <code>owner</code>. Otherwise (that is, if <code>retain</code>
	 * is false), all territories that are <em>not</em> owned by <code>owner
	 * </code>.
	 *
	 * @param owner The player who's territories should be returned.
	 * @return A list of border territories.
	 */
    public ITerritory[] getBorders(IPlayer owner, boolean retain) {
        List<ITerritory> b = new ArrayList<ITerritory>(Arrays.asList(borders));
        if (retain) {
            b.retainAll(Arrays.asList(Game.Map.getTerritories(owner)));
        } else {
            b.removeAll(Arrays.asList(Game.Map.getTerritories(owner)));
        }
        return b.toArray(new ITerritory[0]);
    }

    public Shape getGData() {
        return GData;
    }

    public IPlayer getOwner() {
        return owner;
    }

    public void setOwner(IPlayer newOwner) {
        owner = newOwner;
    }

    public String getGarrisonFormatted() {
        return Integer.toString(garrison);
    }

    public int getGarrison() {
        return garrison;
    }

    public void addToGarrison(int count) {
        this.garrison += count;
    }

    public Continent getContinent() {
        return Game.Map.getContinent(this);
    }

    public boolean borders(ITerritory versus) {
        for (ITerritory t : borders) {
            if (t.equals(versus)) {
                return true;
            }
        }
        return false;
    }
}
