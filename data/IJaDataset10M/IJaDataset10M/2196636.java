package org.sodeja.swing.action;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.sodeja.lang.reflect.ReflectUtils;
import org.sodeja.swing.context.ApplicationContext;
import org.sodeja.swing.util.SwingUtils;

public abstract class ApplicationAction<T extends ApplicationContext> extends BaseAction {

    private ActionPropertyChangeListener changeListener = new ActionPropertyChangeListener();

    public ApplicationAction() {
    }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        Component comp = (Component) ReflectUtils.getUsingMethod(listener, "target");
        comp.addPropertyChangeListener("ancestor", changeListener);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener);
        Component comp = (Component) ReflectUtils.getUsingMethod(listener, "target");
        comp.removePropertyChangeListener("ancestor", changeListener);
    }

    protected abstract void init(T ctx);

    protected abstract void destroy(T ctx);

    private class ActionPropertyChangeListener implements PropertyChangeListener {

        private int attachedTo;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean remove = evt.getNewValue() == null;
            AbstractButton button = (AbstractButton) evt.getSource();
            if (remove) {
                deattached(button);
            } else {
                attached(button);
            }
        }

        private void attached(AbstractButton button) {
            if (attachedTo == 0) {
                T ctx = SwingUtils.getContext(button);
                init(ctx);
            }
            attachedTo++;
        }

        private void deattached(AbstractButton button) {
            JPopupMenu menu = (JPopupMenu) SwingUtilities.getAncestorOfClass(JPopupMenu.class, button);
            if ((menu == null || !(menu.getInvoker() instanceof JMenu)) && (attachedTo == 1)) {
                T ctx = SwingUtils.getContext(button);
                destroy(ctx);
                if (menu != null) {
                    button.removePropertyChangeListener("ancestor", this);
                } else {
                    button.setAction(null);
                }
                attachedTo--;
            }
        }
    }
}
