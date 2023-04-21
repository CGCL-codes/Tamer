package net.sf.lightbound.components.forms;

import net.sf.lightbound.components.Component;

public class FormField extends Component {

    private String name;

    private Object form;

    public FormField() {
        this(null);
    }

    public FormField(String name) {
        this.name = name;
    }

    public FormField(String name, Object insideContext) {
        super(insideContext);
        this.name = name;
    }

    public FormField(String name, Object insideContext, Object form) {
        this(name, insideContext);
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getForm() {
        return form;
    }

    public void setForm(Object form) {
        this.form = form;
    }
}
