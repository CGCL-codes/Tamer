package info.freundorfer.icalendar.components;

import info.freundorfer.icalendar.ICalendarParseException;
import info.freundorfer.icalendar.properties.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josef Freundorfer <http://www.freundorfer.info>
 *
 */
public class NonStandardComponent extends Component<NonStandardComponent> {

    private String name;

    List<Property<?>> properties = new ArrayList<Property<?>>();

    List<Component<?>> subComponents = new ArrayList<Component<?>>();

    public NonStandardComponent() {
    }

    /**
	 * @param name2
	 */
    public NonStandardComponent(final String name) {
        this.name = name;
    }

    /**
	 * @param currentProperty
	 */
    @Override
    public NonStandardComponent addProperty(final Property<?> currentProperty) {
        properties.add(currentProperty);
        return this;
    }

    /**
	 * @param newComponent
	 * @throws ICalendarParseException
	 */
    @Override
    public NonStandardComponent addSubComponent(final Component<?> newComponent) throws ICalendarParseException {
        subComponents.add(newComponent);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
	 * @return the properties
	 */
    @Override
    public final List<Property<?>> getProperties() {
        return properties;
    }

    /**
	 * @return the subComponents
	 */
    @Override
    public final List<Component<?>> getSubComponents() {
        return subComponents;
    }

    @Override
    public NonStandardComponent validateComponent() {
        return null;
    }
}
