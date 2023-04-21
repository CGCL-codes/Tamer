package org.wings.plaf.css.script;

import org.wings.script.ScriptListener;

public class HideSelectBoxesScript implements ScriptListener {

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        final StringBuilder script = new StringBuilder();
        script.append("function hideSelectBoxes() {\n");
        script.append("    for (var i = 0; i < document.forms.length; i++) {\n");
        script.append("        for (var e = 0; e < document.forms[i].length; e++) {\n");
        script.append("            if (document.forms[i].elements[e].tagName == 'SELECT') {\n");
        script.append("                document.forms[i].elements[e].style.visibility = 'hidden';\n");
        script.append("            }\n");
        script.append("        }\n");
        script.append("    }\n");
        script.append("}\n");
        script.append("hideSelectBoxes();\n");
        return script.toString();
    }

    public int getPriority() {
        return 0;
    }
}
