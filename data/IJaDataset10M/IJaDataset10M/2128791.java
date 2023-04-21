package org.springframework.richclient.form.binding.jide;

import com.jidesoft.swing.CheckBoxListWithSelectable;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.AbstractListBinder;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.util.Assert;
import javax.swing.*;
import java.util.Map;

/**
 * Binder for the JIDE check box list with selectable.
 * 
 * This is a copy, paste and modification from the Spring RCP ListBinder.
 * 
 * @author Jonny Wray
 *
 */
public class CheckBoxListSelectableBinder extends AbstractListBinder {

    public static final String RENDERER_KEY = "renderer";

    public static final String SELECTION_MODE_KEY = "selectionMode";

    private ListCellRenderer renderer;

    private Integer selectionMode;

    public CheckBoxListSelectableBinder() {
        this(null, new String[] { SELECTABLE_ITEMS_KEY, COMPARATOR_KEY, RENDERER_KEY, FILTER_KEY, SELECTION_MODE_KEY });
    }

    public CheckBoxListSelectableBinder(String[] supportedContextKeys) {
        this(null, supportedContextKeys);
    }

    public CheckBoxListSelectableBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        Assert.isInstanceOf(CheckBoxListWithSelectable.class, control);
        return new CheckBoxListSelectableBinding((CheckBoxListWithSelectable) control, formModel, formPropertyPath, getRequiredSourceClass());
    }

    protected void applyContext(AbstractListBinding binding, Map context) {
        super.applyContext(binding, context);
        CheckBoxListSelectableBinding listBinding = (CheckBoxListSelectableBinding) binding;
        if (context.containsKey(RENDERER_KEY)) {
            listBinding.setRenderer((ListCellRenderer) decorate(context.get(RENDERER_KEY), listBinding.getRenderer()));
        } else if (renderer != null) {
            listBinding.setRenderer((ListCellRenderer) decorate(renderer, listBinding.getRenderer()));
        }
        if (context.containsKey(SELECTION_MODE_KEY)) {
            Object contextSelectionMode = context.get(SELECTION_MODE_KEY);
            if (contextSelectionMode instanceof Integer) {
                listBinding.setSelectionMode(((Integer) contextSelectionMode).intValue());
            } else {
                try {
                    listBinding.setSelectionMode(((Integer) ListSelectionModel.class.getField((String) contextSelectionMode).get(null)).intValue());
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Unable to access selection mode field in ListSelectionModel", e);
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException("Unknown selection mode '" + contextSelectionMode + "'", e);
                }
            }
        } else if (selectionMode != null) {
            listBinding.setSelectionMode(selectionMode.intValue());
        }
    }

    protected void applyContext(CheckBoxListSelectableBinding binding, Map context) {
        if (context.containsKey(SELECTION_MODE_KEY)) {
        }
    }

    protected JComponent createControl(Map context) {
        return new CheckBoxListWithSelectable();
    }

    public ListCellRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(ListCellRenderer renderer) {
        this.renderer = renderer;
    }

    public Integer getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(Integer selectionMode) {
        this.selectionMode = selectionMode;
    }
}
