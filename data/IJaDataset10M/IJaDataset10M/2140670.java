package nl.contentanalysis.inet.handlers.fieldeditor;

import nl.contentanalysis.inet.extensions.IEditManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public abstract class FormEditorAbstract<T extends Control> extends FieldEditorAbstract<T> {

    protected abstract T createControl(Composite parent, FormToolkit toolkit);

    @Override
    public void initialize(IEditManager manager, Object initialvalue) {
        this.control = createControl(manager.getControlParent());
        assignHandlers();
        super.initialize(manager, initialvalue);
    }

    @Override
    protected String valueToText(Object value) {
        return value == null ? "" : value.toString();
    }

    public void handle(TypedEvent e, String typed) {
    }

    public void stop(boolean save) {
    }

    @Override
    protected T createControl(Composite parent) {
        FormToolkit t = new FormToolkit(parent.getDisplay());
        T c = createControl(parent, t);
        TableWrapData dat = new TableWrapData(TableWrapData.FILL_GRAB);
        c.setLayoutData(dat);
        return c;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        setText(valueToText(value), false);
    }

    protected void assignHandlers() {
        assignHandlers(control);
    }

    protected void assignControlHandlers(Control c) {
        c.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                manager.stopEdit(e.detail != SWT.TRAVERSE_ESCAPE, e);
            }
        });
        c.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                manager.stopEdit(true, e);
            }
        });
    }

    protected void assignHandlers(T c) {
        assignControlHandlers(c);
    }
}
