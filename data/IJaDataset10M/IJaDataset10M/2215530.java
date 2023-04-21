package com.l2jserver.gameserver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2ManufactureItem;
import com.l2jserver.gameserver.model.L2RecipeInstance;
import com.l2jserver.gameserver.model.L2RecipeList;
import com.l2jserver.gameserver.model.L2RecipeStatInstance;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.ItemList;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.RecipeBookItemList;
import com.l2jserver.gameserver.network.serverpackets.RecipeItemMakeInfo;
import com.l2jserver.gameserver.network.serverpackets.RecipeShopItemInfo;
import com.l2jserver.gameserver.network.serverpackets.SetupGauge;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.skills.Stats;
import com.l2jserver.gameserver.taskmanager.AttackStanceTaskManager;
import com.l2jserver.gameserver.templates.StatsSet;
import com.l2jserver.gameserver.templates.item.L2Item;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

public class RecipeController {

    protected static final Logger _log = Logger.getLogger(RecipeController.class.getName());

    private Map<Integer, L2RecipeList> _lists;

    private static final Map<Integer, RecipeItemMaker> _activeMakers = new FastMap<Integer, RecipeItemMaker>();

    private static final String RECIPES_FILE = "recipes.xml";

    public static RecipeController getInstance() {
        return SingletonHolder._instance;
    }

    private RecipeController() {
        _lists = new FastMap<Integer, L2RecipeList>();
        try {
            this.loadFromXML();
            _log.info("RecipeController: Loaded " + _lists.size() + " recipes.");
        } catch (Exception e) {
            _log.log(Level.SEVERE, "Failed loading recipe list", e);
        }
    }

    public int getRecipesCount() {
        return _lists.size();
    }

    public L2RecipeList getRecipeList(int listId) {
        return _lists.get(listId);
    }

    public L2RecipeList getRecipeByItemId(int itemId) {
        for (L2RecipeList find : _lists.values()) {
            if (find.getRecipeId() == itemId) return find;
        }
        return null;
    }

    public int[] getAllItemIds() {
        int[] idList = new int[_lists.size()];
        int i = 0;
        for (L2RecipeList rec : _lists.values()) {
            idList[i++] = rec.getRecipeId();
        }
        return idList;
    }

    public synchronized void requestBookOpen(L2PcInstance player, boolean isDwarvenCraft) {
        RecipeItemMaker maker = null;
        if (Config.ALT_GAME_CREATION) maker = _activeMakers.get(player.getObjectId());
        if (maker == null) {
            RecipeBookItemList response = new RecipeBookItemList(isDwarvenCraft, player.getMaxMp());
            response.addRecipes(isDwarvenCraft ? player.getDwarvenRecipeBook() : player.getCommonRecipeBook());
            player.sendPacket(response);
            return;
        }
        player.sendPacket(new SystemMessage(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING));
    }

    public synchronized void requestMakeItemAbort(L2PcInstance player) {
        _activeMakers.remove(player.getObjectId());
    }

    public synchronized void requestManufactureItem(L2PcInstance manufacturer, int recipeListId, L2PcInstance player) {
        L2RecipeList recipeList = getValidRecipeList(player, recipeListId);
        if (recipeList == null) return;
        List<L2RecipeList> dwarfRecipes = Arrays.asList(manufacturer.getDwarvenRecipeBook());
        List<L2RecipeList> commonRecipes = Arrays.asList(manufacturer.getCommonRecipeBook());
        if (!dwarfRecipes.contains(recipeList) && !commonRecipes.contains(recipeList)) {
            Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false recipe id.", Config.DEFAULT_PUNISH);
            return;
        }
        RecipeItemMaker maker;
        if (Config.ALT_GAME_CREATION && (maker = _activeMakers.get(manufacturer.getObjectId())) != null) {
            player.sendMessage("Manufacturer is busy, please try later.");
            return;
        }
        maker = new RecipeItemMaker(manufacturer, recipeList, player);
        if (maker._isValid) {
            if (Config.ALT_GAME_CREATION) {
                _activeMakers.put(manufacturer.getObjectId(), maker);
                ThreadPoolManager.getInstance().scheduleGeneral(maker, 100);
            } else maker.run();
        }
    }

    public synchronized void requestMakeItem(L2PcInstance player, int recipeListId) {
        if (AttackStanceTaskManager.getInstance().getAttackStanceTask(player) || player.isInDuel()) {
            player.sendPacket(new SystemMessage(SystemMessageId.CANT_OPERATE_PRIVATE_STORE_DURING_COMBAT));
            return;
        }
        L2RecipeList recipeList = getValidRecipeList(player, recipeListId);
        if (recipeList == null) return;
        List<L2RecipeList> dwarfRecipes = Arrays.asList(player.getDwarvenRecipeBook());
        List<L2RecipeList> commonRecipes = Arrays.asList(player.getCommonRecipeBook());
        if (!dwarfRecipes.contains(recipeList) && !commonRecipes.contains(recipeList)) {
            Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false recipe id.", Config.DEFAULT_PUNISH);
            return;
        }
        RecipeItemMaker maker;
        if (Config.ALT_GAME_CREATION && ((maker = _activeMakers.get(player.getObjectId())) != null)) {
            SystemMessage sm = new SystemMessage(SystemMessageId.S2_S1);
            sm.addItemName(recipeList.getItemId());
            sm.addString("You are busy creating");
            player.sendPacket(sm);
            return;
        }
        maker = new RecipeItemMaker(player, recipeList, player);
        if (maker._isValid) {
            if (Config.ALT_GAME_CREATION) {
                _activeMakers.put(player.getObjectId(), maker);
                ThreadPoolManager.getInstance().scheduleGeneral(maker, 100);
            } else maker.run();
        }
    }

    private void loadFromXML() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        File file = new File(Config.DATAPACK_ROOT + "/data/" + RECIPES_FILE);
        if (file.exists()) {
            Document doc = factory.newDocumentBuilder().parse(file);
            List<L2RecipeInstance> recipePartList = new FastList<L2RecipeInstance>();
            List<L2RecipeStatInstance> recipeStatUseList = new FastList<L2RecipeStatInstance>();
            List<L2RecipeStatInstance> recipeAltStatChangeList = new FastList<L2RecipeStatInstance>();
            for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ("list".equalsIgnoreCase(n.getNodeName())) {
                    recipesFile: for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                        if ("item".equalsIgnoreCase(d.getNodeName())) {
                            recipePartList.clear();
                            recipeStatUseList.clear();
                            recipeAltStatChangeList.clear();
                            NamedNodeMap attrs = d.getAttributes();
                            Node att;
                            int id = -1;
                            boolean haveRare = false;
                            StatsSet set = new StatsSet();
                            att = attrs.getNamedItem("id");
                            if (att == null) {
                                _log.severe("Missing id for recipe item, skipping");
                                continue;
                            }
                            id = Integer.parseInt(att.getNodeValue());
                            set.set("id", id);
                            att = attrs.getNamedItem("recipeId");
                            if (att == null) {
                                _log.severe("Missing recipeId for recipe item id: " + id + ", skipping");
                                continue;
                            }
                            set.set("recipeId", Integer.parseInt(att.getNodeValue()));
                            att = attrs.getNamedItem("name");
                            if (att == null) {
                                _log.severe("Missing name for recipe item id: " + id + ", skipping");
                                continue;
                            }
                            set.set("recipeName", att.getNodeValue());
                            att = attrs.getNamedItem("craftLevel");
                            if (att == null) {
                                _log.severe("Missing level for recipe item id: " + id + ", skipping");
                                continue;
                            }
                            set.set("craftLevel", Integer.parseInt(att.getNodeValue()));
                            att = attrs.getNamedItem("type");
                            if (att == null) {
                                _log.severe("Missing type for recipe item id: " + id + ", skipping");
                                continue;
                            }
                            set.set("isDwarvenRecipe", att.getNodeValue().equalsIgnoreCase("dwarven"));
                            att = attrs.getNamedItem("successRate");
                            if (att == null) {
                                _log.severe("Missing successRate for recipe item id: " + id + ", skipping");
                                continue;
                            }
                            set.set("successRate", Integer.parseInt(att.getNodeValue()));
                            for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling()) {
                                if ("statUse".equalsIgnoreCase(c.getNodeName())) {
                                    String statName = c.getAttributes().getNamedItem("name").getNodeValue();
                                    int value = Integer.parseInt(c.getAttributes().getNamedItem("value").getNodeValue());
                                    try {
                                        recipeStatUseList.add(new L2RecipeStatInstance(statName, value));
                                    } catch (Exception e) {
                                        _log.severe("Error in StatUse parameter for recipe item id: " + id + ", skipping");
                                        continue recipesFile;
                                    }
                                } else if ("altStatChange".equalsIgnoreCase(c.getNodeName())) {
                                    String statName = c.getAttributes().getNamedItem("name").getNodeValue();
                                    int value = Integer.parseInt(c.getAttributes().getNamedItem("value").getNodeValue());
                                    try {
                                        recipeAltStatChangeList.add(new L2RecipeStatInstance(statName, value));
                                    } catch (Exception e) {
                                        _log.severe("Error in AltStatChange parameter for recipe item id: " + id + ", skipping");
                                        continue recipesFile;
                                    }
                                } else if ("ingredient".equalsIgnoreCase(c.getNodeName())) {
                                    int ingId = Integer.parseInt(c.getAttributes().getNamedItem("id").getNodeValue());
                                    int ingCount = Integer.parseInt(c.getAttributes().getNamedItem("count").getNodeValue());
                                    recipePartList.add(new L2RecipeInstance(ingId, ingCount));
                                } else if ("production".equalsIgnoreCase(c.getNodeName())) {
                                    set.set("itemId", Integer.parseInt(c.getAttributes().getNamedItem("id").getNodeValue()));
                                    set.set("count", Integer.parseInt(c.getAttributes().getNamedItem("count").getNodeValue()));
                                } else if ("productionRare".equalsIgnoreCase(c.getNodeName())) {
                                    set.set("rareItemId", Integer.parseInt(c.getAttributes().getNamedItem("id").getNodeValue()));
                                    set.set("rareCount", Integer.parseInt(c.getAttributes().getNamedItem("count").getNodeValue()));
                                    set.set("rarity", Integer.parseInt(c.getAttributes().getNamedItem("rarity").getNodeValue()));
                                    haveRare = true;
                                }
                            }
                            L2RecipeList recipeList = new L2RecipeList(set, haveRare);
                            for (L2RecipeInstance recipePart : recipePartList) {
                                recipeList.addRecipe(recipePart);
                            }
                            for (L2RecipeStatInstance recipeStatUse : recipeStatUseList) {
                                recipeList.addStatUse(recipeStatUse);
                            }
                            for (L2RecipeStatInstance recipeAltStatChange : recipeAltStatChangeList) {
                                recipeList.addAltStatChange(recipeAltStatChange);
                            }
                            _lists.put(id, recipeList);
                        }
                    }
                }
            }
        } else {
            _log.severe("Recipes file (" + file.getAbsolutePath() + ") doesnt exists.");
        }
    }

    private class RecipeItemMaker implements Runnable {

        protected boolean _isValid;

        protected List<TempItem> _items = null;

        protected final L2RecipeList _recipeList;

        protected final L2PcInstance _player;

        protected final L2PcInstance _target;

        protected final L2Skill _skill;

        protected final int _skillId;

        protected final int _skillLevel;

        protected int _creationPasses = 1;

        protected int _itemGrab;

        protected int _exp = -1;

        protected int _sp = -1;

        protected long _price;

        protected int _totalItems;

        protected int _materialsRefPrice;

        protected int _delay;

        public RecipeItemMaker(L2PcInstance pPlayer, L2RecipeList pRecipeList, L2PcInstance pTarget) {
            _player = pPlayer;
            _target = pTarget;
            _recipeList = pRecipeList;
            _isValid = false;
            _skillId = _recipeList.isDwarvenRecipe() ? L2Skill.SKILL_CREATE_DWARVEN : L2Skill.SKILL_CREATE_COMMON;
            _skillLevel = _player.getSkillLevel(_skillId);
            _skill = _player.getKnownSkill(_skillId);
            _player.isInCraftMode(true);
            if (_player.isAlikeDead()) {
                _player.sendPacket(ActionFailed.STATIC_PACKET);
                abort();
                return;
            }
            if (_target.isAlikeDead()) {
                _target.sendPacket(ActionFailed.STATIC_PACKET);
                abort();
                return;
            }
            if (_target.isProcessingTransaction()) {
                _target.sendPacket(ActionFailed.STATIC_PACKET);
                abort();
                return;
            }
            if (_player.isProcessingTransaction()) {
                _player.sendPacket(ActionFailed.STATIC_PACKET);
                abort();
                return;
            }
            if (_recipeList.getRecipes().length == 0) {
                _player.sendPacket(ActionFailed.STATIC_PACKET);
                abort();
                return;
            }
            if (_recipeList.getLevel() > _skillLevel) {
                _player.sendPacket(ActionFailed.STATIC_PACKET);
                abort();
                return;
            }
            if (_player != _target) {
                for (L2ManufactureItem temp : _player.getCreateList().getList()) {
                    if (temp.getRecipeId() == _recipeList.getId()) {
                        _price = temp.getCost();
                        if (_target.getAdena() < _price) {
                            _target.sendPacket(new SystemMessage(SystemMessageId.YOU_NOT_ENOUGH_ADENA));
                            abort();
                            return;
                        }
                        break;
                    }
                }
            }
            if ((_items = listItems(false)) == null) {
                abort();
                return;
            }
            for (TempItem i : _items) {
                _materialsRefPrice += i.getReferencePrice() * i.getQuantity();
                _totalItems += i.getQuantity();
            }
            if (!calculateStatUse(false, false)) {
                abort();
                return;
            }
            if (Config.ALT_GAME_CREATION) calculateAltStatChange();
            updateMakeInfo(true);
            updateCurMp();
            updateCurLoad();
            _player.isInCraftMode(false);
            _isValid = true;
        }

        public void run() {
            if (!Config.IS_CRAFTING_ENABLED) {
                _target.sendMessage("Item creation is currently disabled.");
                abort();
                return;
            }
            if (_player == null || _target == null) {
                _log.warning("player or target == null (disconnected?), aborting" + _target + _player);
                abort();
                return;
            }
            if (_player.isOnline() == 0 || _target.isOnline() == 0) {
                _log.warning("player or target is not online, aborting " + _target + _player);
                abort();
                return;
            }
            if (Config.ALT_GAME_CREATION && _activeMakers.get(_player.getObjectId()) == null) {
                if (_target != _player) {
                    _target.sendMessage("Manufacture aborted");
                    _player.sendMessage("Manufacture aborted");
                } else {
                    _player.sendMessage("Item creation aborted");
                }
                abort();
                return;
            }
            if (Config.ALT_GAME_CREATION && !_items.isEmpty()) {
                if (!calculateStatUse(true, true)) return;
                updateCurMp();
                grabSomeItems();
                if (!_items.isEmpty()) {
                    _delay = (int) (Config.ALT_GAME_CREATION_SPEED * _player.getMReuseRate(_skill) * GameTimeController.TICKS_PER_SECOND / Config.RATE_CONSUMABLE_COST) * GameTimeController.MILLIS_IN_TICK;
                    MagicSkillUse msk = new MagicSkillUse(_player, _skillId, _skillLevel, _delay, 0);
                    _player.broadcastPacket(msk);
                    _player.sendPacket(new SetupGauge(0, _delay));
                    ThreadPoolManager.getInstance().scheduleGeneral(this, 100 + _delay);
                } else {
                    _player.sendPacket(new SetupGauge(0, _delay));
                    try {
                        Thread.sleep(_delay);
                    } catch (InterruptedException e) {
                    } finally {
                        finishCrafting();
                    }
                }
            } else finishCrafting();
        }

        private void finishCrafting() {
            if (!Config.ALT_GAME_CREATION) calculateStatUse(false, true);
            if ((_target != _player) && _price > 0) {
                L2ItemInstance adenatransfer = _target.transferItem("PayManufacture", _target.getInventory().getAdenaInstance().getObjectId(), _price, _player.getInventory(), _player);
                if (adenatransfer == null) {
                    _target.sendPacket(new SystemMessage(SystemMessageId.YOU_NOT_ENOUGH_ADENA));
                    abort();
                    return;
                }
            }
            if ((_items = listItems(true)) == null) {
            } else if (Rnd.get(100) < _recipeList.getSuccessRate()) {
                rewardPlayer();
                updateMakeInfo(true);
            } else {
                if (_target != _player) {
                    SystemMessage msg = new SystemMessage(SystemMessageId.CREATION_OF_S2_FOR_C1_AT_S3_ADENA_FAILED);
                    msg.addString(_target.getName());
                    msg.addItemName(_recipeList.getItemId());
                    msg.addItemNumber(_price);
                    _player.sendPacket(msg);
                    msg = new SystemMessage(SystemMessageId.C1_FAILED_TO_CREATE_S2_FOR_S3_ADENA);
                    msg.addString(_player.getName());
                    msg.addItemName(_recipeList.getItemId());
                    msg.addItemNumber(_price);
                    _target.sendPacket(msg);
                } else _target.sendPacket(new SystemMessage(SystemMessageId.ITEM_MIXING_FAILED));
                updateMakeInfo(false);
            }
            updateCurMp();
            updateCurLoad();
            _activeMakers.remove(_player.getObjectId());
            _player.isInCraftMode(false);
            _target.sendPacket(new ItemList(_target, false));
        }

        private void updateMakeInfo(boolean success) {
            if (_target == _player) _target.sendPacket(new RecipeItemMakeInfo(_recipeList.getId(), _target, success)); else _target.sendPacket(new RecipeShopItemInfo(_player.getObjectId(), _recipeList.getId()));
        }

        private void updateCurLoad() {
            StatusUpdate su = new StatusUpdate(_target);
            su.addAttribute(StatusUpdate.CUR_LOAD, _target.getCurrentLoad());
            _target.sendPacket(su);
        }

        private void updateCurMp() {
            StatusUpdate su = new StatusUpdate(_target);
            su.addAttribute(StatusUpdate.CUR_MP, (int) _target.getCurrentMp());
            _target.sendPacket(su);
        }

        private void grabSomeItems() {
            int grabItems = _itemGrab;
            while (grabItems > 0 && !_items.isEmpty()) {
                TempItem item = _items.get(0);
                int count = item.getQuantity();
                if (count >= grabItems) count = grabItems;
                item.setQuantity(item.getQuantity() - count);
                if (item.getQuantity() <= 0) _items.remove(0); else _items.set(0, item);
                grabItems -= count;
                if (_target == _player) {
                    SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2_EQUIPPED);
                    sm.addItemNumber(count);
                    sm.addItemName(item.getItemId());
                    _player.sendPacket(sm);
                } else _target.sendMessage("Manufacturer " + _player.getName() + " used " + count + " " + item.getItemName());
            }
        }

        private void calculateAltStatChange() {
            _itemGrab = _skillLevel;
            for (L2RecipeStatInstance altStatChange : _recipeList.getAltStatChange()) {
                if (altStatChange.getType() == L2RecipeStatInstance.StatType.XP) {
                    _exp = altStatChange.getValue();
                } else if (altStatChange.getType() == L2RecipeStatInstance.StatType.SP) {
                    _sp = altStatChange.getValue();
                } else if (altStatChange.getType() == L2RecipeStatInstance.StatType.GIM) {
                    _itemGrab *= altStatChange.getValue();
                }
            }
            _creationPasses = (_totalItems / _itemGrab) + ((_totalItems % _itemGrab) != 0 ? 1 : 0);
            if (_creationPasses < 1) _creationPasses = 1;
        }

        private boolean calculateStatUse(boolean isWait, boolean isReduce) {
            boolean ret = true;
            for (L2RecipeStatInstance statUse : _recipeList.getStatUse()) {
                double modifiedValue = statUse.getValue() / _creationPasses;
                if (statUse.getType() == L2RecipeStatInstance.StatType.HP) {
                    if (_player.getCurrentHp() <= modifiedValue) {
                        if (Config.ALT_GAME_CREATION && isWait) {
                            _player.sendPacket(new SetupGauge(0, _delay));
                            ThreadPoolManager.getInstance().scheduleGeneral(this, 100 + _delay);
                        } else {
                            _target.sendPacket(new SystemMessage(SystemMessageId.NOT_ENOUGH_HP));
                            abort();
                        }
                        ret = false;
                    } else if (isReduce) _player.reduceCurrentHp(modifiedValue, _player, _skill);
                } else if (statUse.getType() == L2RecipeStatInstance.StatType.MP) {
                    if (_player.getCurrentMp() < modifiedValue) {
                        if (Config.ALT_GAME_CREATION && isWait) {
                            _player.sendPacket(new SetupGauge(0, _delay));
                            ThreadPoolManager.getInstance().scheduleGeneral(this, 100 + _delay);
                        } else {
                            _target.sendPacket(new SystemMessage(SystemMessageId.NOT_ENOUGH_MP));
                            abort();
                        }
                        ret = false;
                    } else if (isReduce) _player.reduceCurrentMp(modifiedValue);
                } else {
                    _target.sendMessage("Recipe error!!!, please tell this to your GM.");
                    ret = false;
                    abort();
                }
            }
            return ret;
        }

        private List<TempItem> listItems(boolean remove) {
            L2RecipeInstance[] recipes = _recipeList.getRecipes();
            Inventory inv = _target.getInventory();
            List<TempItem> materials = new FastList<TempItem>();
            SystemMessage sm;
            for (L2RecipeInstance recipe : recipes) {
                int quantity = _recipeList.isConsumable() ? (int) (recipe.getQuantity() * Config.RATE_CONSUMABLE_COST) : recipe.getQuantity();
                if (quantity > 0) {
                    L2ItemInstance item = inv.getItemByItemId(recipe.getItemId());
                    long itemQuantityAmount = item == null ? 0 : item.getCount();
                    if (itemQuantityAmount < quantity) {
                        sm = new SystemMessage(SystemMessageId.MISSING_S2_S1_TO_CREATE);
                        sm.addItemName(recipe.getItemId());
                        sm.addItemNumber(quantity - itemQuantityAmount);
                        _target.sendPacket(sm);
                        abort();
                        return null;
                    }
                    TempItem temp = new TempItem(item, quantity);
                    materials.add(temp);
                }
            }
            if (remove) {
                for (TempItem tmp : materials) {
                    inv.destroyItemByItemId("Manufacture", tmp.getItemId(), tmp.getQuantity(), _target, _player);
                    sm = new SystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
                    sm.addItemName(tmp.getItemId());
                    sm.addItemNumber(tmp.getQuantity());
                    _target.sendPacket(sm);
                }
            }
            return materials;
        }

        private void abort() {
            updateMakeInfo(false);
            _player.isInCraftMode(false);
            _activeMakers.remove(_player.getObjectId());
        }

        /**
		 * FIXME: This class should be in some other file, but I don't know where
		 *
		 * Class explanation:
		 * For item counting or checking purposes. When you don't want to modify inventory
		 * class contains itemId, quantity, ownerId, referencePrice, but not objectId
		 */
        private class TempItem {

            private int _itemId;

            private int _quantity;

            private int _ownerId;

            private int _referencePrice;

            private String _itemName;

            /**
			 * @param item
			 * @param quantity of that item
			 */
            public TempItem(L2ItemInstance item, int quantity) {
                super();
                _itemId = item.getItemId();
                _quantity = quantity;
                _ownerId = item.getOwnerId();
                _itemName = item.getItem().getName();
                _referencePrice = item.getReferencePrice();
            }

            /**
			 * @return Returns the quantity.
			 */
            public int getQuantity() {
                return _quantity;
            }

            /**
			 * @param quantity The quantity to set.
			 */
            public void setQuantity(int quantity) {
                _quantity = quantity;
            }

            public int getReferencePrice() {
                return _referencePrice;
            }

            /**
			 * @return Returns the itemId.
			 */
            public int getItemId() {
                return _itemId;
            }

            /**
			 * @return Returns the ownerId.
			 */
            @SuppressWarnings("unused")
            public int getOwnerId() {
                return _ownerId;
            }

            /**
			 * @return Returns the itemName.
			 */
            public String getItemName() {
                return _itemName;
            }
        }

        private void rewardPlayer() {
            int rareProdId = _recipeList.getRareItemId();
            int itemId = _recipeList.getItemId();
            int itemCount = _recipeList.getCount();
            L2Item template = ItemTable.getInstance().getTemplate(itemId);
            if (rareProdId != -1 && (rareProdId == itemId || Config.CRAFT_MASTERWORK)) {
                if (Rnd.get(100) < _recipeList.getRarity()) {
                    itemId = rareProdId;
                    itemCount = _recipeList.getRareCount();
                }
            }
            _target.getInventory().addItem("Manufacture", itemId, itemCount, _target, _player);
            SystemMessage sm = null;
            if (_target != _player) {
                if (itemCount == 1) {
                    sm = new SystemMessage(SystemMessageId.S2_CREATED_FOR_C1_FOR_S3_ADENA);
                    sm.addString(_target.getName());
                    sm.addItemName(itemId);
                    sm.addItemNumber(_price);
                    _player.sendPacket(sm);
                    sm = new SystemMessage(SystemMessageId.C1_CREATED_S2_FOR_S3_ADENA);
                    sm.addString(_player.getName());
                    sm.addItemName(itemId);
                    sm.addItemNumber(_price);
                    _target.sendPacket(sm);
                } else {
                    sm = new SystemMessage(SystemMessageId.S2_S3_S_CREATED_FOR_C1_FOR_S4_ADENA);
                    sm.addString(_target.getName());
                    sm.addNumber(itemCount);
                    sm.addItemName(itemId);
                    sm.addItemNumber(_price);
                    _player.sendPacket(sm);
                    sm = new SystemMessage(SystemMessageId.C1_CREATED_S2_S3_S_FOR_S4_ADENA);
                    sm.addString(_player.getName());
                    sm.addNumber(itemCount);
                    sm.addItemName(itemId);
                    sm.addItemNumber(_price);
                    _target.sendPacket(sm);
                }
            }
            if (itemCount > 1) {
                sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
                sm.addItemName(itemId);
                sm.addItemNumber(itemCount);
                _target.sendPacket(sm);
            } else {
                sm = new SystemMessage(SystemMessageId.EARNED_ITEM);
                sm.addItemName(itemId);
                _target.sendPacket(sm);
            }
            if (Config.ALT_GAME_CREATION) {
                int recipeLevel = _recipeList.getLevel();
                if (_exp < 0) {
                    _exp = template.getReferencePrice() * itemCount;
                    _exp /= recipeLevel;
                }
                if (_sp < 0) _sp = _exp / 10;
                if (itemId == rareProdId) {
                    _exp *= Config.ALT_GAME_CREATION_RARE_XPSP_RATE;
                    _sp *= Config.ALT_GAME_CREATION_RARE_XPSP_RATE;
                }
                if (_exp < 0) _exp = 0;
                if (_sp < 0) _sp = 0;
                for (int i = _skillLevel; i > recipeLevel; i--) {
                    _exp /= 4;
                    _sp /= 4;
                }
                _player.addExpAndSp((int) _player.calcStat(Stats.EXPSP_RATE, _exp * Config.ALT_GAME_CREATION_XP_RATE * Config.ALT_GAME_CREATION_SPEED, null, null), (int) _player.calcStat(Stats.EXPSP_RATE, _sp * Config.ALT_GAME_CREATION_SP_RATE * Config.ALT_GAME_CREATION_SPEED, null, null));
            }
            updateMakeInfo(true);
        }
    }

    private L2RecipeList getValidRecipeList(L2PcInstance player, int id) {
        L2RecipeList recipeList = getRecipeList(id);
        if ((recipeList == null) || (recipeList.getRecipes().length == 0)) {
            player.sendMessage("No recipe for: " + id);
            player.isInCraftMode(false);
            return null;
        }
        return recipeList;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final RecipeController _instance = new RecipeController();
    }
}
