package net.sf.l2j.gameserver.model;

/**
 * This class ...
 *
 * @version $Revision: 1.2.4.2 $ $Date: 2005/03/27 15:29:33 $
 */
public final class L2PledgeSkillLearn {

    private final int _id;

    private final int _level;

    private final String _name;

    private final int _repCost;

    private final int _baseLvl;

    private final int _itemId;

    public L2PledgeSkillLearn(int id, int lvl, int baseLvl, String name, int cost, int itemId) {
        _id = id;
        _level = lvl;
        _baseLvl = baseLvl;
        _name = name.intern();
        _repCost = cost;
        _itemId = itemId;
    }

    /**
	 * @return Returns the id.
	 */
    public int getId() {
        return _id;
    }

    /**
	 * @return Returns the level.
	 */
    public int getLevel() {
        return _level;
    }

    /**
	 * @return Returns the minLevel.
	 */
    public int getBaseLevel() {
        return _baseLvl;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return _name;
    }

    /**
	 * @return Returns the spCost.
	 */
    public int getRepCost() {
        return _repCost;
    }

    public int getItemId() {
        return _itemId;
    }
}
