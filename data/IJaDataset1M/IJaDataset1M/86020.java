package com.prolix.editor.dialogs.composites.resourceselection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.prolix.editor.GlobalConstants;
import com.prolix.editor.resourcemanager.model.ResourceTreeCategory;
import com.prolix.editor.resourcemanager.model.StandardCategories;

public class SelectCategoryComposite extends Composite {

    private ResourceTreeCategory root_category;

    private ResourceTreeCategory category;

    private Text txt_category;

    /**
	 * 
	 * @param parent
	 * @param root the root element of the categories
	 * @param category if null, no category is preselected; if not null, the given category is preselected
	 */
    public SelectCategoryComposite(Composite parent, ResourceTreeCategory root, ResourceTreeCategory category) {
        super(parent, SWT.NONE);
        this.root_category = root;
        this.category = category;
        setupView();
    }

    /**
	 * creates the content of this composite
	 */
    private void setupView() {
        this.setLayout(new GridLayout(3, false));
        if (this.getParent().getLayout() instanceof GridLayout) this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label lbl_title = new Label(this, SWT.NONE);
        lbl_title.setFont(GlobalConstants.DIALOG_HEADER_FONT);
        lbl_title.setText("Select the Category under which this Resource is assigned to:");
        lbl_title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ((GridData) lbl_title.getLayoutData()).horizontalSpan = 3;
        Label dummy = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.LINE_SOLID);
        dummy.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ((GridData) dummy.getLayoutData()).horizontalSpan = 3;
        new Label(this, SWT.NONE).setText("Category:");
        txt_category = new Text(this, SWT.BORDER);
        txt_category.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txt_category.setEditable(false);
        Button btn_category = new Button(this, SWT.PUSH);
        btn_category.setText("Choose Category");
        btn_category.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                SelectCategoryDialog dialog = new SelectCategoryDialog(Display.getCurrent().getActiveShell(), root_category);
                dialog.openDialog();
                if (dialog.getReturnCode() == Dialog.OK) {
                    category = dialog.getSelectedCategory();
                    updateCategoryText();
                }
            }
        });
        setInitialCategory();
        initializeCategoryControls();
    }

    private void setInitialCategory() {
        if (this.category == null) {
            this.category = this.root_category.retrieveCategory(StandardCategories.INDIVIDUAL_CATEGORIES_ID);
        }
    }

    private void initializeCategoryControls() {
        if (this.category != null) updateCategoryText();
    }

    private void updateCategoryText() {
        if (this.category != null) {
            String str_category = category.getLabel();
            ResourceTreeCategory temp = category.getParent();
            if (temp != null) {
                while (temp.getParent() != null) {
                    str_category = temp.getLabel() + " > " + str_category;
                    temp = temp.getParent();
                }
            }
            txt_category.setText(str_category);
        }
    }

    public ResourceTreeCategory getSelectedCategory() {
        return this.category;
    }
}
