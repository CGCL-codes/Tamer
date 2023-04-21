package net.sf.depcon.ui.editors;

import java.util.LinkedList;
import org.eclipse.core.databinding.Binding;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.Section;

public class BindingSectionPart extends SectionPart {

    LinkedList<Binding> bindings = new LinkedList<Binding>();

    public BindingSectionPart(Section section) {
        super(section);
    }

    @Override
    public void commit(boolean onSave) {
        if (onSave) {
            for (Binding binding : bindings) {
                binding.updateTargetToModel();
            }
            super.commit(onSave);
        }
    }

    public LinkedList<Binding> getBindings() {
        return bindings;
    }
}
