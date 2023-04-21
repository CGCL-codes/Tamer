package com.datas.form.modul.common;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import com.datas.component.panel.DetailTablePanel;
import com.datas.component.toolbar.DetailModulToolBarListener;
import com.datas.form.frame.main.MainFrame;
import com.datas.form.modul.CalculationFieldsListener;
import com.datas.form.modul.manager.DetailModulManager;
import com.datas.form.modul.manager.ModulManager;
import com.datas.form.modul.manager.enums.WorkingMode;
import com.datas.form.util.FieldUtility;

/**
 * @author kimi
 * 
 */
@SuppressWarnings("serial")
public abstract class DetailModul extends DetailModulManager implements DetailModulToolBarListener {

    /**
	 * @param modul
	 * @param idTable
	 */
    public DetailModul(ModulManager modul, String idTable) {
        super(modul, idTable);
    }

    protected abstract void initDetailModul();

    protected abstract void setLayoutDetailModul();

    protected abstract void setBehaviorDetailModul();

    protected abstract void setLookAndFeelDetailModul();

    protected abstract void focusFirstField();

    /** data manipulation */
    protected abstract Object insertData();

    protected abstract void updateData(Object object);

    @Override
    protected void initManager() {
        setLayoutManager();
        setBehaviorManager();
        setLookAndFeelManager();
        initDetailModul();
    }

    @Override
    protected void setLayoutManager() {
        setLayoutDetailModul();
    }

    @Override
    protected void setBehaviorManager() {
        setKeyboard();
        setBehaviorDetailModul();
    }

    @Override
    protected void setLookAndFeelManager() {
        setLookAndFeelDetailModul();
    }

    @Override
    protected void focusField() {
        focusFirstField();
    }

    /** main toolbar listener implementation */
    public void first() {
        DetailTablePanel detailTablePanel = getDetailPanel().getDetailTablePanelsMap().get(idTable);
        if (detailTablePanel.getTable() != null) {
            detailTablePanel.getTablePanel().selectFirstRow();
            FieldUtility.setDataToFields(detailTablePanel.getSysTable().getBean(), fields, detailTablePanel.getTable().getSelected().get(0));
            refreshRowNumberDisplay();
        }
    }

    public void previous() {
        DetailTablePanel detailTablePanel = getDetailPanel().getDetailTablePanelsMap().get(idTable);
        if (detailTablePanel.getTable() != null) {
            detailTablePanel.getTablePanel().selectPreviousRow();
            FieldUtility.setDataToFields(detailTablePanel.getSysTable().getBean(), fields, detailTablePanel.getTable().getSelected().get(0));
            refreshRowNumberDisplay();
        }
    }

    public void next() {
        DetailTablePanel detailTablePanel = getDetailPanel().getDetailTablePanelsMap().get(idTable);
        if (detailTablePanel.getTable() != null) {
            detailTablePanel.getTablePanel().selectNextRow();
            FieldUtility.setDataToFields(detailTablePanel.getSysTable().getBean(), fields, detailTablePanel.getTable().getSelected().get(0));
            refreshRowNumberDisplay();
        }
    }

    public void last() {
        DetailTablePanel detailTablePanel = getDetailPanel().getDetailTablePanelsMap().get(idTable);
        if (detailTablePanel.getTable() != null) {
            detailTablePanel.getTablePanel().selectLastRow();
            FieldUtility.setDataToFields(detailTablePanel.getSysTable().getBean(), fields, detailTablePanel.getTable().getSelected().get(0));
            refreshRowNumberDisplay();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(buttonsPanel.getApplyButton())) {
            apply();
        } else if (e.getSource().equals(buttonsPanel.getExitButton())) {
            exit();
        }
    }

    private void setKeyboard() {
        KeyStroke ctrlEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK);
        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        KeyStroke ctrlF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK);
        KeyStroke ctrlF6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.CTRL_MASK);
        buttonsPanel.getApplyButton().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f12, "apply");
        buttonsPanel.getApplyButton().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlEnter, "apply");
        buttonsPanel.getApplyButton().getActionMap().put("apply", new Action(0));
        buttonsPanel.getExitButton().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlF4, "exit");
        buttonsPanel.getExitButton().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlF6, "exit");
        buttonsPanel.getExitButton().getActionMap().put("exit", new Action(1));
    }

    private void apply() {
        switch(workingMode) {
            case WORKING_MODE_INSERT:
                if (FieldUtility.checkMandatoryFields(this)) {
                    applyInsert(insertData());
                    FieldUtility.clearFields(fields);
                    FieldUtility.setDefaultValues(this, fields);
                    FieldUtility.setDetailPrimaryKeyValues(modul.getFields(), fields);
                    FieldUtility.setFieldsColor(fields, true);
                    setWorkingMode(WorkingMode.WORKING_MODE_INSERT);
                    focusFirstField();
                }
                break;
            case WORKING_MODE_UPDATE:
                if (FieldUtility.checkMandatoryFields(this)) {
                    applyUpdate();
                    FieldUtility.setFieldsColor(fields, true);
                    setWorkingMode(WorkingMode.WORKING_MODE_FIELD_VIEW);
                    focusFirstField();
                }
                break;
        }
        modul.getSecurityManager().checkButtonsActivity();
    }

    private void exit() {
        closeFrame();
    }

    private void applyInsert(Object data) {
        if (data != null) {
            DetailTablePanel detailTablePanel = getSelectedDetailTablePanel();
            List list = detailTablePanel.getTable().getAll();
            list.add(data);
            detailTablePanel.createDetailTableUsingMetaData(detailTablePanel.getSysTable().getBean(), fields, list);
            MainFrame.getInstance().getStatusBar().confirmationMessage(getServiceContainer().getLocalizationService().getTranslation("ITEM_SUCCESSUFULY_ADDED"));
            if (modul instanceof CalculationFieldsListener) {
                ((CalculationFieldsListener) modul).populateCalculationFields();
            }
        }
    }

    private void applyUpdate() {
        DetailTablePanel detailTablePanel = getSelectedDetailTablePanel();
        updateData(detailTablePanel.getTable().getSelected().get(0));
        MainFrame.getInstance().getStatusBar().confirmationMessage(getServiceContainer().getLocalizationService().getTranslation("DATA_SUCCESSUFULY_UPDATED"));
        if (modul instanceof CalculationFieldsListener) {
            ((CalculationFieldsListener) modul).populateCalculationFields();
        }
    }

    private class Action extends AbstractAction {

        private int actionId;

        public Action(int actionId) {
            this.actionId = actionId;
        }

        public void actionPerformed(ActionEvent e) {
            if (actionId == 0) {
                apply();
            } else if (actionId == 1) {
                exit();
            }
        }
    }
}
