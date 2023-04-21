package org.springframework.richclient.binding.form.swing;

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.binding.form.BinderSelectionStrategy;
import org.springframework.richclient.binding.form.Binding;
import org.springframework.richclient.binding.form.swing.AbstractSwingBinder;
import org.springframework.richclient.binding.form.swing.ScrollPaneDecoratedBinding;
import org.springframework.richclient.binding.form.swing.SwingBinding;
import org.springframework.util.Assert;

/**
 * A binder that binds a scroll pane and the scroll pane's view. If the 
 * scroll pane does not have a view a default binding will be created and
 * set as the scroll pane's view.
 * 
 * @author Oliver Hutchison
 */
public class ScrollPaneBinder extends AbstractSwingBinder {

    private final BinderSelectionStrategy viewBinderSelectionStrategy;

    private final Class defaultViewType;

    /**
     * Constructs a new ScrollPaneBinder
     * 
     * @param viewBinderSelectionStrategy the {@link BinderSelectionStrategy} which will be used 
     * to select a Binder for the scrollpane's view component.
     * @param defaultViewType the type of the component that will be created and bound if the 
     * scroll pane does not already have a view    
     */
    public ScrollPaneBinder(BinderSelectionStrategy viewBinderSelectionStrategy, Class defaultViewType) {
        super(null);
        this.viewBinderSelectionStrategy = viewBinderSelectionStrategy;
        this.defaultViewType = defaultViewType;
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createScrollPane();
    }

    protected Binding doBind(JComponent control, FormModel formModel, String field, Map context) {
        Assert.isTrue(control instanceof JScrollPane, "Control must be an instance of JScrollPane.");
        JScrollPane scrollPane = (JScrollPane) control;
        Binding viewBinding = getViewBinding(scrollPane, formModel, field, context);
        return new ScrollPaneDecoratedBinding(viewBinding, scrollPane);
    }

    protected Binding getViewBinding(JScrollPane scrollPane, FormModel formModel, String field, Map context) {
        JComponent view = (JComponent) scrollPane.getViewport().getView();
        if (view == null) {
            Binding viewBinding = viewBinderSelectionStrategy.selectBinder(defaultViewType, formModel, field).bind(formModel, field, context);
            Assert.isInstanceOf(SwingBinding.class, viewBinding);
            scrollPane.setViewportView(((SwingBinding) viewBinding).getSwingComponent());
            return viewBinding;
        } else {
            Binding existingBinding = (Binding) view.getClientProperty(BINDING_CLIENT_PROPERTY_KEY);
            if (existingBinding != null) {
                return existingBinding;
            } else {
                return viewBinderSelectionStrategy.selectBinder(view.getClass(), formModel, field).bind(view, formModel, field, context);
            }
        }
    }

    protected void validateContextKeys(Map context) {
    }
}
