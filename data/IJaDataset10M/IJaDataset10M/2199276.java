package fastforward.wicket.renderer.component;

import org.apache.wicket.model.PropertyModel;

public interface AjaxObjectListStorageCallback {

    /**
     * Called after the user click OK in the edition panel or directly if no edition panel is provided. 
     * to store the new object inside the target property.
     * @param propertyModel
     * @param newObject
     */
    void storeNewObject(PropertyModel propertyModel, Object newObject);

    /**
     * Called after the user click OK in the user input panel or directly if no user input panel is provided. 
     * @param propertyModel
     * @param selectedChoice
     */
    void deleteObject(PropertyModel propertyModel, Object selectedChoice);
}
