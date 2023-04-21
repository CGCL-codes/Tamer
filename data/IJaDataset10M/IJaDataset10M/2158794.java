package eu.en13606.rm.text;

import java.util.List;

public class CE extends CV {

    private List<CD> mappings;

    public void setMappings(List<CD> mappings) {
        this.mappings = mappings;
    }

    public List<CD> getMappings() {
        return mappings;
    }
}
