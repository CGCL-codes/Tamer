package net.webassembletool.jsf;

import java.io.IOException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class IncludeParamComponent extends UIComponentBase {

    private String name;

    private String value;

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        value = UIComponentUtils.renderChildrenToString(this);
    }

    public String getValue() {
        return UIComponentUtils.getParam(this, "value", value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public String getName() {
        return UIComponentUtils.getParam(this, "name", name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFamily() {
        return IncludeParamComponent.class.getPackage().toString();
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        name = (String) values[1];
    }

    @Override
    public Object saveState(FacesContext context) {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = name;
        return values;
    }
}
