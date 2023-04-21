package net.slashie.expedition.ui.oryx;

import java.awt.Color;
import java.awt.Image;
import net.slashie.expedition.domain.Armor;
import net.slashie.expedition.domain.ExpeditionItem;
import net.slashie.expedition.domain.ExpeditionUnit;
import net.slashie.expedition.domain.Vehicle;
import net.slashie.expedition.domain.Weapon;
import net.slashie.libjcsi.CharKey;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.oryxUI.GFXAppearance;
import net.slashie.serf.ui.oryxUI.SwingSystemInterface;
import net.slashie.utils.swing.CustomGFXMenuItem;

@SuppressWarnings("serial")
public class InventoryCustomGFXMenuItem implements CustomGFXMenuItem {

    private Equipment item;

    public InventoryCustomGFXMenuItem(Equipment item) {
        this.item = item;
    }

    public Equipment getEquipment() {
        return item;
    }

    public String getGroupClassifier() {
        return ((ExpeditionItem) item.getItem()).getGroupClassifier();
    }

    public Image getMenuImage() {
        return null;
    }

    public String getMenuDetail() {
        return null;
    }

    public String getMenuDescription() {
        return null;
    }

    @Override
    public boolean showTooltip() {
        return true;
    }

    @Override
    public void drawTooltip(SwingSystemInterface si, int x, int y, int index) {
        ExpeditionItem eitem = (ExpeditionItem) item.getItem();
        Image unitImage = ((GFXAppearance) eitem.getDialogAppearance()).getImage();
        String itemDescription = item.getItem().getDescription();
        int quantity = item.getQuantity();
        if (eitem instanceof ExpeditionUnit) {
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_HIGHLIGHT_COLOR);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).fillRect(x + 1, y + 1, 350 - 2, 60 - 2);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_BORDER_COLOR);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).drawRect(x + 1, y + 1, 350 - 2, 60 - 2);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).drawRect(x + 2, y + 2, 350 - 4, 60 - 4);
        } else {
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_HIGHLIGHT_COLOR);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).fillRect(x + 1, y + 1, 200 - 2, 60 - 2);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_BORDER_COLOR);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).drawRect(x + 1, y + 1, 200 - 2, 60 - 2);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).drawRect(x + 2, y + 2, 200 - 4, 60 - 4);
        }
        si.drawImage(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 12, y + 12, unitImage);
        if (eitem instanceof ExpeditionUnit) {
            ExpeditionUnit unit = (ExpeditionUnit) eitem;
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 15, unit.getLongDescription(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 28, "ATK: " + unit.getAttack().getString(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 42, "DEF: " + unit.getDefense().getString(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 198, y + 42, "Weight: " + unit.getWeight(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 5, y + 55, quantity + " " + itemDescription, Color.WHITE);
        } else if (eitem instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) eitem;
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 15, vehicle.getName(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 28, "Int: " + vehicle.getResistance() + "/" + vehicle.getMaxResistance(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 42, "Cap: " + vehicle.getCarryCapacity(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 55, itemDescription, Color.WHITE);
        } else {
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 5, y + 55, quantity + " " + itemDescription, Color.WHITE);
        }
    }

    @Override
    public void drawMenuItem(SwingSystemInterface si, int x, int y, int index, boolean highlight) {
        ExpeditionItem eitem = (ExpeditionItem) item.getItem();
        Image unitImage = ((GFXAppearance) eitem.getDialogAppearance()).getImage();
        String itemDescription = item.getItem().getDescription();
        int quantity = item.getQuantity();
        if (highlight) {
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_HIGHLIGHT_COLOR);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).fillRect(x + 1, y + 1, 200 - 2, 60 - 2);
        } else {
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_COLOR);
            si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).fillRect(x + 1, y + 1, 200 - 2, 60 - 2);
        }
        si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).setColor(ExpeditionOryxUI.ITEM_BOX_BORDER_COLOR);
        si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).drawRect(x + 1, y + 1, 200 - 2, 60 - 2);
        si.getDrawingGraphics(ExpeditionOryxUI.UI_WIDGETS_LAYER).drawRect(x + 2, y + 2, 200 - 4, 60 - 4);
        si.drawImage(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 12, y + 12, unitImage);
        if (eitem instanceof ExpeditionUnit) {
            Weapon weapon = ((ExpeditionUnit) eitem).getWeapon();
            String weaponDescription = weapon != null ? weapon.getFullDescription() : "Unarmed";
            Armor armor = ((ExpeditionUnit) eitem).getArmor();
            String armorDescription = armor != null ? armor.getFullDescription() : "Clothes";
            String status = ((ExpeditionUnit) eitem).getStatusModifiersString();
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 15, weaponDescription, Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 28, armorDescription, Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 42, status, Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 5, y + 55, quantity + " " + itemDescription, Color.WHITE);
        } else if (eitem instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) eitem;
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 15, vehicle.getName(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 28, "Int: " + vehicle.getResistance() + "/" + vehicle.getMaxResistance(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 42, "Cap: " + vehicle.getCarryCapacity(), Color.WHITE);
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 48, y + 55, itemDescription, Color.WHITE);
        } else {
            si.printAtPixel(ExpeditionOryxUI.UI_WIDGETS_LAYER, x + 5, y + 55, quantity + " " + itemDescription, Color.WHITE);
        }
    }
}
