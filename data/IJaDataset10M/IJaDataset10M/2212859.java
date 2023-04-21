package uk.ac.lkl.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Ken Kahn
 *
 */
public class TextBoxDialogBox extends DialogBox {

    private TextBox textBox;

    public TextBoxDialogBox(String captionHTML, final TextBoxEntryHandler textBoxEntryHandler) {
        super();
        setHTML(captionHTML);
        textBox = new TextBox();
        setWidget(textBox);
        setAnimationEnabled(true);
        center();
        KeyDownHandler keyDownHandler = new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                int charCode = event.getNativeKeyCode();
                if (charCode == KeyCodes.KEY_ENTER) {
                    textBoxEntryHandler.onTextBoxEntry(textBox.getText().trim());
                    hide();
                }
            }
        };
        textBox.addKeyDownHandler(keyDownHandler);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        textBox.setWidth((9 * getOffsetWidth()) / 10 + "px");
    }
}
