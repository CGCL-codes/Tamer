package org.rjam.gui.fieldeditors;

import org.rjam.gui.beans.Value;
import javax.swing.JButton;
import javax.swing.JComponent;

public interface IFieldEditor {

    public static final String PROMPT = "Prompt";

    public abstract void init(Value object);

    public abstract JButton createDialogButton();

    public abstract JComponent getValueComponent();

    public abstract void _accept();

    public abstract Wrapper getWrapper();

    public abstract void setWrapper(Wrapper wrapper);

    public abstract boolean getPromptRight();

    public abstract void setPromptRight(boolean b);

    public abstract String getPrompt();

    public abstract void setPrompt(String prompt);

    public abstract boolean isEditable();

    public abstract void setEditable(boolean editable);

    public abstract boolean isAutoAccept();

    public abstract void setAutoAccept(boolean autoAccept);

    public abstract void setHighlite(boolean b);

    public abstract boolean isHighlite();

    public abstract boolean isHighliteOnChange();

    public abstract void setHighliteOnChange(boolean b);
}
