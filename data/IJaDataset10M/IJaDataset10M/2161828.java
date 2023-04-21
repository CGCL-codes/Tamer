package org.akrogen.tkui.ui.swing.controls.simples;

import java.awt.Container;
import javax.swing.JCheckBox;
import org.akrogen.tkui.core.ui.bindings.ISelectionBindable;
import org.akrogen.tkui.core.ui.elements.IUIElementInfo;
import org.akrogen.tkui.ui.swing.AbstractSwingElementImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.ufacekit.ui.swing.databinding.swing.SwingObservables;

/**
 * Swing checkbox element implementation with {@link JCheckBox}.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SwingCheckBoxImpl extends AbstractSwingElementImpl implements ISelectionBindable {

    private static Log logger = LogFactory.getLog(SwingCheckBoxImpl.class);

    protected Container buildContainer(Container parent, IUIElementInfo info) {
        JCheckBox checkBox = createCheckBox(parent, info);
        return checkBox;
    }

    protected JCheckBox createCheckBox(Container parent, IUIElementInfo info) {
        JCheckBox checkBox = new JCheckBox();
        if (logger.isDebugEnabled()) logger.debug("Create Swing JCheckBox.");
        return checkBox;
    }

    public Binding bindSelection(IObservableValue observableValue) {
        return super.bindValue(SwingObservables.observeSelection(getContainer()), observableValue);
    }

    public void doLayout(boolean refreshParent) {
        doMigLayout(refreshParent);
        if (logger.isDebugEnabled()) logger.debug("Do layout for Swing JCheckBox.");
    }
}
