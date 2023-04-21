package net.pleso.framework.client.bl.forms.items;

/**
 * Represents abstract editable form item with required status.
 * 
 * This interface is used for items which can be optionally required 
 * for input.
 */
public interface IEditFormItem extends IFormItem {

    /**
	 * Determines whether item is requred for input.
	 * 
	 * @return <code>true</code> if item is required
	 */
    boolean isRequired();
}
