package lablog.ui.widgets;

import java.util.Collections;
import java.util.List;
import lablog.lib.control.Controller;
import lablog.lib.db.entity.base.AbstractEntity;
import lablog.lib.db.entity.base.EntityDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A combo box to display drop down lists of entities.
 */
public class EntitySingleWidget<T extends AbstractEntity> extends WidgetViewer<T> {

    private final Combo combo;

    private final List<T> entities;

    public EntitySingleWidget(EntityDescriptor<T> descriptor) {
        this(null, descriptor);
    }

    public EntitySingleWidget(Composite parent, T entity) {
        this(parent, EntityDescriptor.describe(entity));
        setInput(entity);
    }

    public EntitySingleWidget(Composite parent, EntityDescriptor<T> descriptor) {
        super();
        combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        entities = Controller.findAll(descriptor.getEntityClass());
        Collections.sort(entities);
        for (T e : entities) combo.add(e.toString());
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                fireValueChanged();
            }
        });
    }

    @Override
    public Control getControl() {
        return combo;
    }

    @Override
    public T getValue() {
        int index = combo.getSelectionIndex();
        if (index >= 0 && index < entities.size()) return entities.get(index); else return null;
    }

    @Override
    public void setInput(Object input) {
        int index = entities.indexOf(input);
        if (index >= 0 && index < entities.size()) combo.select(index); else combo.deselectAll();
    }

    @Override
    public ISelection getSelection() {
        return new StructuredSelection(getValue());
    }

    @Override
    public void setSelection(ISelection selection, boolean reveal) {
        setInput(((StructuredSelection) selection).getFirstElement());
    }
}
