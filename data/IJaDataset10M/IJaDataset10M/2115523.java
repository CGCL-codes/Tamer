package astcentric.structure.validation;

import java.util.HashMap;
import java.util.Map;
import astcentric.structure.basic.Node;

public class DefaultValueValidatorFactory implements ValueValidatorFactory {

    private final Map<Node, ValueValidatorFactory> _factories = new HashMap<Node, ValueValidatorFactory>();

    public void register(Node declaration, ValueValidatorFactory factory) {
        _factories.put(declaration, factory);
    }

    public ValueValidator create(Node specification) {
        Node reference = specification.getReference();
        if (reference == null) {
            return OKValueValidator.OK_VALUE_VALIDATOR;
        }
        ValueValidatorFactory factory = _factories.get(reference.getOriginalNode());
        return factory == null ? OKValueValidator.OK_VALUE_VALIDATOR : factory.create(specification);
    }
}
