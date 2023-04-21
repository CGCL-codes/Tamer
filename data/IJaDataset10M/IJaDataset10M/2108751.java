package wood.model.entity;

import java.util.Map;
import lawu.util.iterator.UniversalIterator;
import org.apache.log4j.Logger;
import wood.model.entity.occupation.Occupation;
import wood.model.item.EquippableItem;
import wood.model.item.Item;
import wood.model.item.TakeableItem;
import wood.model.map.Map.Tile;
import wood.model.tileobject.EntitySnapshot;
import wood.model.tileobject.MobileObject;
import wood.model.tileobject.TileObject;
import wood.model.tileobject.TileObjectVisitor;
import wood.skills.ActiveSkill;
import wood.skills.PassiveSkill;
import wood.skills.SkillPlacement;

public abstract class Entity extends MobileObject {

    private static final Logger logger = Logger.getLogger("wood.model.entity");

    private static final long serialVersionUID = -5622044521586140848L;

    private Occupation occupation;

    private Inventory inventory;

    private final SkillSet<ActiveSkill> activeSkills;

    private final SkillSet<PassiveSkill> passiveSkills;

    private Stats stats;

    private Equipment equipment;

    private int visibilityRange = 4;

    private int money;

    private double visibilityModifier = 0;

    private final String name;

    public Entity(Tile tile, Occupation occupation, String name) {
        super(tile);
        this.occupation = occupation;
        activeSkills = new SkillSet<ActiveSkill>();
        passiveSkills = new SkillSet<PassiveSkill>();
        inventory = new Inventory();
        equipment = new Equipment(this);
        this.stats = occupation.getPrototypeStats();
        this.addUpdateable(stats);
        occupation.createSkills(this.activeSkills, this.passiveSkills, this);
        this.name = name;
    }

    /**=====================================================================
	 * Effin Fightin' Interface
	 */
    public void killThisMotherfucker() {
        doDamageToThisMotherfucker(stats.getLife(), null);
    }

    public void doDamageToThisMotherfucker(int damage, Entity e) {
        logger.log(wood.log.WoodLevel.GAME, name + " has taken " + damage + " damage");
        stats.modifyLife(0 - damage);
        if (stats.getLivesLeft() <= 0) {
            die();
        }
    }

    protected void die() {
        this.setCurrentTile(null);
    }

    public void accept(TileObjectVisitor tileObjectVisitor) {
        tileObjectVisitor.visitEntity(this);
    }

    public void dropItem(int slot) {
        replaceItemOnMap(inventory.removeItem(inventory.getItem(slot)));
    }

    public void removeItemFromInventory(TakeableItem item) {
        replaceItemOnMap(inventory.removeItem(item));
    }

    private void replaceItemOnMap(Item i) {
        i.setCurrentTile(null);
        Tile t = getCurrentTile().getAdjacentTile(getFacingDirection());
        i.setCurrentTile(t);
    }

    public void addItemToInventory(TakeableItem item) {
        inventory.addItem(item);
    }

    public Equipment getEquipement() {
        return this.equipment;
    }

    public boolean take(TakeableItem item) {
        return inventory.addItem(item);
    }

    public void useItem(int slot) {
        inventory.getItem(slot).use(this);
    }

    public void equip(EquippableItem item) {
        EquippableItem old;
        if (!item.isUseful(passiveSkills)) {
            logger.log(wood.log.WoodLevel.GAME, String.format("%s can't equip that %s!", this, item));
            return;
        }
        removeItemFromInventory(item);
        old = equipment.equip(item);
        if (old != null) addItemToInventory(old);
        calcPhys();
        calcMag();
    }

    public void unequip(int slot) {
        EquippableItem item = equipment.getWeapon();
        equipment.unequip(slot);
        if (item != null) inventory.addItem(item);
    }

    public Map<SkillPlacement, ActiveSkill> getActiveSkills() {
        return activeSkills.getMap();
    }

    public Map<SkillPlacement, PassiveSkill> getPassiveSkills() {
        return passiveSkills.getMap();
    }

    public void useSkill(SkillPlacement placement) {
        ActiveSkill s = activeSkills.getSkill(placement);
        if (s != null) s.execute();
    }

    public void addSkillPoint(SkillPlacement placement) {
        if (getAvailableSkillPoints() <= 0 || activeSkills.getSkill(placement) == null) {
            logger.info("Skill error!");
            return;
        }
        stats.decreaseAvailableSkillPoints();
        activeSkills.getSkill(placement).incSkillLevel();
    }

    public void addSkill(SkillPlacement placement, ActiveSkill skill) {
        activeSkills.addSkill(placement, skill);
    }

    public double getVisibilityRange() {
        return this.getVisibilityModifier() + visibilityRange * getStats().getLifePercentage();
    }

    public UniversalIterator<Tile> getVisibleTileSet() {
        return new VisibleTilesIterator((int) Math.round(getVisibilityRange()), getCurrentTile()).getTiles();
    }

    public Stats getStats() {
        return stats;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public UniversalIterator<TakeableItem> getInventory() {
        return inventory.getItems();
    }

    public int getInventoryCapacity() {
        return inventory.getCapacity();
    }

    public boolean canSwim() {
        return false;
    }

    public boolean canWalk() {
        return true;
    }

    public Entity getSnapshot() {
        return new EntitySnapshot(this);
    }

    protected int getSpeed() {
        return getStats().getMovement();
    }

    public void calcPhys() {
        stats.setEquipmentsPhysicalAttack(equipment.calcTotalPhysAttack(this.passiveSkills));
        stats.setEquipmentPhysicalDefense(equipment.calcTotalPhysDefense());
    }

    public void calcMag() {
        stats.setEquipmentsMagicalAttack(equipment.calcTotalMagAttack(this.passiveSkills));
        stats.setEquipmentMagicalDefense(equipment.calcTotalMagDefense());
    }

    public synchronized void setVisibilityModifier(double amount) {
        this.visibilityModifier = amount;
    }

    public synchronized double getVisibilityModifier() {
        return visibilityModifier;
    }

    @Override
    public boolean blocks(TileObject tileObject) {
        if (tileObject instanceof Entity) return true;
        return super.blocks(tileObject);
    }

    protected void setMoney(int value) {
        money = value;
    }

    protected int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void addExperience(int amount) {
        this.stats.incrementExperience(amount);
    }

    public String observe(int level) {
        switch(level) {
            case 0:
                return getName() + " has level " + getStats().getLevel() + ".";
            case 1:
                return getName() + " has level " + getStats().getLevel() + " and health " + getStats().getLife() + ".";
            default:
                return getName() + " has level " + getStats().getLevel() + ", health " + getStats().getLife() + ", and " + getStats().getMana() + ".";
        }
    }

    public abstract String talkTo();

    public int getAvailableSkillPoints() {
        return stats.getAvailableSkillPoints();
    }
}
