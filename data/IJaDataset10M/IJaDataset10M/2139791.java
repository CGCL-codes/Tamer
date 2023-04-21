package org.apache.myfaces.renderkit.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFRenderer;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlOutcomeTargetButtonRendererBase;

/**
 * @since 2.0
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 812756 $ $Date: 2009-09-08 22:19:39 -0500 (Tue, 08 Sep 2009) $
 */
@JSFRenderer(renderKitId = "HTML_BASIC", family = "javax.faces.OutcomeTarget", type = "javax.faces.Button")
public class HtmlOutcomeTargetButtonRenderer extends HtmlOutcomeTargetButtonRendererBase {
}
