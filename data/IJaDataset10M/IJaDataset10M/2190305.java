package net.pleso.framework.client.bl;

/**
 * Represents predefined enumeration of {@link IEnumItem}. It was created
 * because GWT 1.3.3 uses Java 1.4 which doesn't supports enums.
 */
public interface IEnum {

    /**
	 * Returns enumeration items array.
	 * 
	 * @return enumeration items array
	 */
    IEnumItem[] getItems();

    /**
	 * Returns item that represent null value of this enumeration item or
	 * <code>null</code> if there is such item doesn't exists.
	 * 
	 * @return null value item
	 */
    IEnumItem getNullItem();
}
