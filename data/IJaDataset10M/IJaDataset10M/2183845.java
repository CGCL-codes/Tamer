package net.sf.beatrix.module.ui.forms.pages.details;

import net.sf.beatrix.core.container.DataCollection;
import net.sf.beatrix.ui.forms.FormUtility;
import net.sf.beatrix.ui.viewers.outline.ContentOutlineNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.Section;

public class DataDetailsPage extends AbstractVisualizationDetailsPage {

    protected Object data;

    private Label typeLabel;

    private Label toStringLabel;

    public DataDetailsPage() {
    }

    @Override
    public void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        Section section = FormUtility.addSection(toolkit, parent, "Data Details");
        typeLabel = FormUtility.addSectionInfo(toolkit, section, "Type:", "", true);
        toStringLabel = FormUtility.addSectionInfo(toolkit, section, "toString:", "", true);
    }

    @Override
    public void setFocus() {
        typeLabel.setFocus();
    }

    @Override
    public void refresh() {
        if (data != null) {
            final String PROPERTY = "DataDetailsPage#data";
            if (!data.equals(typeLabel.getData(PROPERTY))) {
                typeLabel.setData(PROPERTY, data);
                typeLabel.setText(data.getClass().getName());
            }
            if (!data.equals(toStringLabel.getData(PROPERTY))) {
                toStringLabel.setData(PROPERTY, data);
                toStringLabel.setText(data.toString());
            }
        }
    }

    @Override
    public void selectionChanged(IFormPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();
            if (element instanceof ContentOutlineNode) {
                Object parent = ((ContentOutlineNode) element).getParent();
                if (parent instanceof ContentOutlineNode) {
                    Object obj = ((ContentOutlineNode) parent).getContent();
                    parent = ((ContentOutlineNode) parent).getParent();
                    if (obj instanceof String && parent instanceof DataCollection) {
                        data = ((ContentOutlineNode) element).getContent();
                    }
                }
            }
        }
        refresh();
    }
}
