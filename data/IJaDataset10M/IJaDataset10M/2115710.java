package nctools.editors.internal;

import nctools.colors.FloatColor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * Editor for float-componented colors with separate {@link Spinner} for every component.  
 * 
 * @author Mike
 */
public class FloatColorSpinnerEditor<EditedColor extends FloatColor<EditedColor>> extends AbstractCompositeColorEditor<EditedColor> {

    /** factory method to provide auto-capturing of the type parameter */
    public static <EdColor extends FloatColor<EdColor>> FloatColorSpinnerEditor<EdColor> newEditor(Class<EdColor> editedColorClass, Composite parent, int style) {
        return new FloatColorSpinnerEditor<EdColor>(editedColorClass, parent, style);
    }

    private final Spinner[] m_componentEditors;

    private final ModifyListener m_modifyListener = new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
            final int componentsCount = getColor().getComponentsCount();
            for (int i = 0; i < componentsCount; ++i) {
                if (e.getSource() == m_componentEditors[i]) {
                    onComponentEditorUpdated(i);
                    break;
                }
            }
        }
    };

    public FloatColorSpinnerEditor(Class<EditedColor> editedColorClass, Composite parent, int style) {
        super(editedColorClass, parent, style);
        final int componentsCount = getColor().getComponentsCount();
        m_componentEditors = new Spinner[componentsCount];
        {
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            layout.verticalSpacing = 0;
            setLayout(layout);
        }
        for (int i = 0; i < componentsCount; ++i) {
            createLabel(i);
            createEditor(i);
        }
    }

    private void createLabel(int index) {
        final Label label = new Label(this, SWT.LEAD);
        label.setText(getColor().getComponentNameShort(index));
    }

    private void createEditor(int index) {
        final Spinner spinner = new Spinner(this, SWT.BORDER);
        m_componentEditors[index] = spinner;
        final EditedColor color = getColor();
        spinner.setValues(color.getComponentIntScaled(index), color.getMinComponentValueIntScaled(index), color.getMaxComponentValueIntScaled(index), color.getDecimalDigits(index), 1, 10);
        spinner.addModifyListener(m_modifyListener);
        spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
    }

    private void onComponentEditorUpdated(int componentIndex) {
        final Spinner spinner = m_componentEditors[componentIndex];
        getColor().setComponentIntScaled(componentIndex, spinner.getSelection());
        sendColorChangedEvent();
    }

    @Override
    public void setColor(EditedColor src) {
        disableColorChangedEvent();
        final int componentsCount = getColor().getComponentsCount();
        for (int i = 0; i < componentsCount; ++i) {
            final int value = src.getComponentIntScaled(i);
            if (m_componentEditors[i].getSelection() != value) {
                m_componentEditors[i].setSelection(value);
            }
        }
        enableColorChangedEvent();
    }
}
