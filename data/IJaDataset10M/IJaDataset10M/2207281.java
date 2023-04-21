package bg.invider.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JEditorPane;
import bg.invider.script.Script;
import bg.invider.script.ScriptComponent;

/**
 * The generated {@link Script} is shown in this {@link JEditorPane}.
 * 
 * @author meddle
 * @version 1.0
 */
public class ScriptViewer extends JEditorPane {

    private static final long serialVersionUID = 1L;

    ScriptViewer() {
        setMaximumSize(new Dimension(700, 700));
        setMinimumSize(new Dimension(200, 200));
        setForeground(Color.black);
        setBackground(Color.white);
        setCaretColor(Color.black);
        setMargin(new Insets(5, 5, 5, 5));
    }

    public void showPlain(Script script) {
        StringBuffer buf = new StringBuffer();
        for (String name : Script.SCRIPT_COMPONENTS) {
            String repr = scriptCompRep(script.getComponentByName(name));
            if (repr != null) {
                buf.append(repr);
            }
        }
        setText(buf.toString());
    }

    private String scriptCompRep(ScriptComponent scriptComponent) {
        if (scriptComponent.getRegex() == null) {
            return null;
        }
        String repr = scriptComponent.getName() + "  :  " + scriptComponent.getRegex() + "\n\n\n";
        return repr;
    }
}
