package sod.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import sod.gui.model.ButtonsModel.IconState;
import sod.gui.model.SODModel;
import sod.gui.panel.ButtonsPanel.PanelButtons;
import sod.gui.panel.SODPanel;
import sod.io.datasource.DataSource;
import sod.util.StaticDataUtil;

public class ButtonsActionListener implements ActionListener {

    private final SODPanel panel;

    public ButtonsActionListener(SODPanel panel) {
        this.panel = panel;
    }

    /**
     * ActionListener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(PanelButtons.SAVE.getButton())) {
            this.saveButtonPressed();
        } else if (o.equals(PanelButtons.EDIT.getButton())) {
            this.editButtonPressed();
        } else if (o.equals(PanelButtons.RESET.getButton())) {
            this.resetButtonPressed();
        }
    }

    private void saveButtonPressed() {
        this.saveStrokeOrder();
        this.panel.repaint();
    }

    private void editButtonPressed() {
        SODModel model = this.panel.getModel();
        model.getButtonsModel().setIconState(IconState.NULL);
        model.getState().unlock();
        this.panel.repaint();
    }

    private void resetButtonPressed() {
        this.resetStrokeOrder();
        this.panel.repaint();
    }

    private void saveStrokeOrder() {
        SODModel model = this.panel.getModel();
        int[] seq = model.getCurrentSequence();
        int count = model.getStrokeCount();
        boolean success = StaticDataUtil.isSeqValid(seq, count);
        Date date = new Date();
        success = success && DataSource.updateStrokes(this.panel.getUCS(), seq, date);
        if (success) {
            model.getInfoModel().setDate(date);
            model.getButtonsModel().setIconState(IconState.SUCCESS);
            model.recalcState(seq, count);
        } else {
            model.getButtonsModel().setIconState(IconState.ERROR);
        }
    }

    private void resetStrokeOrder() {
        SODModel model = this.panel.getModel();
        int strokeCount = model.getStrokeCount();
        int[] seq = StaticDataUtil.getSeqArray(strokeCount);
        model.getButtonsModel().setIconState(IconState.NULL);
        model.recalcState(seq, 0);
    }
}
