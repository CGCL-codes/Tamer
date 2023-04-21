package de.boardgamesonline.bgo2.shogun.game;

import de.boardgamesonline.bgo2.shogun.client.Shogun;
import de.boardgamesonline.bgo2.shogun.game.Unit.UnitType;
import de.boardgamesonline.bgo2.shogun.game.GameState.State;

;

/**
 * @author Matthias Gl�fke
 * @author Michael Sch�fers
 * @author Jochen Witt
 * Eine Provinz
 */
public class Province implements IArrayHandling {

    /**
	 * ID of the Province - necessary for Server/Client communication
	 */
    private int provinceID;

    /**
	 *  The name (as written on the map).
	 */
    private String provincename;

    /**
	 *  Neighbours of the province.
	 *  Those you can reach by land for a fight.
	 */
    private Province[] neighbours;

    /**
	 *  Neighbours of the province.
	 *  Those you can reach by seaway for a fight.
	 */
    private Province[] seaways;

    /**
	 *  The slots to place the units in.
	 *  An Array of pairs of coordinates.
	 *  Each with x and y value.
	 */
    private String[] slots1, slots2, slots3, slots4, slots5, slots6, slots7, slots8;

    /**
	 * Spieler dem die Provinz geh�rt
	 */
    private Player player;

    /**
	 * Number of units of type X.
	 */
    private int spearmen, samurai, bowmen, riflemen, ronin, castle, fortress;

    /**
	 * Number of moved units of type X.
	 */
    private int movedSpearmen, movedSamurai, movedBowmen, movedRiflemen, movedRonin;

    /**
	 * indicates wether the has province's army has fought a battle
	 * in the actual round or not
	 */
    private boolean hasAttacked;

    /**
	 * indicates wether a unit was set in the supply-phase or not
	 */
    private boolean isSupplied;

    /**
	 * Which slot is occupied by which unit?
	 * Slots 1-5 are for province units.
	 */
    private UnitType[] slots = new UnitType[5];

    /**
	 * The banner in this province.
	 */
    private Banner banner = null;

    /**
	 *Constructor
	 *@param name Province name.
	 *@param neighbours2 Neighbours.
	 *@param seaways2 Seaways.
	 *@param slots1 The coordinates for slot 1.
	 *@param slots2 The coordinates for slot 2.
	 *@param slots3 The coordinates for slot 3.
	 *@param slots4 The coordinates for slot 4.
	 *@param slots5 The coordinates for slot 5.
	 *@param slots6 The coordinates for slot 6.
	 *@param slots7 The coordinates for slot 7.
	 *@param slots8 The coordinates for slot 8.
	 */
    public Province(String name, Province[] neighbours2, Province[] seaways2, String[] slots1, String[] slots2, String[] slots3, String[] slots4, String[] slots5, String[] slots6, String[] slots7, String[] slots8) {
        super();
        this.provincename = name;
        this.neighbours = neighbours2;
        this.seaways = seaways2;
        this.slots1 = slots1;
        this.slots2 = slots2;
        this.slots3 = slots3;
        this.slots4 = slots4;
        this.slots5 = slots5;
        this.slots6 = slots6;
        this.slots7 = slots7;
        this.slots8 = slots8;
        this.banner = null;
        this.samurai = 0;
        this.bowmen = 0;
        this.spearmen = 1;
        this.riflemen = 0;
        this.ronin = 0;
        this.movedSamurai = 0;
        this.movedBowmen = 0;
        this.movedSpearmen = 0;
        this.movedRiflemen = 0;
        this.movedRonin = 0;
        this.castle = 0;
        this.fortress = 0;
        for (int i = 1; i <= 4; i++) {
            this.slots[i] = null;
        }
    }

    /**
	 * Returns an int representation of the instance.
	 * 
	 * int[] province = {int classID, int provinceID, int playerID,
	 * int bannerID, int samurai, int bowmen, int spearmen, int riflemen,
	 * int castle, int fortress}
	 * 
	 * @return array Representing a province-instance.
	 */
    public int[] instanceToArray() {
        int playerID;
        int bannerID;
        if (player != null) {
            playerID = player.getPlayerID();
        } else {
            playerID = -1;
        }
        if (banner != null) {
            bannerID = banner.getBannerID();
        } else {
            bannerID = -1;
        }
        return new int[] { 2, provinceID, playerID, bannerID, samurai, bowmen, spearmen, riflemen, castle, fortress };
    }

    /**
	 * Updates the instace according to the values sent from the server.
	 * @param values The int array with the new instance values.
	 */
    public void arrayToInstance(int[] values) {
        if (values[2] != -1) {
            this.player = Shogun.getClient().getGamefield().getPlayers().get(values[3]);
        } else {
            this.player = null;
        }
        if (values[3] != -1) {
            this.banner = Shogun.getClient().getGamefield().getBanners().get(values[2])[values[3]];
        } else {
            this.banner = null;
        }
        this.samurai = values[4];
        this.bowmen = values[5];
        this.spearmen = values[6];
        this.riflemen = values[7];
        this.castle = values[8];
        this.fortress = values[9];
    }

    /**
	 * return provinceID
	 * @return provinceID
	 */
    public int getProvinceID() {
        return provinceID;
    }

    /**
	 * returns the name
	 * @return name
	 */
    public String getName() {
        return this.provincename;
    }

    /**
	 * Add a unit to this province.
	 * @param type the type of the unit to add.
	 */
    public void addUnit(UnitType type) {
        if (getTotalunits() <= 5 || type.equals(UnitType.Ronin) || type.equals(UnitType.Castle) || type.equals(UnitType.Fortress)) {
            switch(type) {
                case Castle:
                    if (castle == 0 && fortress == 0) {
                        castle = 1;
                    }
                    break;
                case Fortress:
                    if (fortress == 0 && castle == 1) {
                        castle = 0;
                        fortress = 1;
                    }
                    break;
                case Spearman:
                    spearmen++;
                    slots[getNextFreeSlot()] = type;
                    break;
                case Bowman:
                    bowmen++;
                    slots[getNextFreeSlot()] = type;
                    break;
                case Rifleman:
                    riflemen++;
                    slots[getNextFreeSlot()] = type;
                    break;
                case Samurai:
                    samurai++;
                    slots[getNextFreeSlot()] = type;
                    break;
                case Ronin:
                    ronin++;
                    break;
                default:
                    break;
            }
        }
    }

    /**
	 * Removes a unit to this province.
	 * @param type the type of the unit to add.
	 */
    public void removeUnit(UnitType type) {
        switch(type) {
            case Spearman:
                if (spearmen > 0) {
                    spearmen--;
                    removeUnitFromSlot(type);
                }
                break;
            case Bowman:
                if (bowmen > 0) {
                    bowmen--;
                    removeUnitFromSlot(type);
                }
                break;
            case Rifleman:
                if (riflemen > 0) {
                    riflemen--;
                    removeUnitFromSlot(type);
                }
                break;
            case Samurai:
                if (samurai > 0) {
                    samurai--;
                    removeUnitFromSlot(type);
                }
                break;
            case Ronin:
                if (ronin > 0) {
                    ronin--;
                }
                break;
            default:
                break;
        }
        if (getTotalunits() == 0 && getUnit(UnitType.Ronin) == 0 && getBanner() == null) {
            player.setProvinceCount(player.getProvinceCount() - 1);
            this.player = null;
        }
    }

    /**
	 * Add a unit to this province.
	 * @param type the type of the unit to add.
	 * @return number number of units of this type.
	 */
    public int getUnit(UnitType type) {
        switch(type) {
            case Castle:
                return castle;
            case Fortress:
                return fortress;
            case Spearman:
                return spearmen;
            case Bowman:
                return bowmen;
            case Rifleman:
                return riflemen;
            case Samurai:
                return samurai;
            case Ronin:
                return ronin;
            default:
                return 0;
        }
    }

    /**
	 * Anzahl der bereits bewegten Einheiten eines Typs
	 * @param type - Einheitentyp
	 * @return - Anzahl der bereits bewegten
	 */
    public int getMovedUnits(UnitType type) {
        switch(type) {
            case Spearman:
                return movedSpearmen;
            case Bowman:
                return movedBowmen;
            case Rifleman:
                return movedRiflemen;
            case Samurai:
                return movedSamurai;
            case Ronin:
                return movedRonin;
            default:
                return 0;
        }
    }

    /**
	 * �ndert die Anzahl bewegter Einheiten eines Typs
	 * @param type - Einheitentyp
	 * @param count - neue Anzahl
	 */
    public void setMovedUnits(UnitType type, int count) {
        switch(type) {
            case Spearman:
                movedSpearmen = count;
            case Bowman:
                movedBowmen = count;
            case Rifleman:
                movedRiflemen = count;
            case Samurai:
                movedSamurai = count;
            case Ronin:
                movedRonin = count;
            default:
        }
    }

    /**
	 * commits actions on the initialisation of a specific gamestate
	 * @param state the gamestate
	 */
    public void initState(State state) {
        if (state == State.Attack || state == State.Endmove) {
            movedSamurai = 0;
            movedBowmen = 0;
            movedSpearmen = 0;
            movedRiflemen = 0;
            hasAttacked = false;
        } else if (state == State.DeliverUnits) {
            isSupplied = false;
        }
    }

    /**
	 * @return player The name of the player holding the province.
	 */
    public Player getPlayer() {
        return player;
    }

    /**
	 * @param player The new name of the player.
	 */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
	 * Gesamtzahl der Einheiten in Provinz
	 * @return t
	 */
    public int getTotalunits() {
        return this.bowmen + this.riflemen + this.spearmen + this.samurai;
    }

    /**
	 * Returns an Array of provinces that can be reached by land.
	 * @return neighbours 
	 */
    public Province[] getNeighbours() {
        return neighbours;
    }

    /**
	 * sets the province's neighbour-provinces
	 * @param neighbours - references to the neighbours
	 */
    public void setNeighbours(Province[] neighbours) {
        this.neighbours = neighbours;
    }

    /**
	 * Returns an Array of provinces that can be reached by sea.
	 * @return neighbours 
	 */
    public Province[] getSeaways() {
        return seaways;
    }

    /**
	 * returns whether a given province is linked by a Seaway
	 * with the current province
	 * @param province Province
	 * @return true or false
	 */
    public boolean linkedbySeaway(Province province) {
        boolean b = false;
        for (int j = 0; j < this.getSeaways().length; j++) {
            if (province.equals(this.getSeaways()[j])) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
	 * sets the province's seaway-neighbour-provinces
	 * @param seaways - references to the seaway-neighbours
	 */
    public void setSeaways(Province[] seaways) {
        this.seaways = seaways;
    }

    /**
	 * Returns the name of the province.
	 * @return provincename 
	 */
    public String getProvinceName() {
        return provincename;
    }

    /**
	 * Sets a banner for this province if nothing is set yet.
	 * @param banner The banner that should be placed in this province.
	 */
    public void addBanner(Banner banner) {
        if (this.banner == null) {
            this.banner = banner;
            banner.setProvince(this);
        }
    }

    /**
	 * Remove the banner from this province.
	 */
    public void removeBanner() {
        banner.setProvince(null);
        this.banner = null;
        if (getTotalunits() == 0 && getUnit(UnitType.Ronin) == 0) {
            player.setProvinceCount(player.getProvinceCount() - 1);
        }
    }

    /**
	 * Returns the banner that is currently placed in this province.
	 * @return banner The banner that is currently placed in this province.
	 */
    public Banner getBanner() {
        return banner;
    }

    /**
	 * Returns the slots for unit placing.
	 * @param i the slot number.
	 * @return slots A Stringarray with the slot coordinates.
	 */
    public String[] getSlots(int i) {
        switch(i) {
            case 1:
                return slots1;
            case 2:
                return slots2;
            case 3:
                return slots3;
            case 4:
                return slots4;
            case 5:
                return slots5;
            case 6:
                return slots6;
            case 7:
                return slots7;
            case 8:
                return slots8;
            default:
                return slots1;
        }
    }

    /**
	 * Eine Einheit wird aus dieser Provinz in eine andere
	 * verschoben, sofern
	 * - der Einheitentyp vorhanden ist
	 * - ein Slot in der Provinz frei ist
	 * - die Provinz benachbart ist
	 * - die Provinz dem Spieler geh�rt oder leer ist
	 * wichtig: Vermerk, dass sich eine Einheit nicht mehr
	 * bewegen kann!!
	 * @param unit - der zu bewegende Einheitentyp
	 * @param province - die betreffende (Nachbar-)Provinz
	 */
    public void moveUnitToProvince(UnitType unit, Province province) {
        if (province != this && this.getUnit(unit) > 0 && province.getTotalunits() < 5 && (player == province.getPlayer() || province.getPlayer() == null) && this.getMovedUnits(unit) < this.getUnit(unit)) {
            for (int i = 0; i < neighbours.length; i++) {
                if (neighbours[i] == province) {
                    removeUnit(unit);
                    province.addUnit(unit);
                    province.setMovedUnits(unit, getMovedUnits(unit) + 1);
                    break;
                }
            }
            for (int i = 0; i < seaways.length; i++) {
                if (seaways[i] == province) {
                    removeUnit(unit);
                    province.addUnit(unit);
                    province.setMovedUnits(unit, getMovedUnits(unit) + 1);
                    break;
                }
            }
        }
    }

    /**
	 * bewegt ein in der Provinz befindliches Banner in eine andere
	 * Provinz sofern
	 * - ein Banner vorhanden ist
	 * - in der anderen Provinz kein Banner ist
	 * - die Provinz benachbart ist
	 * - die Provinz dem Spieler geh�rt oder leer ist
	 * - das Banner noch Aktionspunkte hat
	 * wichtig: einen Aktionspunkt abziehn
	 * @param province - die "Zielprovinz"
	 */
    public void moveBannerToProvince(Province province) {
        if (province != this && this.getBanner() != null && province.getBanner() == null && (player == province.getPlayer() || province.getPlayer() == null) && banner.getRemainingMoves() > 0) {
            for (int i = 0; i < neighbours.length; i++) {
                if (neighbours[i] == province) {
                    province.addBanner(banner);
                    banner.setRemainingMoves(banner.getRemainingMoves() - 1);
                    banner = null;
                    break;
                }
            }
            for (int i = 0; i < seaways.length; i++) {
                if (seaways[i] == province) {
                    province.addBanner(banner);
                    banner.setRemainingMoves(banner.getRemainingMoves() - 1);
                    banner = null;
                    break;
                }
            }
        }
    }

    /**
	 * method to move units to the province's banner if
	 * - the UnitType is avaiable
	 * - the Banner has free slots
	 * @param unit - UnitType
	 */
    public void moveUnitToBanner(UnitType unit) {
        if (getUnit(unit) > 0 && banner != null) {
            if (unit == UnitType.Samurai || unit == UnitType.Bowman) {
                if ((getBanner().getUnit(UnitType.Samurai) + getBanner().getUnit(UnitType.Bowman)) < 4) {
                    getBanner().addUnit(unit);
                    removeUnit(unit);
                }
            } else if (unit == UnitType.Spearman || unit == UnitType.Rifleman) {
                if ((getBanner().getUnit(UnitType.Spearman) + getBanner().getUnit(UnitType.Rifleman)) < 10) {
                    getBanner().addUnit(unit);
                    removeUnit(unit);
                }
            }
        }
        if (getMovedUnits(unit) > getUnit(unit)) {
            setMovedUnits(unit, getUnit(unit));
        }
    }

    /**
	 * @return the hasAttacked
	 */
    public boolean isHasAttacked() {
        return hasAttacked;
    }

    /**
	 * @param hasAttacked the hasAttacked to set
	 */
    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
	 * @return the isSupplied
	 */
    public boolean isSupplied() {
        return isSupplied;
    }

    /**
	 * @param isSupplied the isSupplied to set
	 */
    public void setSupplied(boolean isSupplied) {
        this.isSupplied = isSupplied;
    }

    /**
	 * Gets the next free slot and puts the added unit in there.
	 * @return slot The number of the next free slot.
	 */
    public int getNextFreeSlot() {
        int j = 0;
        for (int i = 0; i <= 4; i++) {
            if (slots[i] == null) {
                j = i;
            }
        }
        return j;
    }

    /**
	 * Empties out a slot holding a Unit of this type.
	 * @param type The type of unit that needs to be kicked out of its slot.
	 */
    public void removeUnitFromSlot(UnitType type) {
        for (int i = 0; i <= 4; i++) {
            if (slots[i] == type) {
                slots[i] = null;
            }
        }
    }

    /**
	 * Setzt den Provinz id
	 * @param provinceID Der Provinz id
	 */
    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }
}
