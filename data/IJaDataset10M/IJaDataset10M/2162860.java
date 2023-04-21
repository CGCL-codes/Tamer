package cn.easyact.tdl.state;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;

public class TextEdit extends State {

    private TextBox box;

    private boolean shouldNext;

    public Displayable getDisplayable() {
        return box;
    }

    public void setText(String text) {
        box.setString(text);
    }

    public void setSystemComponent(TextBox textBox) {
        box = textBox;
        box.setCommandListener(this);
    }

    public String getText() {
        return box.getString();
    }

    protected void execute(Command c, Displayable d) {
        shouldNext = true;
    }

    protected boolean isShouldGoToScriptNext() {
        return shouldNext;
    }

    public void update() {
        shouldNext = false;
    }
}
