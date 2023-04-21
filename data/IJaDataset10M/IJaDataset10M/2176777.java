package search.example.srch;

import java.util.Map;

public interface Document {

    public void add(Field field);

    public void addText(String name, String value);

    public String get(String name);

    public void setFields(Map<String, Field> fields);

    public String[] getValues(String name);

    public Map<String, Field> getFields();
}
