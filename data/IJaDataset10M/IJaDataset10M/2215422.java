package lu.etat.pch.icewebadf.jsf;

import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebMap;
import javax.el.ValueExpression;
import javax.faces.component.UICommand;

public class AGSMap extends UICommand {

    public static final String COMPONENT_TYPE = "lu.etat.pch.icewebadf.jsf.AGSMap";

    private WebMap webMap;

    private AGSTool currentTool;

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public AGSMap() {
        setImmediate(true);
    }

    public WebMap getWebMap() {
        if (webMap != null) {
            WebContext webContext = webMap.getWebContext();
            if (!webContext.isInit()) {
                webContext.init(webContext);
            }
            return webMap;
        }
        ValueExpression vb = getValueExpression("webMap");
        return (WebMap) (vb != null ? vb.getValue(getFacesContext().getELContext()) : null);
    }

    public void setWebMap(WebMap webMap) {
        ValueExpression vb = getValueExpression("webMap");
        if (vb != null) {
            vb.setValue(getFacesContext().getELContext(), webMap);
            this.webMap = null;
        }
    }

    public AGSTool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(AGSTool currentTool) {
        this.currentTool = currentTool;
    }
}
