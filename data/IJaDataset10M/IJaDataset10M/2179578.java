package net.sf.amemailchecker.app.extension.letter;

import net.sf.amemailchecker.app.extension.ExtensionPoint;
import javax.swing.*;

public interface LetterDetailsActionExtensionPoint extends ExtensionPoint {

    public enum State {

        VIEW, EDIT
    }

    AbstractButton[] action(LetterDetailsActionContext context, State state);

    boolean containsActions(State state);

    LetterDetailsChangeListener letterChangeListener();
}
