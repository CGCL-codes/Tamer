package net.slashie.expedition.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.slashie.expedition.item.ItemFactory;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.Appearance;

public class Store implements ItemContainer, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Map<String, StoreItemInfo> prices = new Hashtable<String, StoreItemInfo>();

    private List<StoreItemInfo> pricesList = new ArrayList<StoreItemInfo>();

    private List<Equipment> inventory = new ArrayList<Equipment>();

    private String text;

    private String ownerName;

    private Appearance ownerAppearance;

    private GoodType mainGoodType;

    private Map<String, Integer> maxQuantities = new HashMap<String, Integer>();

    public Store(GoodType mainGoodType) {
        super();
        this.mainGoodType = mainGoodType;
    }

    public List<Equipment> getInventory() {
        return inventory;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Appearance getOwnerAppearance() {
        return ownerAppearance;
    }

    public void setOwnerAppearance(Appearance ownerAppearance) {
        this.ownerAppearance = ownerAppearance;
    }

    public StoreItemInfo getBasicInfo(ExpeditionItem item, Expedition expedition) {
        return prices.get(item.getFullID());
    }

    public StoreItemInfo getBuyInfo(ExpeditionItem item, Expedition expedition) {
        return prices.get(item.getFullID());
    }

    public void addItem(int quantity, StoreItemInfo info) {
        ExpeditionItem item = ItemFactory.createItem(info.getFullId());
        addItem(item, quantity);
        prices.put(item.getFullID(), info);
        pricesList.add(info);
        maxQuantities.put(item.getFullID(), quantity);
    }

    public void addItem(StoreShipInfo info) {
        ExpeditionItem ship = ItemFactory.createShip(info.getType(), info.getName());
        addItem(ship, 1);
        prices.put(ship.getFullID(), info);
        pricesList.add(info);
    }

    @Override
    public Store clone() {
        try {
            Store store = (Store) super.clone();
            store.inventory = new ArrayList<Equipment>();
            for (Equipment eq : getInventory()) {
                store.inventory.add(eq.clone());
            }
            store.prices = new Hashtable<String, StoreItemInfo>();
            for (String key : prices.keySet()) {
                store.prices.put(key, prices.get(key).clone());
            }
            return store;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GoodType getMainGoodType() {
        return mainGoodType;
    }

    @Override
    public void addItem(ExpeditionItem item, int quantity) {
        Equipment existingEquipment = null;
        for (Equipment equipment : inventory) {
            if (equipment.getItem().getFullID().equals(item.getFullID())) {
                existingEquipment = equipment;
                break;
            }
        }
        if (existingEquipment == null) {
            inventory.add(new Equipment(item, quantity));
        } else {
            existingEquipment.increaseQuantity(quantity);
        }
    }

    @Override
    public boolean canCarry(ExpeditionItem item, int quantity) {
        return true;
    }

    @Override
    public Appearance getAppearance() {
        return getOwnerAppearance();
    }

    @Override
    public Appearance getDialogAppearance() {
        return null;
    }

    @Override
    public int getCarryCapacity() {
        return -1;
    }

    @Override
    public int getCarryable(ExpeditionItem eitem) {
        return -1;
    }

    @Override
    public int getCurrentFood() {
        return 0;
    }

    @Override
    public int getCurrentlyCarrying() {
        return 0;
    }

    @Override
    public String getDescription() {
        return getOwnerName();
    }

    @Override
    public int getFoodDays() {
        return 0;
    }

    @Override
    public List<Equipment> getGoods(GoodType goodType) {
        List<Equipment> ret = new ArrayList<Equipment>();
        for (Equipment e : getInventory()) {
            ExpeditionItem g = (ExpeditionItem) e.getItem();
            if (g.getGoodType() == goodType) ret.add(new Equipment(e.getItem(), e.getQuantity()));
        }
        return ret;
    }

    @Override
    public int getItemCount(String fullID) {
        int goodCount = 0;
        List<Equipment> inventory = getInventory();
        for (Equipment equipment : inventory) {
            if (equipment.getItem().getFullID().equals(fullID)) {
                goodCount += equipment.getQuantity();
            }
        }
        return goodCount;
    }

    public int getVehicleCount(String fullId) {
        return getItemCount(fullId);
    }

    @Override
    public int getItemCountBasic(String basicID) {
        int goodCount = 0;
        List<Equipment> inventory = getInventory();
        for (Equipment equipment : inventory) {
            if (((ExpeditionItem) equipment.getItem()).getBaseID().equals(basicID)) {
                goodCount += equipment.getQuantity();
            }
        }
        return goodCount;
    }

    @Override
    public List<Equipment> getItems() {
        return inventory;
    }

    @Override
    public int getTotalShips() {
        return 0;
    }

    @Override
    public int getTotalUnits() {
        return 0;
    }

    @Override
    public String getTypeDescription() {
        return "Store";
    }

    @Override
    public int getWaterDays() {
        return 0;
    }

    @Override
    public boolean isPeopleContainer() {
        return false;
    }

    @Override
    public void reduceQuantityOf(AbstractItem item, int quantity) {
        for (int i = 0; i < inventory.size(); i++) {
            Equipment equipment = (Equipment) inventory.get(i);
            if (equipment.getItem().equals(item)) {
                equipment.reduceQuantity(quantity);
                if (equipment.isEmpty()) {
                    inventory.remove(equipment);
                }
                return;
            }
        }
    }

    @Override
    public boolean requiresUnitsToContainItems() {
        return false;
    }

    public boolean canBuy(ExpeditionItem item, int quantity) {
        return prices.get(item.getFullID()) != null;
    }

    public int getSellPrice(ExpeditionItem item) {
        return prices.get(item.getFullID()).getPrice();
    }

    public void restock() {
        for (StoreItemInfo storeItemInfo : pricesList) {
            int currentQuantity = getItemCount(storeItemInfo.getFullId());
            Integer maxQuantity = maxQuantities.get(storeItemInfo.getFullId());
            if (maxQuantity == null) maxQuantity = 0;
            if (currentQuantity < maxQuantity) {
                int restockQuantity = storeItemInfo.getWeeklyRestock();
                if (restockQuantity + currentQuantity > maxQuantity) {
                    restockQuantity = maxQuantity - currentQuantity;
                }
                if (restockQuantity > 0) {
                    ExpeditionItem item = ItemFactory.createItem(storeItemInfo.getFullId());
                    addItem(item, restockQuantity);
                }
            }
        }
    }
}
