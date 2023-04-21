package org.developerservices.moviedb.application.dialogs;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.developerservices.moviedb.application.list.container.CategoryTreeItem;
import org.developerservices.moviedb.jpa.model.Category;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author rene
 *
 */
public class CategoryEditorDialog extends Dialog implements SelectionListener {

    private Logger log = Logger.getLogger(CategoryEditorDialog.class);

    private Category category;

    private Text textTitle;

    private Text textDescription;

    private Text textIconPath;

    private Button btnSelectIcon;

    private boolean updateGuid = false;

    public CategoryEditorDialog(Shell parentShell) {
        super(parentShell);
        this.category = new Category();
    }

    @Override
    public void create() {
        super.create();
        this.getShell().setText("Category editor");
        this.getShell().setSize(300, 250);
        this.getShell().setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = new Composite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        area.setLayoutData(data);
        GridLayout layout = new GridLayout(3, false);
        area.setLayout(layout);
        area.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridData gd = new GridData();
        Label labelTitle = new Label(area, SWT.NONE);
        labelTitle.setText("Name:");
        labelTitle.setLayoutData(gd);
        labelTitle.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        textTitle = new Text(area, SWT.BORDER);
        textTitle.setLayoutData(gd);
        textTitle.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        Label labelDescription = new Label(area, SWT.NONE);
        labelDescription.setText("Description:");
        labelDescription.setLayoutData(gd);
        labelDescription.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        textDescription = new Text(area, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        textDescription.setLayoutData(gd);
        textDescription.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gd = new GridData();
        Label labelIcon = new Label(area, SWT.NONE);
        labelIcon.setText("Icon:");
        labelIcon.setLayoutData(gd);
        labelIcon.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textIconPath = new Text(area, SWT.BORDER | SWT.READ_ONLY);
        textIconPath.setLayoutData(gd);
        textIconPath.setBackground(this.getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gd = new GridData();
        btnSelectIcon = new Button(area, SWT.PUSH);
        btnSelectIcon.setLayoutData(gd);
        btnSelectIcon.setText("Select");
        btnSelectIcon.addSelectionListener(this);
        if (this.updateGuid) {
            this.textTitle.setText(StringUtils.trimToEmpty(this.category.getName()));
            this.textDescription.setText(StringUtils.trimToEmpty(this.category.getDescription()));
        }
        return area;
    }

    public void setParentCategory(CategoryTreeItem parent) {
        this.category.setParent(parent.getData());
    }

    public void setNeighbourCategory(CategoryTreeItem neighbour) {
        log.debug("Setting neighbour parent: " + neighbour.getParent().getData());
        if (neighbour.getParent().getData() != null) {
            this.category.setParent(neighbour.getParent().getData());
        }
    }

    public Category getCategory() {
        return this.category;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (e.widget == btnSelectIcon) {
            FileDialog dialog = new FileDialog(this.getShell(), SWT.OPEN);
            dialog.setFilterExtensions(new String[] { "*.gif", "*.jpg", "*.jpeg", "*.png" });
            dialog.setFilterNames(new String[] { "Image/GIF", "Image/JPG", "Image/JPEG", "Image/PNG" });
            this.textIconPath.setText(dialog.open());
        }
    }

    @Override
    protected void okPressed() {
        if (this.isValid()) {
            this.updateCategory();
            super.okPressed();
        }
    }

    private void updateCategory() {
        this.category.setName(StringUtils.trimToEmpty(this.textTitle.getText()));
        this.category.setDescription(StringUtils.trimToEmpty(this.textDescription.getText()));
        if (StringUtils.isNotEmpty(textIconPath.getText())) {
            try {
                this.category.setIcon(FileUtils.readFileToByteArray(new File(textIconPath.getText())));
            } catch (IOException e) {
                log.error("Cant read Icon file to bytearray", e);
            }
        }
    }

    private boolean isValid() {
        if (StringUtils.isEmpty(this.textTitle.getText())) {
            MessageDialog.openError(this.getShell(), "Missing field error", "No Title entered!");
            return false;
        }
        return true;
    }

    public void setCategoryTreeItem(CategoryTreeItem categoryTreeItem) {
        this.category = categoryTreeItem.getData();
        this.updateGuid = true;
    }
}
