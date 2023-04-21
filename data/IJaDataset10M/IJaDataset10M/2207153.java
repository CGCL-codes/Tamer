package simple.label;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public class FieldComponent extends UIInput {

    private String label;

    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = label;
        return ((Object) (values));
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        label = (String) values[1];
    }

    public FieldComponent() {
        this.setRendererType("simple.Renderer");
    }

    /**
	 * @return Returns the label.
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * @param label
	 *  The label to set.
	 */
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getFamily() {
        return "simple.Field";
    }

    public boolean isError() {
        return !this.isValid();
    }
}
