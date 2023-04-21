package i18ntool.component;

import i18ntool.consts.Filter;
import i18ntool.property.Resource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FilterModel implements IComponent {

    private Button rdNormal;

    private Button rdChanged;

    private Button rdEmpty;

    private Button rdChangedOrEmpty;

    private Composite parent;

    private Composite composite;

    /**
	 * the constructor will init the Controls, and add listener to them
	 * @param composite
	 * @param value
	 */
    public FilterModel(final Composite parent) {
        this.parent = parent;
        init();
    }

    protected Composite getComposite() {
        return composite;
    }

    private void init() {
        composite = new Composite(parent, SWT.NONE);
        GridData gdOne = new GridData(GridData.FILL, GridData.FILL, true, false);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        composite.setLayoutData(gdOne);
        rdNormal = new Button(composite, SWT.RADIO);
        rdNormal.setLayoutData(gdOne);
        rdNormal.setText(Resource.SHOW_NORMAL);
        rdNormal.setSelection(true);
        rdChanged = new Button(composite, SWT.RADIO);
        rdChanged.setLayoutData(gdOne);
        rdChanged.setText(Resource.SHOW_CHANGED);
        rdEmpty = new Button(composite, SWT.RADIO);
        rdEmpty.setLayoutData(gdOne);
        rdEmpty.setText(Resource.SHOW_EMPTY);
        rdChangedOrEmpty = new Button(composite, SWT.RADIO);
        rdChangedOrEmpty.setLayoutData(gdOne);
        rdChangedOrEmpty.setText(Resource.SHOW_CHANGED_OR_EMPTY);
    }

    public void setChangedOrEmptyText(final boolean excelModel) {
        rdChangedOrEmpty.setText(excelModel ? Resource.SHOW_CHANGED_OR_EMPTY_IN_EXCEL : Resource.SHOW_CHANGED_OR_EMPTY);
    }

    public void setDefaultSelection() {
        Control[] children = composite.getChildren();
        for (Control child : children) {
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setSelection(false);
            }
        }
        rdNormal.setSelection(true);
    }

    protected Filter getFilter() {
        if (rdNormal.getSelection()) {
            return Filter.NORMAL;
        } else if (rdChangedOrEmpty.getSelection()) {
            return Filter.CHANGED_OR_EMPTY;
        } else if (rdChanged.getSelection()) {
            return Filter.CHANGED;
        } else if (rdEmpty.getSelection()) {
            return Filter.EMPTY;
        }
        return Filter.UNKNOWN;
    }

    /**
	 * get which one you selected
	 * Normal
	 * Changed
	 * Empty
	 * Changed/Empty
	 * @return
	 */
    public Filter getValue() {
        return getFilter();
    }
}
