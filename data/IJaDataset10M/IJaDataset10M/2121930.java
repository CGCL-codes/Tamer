package com.gc.gwt.wysiwyg.client.defaults.widgets;

import com.gc.gwt.wysiwyg.client.Editor;
import com.gc.gwt.wysiwyg.client.EditorToolbarButton;
import com.gc.gwt.wysiwyg.client.EditorUtils;
import com.gc.gwt.wysiwyg.client.defaults.DefaultConstants;
import com.gc.gwt.wysiwyg.client.defaults.EditorCommand;
import com.gc.gwt.wysiwyg.client.defaults.SimpleOneFieldPromptBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class LinkButton extends EditorToolbarButton implements ClickHandler {

    private Editor editor;

    public LinkButton(Editor editor) {
        super(DefaultConstants.BUTTON_LINK);
        this.editor = editor;
        this.addClickHandler(this);
    }

    public void onClick(ClickEvent event) {
        new SimpleOneFieldPromptBox(editor, new EditorCommand() {

            public void exec(String[] params) {
                EditorUtils.saveSelection(editor.getEditorWYSIWYG().getFrame().getElement());
                EditorUtils.doCreateLink(editor.getEditorWYSIWYG().getFrame().getElement(), params[0]);
            }
        }, "Create Link", "Link URL: ", "Create Link").show(editor);
    }
}
