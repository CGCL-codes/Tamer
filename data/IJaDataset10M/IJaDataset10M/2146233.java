package de.ui.sushi.metadata.xml;

import java.util.List;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import de.ui.sushi.metadata.Item;
import de.ui.sushi.metadata.SimpleType;
import de.ui.sushi.metadata.SimpleTypeException;
import de.ui.sushi.metadata.Type;

public class SimpleElement extends Element {

    private final StringBuilder builder;

    private final SimpleType type;

    public SimpleElement(Item<?> owner, SimpleType type) {
        super(owner);
        this.builder = new StringBuilder();
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Item<?> lookup(String name) {
        return null;
    }

    @Override
    public void addChild(Item<?> item, Object child) {
        throw new IllegalStateException();
    }

    @Override
    public boolean addContent(char[] ch, int ofs, int len) {
        builder.append(ch, ofs, len);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return builder.length() == 0;
    }

    @Override
    public Object create(List<SAXException> exceptions, Locator locator) {
        String str;
        str = builder.toString();
        try {
            return type.stringToValue(str);
        } catch (SimpleTypeException e) {
            exceptions.add(new SAXLoaderException("cannot set simple value '" + str + "': " + e.getMessage(), locator));
            return type.newInstance();
        }
    }
}
