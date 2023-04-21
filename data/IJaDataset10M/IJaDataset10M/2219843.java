package net.sf.jvibes.io;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.jvibes.kernel.elements.Model;
import org.jdom.Element;

class PointPropertyHandler extends PropertyHandler<Point> {

    private static final String ATTR_X = "x";

    private static final String ATTR_Y = "y";

    public PointPropertyHandler() {
        super(Point.class);
    }

    @Override
    public Point read(Element element, Model model) {
        String xStr = element.getAttributeValue(ATTR_X);
        String yStr = element.getAttributeValue(ATTR_Y);
        if (!xStr.matches("\\d+") || !yStr.matches("\\d+")) return null;
        return new Point(Integer.parseInt(xStr), Integer.parseInt(yStr));
    }

    @Override
    public Element write(Point value, Element e) {
        e.setAttribute(ATTR_X, String.valueOf(value.x));
        e.setAttribute(ATTR_Y, String.valueOf(value.y));
        return e;
    }

    @Override
    public Point read(String s) {
        s = s.trim();
        Pattern p = Pattern.compile("\\(?\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)?");
        Matcher m = p.matcher(s);
        if (!m.matches()) return null;
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        return new Point(x, y);
    }

    @Override
    public String write(Point value) {
        return "(" + value.x + ", " + value.y + ")";
    }
}
