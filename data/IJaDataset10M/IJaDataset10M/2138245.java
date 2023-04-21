package com.mw3d.swt.component;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import com.mw3d.core.entity.DynamicEntity;
import com.mw3d.core.entity.data.Trigger;
import com.mw3d.swt.EditorGUI;
import com.mw3d.swt.component.dialog.LocalTriggerEditDialog;
import com.mw3d.swt.component.dialog.TriggerBrowseDialog;
import com.mw3d.swt.util.command.TriggerCreationCommand;
import com.mw3d.swt.util.command.TriggerDeletionCommand;

/**
 * This is a trigger canvas. It will manage all the triggers for a selected entity.
 * 
 * @author ndebruyn
 * Created on June 22, 2005
 */
public class TriggerCanvas {

    private Button addButton, removeButton, editButton, alwaysButton;

    private TriggerBrowseDialog triggerBrowseDialog;

    private EditorGUI editorGUI;

    private List list;

    private Color labelBackground;

    private Trigger currentTrigger;

    private DynamicText triggerRaduis;

    public TriggerCanvas(Composite container, EditorGUI editorGUI) {
        this.editorGUI = editorGUI;
        labelBackground = new Color(editorGUI.getShell().getDisplay(), 255, 255, 255);
        Composite main = new Composite(container, SWT.CENTER);
        main.setBackground(labelBackground);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        main.setLayoutData(data);
        main.setLayout(new GridLayout(2, true));
        Composite butComp = new Composite(main, SWT.NONE);
        butComp.setBackground(labelBackground);
        data = new GridData();
        data.horizontalSpan = 2;
        butComp.setLayoutData(data);
        butComp.setLayout(new GridLayout(3, true));
        addButton = new Button(butComp, SWT.PUSH);
        addButton.setText("New");
        addButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                triggerBrowseDialog = new TriggerBrowseDialog(TriggerCanvas.this.editorGUI);
                triggerBrowseDialog.open();
                File file = triggerBrowseDialog.getSelectedFile();
                if (file != null) {
                    Trigger trigger = ((DynamicEntity) TriggerCanvas.this.editorGUI.getRuntime().getLevel().getSelectedEntity()).getEntityTriggers().createTrigger(file.getAbsolutePath(), 50, -1, false);
                    TriggerCreationCommand command = new TriggerCreationCommand((DynamicEntity) TriggerCanvas.this.editorGUI.getRuntime().getLevel().getSelectedEntity(), trigger);
                    TriggerCanvas.this.editorGUI.getRuntime().registerCommand(command);
                    reset();
                }
            }
        });
        editButton = new Button(butComp, SWT.PUSH);
        editButton.setText("Edit");
        editButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (currentTrigger != null) {
                    LocalTriggerEditDialog dialog = new LocalTriggerEditDialog(TriggerCanvas.this.editorGUI.getShell(), TriggerCanvas.this.editorGUI, currentTrigger);
                    dialog.open();
                }
            }
        });
        removeButton = new Button(butComp, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (currentTrigger != null) {
                    ((DynamicEntity) TriggerCanvas.this.editorGUI.getRuntime().getLevel().getSelectedEntity()).getEntityTriggers().removeTrigger(currentTrigger);
                    TriggerDeletionCommand command = new TriggerDeletionCommand((DynamicEntity) TriggerCanvas.this.editorGUI.getRuntime().getLevel().getSelectedEntity(), currentTrigger);
                    TriggerCanvas.this.editorGUI.getRuntime().registerCommand(command);
                    reset();
                }
            }
        });
        data = new GridData();
        data.widthHint = 150;
        data.heightHint = 60;
        data.horizontalSpan = 2;
        list = new List(main, SWT.BORDER);
        list.setLayoutData(data);
        list.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                String name = list.getSelection()[0];
                currentTrigger = (Trigger) list.getData(name);
                if (currentTrigger != null) {
                    removeButton.setEnabled(true);
                    editButton.setEnabled(true);
                    alwaysButton.setEnabled(true);
                    triggerRaduis.setValue(currentTrigger.getRadius());
                    alwaysButton.setSelection(currentTrigger.isAlways());
                }
            }
        });
        Label radiusLabel = new Label(main, SWT.NONE);
        radiusLabel.setText("Radius");
        radiusLabel.setBackground(labelBackground);
        triggerRaduis = new DynamicText(main, 0.0F, 50);
        triggerRaduis.setMinimum(0.0F);
        triggerRaduis.setMaximum(500);
        triggerRaduis.setIncrement(1.0F);
        triggerRaduis.setEnabled(true);
        triggerRaduis.addDynamicChangeListener(new DynamicChangeListener() {

            public void changeAction() {
                if (currentTrigger != null) {
                    currentTrigger.setRadius(triggerRaduis.getValue());
                }
            }
        });
        Label label = new Label(main, SWT.NONE);
        label.setText("Always");
        label.setBackground(labelBackground);
        alwaysButton = new Button(main, SWT.CHECK);
        alwaysButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (currentTrigger != null) {
                    currentTrigger.setAlways(alwaysButton.getSelection());
                }
            }
        });
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
        alwaysButton.setEnabled(false);
    }

    public void reset() {
        if (((DynamicEntity) TriggerCanvas.this.editorGUI.getRuntime().getLevel().getSelectedEntity()).getEntityTriggers().hasTriggers()) {
            list.removeAll();
            java.util.List tList = ((DynamicEntity) TriggerCanvas.this.editorGUI.getRuntime().getLevel().getSelectedEntity()).getEntityTriggers().getTriggerList();
            for (int i = 0; i < tList.size(); i++) {
                Trigger trigger = (Trigger) tList.get(i);
                if (trigger != null) {
                    list.add(trigger.getName() + "");
                    list.setData(trigger.getName() + "", trigger);
                }
            }
            currentTrigger = null;
            removeButton.setEnabled(false);
            alwaysButton.setEnabled(false);
            triggerRaduis.setValue(0);
        } else {
            list.removeAll();
            currentTrigger = null;
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
            alwaysButton.setEnabled(false);
            triggerRaduis.setValue(0);
            list.redraw();
        }
    }
}
