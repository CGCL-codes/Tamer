package com.kenstevens.stratdom.site.parser;

import java.util.regex.Matcher;
import org.w3c.dom.Node;
import com.kenstevens.stratdom.model.Unit;
import com.kenstevens.stratdom.model.Unit.Type;

public class UnitParser extends XMLParser {

    public Unit parse(Node tr) {
        Unit unit = new Unit();
        Node td = tr.getFirstChild();
        assertNode("td", td);
        Node a = td.getFirstChild();
        if (a.getNodeName().equalsIgnoreCase("script")) {
            return null;
        }
        assertNode("a", a);
        unit.setId(Integer.valueOf(getId(a)));
        Node img = a.getFirstChild();
        assertNode("img", img);
        unit.setType(getType(img));
        unit.setHoursToMove(getHoursToMove(img));
        td = td.getNextSibling();
        assertNode("td", td);
        unit.setMove(getMoves(td));
        td = td.getNextSibling();
        assertNode("td", td);
        setXY(unit, td);
        return unit;
    }

    private void setXY(Unit unit, Node td) {
        String text = td.getTextContent();
        Matcher matcher = match(text, "(\\d+), (\\d+)", "x,y");
        unit.setX(Integer.valueOf(matcher.group(1)));
        unit.setY(Integer.valueOf(matcher.group(2)));
    }

    private int getMoves(Node td) {
        String text = td.getTextContent();
        Matcher matcher = match(text, "(\\d+)", "moves");
        return Integer.valueOf(matcher.group(1));
    }

    private float getHoursToMove(Node img) {
        String srcValue = img.getAttributes().getNamedItem("title").getNodeValue();
        Matcher matcher = match(srcValue, "(\\d+.\\d)", "hours to move");
        return Float.valueOf(matcher.group(1));
    }

    private Type getType(Node img) {
        String srcValue = img.getAttributes().getNamedItem("src").getNodeValue();
        Matcher matcher = match(srcValue, "(\\w+).jpg", "type");
        String typeString = matcher.group(1);
        return Enum.valueOf(Type.class, typeString.toUpperCase());
    }

    private String getId(Node a) {
        String hrefValue = a.getAttributes().getNamedItem("href").getNodeValue();
        Matcher matcher = match(hrefValue, "(\\d+)", "id");
        return matcher.group(1);
    }

    public Unit parseActiveUnit(Node tr) {
        Unit unit = new Unit();
        Node td = tr.getFirstChild();
        assertNode("td", td);
        td = td.getNextSibling();
        assertNode("td", td);
        td = td.getNextSibling();
        assertNode("td", td);
        unit.setHp(getHP(td));
        td = td.getNextSibling();
        if (td != null) {
            assertNode("td", td);
            unit.setFuel(getFuel(td));
            td = td.getNextSibling();
            if (td != null) {
                assertNode("td", td);
                unit.setBlastRadius(getBlastRadius(td));
            }
        }
        return unit;
    }

    private int getHP(Node td) {
        String text = td.getTextContent();
        Matcher matcher = match(text, "(\\d+)", "hp");
        return Integer.valueOf(matcher.group(1));
    }

    private int getFuel(Node td) {
        String text = td.getTextContent();
        Matcher matcher = match(text, "(\\d+|fumes)", "fuel");
        if ("fumes".equals(matcher.group(1))) {
            return 0;
        }
        return Integer.valueOf(matcher.group(1));
    }

    private int getBlastRadius(Node td) {
        String text = td.getTextContent();
        Matcher matcher = match(text, "(\\d+)", "blast radius");
        return Integer.valueOf(matcher.group(1));
    }
}
