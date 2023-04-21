package org.gamegineer.common.internal.core.services.logging.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gamegineer.common.core.services.component.util.attribute.AbstractAttributeTestCase;
import org.gamegineer.common.core.services.component.util.attribute.IAttribute;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.internal.core.services.logging.attributes.LoggingPropertiesAttribute}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.common.core.services.component.util.attribute.IAttribute}
 * interface.
 */
public final class LoggingPropertiesAttributeAsAttributeTest extends AbstractAttributeTestCase<Map<String, String>> {

    /**
     * Initializes a new instance of the
     * {@code LoggingPropertiesAttributeAsAttributeTest} class.
     */
    public LoggingPropertiesAttributeAsAttributeTest() {
        super();
    }

    @Override
    protected IAttribute<Map<String, String>> createAttribute() {
        return LoggingPropertiesAttribute.INSTANCE;
    }

    @Override
    protected List<Map<String, String>> getLegalValues() {
        final List<Map<String, String>> values = new ArrayList<Map<String, String>>();
        values.add(Collections.<String, String>emptyMap());
        values.add(Collections.singletonMap("name", "value"));
        final Map<String, String> multiElementMap = new HashMap<String, String>();
        multiElementMap.put("name1", "value1");
        multiElementMap.put("name2", "value2");
        values.add(multiElementMap);
        return values;
    }
}
