package org.jcompany.view.jsf.renderer;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.FacesBean.Type;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.skin.Icon;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputUtils;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleInputDateRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlUtils;

public class PlcSimpleDateRenderer extends SimpleInputDateRenderer {

    public PlcSimpleDateRenderer() {
        super();
    }

    public PlcSimpleDateRenderer(FacesBean.Type type) {
        super(type);
    }

    @Override
    protected void renderIcon(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {
    }
}
