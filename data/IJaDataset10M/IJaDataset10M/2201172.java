package org.stamppagetor.property;

/**
 * This extends PropertyInfoString by changing PropertyCell type to PropertyCellImage.
 * So user will get image selection button to property pane.
 */
public class PropertyInfoImage extends PropertyInfoString {

    /**
	 * Constructor
	 * 
	 * @param propertyName    Name of the property
	 */
    public PropertyInfoImage(String propertyName) {
        super(propertyName, null);
    }

    @Override
    public PropertyCell createPropertyCell(PropertyTableModel model, int index) {
        return new PropertyCellImage(model, index);
    }
}
