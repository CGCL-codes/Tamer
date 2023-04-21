package neon.tools.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import neon.entities.items.Armor;
import neon.objects.property.Slot;
import neon.tools.Mod;

public class RItem extends PResource {

    public int cost;

    public String name;

    public float weight;

    public Type type;

    public RSpell spell;

    public boolean top;

    public String svg;

    private static XMLOutputter outputter = new XMLOutputter();

    private static SAXBuilder builder = new SAXBuilder();

    public RItem(Element e, Mod mod) {
        super(e.getAttributeValue("id"), mod);
        type = Type.valueOf(e.getName());
        if (e.getAttribute("cost") != null) {
            cost = Integer.parseInt(e.getAttributeValue("cost"));
        }
        name = e.getAttributeValue("name");
        if (e.getAttribute("weight") != null) {
            weight = Float.parseFloat(e.getAttributeValue("weight"));
        }
        top = e.getAttribute("z") != null;
        if (e.getChild("svg") != null) {
            svg = outputter.outputString((Element) e.getChild("svg").getChildren().get(0));
        } else {
            color = e.getAttributeValue("color");
            text = e.getAttributeValue("char");
        }
    }

    protected RItem(String id, Mod mod) {
        super(id, mod);
    }

    public RItem(String id, Mod mod, Type type) {
        super(id, mod);
        this.type = type;
    }

    public Element toElement() {
        Element item = new Element(type.toString());
        item.setAttribute("id", id);
        if (svg != null) {
            try {
                Element graphics = new Element("svg");
                ByteArrayInputStream stream = new ByteArrayInputStream(svg.getBytes("UTF-8"));
                Element shape = (Element) builder.build(stream).getRootElement().detach();
                graphics.addContent(shape);
                item.addContent(graphics);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            item.setAttribute("char", text);
            item.setAttribute("color", color);
        }
        if (cost > 0) {
            item.setAttribute("cost", Integer.toString(cost));
        }
        if (weight > 0) {
            item.setAttribute("weight", Float.toString(weight));
        }
        if (name != null && !name.isEmpty()) {
            item.setAttribute("name", name);
        }
        if (spell != null) {
            item.setAttribute("spell", spell.id);
        }
        return item;
    }

    public static class Door extends RItem {

        public Door(Element e, Mod mod) {
            super(e, mod);
        }

        public Door(String id, Mod mod, Type type) {
            super(id, mod, type);
        }

        public Element toElement() {
            Element door = super.toElement();
            return door;
        }
    }

    public static class Weapon extends RItem {

        public String dmg;

        public neon.entities.items.Weapon.Type kind;

        public Weapon(Element e, Mod mod) {
            super(e, mod);
            dmg = e.getAttributeValue("dmg");
            kind = neon.entities.items.Weapon.Type.valueOf(e.getAttributeValue("type").toUpperCase());
        }

        public Weapon(String id, Mod mod, Type type) {
            super(id, mod, type);
        }

        public Element toElement() {
            Element weapon = super.toElement();
            weapon.setAttribute("dmg", dmg);
            weapon.setAttribute("type", kind.toString());
            return weapon;
        }
    }

    public static class Clothing extends RItem {

        public Slot slot;

        public int rating;

        public Armor.Type kind;

        public Clothing(Element e, Mod mod) {
            super(e, mod);
            if (e.getChild("stats").getAttribute("ar") != null) {
                rating = Integer.parseInt(e.getChild("stats").getAttributeValue("ar"));
            }
            slot = Slot.valueOf(e.getChild("stats").getAttributeValue("slot").toUpperCase());
            if (e.getChild("stats").getAttribute("class") != null) {
                kind = Armor.Type.valueOf(e.getChild("stats").getAttributeValue("class").toUpperCase());
            }
        }

        public Clothing(String id, Mod mod, Type type) {
            super(id, mod, type);
        }

        public Element toElement() {
            Element clothing = super.toElement();
            Element stats = new Element("stats");
            stats.setAttribute("slot", slot.toString());
            if (type == Type.armor) {
                stats.setAttribute("class", kind.toString());
                stats.setAttribute("ar", Integer.toString(rating));
            }
            clothing.addContent(stats);
            return clothing;
        }
    }

    public static class Book extends RItem {

        public String content;

        public Book(Element e, Mod mod) {
            super(e, mod);
            content = e.getText();
        }

        public Book(String id, Mod mod, Type type) {
            super(id, mod, type);
        }

        public Element toElement() {
            Element book = super.toElement();
            book.setText(content);
            return book;
        }
    }

    public static class Container extends RItem {

        public Container(Element e, Mod mod) {
            super(e, mod);
        }

        public Container(String id, Mod mod, Type type) {
            super(id, mod, type);
        }
    }

    public enum Type {

        aid, armor, book, clothing, coin, container, door, food, item, light, potion, scroll, weapon
    }
}
