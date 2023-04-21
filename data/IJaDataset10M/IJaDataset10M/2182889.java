package net.sf.rcpforms.widgetwrapper.gen;

import org.eclipse.swt.SWT;
import net.sf.rcpforms.widgetwrapper.wrapper.*;
import org.eclipse.swt.widgets.Group;
import java.util.*;
import java.lang.String;
import net.sf.rcpforms.widgets2.*;
import net.sf.rcpforms.widgetwrapper.util.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.RGB;

/**
 * <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to user or subclassed!</h3> 
 * <p>
 * <ul><li><i>This class is generated, do not alter it manually!!!</i></li></ul>
 * <p>
 */
public abstract class RCPGroup_ extends RCPControl implements IRCPGroupProperties_, IRCPGroup_ {

    private String cashedText;

    protected RCPGroup_(String labelText, int style) {
        super(labelText, style);
    }

    protected RCPGroup_(String labelText, int style, Object uid) {
        super(labelText, style, uid);
    }

    public Group getSWTGroup() {
        return getTypedWidget();
    }

    public void setText(String text) {
        setText_impl(text, true, true);
    }

    protected void setText_impl(String text, boolean event, boolean update) {
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_TEXT, text);
            return;
        }
        String oldText = cashedText;
        if (update) setText_ui(text);
        if (event) firePropertyChange(PROP_TEXT, oldText, text);
        cashedText = text;
    }

    public String getText() {
        if (!hasSpawned()) {
            return (String) getCashedHookPropertyValue(PROP_TEXT, null);
        }
        return getText_ui();
    }

    protected String getText_ui() {
        return getSWTGroup().getText();
    }

    protected void setText_ui(String text) {
        getSWTGroup().setText(text);
    }
}
