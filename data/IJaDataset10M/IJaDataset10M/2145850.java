package er.directtoweb;

import com.webobjects.appserver.*;

public class ERDDisplayTemplateString extends ERDCustomComponent {

    public ERDDisplayTemplateString(WOContext context) {
        super(context);
    }

    public String templateString() {
        return (String) valueForBinding("templateString");
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }
}
