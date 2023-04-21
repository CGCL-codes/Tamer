package consciouscode.seedling.config;

import consciouscode.seedling.NodeInstantiationException;
import consciouscode.seedling.NodeLocation;
import consciouscode.seedling.SeedlingConstants;

/**
    An error that occurred while evaluating the configuration
    expression for a property of a Seedling node.
*/
public class PropertyEvaluationException extends NodeInstantiationException {

    public PropertyEvaluationException(NodeLocation location, String property, String reason) {
        super(location, reason);
        myProperty = property;
    }

    public PropertyEvaluationException(NodeLocation location, String property, Throwable cause) {
        super(location, cause);
        myProperty = property;
    }

    public String getProperty() {
        return myProperty;
    }

    @Override
    public String getMessage() {
        StringBuilder buffer = new StringBuilder("Error evaluating ");
        buffer.append(getLocalPath());
        if (myProperty.startsWith(SeedlingConstants.SPECIAL_PROPERTY_PREFIX)) {
            buffer.append(" meta-property ");
        } else {
            buffer.append('.');
        }
        buffer.append(myProperty);
        renderReasonAndCause(buffer);
        return buffer.toString();
    }

    private String myProperty;
}
