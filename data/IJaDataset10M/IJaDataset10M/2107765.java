package com.prolix.editor.dialogs.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.prolix.editor.GlobalConstants;
import com.prolix.editor.resourcemanager.exceptions.GLMRessourceManagerException;
import com.prolix.editor.resourcemanager.model.ResourceTreeCategory;
import com.prolix.editor.resourcemanager.model.ResourceTreeItemText;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemContainer;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemType;

public class SimpleItemModelComposite extends ProlixDialogComposite {

    public String ERROR_MESSAGE = null;

    private boolean text_changed = false;

    private boolean fault_mode = false;

    private ItemContainer itemContainer;

    private Text txt_content;

    private Composite cmp_main;

    private ResourceTreeCategory category;

    /**
	 * constructor for making a new textual resource
	 * @param container
	 */
    public SimpleItemModelComposite(ItemContainer container, ResourceTreeCategory category) {
        this.itemContainer = container;
        this.category = category;
    }

    public void createView(Composite parent) {
        createView(parent, "Please provide a textual description for the element " + this.itemContainer.getTitle() + ":");
    }

    public void createView(Composite parent, String header) {
        cmp_main = new Composite(parent, SWT.NONE);
        cmp_main.setLayout(new GridLayout());
        if (parent.getLayout() instanceof GridLayout) cmp_main.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label lbl_header = new Label(cmp_main, SWT.None);
        lbl_header.setFont(GlobalConstants.DIALOG_HEADER_FONT);
        if (header == null) lbl_header.setText("Please provide a textual description for the element " + this.itemContainer.getTitle() + ":"); else lbl_header.setText(header);
        txt_content = new Text(cmp_main, SWT.BORDER | SWT.WRAP | SWT.MULTI);
        txt_content.setText("Enter your Description here.");
        txt_content.setLayoutData(new GridData(GridData.FILL_BOTH));
        txt_content.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                text_changed = true;
            }
        });
        initializeControls();
    }

    /**
	 * INFO there could be an error if the user moves the first item type so that another cildren is now the first item;
	 * but if there is more than one item a dialog automatically switches to the advanced view
	 * @param item
	 */
    private void initializeDescriptionText(ItemType item) {
        txt_content.setText(((ResourceTreeItemText) item.getResourceTreeItem()).getFileContent());
    }

    private void initializeControls() {
        if (this.itemContainer.getItemTypes().length >= 1) {
            if (((ItemType) this.itemContainer.getItemTypes()[0]).getResourceTreeItem() instanceof ResourceTreeItemText) {
                initializeDescriptionText((ItemType) this.itemContainer.getItemTypes()[0]);
                fault_mode = false;
            } else {
                txt_content.setText("The contents of the associated Resource can not be displayed.");
                fault_mode = true;
            }
        }
    }

    /**
	 * Shows the content of the itemtype when swithed back from advanced view
	 */
    public void initializeComposite() {
        initializeControls();
    }

    /**
	 * creates a new resouceitem and its corresponding itemtype
	 * TODO evtl. pass item container AND category (if not already passed in constr.)
	 * TODO not good; should be better done by the calling class
	 * @return
	 */
    public boolean createResourceItem() {
        if (!getTextChanged()) return false;
        if (!validate()) return false;
        try {
            ResourceTreeItemText resourceItemText = new ResourceTreeItemText(((LD_DataComponent) this.itemContainer.getParent()).getTitle() + " " + this.itemContainer.getTitle() + " (default)", this.category, this.itemContainer.getLearningDesign(), txt_content.getText());
            ItemType item_type = resourceItemText.createItemType();
            item_type.getResourceTreeItem().addItemType(item_type);
            this.itemContainer.addChildAt(item_type, 0);
            this.itemContainer.componentAdded(item_type);
            this.itemContainer.getDataModel().fireDataComponentAdded(item_type);
            this.text_changed = false;
            return true;
        } catch (GLMRessourceManagerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * updates the given resource item (itemContainer.getItemtypes[0].getResaItem())
	 * @param resourceItem
	 * @return
	 */
    public boolean updateResourceItem(ResourceTreeItemText resourceItem) {
        if (!getTextChanged()) return false;
        if (!validate()) return false;
        try {
            resourceItem.setLabel(((LD_DataComponent) this.itemContainer.getParent()).getTitle() + " " + this.itemContainer.getTitle() + " (default)");
            resourceItem.updateContent(txt_content.getText());
            resourceItem.notifyResourceItemChanged();
            this.text_changed = false;
            return true;
        } catch (GLMRessourceManagerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * TODO make this an abstract method
	 * @return
	 */
    public boolean validate() {
        String message = null;
        if (fault_mode) message = (message == null ? "" : message) + "Invalid element! Cannot create or modify this resource. ";
        if (txt_content.getText().length() <= 0) message = (message == null ? "" : message) + "Please provide a textual description for this element";
        return ((ERROR_MESSAGE = message) == null);
    }

    /**
	 * TODO make this an abstract method; eventually this method is not needed
	 * @return
	 */
    public boolean ok() {
        return validate();
    }

    /**
	 * returns if the text was changed
	 * @return
	 */
    public boolean getTextChanged() {
        return text_changed;
    }

    /**
	 * use this method to set the text_changed flag to false
	 */
    public void resetTextChanged() {
        this.text_changed = false;
    }

    /**
	 * @return the text the user entered
	 */
    public String getText() {
        return txt_content.getText();
    }

    public Composite getMainComposite() {
        return this.cmp_main;
    }
}
