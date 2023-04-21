package net.sf.myfacessandbox.components.ajax.autocompleteTextArea;

import javax.faces.context.FacesContext;
import net.sf.myfacessandbox.components.ajax.autocomplete.AjaxTextField;

/**
 * Ajaxed text area component
 * 
 * @author Werner Punz werpu@gmx.at
 *
 */
public class AjaxTextarea extends AjaxTextField {

    Integer width = new Integer(20);

    Integer height = new Integer(20);

    /**
	 * constructor for the ajaxed text area
	 * adds the link to the renderer
	 *
	 */
    public AjaxTextarea() {
        super();
        setRendererType("ajaxtextarea");
    }

    /**
	 * standard savestate
	 */
    public Object saveState(FacesContext context) {
        Object[] values = new Object[3];
        values[0] = super.saveState(context);
        values[1] = width;
        values[2] = height;
        return values;
    }

    /**
	 * standard restore state
	 */
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        width = (Integer) values[1];
        height = (Integer) values[2];
    }

    /**
	 * setter for the height attribute
	 * @return
	 */
    public Integer getHeight() {
        return height;
    }

    /**
	 * setter for the height attribute
	 * @param height
	 */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
	 * getter for the width attribute
	 * @return
	 */
    public Integer getWidth() {
        return width;
    }

    /**
	 * setter for the width attribute
	 * @param width
	 */
    public void setWidth(Integer width) {
        this.width = width;
    }
}
