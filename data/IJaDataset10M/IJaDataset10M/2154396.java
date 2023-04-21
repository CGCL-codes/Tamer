package nl.contentanalysis.inet.anokonet.ui;

import java.util.List;
import nl.contentanalysis.inet.model.IAnnotationField;
import nl.contentanalysis.inet.model.IEditorFactory;
import nl.contentanalysis.inet.model.ValidationError;
import nl.contentanalysis.inet.ui.ComboEditorFactory;
import org.eclipse.swt.SWT;

public class FixedListField implements IAnnotationField {

    private List<String> values;

    public FixedListField(List<String> values) {
        this.values = values;
    }

    public Object acceptValueText(Object rawValue) throws ValidationError {
        return rawValue;
    }

    public IEditorFactory getEditorFactory() {
        return new ComboEditorFactory(values, SWT.DROP_DOWN | SWT.READ_ONLY);
    }

    public String getLabel(Object o) {
        if (o == null) return null;
        return o.toString();
    }

    public String validate(Object value) {
        return null;
    }
}
