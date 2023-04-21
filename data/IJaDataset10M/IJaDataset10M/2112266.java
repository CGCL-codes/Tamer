package code.items.chest;

import java.util.ArrayList;
import java.util.List;
import code.items.Item;
import code.items.armor.Armor;
import code.items.armor.BasicNinjaArmor;
import code.items.armor.BasicPirateArmor;

public class ArmorChest extends Chest {

    private List<Armor> armors;

    public ArmorChest(int minLevel, int maxLevel, String background) {
        super(minLevel, maxLevel, background);
        this.name = "Armor Chest";
        this.armors = generateArmor();
    }

    @Override
    public Item getItem() {
        while (armors.size() != 0) {
            int index = (int) (Math.random() * armors.size());
            Armor armor = armors.get(index);
            if (armor.getLevel() >= this.minLevel && armor.getLevel() <= this.maxLevel && this.background.equalsIgnoreCase(armor.getBackground())) return armor; else armors.remove(index);
        }
        return null;
    }

    private List<Armor> generateArmor() {
        List<Armor> list = new ArrayList<Armor>();
        list.add(new BasicNinjaArmor());
        list.add(new BasicNinjaArmor());
        list.add(new BasicNinjaArmor());
        list.add(new BasicPirateArmor());
        list.add(new BasicPirateArmor());
        list.add(new BasicPirateArmor());
        return list;
    }
}
