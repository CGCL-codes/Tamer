package yapgen.base.graphml;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author riccardo
 */
public class GraphMLElementNode extends GraphMLElement {

    public GraphMLElementNode() {
        super(GraphMLElementTagsEnum.node.name());
        allowedChildTags.add(GraphMLElementTagsEnum.data);
    }

    public String getFactSubjectivity() {
        return attributeMap.get(GraphMLElementAttributesEnum.FactSubjectivity.getText());
    }

    public String getFactType() {
        return attributeMap.get(GraphMLElementAttributesEnum.FactType.getText());
    }

    public String getFactComplementNumber() {
        return attributeMap.get(GraphMLElementAttributesEnum.FactComplementNumber.getText());
    }

    public String getFactConsequencePossibility() {
        return defaultValue(attributeMap.get(GraphMLElementAttributesEnum.FactConsequencePossibility.getText()), "possible");
    }

    @Override
    protected boolean pullUpElementsAsAttributes(Map<String, GraphMLElementKey> keyMap, GraphMLElement subElement) {
        for (Entry<String, String> attributeEntry : subElement.getAttributes().entrySet()) {
            this.getAttributes().put(attributeEntry.getKey(), attributeEntry.getValue());
        }
        return true;
    }
}
