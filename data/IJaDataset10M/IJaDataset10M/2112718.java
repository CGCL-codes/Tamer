package org.imogene.model.core.descriptions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.imogene.model.core.Description;
import org.imogene.model.core.FieldGroup;

public class FieldGroupDescriptionWidget extends Composite implements MyItem {

    private FieldGroup fieldGroup;

    private Group descriptions;

    private EditingDomain editingDomain;

    private List<MyItem> children;

    public FieldGroupDescriptionWidget(Composite parent, int style) {
        super(parent, style);
    }

    public void setFieldGroup(FieldGroup fieldGroup) {
        this.fieldGroup = fieldGroup;
        onFieldGroupChanged();
    }

    public void setEditingDomain(EditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }

    protected void onFieldGroupChanged() {
        if (descriptions != null) {
            descriptions.dispose();
        }
        if (children != null) {
            children.clear();
        }
        createComposite();
        Notifier.getInstance().layout(fieldGroup.getParentCard());
    }

    private void createComposite() {
        setLayout(new GridLayout());
        descriptions = new Group(this, SWT.NONE);
        descriptions.setText("Field group descriptions");
        descriptions.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        descriptions.setLayout(new GridLayout(2, false));
        for (Description description : fieldGroup.getDescriptions()) {
            LabelWidget label = new LabelWidget(descriptions, SWT.NONE);
            label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
            label.setDescription(description);
            DescriptionWidget text = new DescriptionWidget(descriptions, SWT.NONE);
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            text.setEditingDomain(editingDomain);
            text.setLabel(label);
            text.setDescription(description);
            if (children == null) {
                children = new ArrayList<MyItem>();
            }
            children.add(text);
        }
    }

    @Override
    public void refreshItem() {
        onFieldGroupChanged();
    }

    @Override
    public void updateItem() {
    }

    @Override
    public List<MyItem> getChildrenItem() {
        if (children != null) {
            return new ArrayList<MyItem>(children);
        }
        return null;
    }

    public EObject getEObject() {
        return null;
    }

    ;

    @Override
    public boolean hasChildren() {
        return children != null && children.size() > 0;
    }
}
