package bioinfo.comaWebServer.components;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FormSupport;
import bioinfo.comaWebServer.util.CheckboxContainer;

public class CheckboxGroup {

    @Environmental
    private FormSupport formSupport;

    @Inject
    private Environment environment;

    @Inject
    private ComponentResources compResources;

    private Collection<String> checkboxNames;

    private String functionName;

    void beginRender(MarkupWriter writer) {
        initialize();
        if (checkboxNames == null) {
            checkboxNames = new HashSet<String>();
        }
        environment.push(CheckboxContainer.class, new CheckboxContainer() {

            public void registerControlledCheckbox(ControlledCheckbox checkbox) {
                String name = checkbox.getCheckboxName();
                String form = formSupport.getClientId();
                checkboxNames.add(form + "." + name);
            }

            public String getFunctionName() {
                return functionName;
            }
        });
    }

    public void afterRender(MarkupWriter writer) {
        Element body = writer.getDocument().find("html/body");
        body.elementAt(0, "script", "type", "text/javascript");
        Element script = writer.getDocument().find("html/body/script");
        script.raw("\n");
        script.text("function " + functionName + "(value) {");
        script.raw("\n");
        Iterator<String> it = checkboxNames.iterator();
        while (it.hasNext()) {
            String checkboxName = it.next();
            String str = "document.forms." + checkboxName + ".checked=value;";
            script.text(str);
            script.raw("\n");
        }
        script.text("}");
        environment.pop(CheckboxContainer.class);
    }

    private void initialize() {
        String id = compResources.getId();
        functionName = "setCheckboxGroup_" + id;
    }
}
