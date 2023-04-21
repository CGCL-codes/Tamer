package interfaces.options;

import interfaces.GUISource;
import org.fenggui.TextEditor;

public class TextEditorOptionWidget extends OptionWidget {

    private TextEditor textEditor;

    public TextEditorOptionWidget(String initValue, String description, String label) {
        super(description, label);
        textEditor = new TextEditor();
        textEditor.getAppearance().setFont(GUISource.labelFont);
        textEditor.setText(initValue);
        addWidget(textEditor);
    }

    @Override
    public String getValue() {
        return textEditor.getText();
    }
}
