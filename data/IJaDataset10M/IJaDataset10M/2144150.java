package org.illico.web.widget.form;

import org.illico.common.component.ComponentUtils;
import org.illico.common.component.Published;
import org.illico.common.registry.RegistryAppender;
import org.illico.common.text.TextUtils;
import org.illico.common.widget.AbstractComposite;
import org.illico.common.widget.SubWidget;
import org.illico.web.widget.basic.Text;

/**
 * Test de composite
 * 
 * @author grossi
 *
 */
public class FormField<T> extends AbstractComposite implements SubWidget {

    private static final String LABEL_NAME = "label";

    @Override
    protected void createWidgets() {
        addWidget(LABEL_NAME, new Text<Object>());
    }

    @Published
    public Text<T> getLabel() {
        return (Text<T>) getWidget(LABEL_NAME);
    }

    public Class<?> getParentClass() {
        return Form.class;
    }

    public static void main(String[] args) {
        FormField<String> field = new FormField<String>();
        System.out.println("@" + Integer.toHexString(System.identityHashCode(field.getLabel())));
        ComponentUtils.getRegistry().register(field);
        System.out.println(TextUtils.toString(ComponentUtils.getRegistry(), new RegistryAppender()));
        FormField<String> field2 = (FormField<String>) field.clone();
        System.out.println("@" + Integer.toHexString(System.identityHashCode(field2.getLabel())));
        System.out.println("-");
        ComponentUtils.getRegistry().unregister(field);
        System.out.println(TextUtils.toString(ComponentUtils.getRegistry(), new RegistryAppender()));
    }
}
