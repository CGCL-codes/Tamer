package ui.controller.initializer;

import ui.controller.action.AdjustSellTotalAction;
import ui.controller.action.CloseDialogAction;
import ui.view.component.DialogUI;
import ui.view.component.SellUI;
import ui.view.swing.component.detail.AdjustTotalDialog;

public class AdjustSellTotalDialogInitializer implements DialogInitializer {

    private final SellUI sellUI;

    public AdjustSellTotalDialogInitializer(SellUI sellUI) {
        this.sellUI = sellUI;
    }

    public DialogUI dialog() {
        AdjustTotalDialog dialog = new AdjustTotalDialog();
        dialog.setAcceptAction(new AdjustSellTotalAction(dialog, sellUI));
        dialog.setCancelAction(new CloseDialogAction(dialog));
        return dialog;
    }
}
