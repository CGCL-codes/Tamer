package xhack.menu;

import xhack.object.Creature;
import xhack.object.Player;
import xhack.game.Constants;

public class InfoMenu extends HackMenu {

    Creature creature;

    public InfoMenu(Player creature) {
        super(12, 5);
        this.creature = creature;
        int row = 0;
        items[row][0] = new MenuItem("Name: ");
        items[row][1] = new MenuItem("" + creature.getName(), HackMenu.ACTIVE_COLOUR);
        row++;
        items[row][0] = new MenuItem("Race: ");
        items[row][1] = new MenuItem("" + creature.getRaceName(), HackMenu.ACTIVE_COLOUR);
        row++;
        items[row][0] = new MenuItem("Class: ");
        items[row][1] = new MenuItem("" + creature.getClassName(), HackMenu.ACTIVE_COLOUR);
        row++;
        row++;
        items[row][0] = new MenuItem("Level: ");
        items[row][1] = new MenuItem("" + creature.getLevel(), HackMenu.ACTIVE_COLOUR);
        row++;
        items[row][0] = new MenuItem("XP: ");
        items[row][1] = new MenuItem("" + creature.getXp() + " / " + creature.nextXP(), HackMenu.ACTIVE_COLOUR);
        row += 2;
        for (int i = row; i < row + 3; i++) {
            items[i][0] = new MenuItem(Creature.ABIL_NAMES[i - row].substring(0, 3).toUpperCase());
            items[i][2] = new MenuItem(Creature.ABIL_NAMES[i + 3 - row].substring(0, 3).toUpperCase());
        }
        for (int i = row; i < row + 3; i++) {
            items[i][1] = new MenuItem("" + creature.getAttribute(i - row), HackMenu.HIGHLIGHT_COLOUR);
            items[i][3] = new MenuItem("" + creature.getAttribute(i + 3 - row), HackMenu.HIGHLIGHT_COLOUR);
        }
        row += 3;
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) {
            items[i][j].isSelectable(false);
        }
        items[0][0].isSelected(true);
    }
}
