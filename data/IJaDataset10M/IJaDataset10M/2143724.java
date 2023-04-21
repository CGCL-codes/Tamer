package neon.objects.resources;

import java.util.HashMap;
import org.jdom2.Element;

public class LSpell extends RSpell {

    public HashMap<String, Integer> spells = new HashMap<String, Integer>();

    public LSpell(Element e, String... path) {
        super(e.getAttributeValue("id"), SpellType.SPELL, path);
        for (Element s : e.getChildren()) {
            spells.put(s.getAttributeValue("id"), Integer.parseInt(s.getAttributeValue("l")));
        }
    }

    public LSpell(String id, String path) {
        super(id, SpellType.SPELL, path);
    }

    public Element toElement() {
        Element list = new Element("list");
        list.setAttribute("id", id);
        for (String s : spells.keySet()) {
            Element spell = new Element("spell");
            spell.setAttribute("id", s);
            spell.setAttribute("l", spells.get(s).toString());
            list.addContent(spell);
        }
        return list;
    }
}
