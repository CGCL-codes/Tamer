package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.List;

public class SectionContainer {

    private List<Section> content = new ArrayList<Section>();

    public Section[] getContent() {
        return content.toArray(new Section[content.size()]);
    }

    public void addContent(Section newValue) {
        content.add(newValue);
    }
}
