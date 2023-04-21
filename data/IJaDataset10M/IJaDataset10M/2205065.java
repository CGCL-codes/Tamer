package ua.org.nuos.sdms.clientgui.client.components.fieldFactory;

import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import ua.org.nuos.sdms.clientgui.client.components.factory.CustomFieldFactory;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 25.03.12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class GroupFieldFactory extends CustomFieldFactory {

    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field f = super.createField(item, propertyId, uiContext);
        if (propertyId.equals("name")) {
            f = createTextField("Название", true);
            f.addValidator(new StringLengthValidator("Поле \"Название\" должно содержать не более 255 символов.", 0, 255, false));
        } else if (propertyId.equals("about")) {
            f = createTextAreaField("Описание", false);
        } else if (propertyId.equals("organisation")) {
            f = createTextField("Организация", false);
            f.addValidator(new StringLengthValidator("Поле \"Организация\" должно содержать не более 255 символов.", 0, 255, false));
        }
        return f;
    }
}
