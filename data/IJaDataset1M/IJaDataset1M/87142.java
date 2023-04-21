package bg.tu_sofia.refg.imsqti.response.processor.expression;

import java.util.Map;
import org.w3c.dom.Node;
import bg.tu_sofia.refg.imsqti.item.datamodel.AssessmentItem;
import bg.tu_sofia.refg.imsqti.response.Outcome;
import bg.tu_sofia.refg.imsqti.response.Response;

public class SetOutcomeValue extends Expression {

    private static final long serialVersionUID = 1L;

    private static final String ATTR_IDENTIFIER = "identifier";

    private String identifier;

    public SetOutcomeValue(Node node) {
        super(node);
        this.identifier = (node.getAttributes().getNamedItem(ATTR_IDENTIFIER) != null) ? node.getAttributes().getNamedItem(ATTR_IDENTIFIER).getNodeValue() : "";
    }

    @Override
    public Object eval(Map<String, Response> valueMap, AssessmentItem context) {
        Object value = this.get(0).eval(valueMap, context);
        Outcome outcome = new Outcome(value == null ? null : (Float.valueOf(value.toString())));
        valueMap.put(identifier, outcome);
        return value;
    }
}
