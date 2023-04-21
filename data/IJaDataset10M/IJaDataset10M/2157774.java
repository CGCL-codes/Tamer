package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExAttributeEnchantResult;
import com.l2jserver.gameserver.network.serverpackets.ExBrExtraUserInfo;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;
import com.l2jserver.gameserver.templates.item.L2Item;
import com.l2jserver.gameserver.templates.item.L2WeaponType;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

public class RequestExEnchantItemAttribute extends L2GameClientPacket {

    private static final String D0_35_REQUEST_EX_ENCHANT_ITEM_ATTRIBUTE = "[C] D0:35 RequestExEnchantItemAttribute";

    private int _objectId;

    @Override
    protected void readImpl() {
        _objectId = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (_objectId == 0xFFFFFFFF) {
            player.setActiveEnchantAttrItem(null);
            player.sendPacket(new SystemMessage(SystemMessageId.ELEMENTAL_ENHANCE_CANCELED));
            return;
        }
        if (player.isOnline() == 0) {
            player.setActiveEnchantAttrItem(null);
            return;
        }
        if (player.getPrivateStoreType() != 0) {
            player.sendPacket(new SystemMessage(SystemMessageId.CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_PRIVATE_STORE_OR_WORKSHOP));
            return;
        }
        if (player.getActiveRequester() != null) {
            player.cancelActiveTrade();
            player.sendMessage("Enchanting items is not allowed during a trade.");
            return;
        }
        L2ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
        L2ItemInstance stone = player.getActiveEnchantAttrItem();
        if (item == null || stone == null) {
            player.setActiveEnchantAttrItem(null);
            return;
        }
        if ((item.getLocation() != L2ItemInstance.ItemLocation.INVENTORY) && (item.getLocation() != L2ItemInstance.ItemLocation.PAPERDOLL)) return;
        if (item.isWear()) {
            Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to enchant a weared Item", Config.DEFAULT_PUNISH);
            return;
        }
        if (item.getItem().getItemType() == L2WeaponType.ROD || item.isShadowItem() || item.isPvp() || item.isHeroItem() || item.isTimeLimitedItem() || (item.getItemId() >= 7816 && item.getItemId() <= 7831) || (item.getItem().getItemType() == L2WeaponType.NONE) || item.getItem().getItemGradeSPlus() != L2Item.CRYSTAL_S || item.getItem().getBodyPart() == L2Item.SLOT_BACK || item.getItem().getBodyPart() == L2Item.SLOT_R_BRACELET || item.getItem().getBodyPart() == L2Item.SLOT_UNDERWEAR || item.getItem().getBodyPart() == L2Item.SLOT_BELT || item.getItem().getBodyPart() == L2Item.SLOT_NECK || item.getItem().getBodyPart() == L2Item.SLOT_R_EAR || item.getItem().getBodyPart() == L2Item.SLOT_R_FINGER || item.getItem().getElementals() != null) {
            player.sendPacket(new SystemMessage(SystemMessageId.ELEMENTAL_ENHANCE_REQUIREMENT_NOT_SUFFICIENT));
            player.setActiveEnchantAttrItem(null);
            return;
        }
        switch(item.getLocation()) {
            case INVENTORY:
            case PAPERDOLL:
                {
                    if (item.getOwnerId() != player.getObjectId()) {
                        player.setActiveEnchantAttrItem(null);
                        return;
                    }
                    break;
                }
            default:
                {
                    player.setActiveEnchantAttrItem(null);
                    Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to use enchant Exploit!", Config.DEFAULT_PUNISH);
                    return;
                }
        }
        int stoneId = stone.getItemId();
        Elementals oldElement = item.getElementals();
        int elementValue = oldElement == null ? 0 : oldElement.getValue();
        int limit = getLimit(stoneId, item);
        int powerToAdd = getPowerToAdd(stoneId, elementValue, item);
        byte elementToAdd = getElementFromItemId(stoneId);
        if (item.isArmor()) elementToAdd = Elementals.getOppositeElement(elementToAdd);
        int newPower = elementValue + powerToAdd;
        if (newPower > limit) {
            newPower = limit;
            powerToAdd = limit - elementValue;
        }
        if (oldElement != null && oldElement.getElement() != elementToAdd && oldElement.getElement() != -2) {
            player.sendPacket(new SystemMessage(SystemMessageId.ANOTHER_ELEMENTAL_POWER_ALREADY_ADDED));
            player.setActiveEnchantAttrItem(null);
            return;
        }
        if (powerToAdd <= 0) {
            player.sendPacket(new SystemMessage(SystemMessageId.ELEMENTAL_ENHANCE_CANCELED));
            player.setActiveEnchantAttrItem(null);
            return;
        }
        if (!player.destroyItem("AttrEnchant", stone, 1, player, true)) {
            player.sendPacket(new SystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
            Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to attribute enchant with a stone he doesn't have", Config.DEFAULT_PUNISH);
            player.setActiveEnchantAttrItem(null);
            return;
        }
        if (Rnd.get(100) <= Elementals.ENCHANT_CHANCE) {
            SystemMessage sm;
            if (item.getEnchantLevel() == 0) {
                sm = new SystemMessage(SystemMessageId.ELEMENTAL_POWER_S2_SUCCESSFULLY_ADDED_TO_S1);
                sm.addItemName(item);
                sm.addString(Elementals.getElementName(elementToAdd));
            } else {
                sm = new SystemMessage(SystemMessageId.ELEMENTAL_POWER_S3_SUCCESSFULLY_ADDED_TO_S1_S2);
                sm.addNumber(item.getEnchantLevel());
                sm.addItemName(item);
                sm.addString(Elementals.getElementName(elementToAdd));
            }
            player.sendPacket(sm);
            item.setElementAttr(elementToAdd, newPower);
            if (item.isEquipped()) item.updateElementAttrBonus(player);
            InventoryUpdate iu = new InventoryUpdate();
            iu.addModifiedItem(item);
            player.sendPacket(iu);
        } else player.sendPacket(new SystemMessage(SystemMessageId.FAILED_ADDING_ELEMENTAL_POWER));
        player.sendPacket(new ExAttributeEnchantResult(powerToAdd));
        player.sendPacket(new UserInfo(player));
        player.sendPacket(new ExBrExtraUserInfo(player));
        player.setActiveEnchantAttrItem(null);
    }

    public byte getElementFromItemId(int itemId) {
        byte element = 0;
        for (int id : Elementals.STONES) {
            if (id == itemId) return element;
            element++;
        }
        element = 0;
        for (int id : Elementals.CRYSTALS) {
            if (id == itemId) return element;
            element++;
        }
        element = 0;
        for (int id : Elementals.JEWELS) {
            if (id == itemId) return element;
            element++;
        }
        element = 0;
        for (int id : Elementals.ENERGIES) {
            if (id == itemId) return element;
            element++;
        }
        element = 0;
        for (int id : Elementals.ROUGHORES) {
            if (id == itemId) return element;
            element++;
        }
        return -1;
    }

    public int getLimit(int itemId, L2ItemInstance item) {
        for (int id : Elementals.STONES) {
            if (id == itemId) {
                if (item.isWeapon()) return Elementals.WEAPON_VALUES[3];
                return Elementals.ARMOR_VALUES[3];
            }
        }
        for (int id : Elementals.CRYSTALS) {
            if (id == itemId) {
                if (item.isWeapon()) return Elementals.WEAPON_VALUES[6];
                return Elementals.ARMOR_VALUES[6];
            }
        }
        for (int id : Elementals.JEWELS) {
            if (id == itemId) {
                if (item.isWeapon()) return Elementals.WEAPON_VALUES[9];
                return Elementals.ARMOR_VALUES[9];
            }
        }
        for (int id : Elementals.ENERGIES) {
            if (id == itemId) {
                if (item.isWeapon()) return Elementals.WEAPON_VALUES[12];
                return Elementals.ARMOR_VALUES[12];
            }
        }
        for (int id : Elementals.ROUGHORES) {
            if (id == itemId) {
                if (item.isWeapon()) return Elementals.WEAPON_VALUES[3];
                return Elementals.ARMOR_VALUES[3];
            }
        }
        return 0;
    }

    public int getPowerToAdd(int stoneId, int oldValue, L2ItemInstance item) {
        boolean found = false;
        for (int id : Elementals.STONES) {
            if (id == stoneId) {
                found = true;
                break;
            }
        }
        if (!found) {
            for (int id : Elementals.CRYSTALS) {
                if (id == stoneId) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                for (int id : Elementals.JEWELS) {
                    if (id == stoneId) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    for (int id : Elementals.ENERGIES) {
                        if (id == stoneId) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        for (int id : Elementals.ROUGHORES) {
                            if (id == stoneId) {
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (found) {
            if (item.isWeapon()) {
                if (oldValue == 0) return Elementals.FIRST_WEAPON_BONUS; else return Elementals.NEXT_WEAPON_BONUS;
            } else if (item.isArmor()) return Elementals.ARMOR_BONUS;
        }
        return 0;
    }

    @Override
    public String getType() {
        return D0_35_REQUEST_EX_ENCHANT_ITEM_ATTRIBUTE;
    }
}
