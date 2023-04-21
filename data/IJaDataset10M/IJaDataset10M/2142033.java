package org.isistan.flabot.edit.componenteditor.dialogs.responsibility.editionitem;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.isistan.flabot.coremodel.Responsibility;
import org.isistan.flabot.edit.componenteditor.dialogs.responsibility.ResponsibilityEditionItem;
import org.isistan.flabot.messages.Messages;
import org.isistan.flabot.util.edition.CompositeEditionItemStatus;
import org.isistan.flabot.util.edition.EditionItemStatus;
import org.isistan.flabot.util.edition.SingleEditionItemStatus;
import org.isistan.flabot.util.edition.tab.EditionTabItemImpl;

/**
 * Edits main responsibility properties
 * 
 * @author $Author: dacostae $
 *
 */
public class MainResponsibilityEditionItem extends EditionTabItemImpl<Responsibility> implements ResponsibilityEditionItem {

    private Responsibility responsibility;

    private Composite control;

    private String newName = "";

    private String newDescription = "";

    private String oldName;

    private String oldDescription;

    private EditionItemStatus status = EditionItemStatus.DEFAULT_OK;

    @Override
    public void initialize(TabFolder tabFolder, TabItem tabItem, final Responsibility responsibility) {
        control = new Composite(tabFolder, SWT.NONE);
        tabItem.setText(Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.EditResponsibilityDialog.text"));
        tabItem.setToolTipText(Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.EditResponsibilityDialog.propertiesToolTipText"));
        GridLayout layout = new GridLayout(2, false);
        layout.verticalSpacing = 30;
        layout.horizontalSpacing = 12;
        control.setLayout(layout);
        control.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.responsibility = responsibility;
        newName = oldName = responsibility.getName();
        newDescription = oldDescription = responsibility.getDescription();
        updateStatus();
        final Label name = new Label(control, SWT.NULL);
        name.setText(Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.EditResponsibilityDialog.responsibilityName"));
        final Text nametext = new Text(control, SWT.SINGLE | SWT.BORDER);
        nametext.setText(oldName);
        nametext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nametext.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                newName = nametext.getText();
                updateStatus();
            }
        });
        final Label description = new Label(control, SWT.NULL);
        description.setText(Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.EditResponsibilityDialog.description"));
        description.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Text descriptiontext = new Text(control, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
        gd.widthHint = 150;
        gd.heightHint = 80;
        descriptiontext.setLayoutData(gd);
        descriptiontext.setText(oldDescription);
        descriptiontext.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                newDescription = descriptiontext.getText();
                updateStatus();
            }
        });
    }

    public void activate() {
    }

    private void updateStatus() {
        EditionItemStatus nameStatus;
        if (newName.trim().length() == 0) {
            nameStatus = new SingleEditionItemStatus(EditionItemStatus.Type.ERROR, Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.responsibility.editionitem.MainResponsibilityEditionItem.noNameSet"));
        } else {
            nameStatus = EditionItemStatus.DEFAULT_OK;
        }
        EditionItemStatus descriptionStatus;
        if (newDescription.trim().length() == 0) {
            descriptionStatus = new SingleEditionItemStatus(EditionItemStatus.Type.WARNING, Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.responsibility.editionitem.MainResponsibilityEditionItem.noDescriptionSet"));
        } else {
            descriptionStatus = EditionItemStatus.DEFAULT_OK;
        }
        status = new CompositeEditionItemStatus(nameStatus, descriptionStatus);
        notifyChange();
    }

    @Override
    public Control getControl() {
        return control;
    }

    public Command getCommand() {
        return new Command(Messages.getString("org.isistan.flabot.edit.componenteditor.dialogs.responsibility.editionitem.MainResponsibilityEditionItem.tabCommandLabel")) {

            @Override
            public void execute() {
                responsibility.setName(newName);
                responsibility.setDescription(newDescription);
            }

            @Override
            public void undo() {
                responsibility.setName(oldName);
                responsibility.setDescription(oldDescription);
            }
        };
    }

    public boolean canCreateCommand() {
        EditionItemStatus.Type type = getStatus().getType();
        return type != EditionItemStatus.Type.ERROR;
    }

    public EditionItemStatus getStatus() {
        return status;
    }

    public boolean accepts(Responsibility element) {
        return true;
    }
}
