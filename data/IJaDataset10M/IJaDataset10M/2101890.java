package org.fudaa.ctulu.gui;

/**
 * @author deniger
 * @version $Id$
 */
public class CtuluCellDialogEditor extends CtuluCellButtonEditor {

    protected CtuluDialog dialog_;

    public CtuluCellDialogEditor(final CtuluDialog _dialog) {
        this(_dialog, null);
    }

    public CtuluCellDialogEditor(final CtuluDialog _dialog, final CtuluCellDecorator _deco) {
        super(_deco);
        dialog_ = _dialog;
    }

    protected void doAction() {
        dialog_.setValue(value_);
        dialog_.doLayout();
        if (dialog_.afficheAndIsOk()) {
            value_ = dialog_.getValue();
            setValue(value_);
        } else {
            cancelCellEditing();
        }
        stopCellEditing();
        if (getParent() != null) {
            getParent().requestFocus();
        }
    }

    public CtuluDialog getDialog() {
        return dialog_;
    }
}
