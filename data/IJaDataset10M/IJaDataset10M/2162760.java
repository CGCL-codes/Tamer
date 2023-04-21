package au.edu.diasb.danno.taglib;

import java.io.IOException;
import java.util.Map;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import au.edu.diasb.annotation.danno.common.DannotateProperties;
import au.edu.diasb.chico.config.ConfigurationProperties;
import au.edu.diasb.chico.taglib.RemapURLTag;

/**
 * This tag rewrites the tendered Dannotate URL so that it is secure (using 
 * the methodology of the secureURL tag) and so that it is mapped
 * into the webspace corresponding to the current request URL.
 * 
 * @author scrawley
 */
public class RemapDannotateURLTag extends RemapURLTag {

    public RemapDannotateURLTag() {
        super();
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspContext context = getJspContext();
        Map<?, ?> props = (Map<?, ?>) context.findAttribute(ConfigurationProperties.SERVLET_CONFIG_PROPERTIES_ATTR);
        setRealBaseUrl(props.get(DannotateProperties.BASE_URL_PROP).toString());
        setRealContainer(props.get(DannotateProperties.CONTAINER_PROP).toString());
        super.doTag();
    }
}
