package com.bradrydzewski.tinyreport.html;

import com.bradrydzewski.tinyreport.ReportBuilderArgs;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.ecs.StringElement;
import org.apache.ecs.xhtml.div;

/**
 *
 * @author Brad
 */
@XmlRootElement
public class Text extends Element {

    private String value;

    private String script;

    private boolean dynamic = false;

    public Text() {
    }

    public Text(String value, boolean dynamic) {
        if (dynamic) this.script = value; else this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(String value, boolean dynamic) {
        this.value = value;
        this.dynamic = dynamic;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public void build(org.apache.ecs.Element parent, ReportBuilderArgs args) {
        div div = new div();
        div.setPrettyPrint(true);
        StringElement innerText = null;
        if (styleName != null) div.setStyle(styleName);
        if (dynamic) {
            try {
                args.getScriptEngine().put("params", args.getParameters());
                args.getScriptEngine().put("reportDefinition", args.getReportDefinition());
                args.getScriptEngine().put("this", this);
                String innerHtml = args.getScriptEngine().eval(script).toString();
                innerText = new StringElement(innerHtml);
            } catch (javax.script.ScriptException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            innerText = new StringElement(value);
        }
        innerText.setPrettyPrint(true);
        div.addElement(innerText);
        parent.addElementToRegistry(div);
    }
}
