package xhack.object;

import xhack.game.*;
import xhack.util.EqualsUtil;
import xhack.xml.*;
import xhack.d20.StatBlock;
import xhack.d20.Attack;
import java.util.Arrays;

public class Race extends DataObject {

    protected DiceRange attributes[] = new DiceRange[Creature.NUM_ABILS];

    protected DiceRange damage = new DiceRange(0, 0, 0);

    protected float xpMod = 1.0f;

    protected String description = "";

    protected int size = 0;

    protected String type = "";

    protected String subtypes = "";

    protected Attack attack = null;

    public Race(Game game) {
        super(game);
        init();
    }

    public Race(Race r) {
        super((DataObject) r);
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = r.attributes[i];
        }
    }

    public Race(XMLNode n, Game game) throws XMLException {
        super(game);
        try {
            this.game = game;
            createFromXML(n, game);
        } catch (XMLException e) {
            throw new XMLException(e.getError() + " in <race>");
        }
    }

    private void init() {
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = new DiceRange(3, 6, 0);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Race)) return false;
        Race t = (Race) obj;
        return EqualsUtil.areEqual(damage, t.damage) && EqualsUtil.areEqual(xpMod, t.xpMod) && EqualsUtil.areEqual(description, t.description) && EqualsUtil.areEqual(size, t.size) && EqualsUtil.areEqual(type, t.type) && EqualsUtil.areEqual(subtypes, t.subtypes) && EqualsUtil.areEqual(attack, t.attack) && Arrays.equals(attributes, t.attributes) && super.equals(t);
    }

    public DataObject copy() {
        return this;
    }

    public void save(String prefix) {
    }

    public Attack getAttack() {
        return attack;
    }

    public int getAtrribute(int i) {
        return attributes[i].random(game.rand);
    }

    public String getName() {
        return name;
    }

    public float getXPMod() {
        return xpMod;
    }

    public DiceRange getDamage() {
        return damage;
    }

    public String getDescription() {
        return description;
    }

    public int getSize() {
        return size;
    }

    public void createFromXML(XMLNode root, Game game) throws XMLException {
        this.game = game;
        name = root.getAttribute("id").domNode.getNodeValue();
        XMLNode attr = root.getChild("attributes");
        for (int i = 0; i < Creature.NUM_ABILS; i++) {
            attributes[i] = new DiceRange(attr.getChild(Creature.ABIL_ABBREV[i]).getText());
        }
        damage = new DiceRange(root.getChild("damage").getText());
        xpMod = root.getChild("xp-modifier").getFloatValue();
        description = root.getChild("description").getText();
        XMLNode n = root.getChild("size_and_type");
        size = StatBlock.getSize(n.getAttributeText("size"));
        type = n.getAttributeText("type");
        subtypes = n.getAttributeText("subtypes");
        attack = new Attack(root.getChild("attack"));
    }

    public void createXML(XMLNode root) throws XMLException {
    }
}
