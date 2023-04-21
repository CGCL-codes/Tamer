package org.apache.isis.extensions.wicket.ui.components.widgets.buttons;

import java.io.Serializable;
import org.apache.isis.extensions.wicket.model.models.EntityModel;
import org.apache.isis.extensions.wicket.ui.panels.PanelAbstract;
import org.apache.wicket.Component;

/**
 * Abstraction of show/hide, ie two buttons only one of which is visible.
 */
public class ToggleButtonsPanel extends PanelAbstract<EntityModel> {

    private static final long serialVersionUID = 1L;

    private static final String ID_BUTTON_1 = "button1";

    private static final String ID_BUTTON_2 = "button2";

    private boolean flag;

    private Toggler toggler;

    private ContainedButton button1;

    private ContainedButton button2;

    public ToggleButtonsPanel(String id, String button1Caption, String button2Caption) {
        super(id, null);
        this.flag = false;
        buildGui(button1Caption, button2Caption);
        onInit();
    }

    private void buildGui(String button1Caption, String button2Caption) {
        button1 = new ContainedButton(ID_BUTTON_1, button1Caption) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                toggler.toggle();
            }
        };
        addOrReplace(button1);
        button2 = new ContainedButton(ID_BUTTON_2, button2Caption) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                toggler.toggle();
            }
        };
        toggler = new Toggler(button1, button2);
        addOrReplace(button2);
    }

    public void addComponentToRerender(Component... components) {
        for (Component component : components) {
            button1.addComponentToRerender(component);
            button2.addComponentToRerender(component);
        }
    }

    /**
	 * Hook method to override.
	 */
    protected void onInit() {
    }

    /**
	 * Hook method to override.
	 */
    protected void onButton1() {
    }

    /**
	 * Hook method to override.
	 */
    protected void onButton2() {
    }

    /**
	 * For subclasses to use.
	 */
    protected final void hideButton1() {
        flag = true;
        toggler.syncButtonVisibility();
    }

    /**
	 * For subclasses to use.
	 */
    protected final void hideButton2() {
        flag = false;
        toggler.syncButtonVisibility();
    }

    private class Toggler implements Serializable {

        private static final long serialVersionUID = 1L;

        private Component component1;

        private Component component2;

        public Toggler(Component component1, Component component2) {
            this.component1 = component1;
            this.component2 = component2;
            syncButtonVisibility();
        }

        public void toggle() {
            fireHooks();
            syncButtonVisibility();
        }

        private void fireHooks() {
            flag = !flag;
            if (flag) {
                onButton1();
            } else {
                onButton2();
            }
        }

        private void syncButtonVisibility() {
            component1.setVisible(!flag);
            component2.setVisible(flag);
        }
    }
}
