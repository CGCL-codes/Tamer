package org.eclipse.babel.editor.actions;

import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.MessagesEditor;
import org.eclipse.babel.editor.MessagesEditorChangeAdapter;
import org.eclipse.babel.editor.MessagesEditorContributor;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * 
 * @author Hugues Malphettes
 */
public class FilterKeysAction extends Action {

    private MessagesEditor editor;

    private final int flagToSet;

    private ChangeListener listener;

    /**
     * @param flagToSet The flag that will be set on unset
     */
    public FilterKeysAction(int flagToSet) {
        super("", IAction.AS_CHECK_BOX);
        this.flagToSet = flagToSet;
        listener = new ChangeListener();
        update();
    }

    private class ChangeListener extends MessagesEditorChangeAdapter {

        public void showOnlyUnusedAndMissingChanged(int hideEverythingElse) {
            MessagesEditorContributor.FILTERS.updateActionBars();
        }
    }

    public void run() {
        if (editor != null) {
            if (editor.isShowOnlyUnusedAndMissingKeys() != flagToSet) {
                editor.setShowOnlyUnusedMissingKeys(flagToSet);
            } else {
                editor.setShowOnlyUnusedMissingKeys(IMessagesEditorChangeListener.SHOW_ALL);
            }
        }
    }

    public void update() {
        if (editor == null) {
            super.setEnabled(false);
        } else {
            super.setEnabled(true);
        }
        if (editor != null && editor.isShowOnlyUnusedAndMissingKeys() == flagToSet) {
            setChecked(true);
        } else {
            setChecked(false);
        }
        setText(getTextInternal());
        setToolTipText(getTooltipInternal());
        setImageDescriptor(UIUtils.getImageDescriptor(getImageKey()));
    }

    public String getImageKey() {
        switch(flagToSet) {
            case IMessagesEditorChangeListener.SHOW_ONLY_MISSING:
                return UIUtils.IMAGE_MISSING_TRANSLATION;
            case IMessagesEditorChangeListener.SHOW_ONLY_MISSING_AND_UNUSED:
                return UIUtils.IMAGE_UNUSED_AND_MISSING_TRANSLATIONS;
            case IMessagesEditorChangeListener.SHOW_ONLY_UNUSED:
                return UIUtils.IMAGE_UNUSED_TRANSLATION;
            case IMessagesEditorChangeListener.SHOW_ALL:
            default:
                return UIUtils.IMAGE_KEY;
        }
    }

    public String getTextInternal() {
        switch(flagToSet) {
            case IMessagesEditorChangeListener.SHOW_ONLY_MISSING:
                return "Show only missing translations";
            case IMessagesEditorChangeListener.SHOW_ONLY_MISSING_AND_UNUSED:
                return "Show only missing or unused translations";
            case IMessagesEditorChangeListener.SHOW_ONLY_UNUSED:
                return "Show only unused translations";
            case IMessagesEditorChangeListener.SHOW_ALL:
            default:
                return "Show all";
        }
    }

    private String getTooltipInternal() {
        return getTextInternal();
    }

    public void setEditor(MessagesEditor editor) {
        if (editor == this.editor) {
            return;
        }
        if (this.editor != null) {
            this.editor.removeChangeListener(listener);
        }
        this.editor = editor;
        update();
        if (editor != null) {
            editor.addChangeListener(listener);
        }
    }
}
