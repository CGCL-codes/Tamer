package org.aplikator.server.descriptor;

import org.aplikator.client.descriptor.RepeatedFormDTO;
import org.aplikator.client.descriptor.WidgetDTO;
import org.aplikator.server.Context;

public class RepeatedForm implements Widget, HasProperty<Integer> {

    private Collection<? extends Entity> property;

    private View view;

    public RepeatedForm(Collection<? extends Entity> property, View view) {
        this.property = property;
        this.view = view;
    }

    public Property<Integer> getProperty() {
        return property;
    }

    public WidgetDTO getWidgetDescriptor(Context ctx) {
        RepeatedFormDTO desc = new RepeatedFormDTO(property.clientClone(ctx), view.getViewDTO(ctx));
        return desc;
    }

    public void registerProperties(Form form) {
    }

    public static RepeatedForm repeated(Collection<? extends Entity> property) {
        return new RepeatedForm(property, property.referredEntity.view());
    }

    public static RepeatedForm repeated(Collection<? extends Entity> property, View view) {
        return new RepeatedForm(property, view);
    }
}
