package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.util.SStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.StringTokenizer;

/**
 * Script listener which prevents default key bindings as declared in <code>web.xml</code>
 * by <code>wings.prevent.default.keybindings</code> property.
 *
 * @author hengels
 */
class CaptureDefaultBindingsScriptListener extends JavaScriptListener {

    /**
     * Apache jakarta commons logger
     */
    private static final Log log = LogFactory.getLog(CaptureDefaultBindingsScriptListener.class);

    private static final String WEB_XML_CAPTURE_DEFAULT_PROPERTYKEY = "wings.prevent.default.keybindings";

    CaptureDefaultBindingsScriptListener(String event, String code, String script) {
        super(event, code, script);
        setPriority(LOW_PRIORITY);
    }

    static void install(SComponent component) {
        ScriptListener[] scriptListeners = component.getScriptListeners();
        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            if (scriptListener instanceof CaptureDefaultBindingsScriptListener) component.removeScriptListener(scriptListener);
        }
        String string = (String) component.getSession().getProperty(WEB_XML_CAPTURE_DEFAULT_PROPERTYKEY, "");
        SStringBuilder typed = new SStringBuilder();
        for (StringTokenizer tokenizer = new StringTokenizer(string, ","); tokenizer.hasMoreTokens(); ) {
            String token = tokenizer.nextToken();
            KeyStroke keyStroke = KeyStroke.getKeyStroke(token);
            if (keyStroke != null) {
                writeMatch(typed, keyStroke);
                writeCapture(typed);
            } else {
                log.warn("Invalid KeyStroke String" + keyStroke + " defined in web.xml for " + WEB_XML_CAPTURE_DEFAULT_PROPERTYKEY);
            }
        }
        if (typed.length() > 0) component.addScriptListener(new CaptureDefaultBindingsScriptListener("onkeydown", "return keydown_" + component.getName() + "(event)", "function keydown_" + component.getName() + "(event) {\n  " + "event = getEvent(event);\n  " + typed.toString() + "  return true;\n}\n"));
    }

    private static void writeMatch(SStringBuilder buffer, KeyStroke keyStroke) {
        final int unicodeKeyCode = keyStroke.getKeyCode();
        buffer.append("if (event.keyCode == " + unicodeKeyCode);
        if ((keyStroke.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0) buffer.append(" && event.shiftKey"); else buffer.append(" && !event.shiftKey");
        if ((keyStroke.getModifiers() & InputEvent.CTRL_DOWN_MASK) != 0) buffer.append(" && event.ctrlKey"); else buffer.append(" && !event.ctrlKey");
        if ((keyStroke.getModifiers() & InputEvent.ALT_DOWN_MASK) != 0) buffer.append(" && event.altKey"); else buffer.append(" && !event.altKey");
        buffer.append(")");
    }

    private static void writeCapture(SStringBuilder buffer) {
        buffer.append(" { preventDefault(event); return false; }\n");
    }
}
