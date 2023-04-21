package wh2fo.parser;

import java.util.Hashtable;

public interface PropertyAdapter {

    public String convertProperty(String property);

    public boolean convertibleProperty(String property);
}
