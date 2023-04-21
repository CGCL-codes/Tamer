package org.sqlexp.controls.messages;

import org.eclipse.swt.widgets.Shell;

/**
 * Question message, with a single text and "Yes" / "No" / "Cancel" buttons.
 * @author Matthieu Réjou
 */
public final class ExpQuestionDialog extends ExpMessageDialog {

    /** Question dialog styles. */
    public enum QuestionStyle {

        /** Yes / No Question dialog styles. */
        YesNo, /** Yes / No / Cancel question dialog styles. */
        YesNoCancel
    }

    /**
	 * Constructs a new Yes / No information dialog.
	 * @param parent shell, may be null
	 */
    public ExpQuestionDialog(final Shell parent) {
        this(parent, null);
    }

    /**
	 * Constructs a new information dialog.
	 * @param parent shell, may be null
	 * @param style defining available inputs, if not specified, Yes / No is applied
	 */
    public ExpQuestionDialog(final Shell parent, final QuestionStyle style) {
        super(parent);
        setImage("question");
        if (style == QuestionStyle.YesNoCancel) {
            setButtons(ButtonId.Yes, ButtonId.No, ButtonId.Cancel);
        } else {
            setButtons(ButtonId.Yes, ButtonId.No);
        }
    }

    /**
	 * Opens the message dialog.<br>
	 * The thread is blocked until the user clicks on any button.
	 * @return clicked button, if user closes the dialog, Cancel is returned
	 * (even if cancel button is not displayed)
	 */
    public ButtonId open() {
        ButtonId button = super.getClickedButton();
        if (button != null) {
            return button;
        } else {
            return ButtonId.Cancel;
        }
    }
}
