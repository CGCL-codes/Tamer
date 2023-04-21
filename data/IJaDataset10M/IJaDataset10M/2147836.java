package org.monet.kernel.model.definition;

import java.util.ArrayList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "field-pattern")
public class PatternFieldDeclaration extends FieldDeclaration {

    public static class Meta {

        @Attribute(name = "index", required = false)
        protected int index;

        @Attribute(name = "name", required = false)
        protected String name;

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
    }

    @Attribute(name = "pattern")
    protected String pattern;

    @ElementList(inline = true, required = false)
    protected ArrayList<Meta> metaList = new ArrayList<Meta>();

    public String getPattern() {
        return pattern;
    }

    public ArrayList<Meta> getMetaList() {
        return metaList;
    }
}
