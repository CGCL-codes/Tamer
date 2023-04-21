package oxdoc.html;

import java.util.ArrayList;
import oxdoc.OxDoc;

public class DefinitionList extends Element {

    String cssClass;

    ArrayList labels = new ArrayList();

    ArrayList definitions = new ArrayList();

    public DefinitionList(OxDoc oxdoc, String cssClass) {
        super(oxdoc);
        this.cssClass = cssClass;
    }

    public void addItem(String label, String definition) {
        labels.add(label);
        definitions.add(definition);
    }

    protected void render(StringBuffer buffer) {
        buffer.append(String.format("<dl%s>\n", classAttr(cssClass)));
        for (int i = 0; i < labels.size(); i++) {
            String label = (String) labels.get(i);
            String definition = (String) definitions.get(i);
            buffer.append(String.format("<dt>%s</dt><dd>%s</dd>\n", label, definition));
        }
        buffer.append("</dl>\n");
    }

    public String toString() {
        StringBuffer bf = new StringBuffer();
        render(bf);
        return bf.toString();
    }
}
