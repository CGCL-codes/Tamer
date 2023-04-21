package cn.edu.thss.iise.beehivez.server.index.petrinetindex.invertedindex;

import java.util.ArrayList;

public class DocumentInfo {

    private String name;

    public ArrayList<Field> content = new ArrayList<Field>();

    public int getSize() {
        return content.size();
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public void add(Field f) {
        for (Field field : content) {
            if (field.equal(f)) {
                field.frequency++;
                return;
            }
        }
        content.add(f);
    }

    public void addPhrase(String value) {
        for (Field field : content) {
            if (field.value.equals(value)) {
                field.frequency++;
                return;
            }
        }
        content.add(new Field(value));
    }
}
