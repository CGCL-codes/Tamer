package com.tensegrity.palowebviewer.modules.widgets.client.dialogs;

import java.util.HashMap;
import java.util.Map;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class DialogController {

    private IDialogFactory factory;

    private final Map dialogMap = new HashMap();

    public void setDialogFactory(IDialogFactory factory) {
        this.factory = factory;
    }

    public void showDialog(IDialogModel dialog) {
        DialogBox dialogBox = getDialogBox(dialog);
        if (dialogBox != null) {
            dialogBox.show();
            dialogBox.center();
        }
    }

    public void hideDialog(IDialogModel dialog) {
        DialogBox dialogBox = getDialogBox(dialog);
        if (dialogBox != null) {
            dialogBox.hide();
        }
    }

    private DialogBox getDialogBox(IDialogModel dialog) {
        DialogBox dialogBox = (DialogBox) dialogMap.get(dialog);
        if (dialogBox == null) {
            Widget widget = getDialogWidget(dialog);
            if (widget != null) {
                dialogBox = createDialogBox(widget);
                String title = dialog.getTitle();
                dialogBox.setTitle(title);
                dialogMap.put(dialog, dialogBox);
            }
        }
        return dialogBox;
    }

    private DialogBox createDialogBox(Widget widget) {
        DialogBox dialogBox;
        dialogBox = new DialogBox();
        dialogBox.setWidget(widget);
        dialogBox.setStyleName("dialog");
        return dialogBox;
    }

    private Widget getDialogWidget(IDialogModel model) {
        Widget result = null;
        if (factory != null) {
            result = factory.getWidgetFor(model);
        }
        return result;
    }
}
