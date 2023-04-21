package guineu.parameters.parametersType;

import guineu.parameters.UserParameter;
import java.util.Collection;
import org.w3c.dom.Element;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public class PercentParameter implements UserParameter<Double, PercentComponent> {

    private String name, description;

    private Double value;

    public PercentParameter(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public PercentComponent createEditingComponent() {
        return new PercentComponent();
    }

    @Override
    public void setValueFromComponent(PercentComponent component) {
        Double componentValue = component.getValue();
        if (componentValue == null) return;
        if ((componentValue < 0) || (componentValue > 100)) throw new IllegalArgumentException("Invalid percentage value");
        this.value = componentValue;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public PercentParameter clone() {
        PercentParameter copy = new PercentParameter(name, description);
        copy.setValue(this.getValue());
        return copy;
    }

    @Override
    public void setValueToComponent(PercentComponent component, Double newValue) {
        if (newValue == null) return;
        component.setValue(newValue);
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void loadValueFromXML(Element xmlElement) {
        String numString = xmlElement.getTextContent();
        if (numString.length() == 0) return;
        this.value = Double.parseDouble(numString);
    }

    @Override
    public void saveValueToXML(Element xmlElement) {
        if (value == null) return;
        xmlElement.setTextContent(value.toString());
    }

    public boolean checkValue(Collection<String> errorMessages) {
        if (value == null) {
            errorMessages.add(name + " is not set");
            return false;
        }
        return true;
    }
}
